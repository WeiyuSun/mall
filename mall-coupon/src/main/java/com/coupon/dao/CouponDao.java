package com.coupon.dao;

import com.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 23:37:03
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
