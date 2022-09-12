package com.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.product.entity.BrandEntity;
import com.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> listRelatedCat(Long brandId);

    void saveCatBrandRelation(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrandName(Long targetBrandId, String newBrandName);

    void updateCatName(Long targetBrandId, String newCatName);

    List<BrandEntity> getBrandsByCatid(Long catId);
}


