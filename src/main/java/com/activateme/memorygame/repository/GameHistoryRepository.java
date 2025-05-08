package com.activateme.memorygame.repository;

import com.activateme.memorygame.entity.GameHistory;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findByUserId(Long userId);

    // 获取某游戏类型的前 100 名玩家的最高分（分页）
    @Query(value = "SELECT g.user_id, MAX(g.score) as score " +
            "FROM game_history g " +
            "WHERE g.game_type = :gameType " +
            "GROUP BY g.user_id " +
            "ORDER BY score DESC " +
            "LIMIT 100 OFFSET :offset", nativeQuery = true)
    List<Object[]> findTopScoresByGameType(@Param("gameType") String gameType, @Param("offset") int offset);

    // 获取某游戏类型的玩家总数（上限 100）
    @Query(value = "SELECT COUNT(DISTINCT g.user_id) " +
            "FROM game_history g " +
            "WHERE g.game_type = :gameType " +
            "LIMIT 100", nativeQuery = true)
    int countDistinctUsersByGameType(@Param("gameType") String gameType);

    // 获取某个用户在某游戏类型中的最高分
    @Query(value = "SELECT MAX(g.score) " +
            "FROM game_history g " +
            "WHERE g.game_type = :gameType AND g.user_id = :userId", nativeQuery = true)
    Integer findMaxScoreByGameTypeAndUserId(@Param("gameType") String gameType, @Param("userId") Long userId);

    // 获取某个用户在某游戏类型中的排名
    //@Query(value = "SELECT COUNT(*) + 1 " +
    //        "FROM (SELECT MAX(score) as score " +
    //        "FROM game_history " +
    //        "WHERE game_type = :gameType " +
    //        "GROUP BY user_id " +
    //        "HAVING MAX(score) > (SELECT MAX(score) " +
    //        "FROM game_history " +
    //        "WHERE game_type = :gameType AND user_id = :userId)) as higher_scores",
    //        nativeQuery = true)
    //Integer findRankByGameTypeAndUserId(@Param("gameType") String gameType, @Param("userId") Long userId);

    @Query(value = "SELECT CASE " +
            "WHEN EXISTS (" +
            "    SELECT 1 " +
            "    FROM game_history " +
            "    WHERE game_type = :gameType AND user_id = :userId AND score IS NOT NULL" +
            ") THEN (" +
            "    SELECT COUNT(*) + 1 " +
            "    FROM (" +
            "        SELECT MAX(score) as score " +
            "        FROM game_history " +
            "        WHERE game_type = :gameType " +
            "        GROUP BY user_id " +
            "        HAVING MAX(score) > (" +
            "            SELECT MAX(score) " +
            "            FROM game_history " +
            "            WHERE game_type = :gameType AND user_id = :userId" +
            "        )" +
            "    ) as higher_scores" +
            ") " +
            "ELSE NULL " +
            "END",
            nativeQuery = true)
    Integer findRankByGameTypeAndUserId(@Param("gameType") String gameType, @Param("userId") Long userId);

    // 按roomId和敌人用户名查询敌人的分数
    @Query(value = "SELECT score " +
            "FROM game_history " +
            "WHERE room_id = :roomId AND user_id = :enemyId", nativeQuery = true)
    Integer findScoreByRoomIdAndUserId(@Param("roomId") String roomId, @Param("enemyId") Long enemyId);
}