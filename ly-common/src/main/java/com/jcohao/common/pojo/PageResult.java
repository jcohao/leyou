package com.jcohao.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;     // 总条数
    private Long totalPage;     // 总页数
    private List<T> items;      // 页数据

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
