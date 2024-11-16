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

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat.newUser")
    public void newUser(ChatMessage message) {
        ChatRoom room = chatRoomManager.getRoom(message.getRoomId());
        if (room == null) {
            room = chatRoomManager.createRoom(message.getRoomId());
        }
        room.addUser(message.getSender());
        message.setType(ChatMessage.MessageType.JOIN);
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }
}

