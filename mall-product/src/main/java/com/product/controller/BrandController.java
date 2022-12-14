package com.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.common.utils.ResultCode;
import com.common.validator.group.AddGroup;
import com.common.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.entity.BrandEntity;
import com.product.service.BrandService;
import com.common.utils.PageUtils;
import com.common.utils.R;


/**
 * 品牌
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
@RestController
@RequestMapping("product/brand")
public class
BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand, BindingResult result){
        if(result != null && result.hasErrors()){
            return produceErrorResult(result);
        }
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand, BindingResult result){

        System.out.println(brand);
        if(result != null && result.hasErrors()){
            R r = produceErrorResult(result);
            System.out.println(r);
            return r;
        }

        brandService.updateBrand(brand);
		//brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

    private R produceErrorResult(BindingResult result){
        Map<String, String> errors = new HashMap<String, String>(8);
        result.getFieldErrors().forEach((error) ->{
            String message = error.getDefaultMessage();
            String field = error.getField();

            errors.put(field, message);
        });

        return R.error(ResultCode.INVALID_DATA, "Invalid submitted data").put("data", errors);
    }


}
