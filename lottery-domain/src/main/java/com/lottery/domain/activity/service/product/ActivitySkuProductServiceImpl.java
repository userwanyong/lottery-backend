package com.lottery.domain.activity.service.product;

import com.lottery.domain.activity.model.entity.SkuProductEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.ActivitySkuProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 永
 * sku商品服务实现类
 */
@Service
public class ActivitySkuProductServiceImpl implements ActivitySkuProductService {
    @Resource
    private ActivityRepository repository;
    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }
}
