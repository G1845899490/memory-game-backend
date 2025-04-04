package com.activateme.memorygame.controller;

import com.activateme.memorygame.entity.GameHistory;
import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.GameHistoryService;
import com.activateme.memorygame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameHistoryService gameHistoryService;
    private final UserService userService;

    public GameController(GameHistoryService gameHistoryService, UserService userService) {
        this.gameHistoryService = gameHistoryService;
        this.userService = userService;
    }

    @PostMapping("/history")
    public ResponseEntity<?> saveGameHistory(@RequestBody GameHistory gameHistory) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        gameHistory.setUser(user);
        GameHistory saved = gameHistoryService.saveGameHistory(gameHistory);
        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("userId", saved.getUser().getId());
        response.put("userName", saved.getUser().getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameHistory>> getGameHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<GameHistory> history = gameHistoryService.getUserGameHistory(user.getId());
        // 去除List<GameHistory> history中的user属性
        history.forEach(gameHistory -> gameHistory.setUser(null));
        return ResponseEntity.ok(history);
    }
}