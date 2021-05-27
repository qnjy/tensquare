package test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MongoTest {

    private MongoClient mongoClient;
    private MongoCollection<Document> comment;
    @Before
    public void init(){
        //1.创建操作mongodb的客户端
        mongoClient = new MongoClient("192.168.200.129",27017);

        //2.选择数据库  use commentdb
        MongoDatabase commentdb = mongoClient.getDatabase("commentdb");

        //3.获取集合    db.comment
        comment = commentdb.getCollection("comment");
    }

    //查询所有数据db.comment.find()
    @Test
    public void test1(){
        //4.使用集合进行查询  db.comment.find()
        FindIterable<Document> documents = comment.find();

        //5.解析结果集（打印）
        for (Document document : documents) {
            System.out.println("-----------------------");
            System.out.println("_id: " + document.get("_id"));
            System.out.println("content: " + document.get("content"));
            System.out.println("userid: " + document.get("userid"));
            System.out.println("thumbup: " + document.get("thumbup"));
        }
    }

    //根据条件查询  db.comment.find({"_id":"1"})
    @Test
    public void test2(){
        //封装查询条件
        BasicDBObject bson = new BasicDBObject("_id","1");
        //执行条件
        FindIterable<Document> documents = comment.find(bson);
        //打印数据
        for (Document document : documents) {
            System.out.println("_id: " + document.get("_id"));
            System.out.println("content: " + document.get("content"));
            System.out.println("userid: " + document.get("userid"));
            System.out.println("thumbup: " + document.get("thumbup"));
        }
    }

    //新增数据 db.comment.insert({"_id":"6","content":"新增测试","userid":"1019"",thumbup":"123")
    @Test
    public void test3(){
        //封装新数据
        Map<String, Object> map = new HashMap<>();
        map.put("_id","6");
        map.put("content","新增测试");
        map.put("userid","1019");
        map.put("thumbup","123");
        //封装新增数据对向
        Document document = new Document(map);
        //执行新增操作
        comment.insertOne(document);
    }

    //修改操作 db.comment.update({_id:"2"},{$set:{thumbup:2000}})
    @Test
    public void test4(){
        //创建修改条件
        BasicDBObject filter = new BasicDBObject("_id","6");
        //创建修改的值
        BasicDBObject update = new BasicDBObject("$set",new Document("userid","999"));
        //执行代码
        comment.updateOne(filter,update);
    }

    //删除 db.comment.remove({thumbup:1000})
    @Test
    public void test5(){
        BasicDBObject bson = new BasicDBObject("_id","6");
        comment.deleteOne(bson);
    }

    @After
    public void close(){
        //6.释放资源
        mongoClient.close();
    }
}
