package com.example.demo.models;

import com.example.demo.entities.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class User implements UserDetails {
    private static final String rolePREFIX = "ROLE_";
    private Long id;
    private String name;
    private String username;
    private String password;
    private List<Comment> comments;
    private String avatarImgUrl;
    private List<Role> roles = new ArrayList<>();

    public User(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.comments = userEntity.getCommentEntity()
                .stream()
                .map(Comment::new)
                .collect(Collectors.toList());
        this.roles = userEntity.getRoles().stream().map(Role::new).collect(Collectors.toList());
        this.avatarImgUrl = userEntity.getAvatarImgUrl();
    }

    public User() {

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(it -> new Role(rolePREFIX + it.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
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
        return true;
    }

    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
    }

    public boolean isAuthor() {
        return roles.stream().anyMatch(role -> role.getRoleName().equals("AUTHOR"));
    }

    public boolean isUser() {
        return roles.stream().anyMatch(role -> role.getRoleName().equals("USER"));
    }

    public boolean canEditArticles() {
        return isAdmin() || isAuthor();
    }
}