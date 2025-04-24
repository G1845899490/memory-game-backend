//package com.activateme.memorygame;
//
//import com.activateme.memorygame.entity.User;
//import com.activateme.memorygame.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class UserDataInitializer {
//
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public UserDataInitializer(BCryptPasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    public CommandLineRunner initUsers(UserRepository userRepository) {
//        return args -> {
//            // 从 testuser20 到 testuser100
//            for (int i = 104; i <= 1000; i++) {
//                String username = "testuser" + i;
//                // 检查是否已存在
//                User existingUser = userRepository.findByUsername(username);
//                if (existingUser == null) {
//                    User user = new User();
//                    user.setUsername(username);
//                    user.setPassword(passwordEncoder.encode("password")); // 加密密码
//                    user.setEmail(i + "test@example.com");
//                    // created_at 由数据库自动设置
//                    userRepository.save(user);
//                    System.out.println("Inserted user: " + username);
//                }
//            }
//        };
//    }
//}
