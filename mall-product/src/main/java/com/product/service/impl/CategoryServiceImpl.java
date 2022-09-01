package com.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.product.dao.CategoryDao;
import com.product.entity.CategoryEntity;
import com.product.service.CategoryService;


@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<CategoryEntity>());

        return new PageUtils(page);
    }

    /**
     * based on category levels, list all categories as a tree.
     * <p>
     * the list should be like:
     * List
     * ｜------ close
     * ｜------ electronic products
     * ｜               ｜----- phone
     * ｜               ｜----- computer
     * ｜----- shoes
     * ｜       ｜----- Athletic shoes
     * ｜       ｜             ｜----- Golf shoes
     * ｜       ｜             ｜----- Running shoes
     * ｜       ｜             ｜----- Climbing shoes
     * ｜       ｜----- boots
     * ｜----- ...
     *
     * @return return the list
     */
    @Override
    public List<CategoryEntity> listByTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        return categoryEntities
                .stream()
                .filter(categoryEntity -> categoryEntity.getCatLevel() == 1) // get all root categories
                .peek(categoryEntity -> categoryEntity.setChildren(getCategoryTreeRec(categoryEntity, categoryEntities)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
    }


    @Override
    public void removeByIds(List<Long> ids){
        //todo: check the categories we want delete referenced on other place
         baseMapper.deleteBatchIds(ids);
    }

    /**
     * <p>
     * get category tree for a root category
     * <p>
     * for example, the category tree for shoes should be something like:
     * shoes
     * ｜----- Athletic shoes
     * ｜             ｜----- Golf shoes
     * ｜             ｜----- Running shoes
     * ｜             ｜----- Climbing shoes
     * ｜----- boots
     * ｜----- high-heeled shoes
     * ｜----- ...
     *
     * @param root             the root category
     * @param categoryEntities the list of all categories
     * @return return a tree as List, like example above
     */
    private List<CategoryEntity> getCategoryTreeRec(CategoryEntity root, List<CategoryEntity> categoryEntities) {

        return categoryEntities
                .stream()
                .filter(categoryEntity -> root.getCatId().equals(categoryEntity.getParentCid()))
                .peek(categoryEntity -> categoryEntity.setChildren(getCategoryTreeRec(categoryEntity, categoryEntities)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }
}