package com.activateme.memorygame.controller;

import com.activateme.memorygame.dto.PasswordRequest;
import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequest request) {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        String result = userService.changePassword(user.getId(), newPassword, oldPassword);

        if (result.equals("Password updated")) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
