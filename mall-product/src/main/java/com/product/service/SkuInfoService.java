package com.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:22
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByConditions(Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
}

