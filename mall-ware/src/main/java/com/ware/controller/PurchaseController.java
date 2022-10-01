package com.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ware.vo.MergeVo;
import com.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ware.entity.PurchaseEntity;
import com.ware.service.PurchaseService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 采购信息
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 16:02:38
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);

        return R.ok();
    }
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
         purchaseService.mergePurchase(mergeVo);
         return R.ok();
    }

    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    public R unreceivedList(@RequestParam Map<String, Object> params ){
        PageUtils page = purchaseService.queryUnreceivedPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        Date date = new Date();
        purchase.setUpdateTime(date);
        purchase.setCreateTime(date);
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
