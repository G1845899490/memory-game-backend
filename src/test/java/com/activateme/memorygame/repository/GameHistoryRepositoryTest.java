package com.activateme.memorygame.repository;

import com.activateme.memorygame.entity.GameHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 启动 Spring 上下文
@Transactional // 每个测试方法在事务中运行，结束后回滚，避免修改数据库
class GameHistoryRepositoryTest {

    @Autowired
    private GameHistoryRepository repository;

    //@BeforeEach
    //void setUp() {
    //    // 清空测试数据（谨慎：仅限测试数据库）
    //    repository.deleteAll();
    //
    //    // 插入测试数据
    //    LocalDateTime now = LocalDateTime.now();
    //    repository.saveAll(List.of(
    //            new GameHistory(1L, "DiscoverNewLocation", 600, now),
    //            new GameHistory(1L, "DiscoverNewLocation", 500, now),
    //            new GameHistory(2L, "DiscoverNewLocation", 700, now),
    //            new GameHistory(3L, "DiscoverNewLocation", 800, now),
    //            new GameHistory(4L, "DiscoverNewLocation", 200, now),
    //            new GameHistory(4L, "DiscoverNewLocation", 150, now),
    //            new GameHistory(5L, "DiscoverNewLocation", 900, now),
    //            new GameHistory(1L, "RecallOldLocation", 400, now) // 不同游戏类型
    //    ));
    //}

    @Test // 标记 JUnit 测试方法
    void testFindByUserId() {
        List<GameHistory> histories = repository.findByUserId(10L);
        //assertEquals(18, histories.size(), "测试失败"); // 2条 DiscoverNewLocation + 1条 RecallOldLocation
        //assertTrue(histories.stream().anyMatch(h -> h.getScore() == 600));
        //assertTrue(histories.stream().anyMatch(h -> h.getScore() == 500));
        //assertTrue(histories.stream().anyMatch(h -> h.getScore() == 400));
        System.out.println(histories.size());
    }

    @Test
    void testFindTopScoresByGameType() {
        List<Object[]> topScores = repository.findTopScoresByGameType("DiscoverNewContent", 0);
        System.out.println(topScores.size());
        for (int i = 0; i < topScores.size(); i++) {
            System.out.println(topScores.get(i)[0]+" "+topScores.get(i)[1]);
        }
        //assertEquals(5, topScores.size());
        //assertEquals(5L, topScores.get(0)[0]); // userId=5
        //assertEquals(900, topScores.get(0)[1]); // score=900
        //assertEquals(3L, topScores.get(1)[0]); // userId=3
        //assertEquals(800, topScores.get(1)[1]); // score=800
    }

    @Test
    void testFindTopScoresByGameTypeWithOffset() {
        List<Object[]> topScores = repository.findTopScoresByGameType("DiscoverNewLocation", 0);
        System.out.println(topScores.size());
        for (int i = 0; i < topScores.size(); i++) {
            System.out.println(topScores.get(i)[0]+" "+topScores.get(i)[1]);
        }
        //assertEquals(3, topScores.size());
        //assertEquals(2L, topScores.get(0)[0]); // userId=2
        //assertEquals(700, topScores.get(0)[1]); // score=700
    }

    @Test
    void testCountDistinctUsersByGameType() {
        int count = repository.countDistinctUsersByGameType("DiscoverNewLocation");
        System.out.println(count);
        //assertEquals(5, count);
        int countOther = repository.countDistinctUsersByGameType("RecallOldLocation");
        System.out.println(countOther);
        //assertEquals(1, countOther);
    }

    @Test
    void testFindMaxScoreByGameTypeAndUserId() {
        Integer maxScore = repository.findMaxScoreByGameTypeAndUserId("DiscoverNewLocation", 10L);
        System.out.println(maxScore);
        //assertEquals(600, maxScore);
        Integer noScore = repository.findMaxScoreByGameTypeAndUserId("DiscoverNewLocation", 9L);
        System.out.println(noScore);
        //assertNull(noScore);
    }

    @Test
    void testFindRankByGameTypeAndUserId() {
        Integer rank = repository.findRankByGameTypeAndUserId("DiscoverNewLocation", 9L);
        System.out.println(rank);
        //assertEquals(5, rank); // userId=4 (score=200) 低于 900,800,700,600
        Integer topRank = repository.findRankByGameTypeAndUserId("DiscoverNewLocation", 10L);
        System.out.println(topRank);
        //assertEquals(1, topRank); // userId=5 (score=900) 是第一
        //Integer noRank = repository.findRankByGameTypeAndUserId("DiscoverNewLocation", 999L);
        //assertNull(noRank); // 不存在用户
    }
}
