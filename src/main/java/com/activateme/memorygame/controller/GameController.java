package com.activateme.memorygame.controller;

import com.activateme.memorygame.entity.GameHistory;
import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.GameHistoryService;
import com.activateme.memorygame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<GameHistory> saveGameHistory(@RequestBody GameHistory gameHistory) {
        System.out.println("gameHistory.getPlayedAt()："+gameHistory.getPlayedAt());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        System.out.println("saveGameHistory user.getId()："+user.getId());
        gameHistory.setUser(user);
        GameHistory saved = gameHistoryService.saveGameHistory(gameHistory);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameHistory>> getGameHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        System.out.println("getGameHistory user.getId()："+user.getId());
        List<GameHistory> history = gameHistoryService.getUserGameHistory(user.getId());
        return ResponseEntity.ok(history);
    }
}