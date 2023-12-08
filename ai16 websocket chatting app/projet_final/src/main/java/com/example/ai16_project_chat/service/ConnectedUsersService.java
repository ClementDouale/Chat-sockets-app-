package com.example.ai16_project_chat.service;

import com.example.ai16_project_chat.errors.*;
import com.example.ai16_project_chat.models.Channel;
import com.example.ai16_project_chat.models.ConnectedUsers;
import com.example.ai16_project_chat.models.MyUserDetails;
import com.example.ai16_project_chat.models.User;
import com.example.ai16_project_chat.repository.ChannelRepository;
import com.example.ai16_project_chat.repository.ConnectedUsersRepository;
import com.example.ai16_project_chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectedUsersService {
    @Autowired
    ConnectedUsersRepository connectedUsersRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ChannelService channelService;

    @Autowired
    MyUserDetailsService userService;

    public ConnectedUsers addUserToChannel(Channel channel, User user) throws ChannelNotFoundException, UserNotFoundException {
        if(!channelService.checkIfChannelExist(channel.getChannelName())){
            throw new ChannelNotFoundException();
        }
        if(!userService.checkIfUserExist(user.getMail())){
            throw new UserNotFoundException();
        }

        ConnectedUsers connectedUsers = new ConnectedUsers();
        connectedUsers.setUser(user.getId());
        connectedUsers.setChannel(channel.getId());

        return connectedUsersRepository.save(connectedUsers);
    }

    public void removeUserFromChannel(Channel channel, User user) {
        ConnectedUsers connectedUsers = connectedUsersRepository
                .findConnectedUsersByUserAndChannel(user.getId(),
                        channel.getId()).orElseThrow(ConnectedUsersNotFoundException::new);
        connectedUsersRepository.delete(connectedUsers);
    }

    public void removeUserFromAllChannels(User user) {
        List<ConnectedUsers> connectedUsers = connectedUsersRepository
                .findAllByUser(user.getId());

        if(!connectedUsers.isEmpty()) connectedUsersRepository.deleteAll(connectedUsers);
    }

    public void removeAllUsersFromChannel(Channel channel) {
        List<ConnectedUsers> connectedUsers = connectedUsersRepository
                .findAllByChannel(channel.getId());

        if(!connectedUsers.isEmpty()) connectedUsersRepository.deleteAll(connectedUsers);
    }

    public List<Channel> getChannelsOfUser(MyUserDetails myUserDetails) {
        List<ConnectedUsers> connectedUsers = connectedUsersRepository
                .findAllByUser(myUserDetails.getId());

        List<Channel> result = new ArrayList<>();

        if(connectedUsers.isEmpty()) return result;

        for(ConnectedUsers con : connectedUsers){
            result.add(channelRepository.getById(con.getChannel()));
        }

        return result;
    }

    public List<User> getUsersOfChannel(Channel channel) {
        List<ConnectedUsers> connectedUsers = connectedUsersRepository
                .findAllByChannel(channel.getId());

        if(connectedUsers.isEmpty()) throw new ConnectedUsersNotFoundException();

        List<User> result = new ArrayList<>();

        for(ConnectedUsers con : connectedUsers){
            result.add(userRepository.getById(con.getUser()));
        }

        return result;
    }

    public ConnectedUsers addUserToChannel(ConnectedUsers connectedUsers) {
        return connectedUsersRepository.save(connectedUsers);
    }

    public void removeUserFromChannel(ConnectedUsers connectedUsers) {
        connectedUsersRepository.delete(connectedUsers);
    }
}
