package com.product.service.impl;

import com.product.dao.AttrAttrgroupRelationDao;
import com.product.entity.AttrAttrgroupRelationEntity;
import com.product.vo.AttrGroupRelationVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.product.dao.AttrGroupDao;
import com.product.entity.AttrGroupEntity;
import com.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long id) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();

        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        if (id == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", id);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public void deleteRelations(AttrGroupRelationVo[] vos) {
        if(vos.length == 0)
            return;

        for(AttrGroupRelationVo vo: vos){

            if(vo == null || vo.getAttrId() == null || vo.getAttrGroupId() == null){
                System.out.println("Error in AttrGroupService.java");
            }
        }

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("attr_id", vos[0].getAttrId()).eq("attr_group_id", vos[0].getAttrGroupId());

        for(int i = 1; i < vos.length; i++){
            final int index = i;
            queryWrapper.or((wrapper) -> {
                wrapper.eq("attr_id", vos[index].getAttrId()).eq("attr_group_id", vos[index].getAttrGroupId());
            });
        }
    }

}