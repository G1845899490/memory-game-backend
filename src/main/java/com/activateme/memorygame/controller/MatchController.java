package com.activateme.memorygame.controller;

import com.activateme.memorygame.entity.User;
import com.activateme.memorygame.service.GameHistoryService;
import com.activateme.memorygame.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

// 所有用户共享同一个后端代码和 MatchController 实例
// Spring Boot 是一个单例模式的服务端框架，MatchController 是一个 @RestController 单例。
// 所有前端请求（无论来自不同用户或设备）都由同一个 MatchController 处理。
// matchQueue、rooms 和 userToRoomId 是 static 变量，存储在 JVM 的静态内存中，共享给所有请求
@RestController
@RequestMapping("/api/match")
public class MatchController {
    private final UserService userService;
    private final GameHistoryService gameHistoryService;

    public MatchController(UserService userService, GameHistoryService gameHistoryService) {
        this.userService = userService;
        this.gameHistoryService = gameHistoryService;
    }
    private static final Queue<Map<String, Object>> matchQueue = new ConcurrentLinkedQueue<>();
    private static final Map<String, List<Map<String, Object>>> rooms = new ConcurrentHashMap<>();


    // 发起匹配请求
    @PostMapping("/apply")
    public boolean apply(@RequestBody Map<String, String> request) {
        System.out.println("apply()执行，请求参数：" + request);

        String username = request.get("username");
        String gameType = request.get("gameType");
        String gameLevel = request.get("gameLevel");
        Map<String, Object> matchRequest = new HashMap<>();
        matchRequest.put("username", username);
        matchRequest.put("gameType", gameType);
        matchRequest.put("gameLevel", gameLevel);

        synchronized (matchQueue) {
            boolean matching = matchQueue.add(matchRequest);
            matchQueue.forEach(e -> {
                System.out.println("matchRequest: " + e);
            });
            System.out.println("matching: " + matching);
            return matching;
        }
    }

    // 后端自动匹配
    // 定时函数，每隔一段时间检查是否有两个用户匹配。可以使用Spring 的 @Scheduled 注解来实现定时任务
    @Scheduled(fixedRate = 5000) // 每5秒执行一次
    public void match() {
        //System.out.println("match()执行");
        synchronized (matchQueue) {
            if (matchQueue.size() >= 2) {
                List<Map<String, Object>> players = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    players.add(matchQueue.poll());
                }
                // 同类型游戏才能匹配成功
                if (players.get(0).get("gameType").equals(players.get(1).get("gameType"))) {
                    String roomId = UUID.randomUUID().toString();
                    rooms.put(roomId, players);
                    System.out.println("match成功");
                } else {
                    System.out.println("匹配失败");
                    return;
                }
            }
        }
    }

    // 查询匹配状态
    @GetMapping("/matchquery")
    public String matchQuery() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username + "的matchQuery()执行...");

        AtomicBoolean isMatched = new AtomicBoolean(false);
        AtomicReference<List<Map<String, Object>>> Players = new AtomicReference<>(new ArrayList<>());
        AtomicReference<String> EnemyUsername = new AtomicReference<>("");
        AtomicReference<String> RoomId = new AtomicReference<>("");

        rooms.forEach((roomId, players) -> {
            //System.out.println("rooms: " + roomId + " " + players);

            players.forEach(player -> {
                //System.out.println("player: " + player);
                //System.out.println("player.username: " + player.get("username"));
                if (player.get("username").equals(username)) {
                    System.out.println(username + "的player.get(\"username\") == " + username);
                    isMatched.set(true);
                    RoomId.set(roomId);
                    //System.out.println("匹配成功，房间号：" + roomId);
                    Players.set(players);
                }
            });
        });

        if (isMatched.get()) {
            Players.get().forEach(player -> {
                if (!player.get("username").equals(username) ) {
                    EnemyUsername.set((String) player.get("username"));
                }
            });
            System.out.println(username + "的EnemyUsername: " + EnemyUsername.get());
            //System.out.println("RoomId: " + RoomId.get());
            return RoomId.get() + "," + EnemyUsername.get();
        }

        System.out.println("没有匹配成功: " + username);
        return "no";
    }

    // 按roomId和敌人用户名查询
    @GetMapping("/resultquery/{roomId}/{enemyUsername}")
    public Integer resultQuery(@PathVariable String roomId, @PathVariable String enemyUsername) {
        System.out.println("resultQuery()执行，roomId: " + roomId + ", enemyUsername: " + enemyUsername);
        // 由username得到user_id
        User enemy = userService.findByUsername(enemyUsername);
        Long enemyId = enemy.getId();
        System.out.println("enemyId: " + enemyId);

        Integer enemyScore = gameHistoryService.findScoreByRoomIdAndUserId(roomId, enemyId);
        System.out.println("enemyScore: " + enemyScore);
        return enemyScore;
    }

    // 移除房间
    @PostMapping("/removeroom")
    public void removeRoom(@RequestBody Map<String, String> request) {
        System.out.println("removeRoom()执行，请求参数：" + request);
        String roomId = request.get("roomId");
        System.out.println("roomId: " + roomId);
        rooms.remove(roomId);
    }

    // 从匹配队列中移除指定用户
    @PostMapping("/removeuser")
    public boolean removeUser(@RequestBody Map<String, String> request) {
        System.out.println("removeUser()执行，请求参数：" + request);
        String username = request.get("username");
        System.out.println("username: " + username);

        boolean remove = matchQueue.removeIf(player -> player.get("username").equals(username));
        System.out.println("移除匹配队列中的用户: " + username);
        return remove;
    }


}
