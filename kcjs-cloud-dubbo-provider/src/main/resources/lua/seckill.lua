-- seckill.lua
-- KEYS[1] = stock key
-- KEYS[2] = user key (set)

-- ARGV[1] = userId
-- ARGV[2] = productId

-- 1. 判断用户是否已经抢过
if redis.call("sismember", KEYS[2], ARGV[1]) == 1 then
    return -1
end

-- 2. 判断库存
local stock = tonumber(redis.call("get", KEYS[1]))
if stock <= 0 then
    return 0
end

-- 3. 扣减库存 & 记录用户
redis.call("decr", KEYS[1])
redis.call("sadd", KEYS[2], ARGV[1])
return 1