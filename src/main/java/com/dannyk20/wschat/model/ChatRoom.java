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

    public boolean addUser (String username) {
        if(users.size()>=2){
            return false;
        }
        return users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean isFull() {
        return users.size() >= 2;
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}