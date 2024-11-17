package com.dannyk20.wschat.controller;

import com.dannyk20.wschat.model.ChatRoom;
import com.dannyk20.wschat.service.ChatRoomManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomManager chatRoomManager;

    public ChatRoomController(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @GetMapping("/rooms")
    public Collection<ChatRoom> getChatRooms() {
        System.out.println("print chat room lists.");
        return chatRoomManager.getChatRooms();
    }

    @PostMapping("/rooms")
    public ChatRoom createRoom(@RequestParam String roomId) {
        return chatRoomManager.createRoom(roomId);
    }

    @DeleteMapping("/rooms/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        chatRoomManager.deleteRoom(roomId);
    }
}