package com.kcjs.cloud.sp.provider.service.storage;


import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.Storage;
import com.kcjs.cloud.mysql.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

@DubboService
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    @Transactional
    public void decrease(Long productId, Integer count) {
        Storage storage = storageRepository.findByProductId(productId);
        if (storage == null) {
            throw new BusinessException("库存不存在");
        }

        if (storage.getResidue() < count) {
            throw new BusinessException("库存不足");
        }

        storage.setUsed(storage.getUsed() + count);
        storage.setResidue(storage.getResidue() - count);

        storageRepository.save(storage);
    }

    @Override
    public void save(Long productId, int count) {
        Storage storage = storageRepository.findByProductId(productId);
        if (storage == null) {
            storage = new Storage();
        }
        storage.setProductId(productId);
        storage.setTotal(count);
        storage.setUsed(0);
        storage.setResidue(count);
        storageRepository.save(storage);
    }
}