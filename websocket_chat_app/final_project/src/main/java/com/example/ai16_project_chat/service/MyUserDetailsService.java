package com.example.ai16_project_chat.service;

import com.example.ai16_project_chat.errors.ChannelNotFoundException;
import com.example.ai16_project_chat.errors.UserAlreadyExistException;
import com.example.ai16_project_chat.errors.UserNotFoundException;
import com.example.ai16_project_chat.models.Channel;
import com.example.ai16_project_chat.models.MyUserDetails;
import com.example.ai16_project_chat.models.User;
import com.example.ai16_project_chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ConnectedUsersService connectedUsersService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String host;


    public User createUser(User user, String password) throws UserAlreadyExistException {
        Date now = new Date();

        //Let's check if user already registered with us
        if(checkIfUserExist(user.getMail())){
            throw new UserAlreadyExistException();
        }

        encodePassword(user, password);
        user.setRegistrationDate(now);
        user.setAdmin(false);

        sendMail(user.getMail(),"creation_compte", "votre compte pour notre application de chat a été créer");

        return userRepository.save(user);
    }

    public void deleteUser(User user){
        if(!checkIfUserExist(user.getMail())){
            throw new UserNotFoundException();
        }

        connectedUsersService.removeUserFromAllChannels(user);
        userRepository.delete(user);
    }

    public void grantAdminToUser(User user){
        if(!checkIfUserExist(user.getMail())){
            throw new UserNotFoundException();
        }
        user.setAdmin(true);
        userRepository.save(user);
    }

    public void removeAdminToUser(User user){
        if(!checkIfUserExist(user.getMail())){
            throw new UserNotFoundException();
        }
        user.setAdmin(false);
        userRepository.save(user);
    }

    public boolean checkIfUserExist(String email) {
        return userRepository.findByMail(email).isPresent();
    }

    private void encodePassword(User user, String password){
        user.setHashPwd(passwordEncoder.encode(password));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByMail(email).orElseThrow(UserNotFoundException::new);
        return new MyUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return new MyUserDetails(user);
    }

    public List<User> loadUsers() {
        return userRepository.findAll();
    }

    public void sendMail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public Page<User> findPaginatedUsers(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        List<User> users = loadUsers();

        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        Page<User> userPage
                = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), users.size());

        return userPage;
    }
}
