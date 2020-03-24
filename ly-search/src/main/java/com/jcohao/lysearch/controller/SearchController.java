package com.jcohao.lysearch.controller;


import com.jcohao.common.pojo.PageResult;
import com.jcohao.common.pojo.SearchRequest;
import com.jcohao.lysearch.document.Goods;
import com.jcohao.lysearch.pojo.SearchResult;
import com.jcohao.lysearch.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private IndexService indexService;

    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        SearchResult result = (SearchResult) indexService.search(request);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(result);
    }
}
