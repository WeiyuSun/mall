package com.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.product.entity.BrandEntity;
import com.product.vo.BrandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.product.entity.CategoryBrandRelationEntity;
import com.product.service.CategoryBrandRelationService;
import com.common.utils.PageUtils;
import com.common.utils.R;
/**
 * 品牌分类关联
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
@RestController
@RequestMapping("product/categoryBrandRelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
//    http://localhost:88/api/product/categorybrandrelation/brands/list?t=1662774021145&catId=225
//    http://localhost:88/api/product/categoryBrandRelation/brands/list?t=1662774021145&catId=225
//    http://localhost:88/api/product/categorybrandrelation/brands/list?t=1662774021145&catId=225
//    http://localhost:88/api/product/categoryBrandRelation/brands/list?catId=225
    @GetMapping("/brands/list")
    public R brandsList(@RequestParam(value = "catId", required = true) Long catId){
        System.out.println("catId is " + catId);
        List<BrandEntity> resultList = categoryBrandRelationService.getBrandsByCatid(catId);

        System.out.println("get result " + resultList);
        List<BrandVo> modifiedResultList = new ArrayList<>();

        for(BrandEntity item: resultList) {
            BrandVo modifiedItem = new BrandVo();
            modifiedItem.setBrandId(item.getBrandId());
            modifiedItem.setBrandName(item.getName());
            modifiedResultList.add(modifiedItem);
        }

        return R.ok().put("data", modifiedResultList);
    }

    @GetMapping("/catelog/list")
    public R listRelatedCat(@RequestParam("brandId") Long brandId){
        List<CategoryBrandRelationEntity> result = categoryBrandRelationService.listRelatedCat(brandId);

        return R.ok().put("data", result);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        categoryBrandRelationService.saveCatBrandRelation(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
