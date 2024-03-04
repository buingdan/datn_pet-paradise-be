package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.RoleRepository;
import com.example.petparadisebe.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser (User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return  userRepository.save(user);
    }

    public Role saveRole (Role role){
        return roleRepository.save(role);
    }

    public void addToUser (String username, String rolename){
        User user = userRepository.findByEmail(username).get();
        Role role = roleRepository.findByName(rolename);
        user.getRoles().add(role);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
