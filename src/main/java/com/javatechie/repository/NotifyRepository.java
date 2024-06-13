package com.javatechie.repository;

import com.javatechie.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<NotificationEntity, Long> {
    Integer countAllByAck(Integer ack);
    @Query(value = "select * from notification order by unix_time desc limit 50", nativeQuery = true)
    List<NotificationEntity> findAllOrderByUnixTime();
}
