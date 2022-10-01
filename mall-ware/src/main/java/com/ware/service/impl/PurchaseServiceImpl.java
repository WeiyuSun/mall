package com.ware.service.impl;

import com.common.constant.PurchaseDetailStatus;
import com.common.constant.PurchaseStatus;
import com.ware.entity.PurchaseDetailEntity;
import com.ware.service.PurchaseDetailService;
import com.ware.service.WareSkuService;
import com.ware.vo.MergeVo;
import com.ware.vo.PurchaseDoneVo;
import com.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.ware.dao.PurchaseDao;
import com.ware.entity.PurchaseEntity;
import com.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceivedPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0).or().eq("status", 1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();

        if (purchaseId == null) {
            PurchaseEntity purchase = new PurchaseEntity();
            Date date = new Date();
            purchase.setStatus(PurchaseStatus.CREATED);
            purchase.setCreateTime(date);
            purchase.setUpdateTime(date);

            this.save(purchase);

            purchaseId = purchase.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(PurchaseDetailStatus.ASSIGNED);
            return detailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(purchaseId);
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);
    }

    @Override
    public void received(List<Long> ids) {
        List<PurchaseEntity> collect =  ids.stream().map(this::getById).filter(item -> item.getStatus().equals(PurchaseStatus.CREATED) ||
                item.getStatus().equals(PurchaseStatus.ASSIGNED)).peek(item -> {
            item.setStatus(PurchaseStatus.RECEIVED);
            item.setUpdateTime(new Date());
        }).collect(Collectors.toList());
        this.updateBatchById(collect);

        for (PurchaseEntity purchase : collect) {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(purchase.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(entity.getId());
                purchaseDetailEntity.setStatus(PurchaseDetailStatus.BUYING);
                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(detailEntities);
        }
    }

    @Override
    @Transactional
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();

        List<PurchaseItemDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        boolean haseError = false;
        for(PurchaseItemDoneVo item: items){
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if(item.getStatus().equals(PurchaseDetailStatus.ERROR)) {
                purchaseDetailEntity.setStatus(PurchaseDetailStatus.ERROR);
                haseError = true;
            } else {
                PurchaseDetailEntity detail = purchaseDetailService.getById(item.getItemId());
                purchaseDetailEntity.setStatus(PurchaseDetailStatus.FINISH);
                wareSkuService.addStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum());
            }

            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }

        purchaseDetailService.updateBatchById(updates);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);

        if(!haseError) {
            purchaseEntity.setStatus(PurchaseStatus.FINISHED);
        } else {
            purchaseEntity.setStatus(PurchaseStatus.ERROR);
        }

        purchaseEntity.setCreateTime(new Date());
        this.updateById(purchaseEntity);


    }

}