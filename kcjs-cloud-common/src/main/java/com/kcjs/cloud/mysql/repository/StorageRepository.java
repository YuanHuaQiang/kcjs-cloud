package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findByProductId(Long productId);
}