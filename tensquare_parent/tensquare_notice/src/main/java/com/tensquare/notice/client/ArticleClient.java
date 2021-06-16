package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tensquare-article")
public interface ArticleClient {

    //根据文章id查询文章数据
    //根据ID查询文章
    @GetMapping(value = "article/{articleId}")
    public Result findById(@PathVariable("articleId") String articleId);
}
