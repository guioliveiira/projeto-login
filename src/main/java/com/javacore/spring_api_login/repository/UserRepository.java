package com.javacore.spring_api_login.repository;

import com.javacore.spring_api_login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPublicIdAndDeletedFalse(UUID publicId);
    Optional<User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByPublicId(UUID publicId);
    boolean existsByEmailAndDeletedFalse(String email);
}
