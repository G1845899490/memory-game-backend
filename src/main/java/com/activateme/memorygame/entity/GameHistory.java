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

    // 可以保留这个字段以保持向后兼容
    //@Column(name = "completion_time")
    //private Integer completionTime;

    // 添加新字段存储灵活的游戏数据
    @Column(name = "game_data", columnDefinition = "TEXT")
    private String gameData;

    @Column(name = "played_at")
    private LocalDateTime playedAt;
}