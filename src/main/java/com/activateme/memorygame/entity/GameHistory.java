// src/main/java/com/example/memorygame/model/GameHistory.java
package com.activateme.memorygame.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_history")
@Data
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "game_type", nullable = false)
    private String gameType;

    @Column
    private Integer score;

    // 存储灵活的游戏数据
    @Column(name = "game_data", columnDefinition = "TEXT")
    private String gameData;

    @Column(name = "played_at")
    private LocalDateTime playedAt;

    @Column(name = "room_id")
    private String roomId;
}