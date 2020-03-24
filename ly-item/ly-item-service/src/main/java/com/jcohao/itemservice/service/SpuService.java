package com.jcohao.itemservice.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.Spu;
import com.jcohao.item.model.SpuBo;
import com.jcohao.item.model.SpuDetail;
import com.jcohao.itemservice.mapper.BrandMapper;
import com.jcohao.itemservice.mapper.CategoryMapper;
import com.jcohao.itemservice.mapper.SpuDetailMapper;
import com.jcohao.itemservice.mapper.SpuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    public PageResult<SpuBo> queryByPage(
            String key, Boolean saleable, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        Example example = new Example(Spu.class);

        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("title", "%" + key + "%");

        }

        example.createCriteria().andEqualTo("saleable", saleable);

        Page<Spu> spuPageInfo = (Page<Spu>) spuMapper.selectByExample(example);

        List<SpuBo> results = spuPageInfo.stream().map(
                item -> {
                    SpuBo spuBo = new SpuBo();
                    BeanUtils.copyProperties(item, spuBo);
                    List<String> names = Arrays.asList(categoryMapper.selectByPrimaryKey(spuBo.getCid1()).getName(),
                            categoryMapper.selectByPrimaryKey(spuBo.getCid2()).getName(),
                            categoryMapper.selectByPrimaryKey(spuBo.getCid3()).getName());
                    spuBo.setCname(StringUtils.join(names, '/'));
                    spuBo.setBname(brandMapper.selectByPrimaryKey(spuBo.getBrandId()).getName());
                    return spuBo;
                }
        ).collect(Collectors.toList());


        return new PageResult<SpuBo>(spuPageInfo.getTotal(), results);
    }


    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    public Spu querySpuById(Long id) {return spuMapper.selectByPrimaryKey(id);}
}
