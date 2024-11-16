package com.dannyk20.wschat.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserManager {
    private final Map<String, String> users = new HashMap<>();
    public UserManager() {
        users.put("admin", "password");
        users.put("user1", "1234");
        users.put("user2", "1234");
    }
    public boolean auth(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
