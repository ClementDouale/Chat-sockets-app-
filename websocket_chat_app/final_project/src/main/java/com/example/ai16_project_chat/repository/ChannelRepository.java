package com.example.ai16_project_chat.repository;

import com.example.ai16_project_chat.models.Channel;
import com.example.ai16_project_chat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel,Long> {
    Optional<Channel> findByChannelName(String channelName);
    Optional<Channel> findById(Long id);
}
