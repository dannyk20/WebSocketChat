package com.dannyk20.wschat.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
