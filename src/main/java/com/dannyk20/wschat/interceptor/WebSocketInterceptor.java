package com.dannyk20.wschat.interceptor;

import com.dannyk20.wschat.model.ChatRoom;
import com.dannyk20.wschat.service.ChatRoomManager;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    private final ChatRoomManager chatRoomManager;

    public WebSocketInterceptor(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String roomId = getRoomIdFromRequest(request);
        if (roomId == null) {
            System.out.println("Room ID not provided in request."); // 디버그 로그
            return false; // 연결 차단
        }

        System.out.println("Room ID provided: " + roomId);

        ChatRoom room = chatRoomManager.getRoom(roomId);
        if (room == null) {
            System.out.println("Room not found. Creating a new room with ID: " + roomId);
            room = chatRoomManager.createRoom(roomId);
        }

        // 방이 가득 찬 경우 연결 차단
        if (room.isFull()) {
            System.out.println("Room is full: " + roomId);
            return false;
        }

        attributes.put("roomId", roomId); // WebSocket 세션에 roomId 저장
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 연결 후 작업 (사용하지 않음)
    }

    private String getRoomIdFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null && query.contains("roomId=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("roomId=")) {
                    return param.substring("roomId=".length());
                }
            }
        }
        return null;
    }
}
