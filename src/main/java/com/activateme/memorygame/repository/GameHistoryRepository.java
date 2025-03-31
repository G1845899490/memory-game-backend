package com.activateme.memorygame.repository;

import com.activateme.memorygame.entity.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findByUserId(Long userId);
}