// src/main/java/com/abhi/nexus_community/repository/UserRepository.java
package com.abhi.nexus_community.repository;

import com.abhi.nexus_community.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
