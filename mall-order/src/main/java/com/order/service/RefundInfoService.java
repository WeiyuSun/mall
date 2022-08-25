package com.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 15:56:29
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

