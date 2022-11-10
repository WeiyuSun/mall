package com.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.product.entity.SpuInfoDescEntity;
import com.product.entity.SpuInfoEntity;
import com.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:22
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByConditions(Map<String, Object> params);

    void up(Long spuId);
}

