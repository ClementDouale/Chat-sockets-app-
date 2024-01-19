package com.example.ai16_project_chat.controller;

import com.example.ai16_project_chat.service.ConnectedUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectedUsersController {
    @Autowired
    private ConnectedUsersService connectedUsersService;
}
