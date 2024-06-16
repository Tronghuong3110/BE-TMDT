package com.javatechie.repository;

import com.javatechie.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<NotificationEntity, Long> {
    @Query(value = "select count(*) from notification " +
                    "where ack = :ack role = :role ", nativeQuery = true)
    Integer countAllByAckAndRole(@Param("ack") Integer ack, @Param("role") String role);
    @Query(value = "select * from notification where role = :role order by unix_time desc limit 50", nativeQuery = true)
    List<NotificationEntity> findAllOrderByUnixTimeAndROle(@Param("role") String role);

    List<NotificationEntity> findAllByRole(String role);
}
