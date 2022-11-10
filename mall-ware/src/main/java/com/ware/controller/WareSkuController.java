package com.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.common.to.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ware.entity.WareSkuEntity;
import com.ware.service.WareSkuService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 商品库存
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 16:02:37
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/hasstock")
    public List<SkuHasStockVo> getSkusHasStock(@RequestBody List<Long> skuIds){
        System.out.println("接收数据");
        System.out.println(skuIds);
        List<SkuHasStockVo> vos = wareSkuService.getSkusHasStock(skuIds);
        R<List<SkuHasStockVo>> ok = R.ok();
        ok.setData(vos);
        System.out.println("输出结果");
        System.out.println(ok.getData());
        System.out.println(ok);
        return vos;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
