package com.example.ai16_project_chat.repository;

import com.example.ai16_project_chat.models.Channel;
import com.example.ai16_project_chat.models.ConnectedUsers;
import com.example.ai16_project_chat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectedUsersRepository extends JpaRepository<ConnectedUsers,Long> {
    List<ConnectedUsers> findAllByUser(Long user);
    List<ConnectedUsers> findByUser(Long user);
    List<ConnectedUsers> findAllByChannel(Long channel);
    Optional<ConnectedUsers> findConnectedUsersByUserAndChannel(Long user, Long channel);
    Optional<ConnectedUsers> findConnectedUsersByChannel(Long channel);
}
