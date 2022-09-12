package com.example.demo.services;

import com.example.demo.entities.RoleEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requests.CreateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final String defaultImg = "https://www.mordeo.org/files/uploads/2021/01/3D-Crewmate-Among-Us-Dark-Background-4K-Ultra-HD-Mobile-Wallpaper-scaled.jpg";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void createUser(CreateUserRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAvatarImgUrl(defaultImg);
        userEntity.setName(request.getName());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setUsername(request.getUsername());
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2L);
        roleEntities.add(roleEntity);
        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {
        UserEntity referenceById = userRepository.getReferenceById(id);
        userRepository.delete(referenceById);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
        return new User(userEntity);
    }

    public boolean checkIfUserExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public void editUser(User user) {
        UserEntity userById = userRepository.findById(user.getId()).orElseThrow(NullPointerException::new);
        userById.setName(user.getName());
        userById.setAvatarImgUrl(user.getAvatarImgUrl());
        userRepository.save(userById);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(User::new).collect(Collectors.toList());
    }

    public void addUserRole(Long userId, Long roleId) {
        UserEntity userEntity = getUserById(userId);
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity referenceById = roleRepository.getReferenceById(roleId);
        roleEntity.setId(roleId);
        userEntity.getRoles().add(referenceById);
        userRepository.save(userEntity);
    }

    public void removeRole(Long userId, Long roleId) {
        UserEntity userEntity = getUserById(userId);
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity referenceById = roleRepository.getReferenceById(roleId);
        roleEntity.setId(roleId);
        userEntity.getRoles().remove(referenceById);
        userRepository.save(userEntity);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll().stream().map(Role::new).collect(Collectors.toList());
    }
}