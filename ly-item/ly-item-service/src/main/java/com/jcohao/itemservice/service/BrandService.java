package com.jcohao.itemservice.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.Brand;
import com.jcohao.itemservice.mapper.BrandMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    // （关键词）查询品牌，并进行分页（外加排序）
    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页，对此静态方法后的第一个查询方法进行分页
        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        // 根据关键词过滤
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toLowerCase());
        }

        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }

        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<Brand>(pageInfo.getTotal(), pageInfo);
    }

    /**
     * 新增品牌，这里不仅要新增品牌信息，还得维护品牌和商品分类的中间表
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // 保存实体信息，null 值的属性不会被保存（这是与 insert 方法的区别）
        brandMapper.insertSelective(brand);

        // 更新商品类目表 id 与商品 id 的关系
        for (Long cid : cids) {
            // brand 插入之后会有更新其 id 域
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    /**
     * 修改品牌信息，修改成功则返回 true，修改失败则返回 false
     * @param brand
     * @param cids
     */
    @Transactional
    public boolean updateBrand(Brand brand, List<Long> cids) {
        // 首先看能不能把 brand 从数据库中查出来，如果没在数据库中的数据，则修改不成功
        if (brandMapper.selectByPrimaryKey(brand.getId()) == null) {
            log.error("品牌：{}，修改不成功", brand);
            return false;
        }
        // 更新品牌信息
        brandMapper.updateByPrimaryKey(brand);

        // 更新商品类目表 id 与商品 id 的关系
        // 这里选择先删除旧的与商品 id 相关的类目 cid
        // 然后根据传过来的 cids 重新建立 id 与 cid 的关系
        brandMapper.deleteByBrandId(brand.getId());

        for (Long cid : cids) {
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }

        return true;
    }

    /**
     * 通过品牌 id 删除品牌信息，先删除品牌信息，删除失败则返回 false
     * 删除成功，则删除品牌 id 与 类别 cid 的关系
     * @param bid
     * @return
     */
    @Transactional
    public boolean deleteBrand(Long bid) {
        if (brandMapper.deleteByPrimaryKey(bid) == 0) {
            return false;
        }
        brandMapper.deleteByBrandId(bid);
        return true;
    }

    /**
     * 通过类别 id 查找对应的商品信息
     * @param cid
     * @return
     */
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.queryBrandsByCid(cid);
    }


//    public List<Brand> queryBrandsByIds(List<Long> ids) {
//        if (ids == null || ids.size() == 0) return null;
//
//        List<Brand> brands = new ArrayList<>();
//        for (Long id : ids) {
//            brands.add(brandMapper.selectByPrimaryKey(id));
//        }
//
//        return brands;
//    }

    public List<Brand> queryBrandsByIds(List<Long> ids) {
        return brandMapper.selectByIdList(ids);
    }
}
