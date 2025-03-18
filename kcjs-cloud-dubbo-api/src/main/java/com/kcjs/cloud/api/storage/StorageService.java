package com.kcjs.cloud.api.storage;


public interface StorageService {


    void decrease(Long productId, Integer count) ;

    void save(Long productId, int count) ;
}