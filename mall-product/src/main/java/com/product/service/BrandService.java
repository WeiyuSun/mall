package com.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateBrand(BrandEntity brand);
}

