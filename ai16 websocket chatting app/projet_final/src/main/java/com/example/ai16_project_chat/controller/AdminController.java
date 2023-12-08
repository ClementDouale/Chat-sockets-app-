package com.example.ai16_project_chat.controller;


import com.example.ai16_project_chat.errors.ChannelAlreadyExistException;
import com.example.ai16_project_chat.models.*;
import com.example.ai16_project_chat.repository.ChannelRepository;
import com.example.ai16_project_chat.repository.ConnectedUsersRepository;
import com.example.ai16_project_chat.repository.UserRepository;
import com.example.ai16_project_chat.service.ChannelService;
import com.example.ai16_project_chat.service.ConnectedUsersService;
import com.example.ai16_project_chat.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ConnectedUsersRepository connectedUsersRepository;

    @Autowired
    ConnectedUsersService connectedUsersService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ChannelService channelService;

    @Autowired
    MyUserDetailsService userService;

    @GetMapping("/")
    public String displayAdminPage(Model model){
        return displayListChannels(model, Optional.of(1), Optional.of(5));
    }

    @GetMapping("/listChannels")
    public String displayListChannels(Model model,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Channel> channelPage = channelService.findPaginatedChannels(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("channelPage", channelPage);

        int totalPages = channelPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        List<Channel> channelsList =  channelService.loadChannels();
        model.addAttribute("channelsList", channelsList);
        return "admin_list_channels";
    }

    @GetMapping("/listUsers")
    public String displayListUsers(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage = userService.findPaginatedUsers(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        return "admin_list_users";
    }

    @GetMapping("/deleteChannels")
    public String displayDeleteChannels(Model model){
        List<Channel> channelsList =  channelService.loadChannels();
        model.addAttribute("channelsList", channelsList);
        return "admin_delete_channels";
    }

    @PostMapping("/deleteChannels")
    public String deleteChannels(Model model, @RequestParam String channelId){
        long id=Long.parseLong(channelId);
        channelService.deleteChannel(channelService.loadChannelById(id));
        return displayAdminPage(model);
    }

    @GetMapping("/deleteUsers")
    public String displayDeleteUsers(Model model){
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        return "admin_delete_users";
    }

    @PostMapping("/deleteUsers")
    public String deleteUsers(Model model, @RequestParam String userId){
        long id=Long.parseLong(userId);
        userService.deleteUser(userRepository.getById(id));
        return displayAdminPage(model);
    }

    @GetMapping("/createChannels")
    public String displayCreateChannels(Model model){
        Channel channel = new Channel();
        model.addAttribute("ChannelObject", channel);
        return "admin_create_channels";
    }

    @PostMapping("/createChannels")
    public String createChannels(Model model, @ModelAttribute Channel ChannelObject) throws ChannelAlreadyExistException {
        channelService.createChannel(ChannelObject);
        return displayAdminPage(model);
    }

    @GetMapping("/subscribeUserToChannel")
    public String displayAddUserToChannel(Model model) throws ChannelAlreadyExistException {
        ConnectedUsers connectedUsers = new ConnectedUsers();
        model.addAttribute("ConnectedUsersObject", connectedUsers);
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        List<Channel> channelsList =  channelService.loadChannels();
        model.addAttribute("channelsList", channelsList);
        return "admin_add_user_to_channel";
    }

    @PostMapping("/subscribeUserToChannel")
    public String addUserToChannel(Model model, @ModelAttribute ConnectedUsers ConnectedUserObject) throws ChannelAlreadyExistException {
        connectedUsersService.addUserToChannel(ConnectedUserObject);
        return displayAdminPage(model);
    }

    @GetMapping("/unsubscribeUserToChannel")
    public String displayRemoveUserFromChannel(Model model) throws ChannelAlreadyExistException {
        ConnectedUsers connectedUsers = new ConnectedUsers();
        model.addAttribute("ConnectedUsersObject", connectedUsers);
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        List<Channel> channelsList =  channelService.loadChannels();
        model.addAttribute("channelsList", channelsList);
        return "admin_add_user_to_channel";
    }

    @PostMapping("/unsubscribeUserToChannel")
    public String removeUserToChannel(Model model, @ModelAttribute ConnectedUsers ConnectedUserObject) throws ChannelAlreadyExistException {
        connectedUsersService.removeUserFromChannel(ConnectedUserObject);
        return displayAdminPage(model);
    }

    @GetMapping("/grantAdminToUser")
    public String displayGrantAdminToUsers(Model model){
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        return "admin_grant_admin";
    }

    @PostMapping("/grantAdminToUsers")
    public String addUserToAdmin(Model model, @RequestParam String userId){
        long id=Long.parseLong(userId);
        userService.grantAdminToUser(userRepository.getById(id));
        return displayAdminPage(model);
    }

    @GetMapping("/removeAdminToUsers")
    public String displayRemoveAdminFromUsers(Model model){
        List<User> userList =  userService.loadUsers();
        model.addAttribute("usersList", userList);
        return "admin_delete_admin";
    }

    @PostMapping("/removeAdminToUsers")
    public String removeUserToAdmin(Model model, @RequestParam String userId){
        long id=Long.parseLong(userId);
        userService.removeAdminToUser(userRepository.getById(id));
        return displayAdminPage(model);
    }
}
