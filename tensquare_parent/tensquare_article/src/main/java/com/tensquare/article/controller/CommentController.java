package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    //GET /comment/{article}/{articleId} 根据文章id查询评论
    @GetMapping(value = "article/{articleId}")
    public Result findByArticleId(@PathVariable String articleId){
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true, StatusCode.OK,"查询成功",list);
    }


    //GET /comment 查询所有评论
    @GetMapping
    public Result finAll(){
        List<Comment> list = commentService.findAll();
        return new Result(true, StatusCode.OK,"查询成功", list);
    }
    //GET /comment/{commenId} 根据评论id查询评论数据
    @GetMapping(value = "{commentId}")
    public Result findById(@PathVariable String commentId){
        Comment comment = commentService.findById(commentId);
        return new Result(true, StatusCode.OK,"查询成功",comment);
    }

    //POST /comment 新增评论
    @PostMapping
    public Result save(@RequestBody Comment comment){
        commentService.save(comment);
        return new Result(true, StatusCode.OK,"新增成功");
    }

    //PUT /comment/{commentId}  修改评论
    @PutMapping(value = "{commentId}")
    public Result updateById(@PathVariable String commentId,@RequestBody Comment comment){
        //设置评论主键
        comment.set_id(commentId);
        //执行修改
        commentService.updateById(comment);

        return new Result(true, StatusCode.OK,"修改成功");
    }

    //DELETE /comment/{commentId} 删除评论
    @DeleteMapping(value = "{commentId}")
    public Result deleteById(@PathVariable String commentId){
        commentService.deleteById(commentId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    //PUT /comment/thumbup/{commentId} 根据评论id点赞评论
    @PutMapping(value = "/thumbup/{commentId}")
    public Result thumbup(@PathVariable String commentId){
        commentService.thumbup(commentId);
        return new Result(true, StatusCode.OK,"点赞成功");
    }
}
