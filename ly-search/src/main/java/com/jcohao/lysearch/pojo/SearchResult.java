package com.jcohao.lysearch.pojo;

import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.Brand;
import com.jcohao.item.model.Category;
import com.jcohao.lysearch.document.Goods;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;      // 过滤结果聚合出来的分类信息

    private List<Brand> brands;             // 过滤结果聚合出来的商品信息

    private List<Map<String, Object>> specs;    // 规格参数过滤条件

    public SearchResult(Long total, Long totalPage, List<Goods> items,
                        List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(List<Category> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
