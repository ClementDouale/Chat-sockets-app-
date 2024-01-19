package com.example.ai16_project_chat.errors;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {super("User already exists.");}
}
