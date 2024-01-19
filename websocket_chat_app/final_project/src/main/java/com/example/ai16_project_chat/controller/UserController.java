package com.example.ai16_project_chat.controller;

import com.example.ai16_project_chat.errors.UserAlreadyExistException;
import com.example.ai16_project_chat.models.*;
import com.example.ai16_project_chat.service.ConnectedUsersService;
import com.example.ai16_project_chat.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private ConnectedUsersService connectedUsersService;

    @GetMapping("/registration")
    public String displayRegistrationPage(Model model){
        User user = new User();
        model.addAttribute("User", user);
        model.addAttribute("password", "");
        return "create_account";
    }

    @PostMapping("/registration")
    public String registration(Model model, @RequestParam String password, @ModelAttribute User user) throws UserAlreadyExistException {
        if(password.length() < 8) {
            model.addAttribute("error", "Password is too small (8 char minimum)");
            return displayRegistrationPage(model);
        }
        else this.userService.createUser(user, password);
        return displayLoginPage(model);
    }

    @GetMapping("/login")
    public String displayLoginPage(Model model){
        User user = new User();
        model.addAttribute("User", user);
        model.addAttribute("password", "");
        model.addAttribute("msg", "");
        return "login";
    }

    @GetMapping("/chat/{channelID}")
    public String displayChattingPage(Model model, @PathVariable("channelID") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            // LOADING CHANNELS
            List<Channel> channelsList =  connectedUsersService.getChannelsOfUser((MyUserDetails) auth.getPrincipal());
            model.addAttribute("channelsList", channelsList);
            // Getting the channel passed in ID
            if(id == 0 && !channelsList.isEmpty()){
                // If id passed is zero and user has access to channels then we select one channel as default
                model.addAttribute("mainChannel", channelsList.get(0));
            } else if(id != 0 && !channelsList.isEmpty()) {
                // If another id is given and if it is accessible by the user then we add it
                model.addAttribute("mainChannel", channelsList.get(0));
                for(Channel channel : channelsList){
                    if(channel.getId() == id) model.addAttribute("mainChannel", channel);
                }
            } else {
                model.addAttribute("mainChannel", null);
            }

            // LOADING USERS
            List<User> usersListTot = new ArrayList<>();
            List<MyUserDetails> usersList = new ArrayList<>();
            if(model.getAttribute("mainChannel") != null) usersListTot =  connectedUsersService.getUsersOfChannel((Channel) model.getAttribute("mainChannel"));
            // Getting the username of each user to prevent from passing objects and leaking data
            for(User usr : usersListTot) usersList.add(new MyUserDetails(usr));
            model.addAttribute("usersList", usersList);
            return "chat";
        }
        return "login";
    }

}
