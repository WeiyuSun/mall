package com.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.product.dao.AttrAttrgroupRelationDao;
import com.product.entity.CategoryEntity;
import com.product.service.AttrAttrgroupRelationService;
import com.product.service.AttrService;
import com.product.service.CategoryService;
import com.product.vo.AttrGroupRelationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.product.entity.AttrGroupEntity;
import com.product.service.AttrGroupService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 属性分组
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

//    /product/attrgroup/attr/relation

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R freeAttrsInGroup(@PathVariable Long attrgroupId, @RequestParam Map<String, Object> params) {

        PageUtils page = attrGroupService.getFreeAttrsInGroup(attrgroupId, params);
        System.out.println("get result: " + page.getList());
        return R.ok().put("page", page);
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId){
        return R.ok().put("data", attrService.getAllAttrByGroupId(attrgroupId));
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelations(@RequestBody AttrGroupRelationVo[] vos){
        attrGroupService.deleteRelations(vos);
        System.out.println("delete finished");
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{id}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("id") Long id){
//        PageUtils page = attrGroupService.queryPage(params);
        System.out.println("get id " + id);
        PageUtils page = attrGroupService.queryPage(params, id);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();

        Long[] path= (Long[]) categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
