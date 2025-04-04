package com.activateme.memorygame.service;


import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        Boolean match = passwordEncoder.matches(password, user.getPassword());
        if (user != null && match) {
            return generateJwtToken(user);
        }
        if (!match){
            return "passwordwrong";
        }
        throw new RuntimeException("Invalid credentials");
    }

    public String changePassword(Long userId, String newPassword, String oldPassword) {
        // 获取当前用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 验证旧密码
        Boolean match = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!match) {
            return "Old password is incorrect";
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "Password updated";
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        // 检查用户名是否已存在
        return userRepository.findByUsername(username) != null;
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24小时
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}