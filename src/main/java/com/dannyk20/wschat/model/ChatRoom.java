package com.dannyk20.wschat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoom {
    private String id;
    private Set<String> users = new HashSet<>();

    public ChatRoom(String id) {
        this.id = id;
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}
