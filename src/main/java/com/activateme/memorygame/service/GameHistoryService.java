package com.activateme.memorygame.service;


import com.activateme.memorygame.entity.GameHistory;
import com.activateme.memorygame.repository.GameHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameHistoryService {
    private final GameHistoryRepository gameHistoryRepository;

    public GameHistoryService(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    public GameHistory saveGameHistory(GameHistory gameHistory) {
        return gameHistoryRepository.save(gameHistory);
    }

    public List<GameHistory> getUserGameHistory(Long userId) {
        return gameHistoryRepository.findByUserId(userId);
    }
}
