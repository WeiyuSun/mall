package com.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-25 15:48:44
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

