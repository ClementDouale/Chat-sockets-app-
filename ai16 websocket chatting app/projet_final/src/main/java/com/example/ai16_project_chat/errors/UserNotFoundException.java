package com.example.ai16_project_chat.errors;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {super("User not found.");}
}
