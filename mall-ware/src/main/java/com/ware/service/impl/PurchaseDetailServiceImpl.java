package com.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.ware.dao.PurchaseDetailDao;
import com.ware.entity.PurchaseDetailEntity;
import com.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("sku_id", key).or()
                        .eq("purchase_id", key);

            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }

        String wareId = (String) params.get("wareId");
        System.out.println("ware id is " + wareId);
        if(!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id){
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("purchase_id", id );
        List<PurchaseDetailEntity> result = this.list(queryWrapper);

        return result;
    }

}