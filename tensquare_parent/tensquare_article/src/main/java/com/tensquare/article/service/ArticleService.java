package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeClient noticeClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IdWorker idWorker;

    public List<Article> findAll() {
        Article article = articleDao.selectById(1);
        return articleDao.selectList(null);
    }

    public Article findById(String id) {
        return articleDao.selectById(id);
    }

    public void save(Article article) {
        //TODO: 使用jwt鉴权获取当前用户的信息，用户id，也就是文章的作者id
        String userId = "3";
        article.setUserid(userId);

        //使用分布式id生成器
        String id = idWorker.nextId() + "";
        article.setId(id);

        //初始化点赞，浏览量，评论数据
        article.setVisits(0);
        article.setComment(0);
        article.setThumbup(0);

        //新增
        articleDao.insert(article);

        //新增文章后需要创建消息通知给订阅者


        //获取订阅者信息
        //存放作者订阅者信息的集合key，里面存放订阅者id
        String authorKey = "article_author_" + userId;

        Set<String> set = redisTemplate.boundSetOps(authorKey).members();

        //最好加一个if判断，如果是新用户，就没有订阅者，避免空指针异常
        if (null != set && set.size() > 0) {
            Notice notice = null;
            //给订阅者创建消息通知
            for (String uid : set) {
                //创建消息对象
                notice = new Notice();

                //接受消息用户的id
                notice.setReceiverId(uid);
                //进行操作用户的id
                notice.setOperatorId(userId);
                //操作类型（评论，点赞等）
                notice.setAction("publish");
                //被操作的对象，例如文章，评论等
                notice.setTargetType("article");
                //被操作对象的id，例如文章id，评论id
                notice.setTargetId(id);
                //通知类型
                notice.setType("sys");

                noticeClient.save(notice);
            }
        }
        //入库成功后，发送mq消息，内容是消息通知id
        //第一个参数是交换机名，使用之前完成的订阅功能的交换机
        //第二个参数是路由键，使用时文章作者的id作为路由键
        //第三个参数是消息内容，这里只完成新消息提醒
        rabbitTemplate.convertAndSend("article_subscribe", userId, id);
    }

    public void updateById(Article article) {
        // 根据主键修改
        articleDao.updateById(article);

        // 根据条件修改
        // 创建条件对象
        // EntityWrapper<Article> wrapper = new EntityWrapper<>();
        // 设置条件
        // wrapper.eq("id", article.getId());
        // articleDao.update(article, wrapper);
    }

    public void deleteById(String articleId) {
        articleDao.deleteById(articleId);
    }

    public Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size) {
        // 设置查询条件
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            // if (map.get(key) != null) {
            //     wrapper.eq(key, map.get(key));
            // }
            //第一个参数是否把后面的条件加入到查询条件中
            //和上面的if判断的写法是一样的效果，实现动态sql
            wrapper.eq(map.get(key) != null, key, map.get(key));
        }
        //设置分页参数
        Page<Article> pageData = new Page<>(page, size);
        // 执行查询
        // 第一个是分页参数，第二个是查询条件
        List<Article> list = articleDao.selectPage(pageData, wrapper);
        pageData.setRecords(list);
        // 返回
        return pageData;
    }

    public Boolean subscribe(String articleId, String userId) {
        //根据文章id查询文章作者id
        String authorId = articleDao.selectById(articleId).getUserid();

        //1 创建Rabbit管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());

        //2 声明Direct类型交换机，处理新增文章消息
        DirectExchange exchange = new DirectExchange("article_subscribe");
        rabbitAdmin.declareExchange(exchange);

        //3 声明队列，每个用户都有自己的一个队列，通过用户id进行区分
        Queue queue = new Queue("article_subscribe_"+ userId,true);

        //4 声明交换机和队列的绑定关系，需要确保队列只收到对应作者的新增消息
        //通过路右键进行绑定作者，队列只收到绑定作者的文章消息
        //第一个是队列，第二个是交换机，第三个是路由键作者id
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);

        //存放用户订阅信息的集合key，里面存放着作者id
        String userKey = "article_subscribe_" + userId;
        //存放作者订阅者的信息的集合key，里面存放订阅者id
        String authorKey = "article_author_" + authorId;

        //查询用户的订阅关系，是否订阅过该作者,true为订阅过，false没有订阅过
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);

        if (flag == true) {
            //如果订阅过作者就取消订阅，并且返回false
            //在用户订阅信息的集合中，删除订阅的作者
            redisTemplate.boundSetOps(userKey).remove(authorId);
            //在作者订阅者信息的集合中，删除订阅者
            redisTemplate.boundSetOps(authorKey).remove(userId);

            // 如果取消订阅，就删除绑定关系
            rabbitAdmin.removeBinding(binding);

            return false;
        } else {
            //如果没有订阅过就进行订阅，并且返回true
            //在用户订阅信息的集合中，增加订阅的作者
            redisTemplate.boundSetOps(userKey).add(authorId);
            //在作者订阅者信息的集合中，增加订阅者
            redisTemplate.boundSetOps(authorKey).add(userId);

            // 声明要绑定的队列
            rabbitAdmin.declareQueue(queue);
            // 添加绑定关系
            rabbitAdmin.declareBinding(binding);

            return true;
        }
    }

    //文章点赞
    public void thumbup(String articleId,String userId) {
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup() + 1);
        articleDao.updateById(article);

        //点赞成功后，需要发送消息给文章作者
        Notice notice = new Notice();
        //接受消息用户的id
        notice.setReceiverId(article.getUserid());
        //进行操作用户的id
        notice.setOperatorId(userId);
        //操作类型（评论，点赞）
        notice.setAction("publish");
        //备操作的对象，例如文章，评论等
        notice.setTargetType("article");
        //被操作对象的id，例如文章的id，评论的id
        notice.setTargetId(articleId);
        //通知类型
        notice.setType("user");

        //保存消息
        noticeClient.save(notice);
    }

    public void thumbupLess(String articleId,String userId) {
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup() - 1);
        articleDao.updateById(article);
    }
}