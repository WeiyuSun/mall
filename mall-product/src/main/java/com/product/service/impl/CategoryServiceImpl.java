package com.product.service.impl;

import com.product.service.CategoryBrandRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
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
        List<CategoryEntity> allEntities = baseMapper.selectList(null);
        HashMap<Long, CategoryEntity> allEntitiesInHashMap= new HashMap<>();

        for(CategoryEntity entity: allEntities) {
            allEntitiesInHashMap.put(entity.getCatId(), entity);
        }

        ArrayList<CategoryEntity> categoryTree = new ArrayList<CategoryEntity>();
        for(CategoryEntity entity: allEntities){
            if(entity.getCatLevel() == 1){
                categoryTree.add(entity);
            }
            else {
                CategoryEntity parent = allEntitiesInHashMap.get(entity.getParentCid());

                if(parent != null){
                    List<CategoryEntity> childrenListFromParent;

                    if((childrenListFromParent = parent.getChildren()) == null){
                        childrenListFromParent = new LinkedList<>();
                        parent.setChildren(childrenListFromParent);
                    }

                    childrenListFromParent.add(entity);
                }
            }
        }

        return categoryTree;
    }


    @Override
    public void removeByIds(List<Long> ids){
        //todo: check the categories we want delete referenced on other place
         baseMapper.deleteBatchIds(ids);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        LinkedList<Long> path = new LinkedList<>();

        CategoryEntity category = getById(catelogId);
        int size = 1;
        while (category.getParentCid() != 0){
            long currId = category.getCatId();
            path.addFirst(currId);
            category = getById(category.getParentCid());
            size ++;
        }

        path.addFirst(category.getCatId());


        return path.toArray(new Long[size]);
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        this.updateById(category);
        System.out.println("update category finished");
        if(!StringUtils.isEmpty(category.getName())){
            System.out.println("updating Cascade");
            categoryBrandRelationService.updateCatName(category.getCatId(), category.getName());
        }
    }
}