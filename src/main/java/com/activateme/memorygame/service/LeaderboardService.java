package com.activateme.memorygame.service;

import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.repository.GameHistoryRepository;
import com.activateme.memorygame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    @Autowired
    private GameHistoryRepository gameHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getLeaderboard(String gameType, int page, int pageSize, Long currentUserId) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        int limit = pageSize; // limit?与pageSize的关系？

        // 获取排行榜数据（前 100 名中的分页部分）
        List<Object[]> topScores = gameHistoryRepository.findTopScoresByGameType(gameType, offset);
        List<Map<String, Object>> leaderboard = topScores.stream()
                .map(row -> {
                    //System.out.println(row);
                    //System.out.println(row.getClass());
                    //System.out.println(row[0].getClass());
                    //System.out.println(row[1].getClass());

                    Map<String, Object> player = new HashMap<>();
                    // 把row[0]转为username
                    Long userId = ((BigInteger) row[0]).longValue();
                    User user = userRepository.getUserById(userId);
                    String username = user.getUsername();
                    player.put("username", username);
                    //player.put("userId", row[0]);
                    player.put("score", row[1]);
                    return player;
                })
                .limit(limit) // Stream 中用 .limit(limit) 截取前 pageSize 条，实现了内存中的分页：限制每页条数：这意味着数据库可能返回较多数据（最多 100 条），但你在 Java 中只取需要的部分
                // page=1, pageSize=10 → offset=0，返回第 1-100 名，截取 1-10。
                // page=2, pageSize=10 → offset=10，返回第 11-100 名，截取 11-20
                .collect(Collectors.toList());

        // 获取总数（上限 100）
        int total = Math.min(gameHistoryRepository.countDistinctUsersByGameType(gameType), 100);

        // 获取我的最高分和排名
        Integer myScore = gameHistoryRepository.findMaxScoreByGameTypeAndUserId(gameType, currentUserId);
        Integer myRank = gameHistoryRepository.findRankByGameTypeAndUserId(gameType, currentUserId);
        System.out.println("==========" + currentUserId + "的rank是: " + myRank + "==========");
        System.out.println("myScore: " + myScore);
        System.out.println("myRank: " + myRank);
        Map<String, Object> myRankData = new HashMap<>();
        //myRankData.put("rank", myRank != null && myRank <= 100 ? myRank : 101); // 未上榜时为 101
        myRankData.put("rank", myRank != null ? myRank : 101); // 空时为 101
        myRankData.put("score", myScore != null ? myScore : 0);

        // 组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("leaderboard", leaderboard);
        result.put("total", total);
        result.put("myRank", myRankData);

        return result;
    }
}