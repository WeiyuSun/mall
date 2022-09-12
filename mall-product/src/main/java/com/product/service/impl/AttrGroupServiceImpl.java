package com.product.service.impl;
import com.common.constant.ProductConstant;
import com.product.dao.AttrAttrgroupRelationDao;
import com.product.dao.AttrDao;
import com.product.entity.AttrAttrgroupRelationEntity;
import com.product.entity.AttrEntity;
import com.product.service.AttrService;
import com.product.vo.AttrGroupRelationVo;
import com.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());

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
        if (vos.length == 0) return;

        for (AttrGroupRelationVo vo : vos) {

            if (vo == null || vo.getAttrId() == null || vo.getAttrGroupId() == null) {
                System.out.println("Error in AttrGroupService.java");
            }
        }

        System.out.println("so far so good");

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("attr_id", vos[0].getAttrId()).eq("attr_group_id", vos[0].getAttrGroupId());

        for (int i = 1; i < vos.length; i++) {
            final int index = i;
            queryWrapper.or((wrapper) -> {
                wrapper.eq("attr_id", vos[index].getAttrId()).eq("attr_group_id", vos[index].getAttrGroupId());
            });
        }

        int rows = relationDao.delete(queryWrapper);

        System.out.println(rows);
    }

    @Override
    public PageUtils getFreeAttrsInGroup(Long attrgroupId, Map<String, Object> params) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catId = attrGroupEntity.getCatelogId();

        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        List<Long> collection = group.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());

        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collection));

        List<Long> attrIds = groupId.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());



//        List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().eq("catelog_id", catId).notIn("attr_id", attrIds));

        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

        if(attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds);
        }

        String key = (String) params.get("key");
        if(StringUtils.isEmpty(key)){
           wrapper.and((w) -> {
               w.eq("attr_id", key).or().like("attr_name", key);
           });
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> geteAttrGroupWithAttrsByCatId(Long catelogId) {
        List<AttrGroupEntity> attrGroups = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        List<AttrGroupWithAttrsVo> result = attrGroups.stream().map(attrGroup -> {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroup, vo);

            List<AttrEntity> attrs = attrService.getAllAttrByGroupId(vo.getAttrGroupId());
            vo.setAttrs(attrs);
            return vo;
        }).collect(Collectors.toList());


        return result;
    }

}