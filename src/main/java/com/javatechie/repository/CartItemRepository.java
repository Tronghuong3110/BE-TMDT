package com.javatechie.repository;

import com.javatechie.entity.CartItemEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
    List<CartItemEntity> findAllByCart_IdAndOrdered(Integer cartId, Integer ordered);
    Optional<CartItemEntity> findByProductItem_IdAndOrderedAndCart_Id(Long productItemId, Integer ordered, Integer cartId);

    @Query(value = "SELECT product.id, JSON_OBJECT('product_id', product.id, 'product_name', product.name, 'quantity', total_quantity ) AS product_info\n" +
            "FROM(\n" +
            "\tSELECT  product.id, SUM(cart_item.quantity) AS total_quantity\n" +
            "\tFROM cart_item\n" +
            "\t\tJOIN product_item ON cart_item.product_item_id = product_item.id\n" +
            "\t\tJOIN product ON product_item.product_id = product.id\n" +
            "\tWHERE cart_item.ordered = 1 AND product.deleted = 0\n" +
            "\tGROUP BY  product.id\n" +
            "    ORDER BY total_quantity DESC limit 10\n" +
            ") AS subquery\n" +
            "JOIN product ON subquery.id = product.id\n" +
            "GROUP BY product.id", nativeQuery = true)
    List<JSONObject> statisticTopProduct();
}
