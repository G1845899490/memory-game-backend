package com.activateme.memorygame.repository;


import com.activateme.memorygame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getUserById(Long id);
}
