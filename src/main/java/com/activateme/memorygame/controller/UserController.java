package com.activateme.memorygame.controller;

import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 更新资源操作，用put方式
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        userService.changePassword(user.getId(), newPassword);
        return ResponseEntity.ok("Password updated");
    }
}
