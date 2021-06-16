package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody User user){
        User result = userService.login(user);

        if(result!= null){
            return new Result(true,StatusCode.OK,"登陆成功",result);
        }

        return new Result(false, StatusCode.OK,"登陆失败");
    }


    //根据id查询用户
    //http://localhost:9008/user/{userId}
    @GetMapping(value = "{userId}")
    public Result selectById(@PathVariable String userId){
        User user = userService.selectById(userId);
        return new Result(true, StatusCode.OK,"查询成功",user);
    }
}
