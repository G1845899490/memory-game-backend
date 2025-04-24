package com.activateme.memorygame.service;


import com.activateme.memorygame.entity.GameHistory;
import com.activateme.memorygame.repository.GameHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LeaderBoardServiceTest {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    //@BeforeEach
    //void setUp() {
    //    // 清空测试数据（仅限测试事务）
    //    gameHistoryRepository.deleteAll();
    //
    //    // 插入 15 条测试数据
    //    String gameType = "DiscoverNewContent";
    //    for (long userId = 1; userId <= 15; userId++) {
    //        GameHistory record = new GameHistory();
    //        record.setUserId(userId);
    //        record.setGameType(gameType);
    //        record.setScore((int) (900 - (userId - 1) * 50)); // 900, 850, 800, ..., 200
    //        gameHistoryRepository.save(record);
    //    }
    //}

    @Test
    void testGetLeaderboard_page1_size10() {
        // 测试第 1 页，每页 10 条
        String gameType = "DiscoverNewContent";
        int page = 2;
        int pageSize = 1;
        Long currentUserId = 10L;

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);
        //result.put("leaderboard", leaderboard);
        //result.put("total", total);
        //result.put("myRank", myRankData);
        System.out.println(result.get("leaderboard"));
        System.out.println(result.get("total"));
        System.out.println(result.get("myRank"));


        // 验证结果结构
        //assertNotNull(result);
        //assertEquals(3, result.size(), "Result should contain leaderboard, total, myRank");

        // 验证 leaderboard
        //List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) result.get("leaderboard");
        //assertEquals(10, leaderboard.size(), "Leaderboard should have 10 records");
        //assertEquals(1L, leaderboard.get(0).get("userId"));
        //assertEquals(900, leaderboard.get(0).get("score"));
        //assertEquals(10L, leaderboard.get(9).get("userId"));
        //assertEquals(450, leaderboard.get(9).get("score"));
        //
        //// 验证 total
        //assertEquals(15, result.get("total"), "Total should be 15");
        //
        //// 验证 myRank
        //Map<String, Object> myRank = (Map<String, Object>) result.get("myRank");
        //assertEquals(1, myRank.get("rank"));
        //assertEquals(900, myRank.get("score"));
    }

    @Test
    void testGetLeaderboard_page2_size5() {
        // 测试第 2 页，每页 5 条
        String gameType = "DiscoverNewContent";
        int page = 2;
        int pageSize = 5;
        Long currentUserId = 6L;

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());

        // 验证 leaderboard
        List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) result.get("leaderboard");
        assertEquals(5, leaderboard.size(), "Leaderboard should have 5 records");
        assertEquals(6L, leaderboard.get(0).get("userId"));
        assertEquals(650, leaderboard.get(0).get("score"));
        assertEquals(10L, leaderboard.get(4).get("userId"));
        assertEquals(450, leaderboard.get(4).get("score"));

        // 验证 total
        assertEquals(15, result.get("total"));

        // 验证 myRank
        Map<String, Object> myRank = (Map<String, Object>) result.get("myRank");
        assertEquals(6, myRank.get("rank"));
        assertEquals(650, myRank.get("score"));
    }

    @Test
    void testGetLeaderboard_unrankedUser() {
        // 测试未上榜用户
        String gameType = "DiscoverNewContent";
        int page = 1;
        int pageSize = 10;
        Long currentUserId = 999L; // 不存在的用户

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());

        // 验证 leaderboard
        List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) result.get("leaderboard");
        assertEquals(10, leaderboard.size());

        // 验证 total
        assertEquals(15, result.get("total"));

        // 验证 myRank
        Map<String, Object> myRank = (Map<String, Object>) result.get("myRank");
        assertEquals(101, myRank.get("rank"), "Unranked user should have rank 101");
        assertEquals(0, myRank.get("score"));
    }

    @Test
    void testGetLeaderboard_emptyData() {
        // 测试空数据
        gameHistoryRepository.deleteAll(); // 清空数据
        String gameType = "DiscoverNewContent";
        int page = 1;
        int pageSize = 10;
        Long currentUserId = 1L;

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());

        // 验证 leaderboard
        List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) result.get("leaderboard");
        assertEquals(0, leaderboard.size(), "Leaderboard should be empty");

        // 验证 total
        assertEquals(0, result.get("total"));

        // 验证 myRank
        Map<String, Object> myRank = (Map<String, Object>) result.get("myRank");
        assertEquals(101, myRank.get("rank"));
        assertEquals(0, myRank.get("score"));
    }

    @Test
    void testGetLeaderboard_invalidGameType() {
        // 测试不存在的 gameType
        String gameType = "InvalidGame";
        int page = 1;
        int pageSize = 10;
        Long currentUserId = 1L;

        Map<String, Object> result = leaderboardService.getLeaderboard(gameType, page, pageSize, currentUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());

        // 验证 leaderboard
        List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) result.get("leaderboard");
        assertEquals(0, leaderboard.size(), "Leaderboard should be empty for invalid gameType");

        // 验证 total
        assertEquals(0, result.get("total"));

        // 验证 myRank
        Map<String, Object> myRank = (Map<String, Object>) result.get("myRank");
        assertEquals(101, myRank.get("rank"));
        assertEquals(0, myRank.get("score"));
    }
}
