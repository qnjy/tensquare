package com.tensquare.article.client;

import com.tensquare.article.pojo.Notice;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("tensquare-notice")
public interface NoticeClient {
    // 3. 新增通知 http://localhost:9014/notice POST
    @PostMapping("notice")
    public Result save(@RequestBody Notice notice);
}
