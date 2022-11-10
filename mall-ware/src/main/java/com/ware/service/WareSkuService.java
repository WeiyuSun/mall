package com.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.to.SkuHasStockVo;
import com.common.utils.PageUtils;
import com.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 16:02:37
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

