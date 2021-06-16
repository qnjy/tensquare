package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tensquare-user")
public interface UserClient {
    //根据id查询用户
    //http://localhost:9008/user/{userId}
    @GetMapping(value = "user/{userId}")
    public Result selectById(@PathVariable("userId") String userId);
}
