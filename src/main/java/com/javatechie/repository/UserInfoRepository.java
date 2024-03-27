package com.javatechie.repository;

import com.javatechie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndDeleted(String username, Integer deleted);
    Boolean existsByUsername(String username);
    List<User> findAllByDeleted(Integer deleted);
    Optional<User> findByIdAndDeleted(Integer id, Integer deleted);
}
