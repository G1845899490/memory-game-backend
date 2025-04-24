package com.activateme.memorygame.controller;

import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.LeaderboardService;
import com.activateme.memorygame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LeaderboardController {
    @Autowired
    private LeaderboardService leaderboardService;
    @Autowired
    private UserService userService;

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> getLeaderboard(
            @RequestParam("gameType") String gameType,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {
        // 从 SecurityContext 中获取当前用户名（假设使用 Spring Security）
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(currentUsername);
        Long currentUserId = user != null ? user.getId() : null;

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);
        return ResponseEntity.ok(result);
    }
}
