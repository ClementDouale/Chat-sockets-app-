package com.example.ai16_project_chat.repository;

import com.example.ai16_project_chat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLastname(String lastname);
    Optional<User> findByMail(String mail);
    Optional<User> findById(Long id);
}
