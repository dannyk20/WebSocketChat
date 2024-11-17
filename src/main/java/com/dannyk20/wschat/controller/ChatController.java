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
            room=chatRoomManager.createRoom(roomId);
        }

        if(room.isEmpty()){
            messagingTemplate.convertAndSendToUser(username, "/queue/errors", "Room is full");
            return;
        }

        message.setType(ChatMessage.MessageType.JOIN);
        message.setContent(username + " has joined the room.");
        messagingTemplate.convertAndSend("/queue/" + roomId, message);
    }
    
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        String roomId = message.getRoomId();
        messagingTemplate.convertAndSend("/queue/" + roomId, message);
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

        message.setType(ChatMessage.MessageType.LEAVE);
        messagingTemplate.convertAndSend("/queue/" + roomId, message);
    }
}