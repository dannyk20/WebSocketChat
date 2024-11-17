package com.dannyk20.wschat.controller;

import com.dannyk20.wschat.model.ChatMessage;
import com.dannyk20.wschat.model.ChatRoom;
import com.dannyk20.wschat.service.ChatRoomManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomManager chatRoomManager;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatRoomManager chatRoomManager) {
        this.messagingTemplate = messagingTemplate;
        this.chatRoomManager = chatRoomManager;
    }

    @MessageMapping("/chat.joinRoom")
    public void joinRoom(ChatMessage message) {
        String roomId = message.getRoomId();
        String username = message.getSender();

        ChatRoom room = chatRoomManager.getRoom(roomId);
        if (room == null) {
            room = chatRoomManager.createRoom(roomId); // 방 생성
            System.out.println("Created room: " + roomId);
        }

        if (room.isFull()) {
            System.out.println("Room is already full: " + roomId);
            messagingTemplate.convertAndSendToUser(username, "/queue/errors", "Room is full");
            return;
        }

        if (!room.addUser(username)) {
            System.out.println("Failed to add user: " + username + " to room: " + roomId);
            messagingTemplate.convertAndSendToUser(username, "/queue/errors", "Unable to join room.");
            return;
        }

        // 참가 메시지 브로드캐스트
        message.setType(ChatMessage.MessageType.JOIN);
        message.setContent(username + " has joined the room.");
        messagingTemplate.convertAndSend("/queue/" + roomId, message);

        // 방 번호 표시를 위한 메시지 전송
        messagingTemplate.convertAndSendToUser(username, "/queue/room", "You are in Room: " + roomId);

        System.out.println("User " + username + " joined room: " + roomId + ", Current Users: " + room.getUsers());
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        String roomId = message.getRoomId();
        messagingTemplate.convertAndSend("/queue/" + roomId, message); // 메시지 브로드캐스트
    }

    @MessageMapping("/chat.leaveRoom")
    public void leaveRoom(ChatMessage message) {
        String roomId = message.getRoomId();
        String username = message.getSender();

        ChatRoom room = chatRoomManager.getRoom(roomId);
        if (room != null) {
            room.removeUser(username);
            if (room.isEmpty()) {
                chatRoomManager.deleteRoom(roomId);
            }
        }

        // 퇴장 메시지 브로드캐스트
        message.setType(ChatMessage.MessageType.LEAVE);
        message.setContent(username + " has left the room.");
        messagingTemplate.convertAndSend("/queue/" + roomId, message);
    }
}
