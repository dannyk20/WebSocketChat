package com.dannyk20.wschat.service;

import com.dannyk20.wschat.model.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@Service
public class ChatRoomManager {
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    public ChatRoom createRoom(String roomId) {
        ChatRoom room = new ChatRoom(roomId);
        chatRooms.put(roomId, room);
        return room;
    }

    public ChatRoom getRoom(String roomId) {
        return chatRooms.get(roomId);
    }

    public void deleteRoom(String roomId) {
        chatRooms.remove(roomId);
    }

    public Collection<ChatRoom> getChatRooms() {
        return chatRooms.values(); // 활성화된 방 목록 반환
    }
}