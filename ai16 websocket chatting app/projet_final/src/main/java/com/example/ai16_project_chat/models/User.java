package com.example.ai16_project_chat.models;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "first_name")
    private String firstname;
    @NonNull
    @Column(name = "last_name")
    private String lastname;
    @NonNull
    @Column(name = "hash_pwd")
    private String hashpwd;     //private String password;
    @NonNull
    @Column(name = "registration_date")
    private Date registrationdate;
    @NonNull
    private String mail;
    private boolean admin;
    //@ManyToMany
    //List<Channel> channels;

    public User User() {
        return new User();
    }

    public String getMail() {
        return mail;
    }

    public Long getId() {
        return id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHashPwd() {
        return hashpwd;
    }

    public void setHashPwd(String hashPwd) {
        this.hashpwd = hashPwd;
    }

    public Date getRegistrationDate() {
        return registrationdate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationdate = registrationDate;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.isAdmin()) authorities.add(new SimpleGrantedAuthority("ADMIN"));
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return getHashPwd();
    }

    @Override
    public String getUsername() {
        return this.firstname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
