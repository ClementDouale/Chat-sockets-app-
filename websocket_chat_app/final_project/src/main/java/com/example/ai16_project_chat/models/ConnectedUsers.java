package com.example.ai16_project_chat.models;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="connected_users")
@IdClass(ConnectedUsers.class)
public class ConnectedUsers implements Serializable {
    @Id
    @Column(name = "user")
    private Long user;
    @Id
    @Column(name = "channel")
    private Long channel;

    public ConnectedUsers ConnectedUsers(){
        return new ConnectedUsers();
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getChannel() {
        return channel;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }
}
