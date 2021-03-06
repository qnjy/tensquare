package com.tensquare.article.repository;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.xml.crypto.Data;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    //SpringDataMongoDB，支持通过查询方法名进行查询定义的方法
    //根据文章id查询文章评论数据
    List<Comment> findByArticleid(String articleId);

    //根据发布时间和点赞数查询
    List<Comment> findByPublishdateAndThumbup(Data data,Integer thumbup);

    //根据用户id查询，并且根据发布时间倒序排序
    List<Comment> findByUseridOrderByPublishdate(String userid);
}
