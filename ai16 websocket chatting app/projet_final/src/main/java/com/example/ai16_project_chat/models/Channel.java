package com.example.ai16_project_chat.models;

import javax.persistence.*;

@Entity
@Table(name="channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "channel_name")
    private String channelName;
    @Column(name = "max_users")
    private Integer maxUsers;
    //@ManyToMany
    //List<User> users;

    public Long getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }
}
