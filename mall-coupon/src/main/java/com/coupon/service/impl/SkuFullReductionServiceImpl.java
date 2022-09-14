package com.coupon.service.impl;

import com.common.to.MemberPrice;
import com.common.to.SkuReductionTo;
import com.coupon.entity.MemberPriceEntity;
import com.coupon.entity.SkuLadderEntity;
import com.coupon.service.MemberPriceService;
import com.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.coupon.dao.SkuFullReductionDao;
import com.coupon.entity.SkuFullReductionEntity;
import com.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount() > 0)
            skuLadderService.save(skuLadderEntity);

        SkuFullReductionEntity skuFullReduction = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReduction);
        if (skuFullReduction.getFullPrice().compareTo(BigDecimal.ZERO) > 0)
            this.save(skuFullReduction);

        List<MemberPrice> memberPriceList = skuReductionTo.getMemberPrice();

        List<MemberPriceEntity> collect = memberPriceList.stream().map(item -> {
            MemberPriceEntity memberPrice = new MemberPriceEntity();
            memberPrice.setSkuId(skuReductionTo.getSkuId());
            memberPrice.setMemberLevelId(item.getId());
            memberPrice.setMemberLevelName(item.getName());
            memberPrice.setMemberPrice(item.getPrice());
            memberPrice.setAddOther(1);
            return memberPrice;
        }).filter(item -> item.getMemberPrice().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());

        memberPriceService.saveBatch(collect);
    }

}