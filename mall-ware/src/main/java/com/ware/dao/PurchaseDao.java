package com.ware.dao;

import com.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 16:02:38
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
