package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Comment> findAll() {
        List<Comment> list = commentRepository.findAll();
        return list;
    }

    public Comment findById(String commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        return comment;
    }

    public void save(Comment comment) {
        //分布式id生成器
        String id = idWorker.nextId() + "";
        //初始化点赞数据和发布时间等
        comment.setPublishdate(new Date());
        comment.setThumbup(0);
        //保存数据
        commentRepository.save(comment);
    }

    public void updateById(Comment comment) {
        //使用的是MongoDBRepository方法
        //其中save方法，主键如果存在，执行修改，如果不存在执行新增
        commentRepository.save(comment);
    }

    public void deleteById(String commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByArticleId(String articleId) {
        //调用持久层，根据文章id查询
        List<Comment> list = commentRepository.findByArticleid(articleId);
        return list;
    }

    public void thumbup(String commentId) {
        //根据评论id查询评论数据
        Comment comment = commentRepository.findById(commentId).get();
        //对评论点赞数据加一
        comment.setThumbup(comment.getThumbup() + 1);
        //保存修改数据
        commentRepository.save(comment);
    }
}
