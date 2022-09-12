package com.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.product.dao.BrandDao;
import com.product.dao.CategoryDao;
import com.product.entity.BrandEntity;
import com.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.product.dao.CategoryBrandRelationDao;
import com.product.entity.CategoryBrandRelationEntity;
import com.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> listRelatedCat(Long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", brandId);

        List<CategoryBrandRelationEntity> result = baseMapper.selectList(wrapper);
        return result;
    }

    @Override
    public void saveCatBrandRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brand = brandDao.selectById(categoryBrandRelation.getBrandId());
        CategoryEntity category = categoryDao.selectById(categoryBrandRelation.getCatelogId());

        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());

        this.save(categoryBrandRelation);
    }

    @Override
    @Transactional
    public void updateBrandName(Long targetBrandId, String newBrandName) {
        assert (targetBrandId != null && newBrandName != null);

        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(targetBrandId);
        entity.setBrandName(newBrandName);

        this.update(entity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", targetBrandId));
    }

    @Override
    @Transactional
    public void updateCatName(Long targetCatId, String newCatName) {
        assert (targetCatId != null && newCatName != null);
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setCatelogId(targetCatId);
        entity.setCatelogName(newCatName);
        System.out.println("cat is " + entity);

        this.update(entity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", targetCatId));
    }

    @Override
    public List<BrandEntity> getBrandsByCatid(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId);

        List<CategoryBrandRelationEntity> relations = this.baseMapper.selectList(queryWrapper);

        List<Long> brandIds = relations.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());

        if (brandIds.isEmpty())
            return new ArrayList<>();
        QueryWrapper<BrandEntity> getBrands = new QueryWrapper<BrandEntity>().in("brand_id", brandIds);

        return brandDao.selectList(getBrands);
    }
}