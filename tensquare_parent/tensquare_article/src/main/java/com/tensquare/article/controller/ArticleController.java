package com.tensquare.article.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 文章分页
    @PostMapping(value = "search/{page}/{size}")
    // 之前接受文章数据使用pojo，但是现在是条件查询
    // 而所有的条件都需要进行判断，遍历pojo的所有属性要使用反射的方式不推荐
    // 直接使用集合的方式遍历，这里接收数据改为Map集合
    public Result findByPage(@PathVariable Integer page,
                             @PathVariable Integer size,
                             @RequestBody Map<String, Object> map) {

        //根据条件分页查询
        Page<Article> pageData = articleService.findByPage(map, page, size);
        //封装分页返回对象
        PageResult<Article> pageResult = new PageResult<>(pageData.getTotal(), pageData.getRecords());
        //返回数据
        return new Result(true, StatusCode.OK, "条件查询成功",pageResult);
    }

    // 查询所有
    @GetMapping
    public Result findAll() {
        List list = articleService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    //根据ID查询文章
    @GetMapping(value = "/{articleId}")
    public Result findById(@PathVariable String articleId) {
        Article article = articleService.findById(articleId);
        return new Result(true, StatusCode.OK, "查询成功", article);
    }

    //新增文章
    @PostMapping
    public Result save(@RequestBody Article article) {
        articleService.save(article);
        return new Result(true, StatusCode.OK, "新增成功");
    }

    // 修改文章
    @PutMapping(value = "{articleId}")
    public Result updateById(@PathVariable String articleId,
                             @RequestBody Article article) {
        // 设置id
        article.setId(articleId);
        // 执行修改
        articleService.updateById(article);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    // 删除文章
    @DeleteMapping(value = "{articleId}")
    public Result deleteById(@PathVariable String articleId) {
        articleService.deleteById(articleId);
        return new Result(true, StatusCode.OK, "删除成功");
    }


}


