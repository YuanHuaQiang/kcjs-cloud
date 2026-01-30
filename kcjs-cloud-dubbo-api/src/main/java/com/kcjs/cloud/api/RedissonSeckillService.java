package com.kcjs.cloud.api;

import com.kcjs.cloud.result.Result;

public interface RedissonSeckillService {

     Result<String> seckillProduct(String userId, String productId);
}
