package com.nand.grievance.publisher.repository;

import com.nand.grievance.publisher.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    UserEntity findByUserId(String userId);

    Optional<List<UserEntity>> findAllByRoleName(String value);

    Optional<Object> findAllByRoleNameAndUserId(String value, String userId);

    Optional<List<UserEntity>> findByActive(int i);

    Optional<UserEntity> findByEmail(String email);
}
