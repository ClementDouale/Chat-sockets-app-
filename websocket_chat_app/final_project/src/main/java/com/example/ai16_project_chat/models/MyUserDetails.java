package com.example.ai16_project_chat.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private final Long id;
    private final String userName;
    private final String password;
    private final String mail;
    private final boolean active;
    private final List<GrantedAuthority> authorities;

    public MyUserDetails(User user){
        this.userName = user.getFirstname();
        this.password = user.getHashPwd();
        this.mail = user.getMail();
        this.active = true;
        this.authorities = (List<GrantedAuthority>) user.getAuthorities();
        this.id = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getMail() {
        return mail;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public boolean getAdmin(){
        if(this.authorities.contains(new SimpleGrantedAuthority("ADMIN"))) return true;
        else return false;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
