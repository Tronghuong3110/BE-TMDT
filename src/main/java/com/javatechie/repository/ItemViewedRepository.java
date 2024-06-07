package com.javatechie.repository;

import com.javatechie.entity.ItemViewedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemViewedRepository extends JpaRepository<ItemViewedEntity, Long> {
    Optional<ItemViewedEntity> findByItemIdAndUserId(Long itemId, Integer userId);
    @Query(value = "select * from item_view where (:favorite is null or favorite = :favorite) and (:viewed is null or viewed = :viewed) and user_id = :userId", nativeQuery = true)
    List<ItemViewedEntity> findAllByFavoriteOrViewedAndUser(@Param("favorite")Integer favorite, @Param("viewed")Integer viewed, @Param("userId")Integer userId);
}
