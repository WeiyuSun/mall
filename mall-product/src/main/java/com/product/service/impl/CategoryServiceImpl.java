package com.product.service.impl;

import com.product.service.CategoryBrandRelationService;
import com.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("查询了数据库");

        //将数据库的多次查询变为一次
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categorys = getLevel1Categories();

        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));

            //2、封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                    //1、找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));

                    if (level3Catelog != null) {
                        List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(category3Vos);
                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        System.out.println("result is ");
        System.out.println(parentCid);

        return parentCid;
    }
}