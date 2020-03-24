package com.jcohao.itemservice.service;

import com.jcohao.item.model.Category;
import com.jcohao.itemservice.mapper.CategoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryListByParent(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }


    public List<Category> queryByBrandId(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }



    public List<String> queryNamesByIds(List<Long> ids) {
        List<String> result = new ArrayList<>();
        for (Long cid : ids) {
            String categoryName = categoryMapper.queryNameById(cid);
            if (StringUtils.isNotBlank(categoryName)) {
                result.add(categoryName);
            }
        }
        return result;
    }
}
