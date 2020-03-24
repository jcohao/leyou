package com.jcohao.itemservice.common.api;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CommonPage<T> {
    // 第几页开始
    private Integer pageNum;
    // 每页有多少项
    private Integer pageSize;
    // 总共有多少页
    private Integer totalPage;
    // 总项数
    private Long total;
    // 要分页的列表
    private List<T> list;


    // PageHelper 分页后的 list 转为分页信息
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        result.setList(pageInfo.getList());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalPage(pageInfo.getPages());
        result.setTotal(pageInfo.getTotal());
        return result;
    }
}
