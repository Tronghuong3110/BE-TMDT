package com.javatechie.repository;

import com.javatechie.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    boolean existsByName(String name);
    Optional<SupplierEntity> findByIdAndDeleted(long id, int deleted);
    List<SupplierEntity> findAllByDeleted(int deleted);
    @Query(value = "select * from supplier where (id != :id) and (name = :nameSupplier or phonenumber = :phoneNumber) and deleted = :deleted limit 1", nativeQuery = true)
    Optional<SupplierEntity> findByIdAndNameOrPhoneNumberAndDeleted(@Param("id")Long id, @Param("nameSupplier") String name,
                                                               @Param("phoneNumber") String phoneNumber, @Param("deleted") Integer deleted);
    boolean existsByPhoneNumber(String phoneNumber);
}
