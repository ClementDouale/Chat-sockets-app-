package com.example.ai16_project_chat.service;

import com.example.ai16_project_chat.errors.ChannelAlreadyExistException;
import com.example.ai16_project_chat.errors.ChannelNotFoundException;
import com.example.ai16_project_chat.models.Channel;
import com.example.ai16_project_chat.models.User;
import com.example.ai16_project_chat.service.ConnectedUsersService;
import com.example.ai16_project_chat.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ConnectedUsersService connectedUsersService;

    public Channel createChannel(Channel channel) throws ChannelAlreadyExistException {
        if(checkIfChannelExist(channel.getChannelName())){
            throw new ChannelAlreadyExistException();
        }

        return channelRepository.save(channel);
    }

    public void deleteChannel(Channel channel){
        if(!checkIfChannelExist(channel.getChannelName())){
            throw new ChannelNotFoundException();
        }

        connectedUsersService.removeAllUsersFromChannel(channel);
        channelRepository.delete(channel);
    }

    public boolean checkIfChannelExist(String channelName) {
        return channelRepository.findByChannelName(channelName).isPresent();
    }

    public Channel loadChannelByNames(String channelName) {
        return channelRepository.findByChannelName(channelName).orElseThrow(ChannelNotFoundException::new);
    }

    public Channel loadChannelById(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
    }

    public List<Channel> loadChannels() {
        return channelRepository.findAll();
    }

    public Page<Channel> findPaginatedChannels(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Channel> list;

        List<Channel> channels = loadChannels();

        if (channels.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, channels.size());
            list = channels.subList(startItem, toIndex);
        }

        Page<Channel> channelPage
                = new PageImpl<Channel>(list, PageRequest.of(currentPage, pageSize), channels.size());

        return channelPage;
    }
}
