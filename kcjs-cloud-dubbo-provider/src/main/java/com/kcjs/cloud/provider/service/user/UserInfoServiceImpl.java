package com.kcjs.cloud.provider.service.user;


import com.kcjs.cloud.api.user.UserInfoService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.UserInfo;
import com.kcjs.cloud.mysql.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@DubboService
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        String bloomName = "bloom:user:id";
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(bloomName);

        // 先删掉已有的布隆过滤器（防止 tryInit 报错）
        if (bloomFilter.isExists()) {
            bloomFilter.delete();
        }

        // 重新初始化布隆过滤器
        bloomFilter.tryInit(10_000_000, 0.01);

        // 加载数据库数据
        List<UserInfo> all = userInfoRepository.findAll();
        List<Long> allUserIds = all.stream().map(UserInfo::getId).toList();

        // 塞进布隆过滤器
        for (Long id : allUserIds) {
            bloomFilter.add(id);
        }

        System.out.println("布隆过滤器已重建，加载用户数：" + allUserIds.size());
    }

    @Override
    public boolean mightContainUserId(Long userId) {
        String bloomName = "bloom:user:id";
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(bloomName);

        // 判断是否存在
        return bloomFilter.contains(userId);
    }

    @Override
    public UserInfo getUserById(Long id) {
        Optional<UserInfo> optional = userInfoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            // 可以抛一个明确的异常，或者返回 null、封装成业务响应
            throw new BusinessException("用户ID不存在");
        }
    }

    @Override
    public List<UserInfo> listUsers() {
        return userInfoRepository.findAll();
    }

    @Override
    public boolean addUser(UserInfo userInfo) {
        userInfoRepository.save(userInfo);

        String bloomName = "bloom:user:id";
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(bloomName);

        bloomFilter.add(userInfo.getId());
        return true;
    }
}