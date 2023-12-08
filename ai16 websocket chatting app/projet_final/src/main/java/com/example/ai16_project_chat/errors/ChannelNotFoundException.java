package com.example.ai16_project_chat.errors;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException() {super("Channel not found.");}
}
