package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author 26568
 * @date 2024-03-17 13:45
 */
@RestController
public class MyController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @GetMapping("/testString")
    public String testString() {
        redisTemplate.opsForValue().set("key1","111");
        redisTemplate.opsForValue().set("key2","222");
        redisTemplate.opsForValue().set("key3","333");
        String value = redisTemplate.opsForValue().get("key2");
        System.out.println("value: "+value);
        return "OK";
    }
    @GetMapping("/testList")
    public String testList() {
        // 先清除之前的数据
        // 通过回调函数
        redisTemplate.execute((RedisConnection connection) -> {
            // execute 要求回调方法中必须写return 语句，返回个东西
            // 这个回调函数返回的对象，就会作为execute 本身的返回值
           connection.flushAll();
            return null;
        });
        redisTemplate.opsForList().leftPush("key","111");
        redisTemplate.opsForList().leftPush("key","222");
        redisTemplate.opsForList().leftPush("key","333");
        String value = redisTemplate.opsForList().rightPop("key");
        System.out.println("vaue: "+value);
        value = redisTemplate.opsForList().rightPop("key");
        System.out.println("vaue: "+value);
        value = redisTemplate.opsForList().rightPop("key");
        System.out.println("vaue: "+value);
        return "list Ok";
    }
    @RequestMapping("/testSet")
    public String testSet() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });
        redisTemplate.opsForSet().add("key","111","222","333");
        Set<String> result = redisTemplate.opsForSet().members("key");
        System.out.println("result: "+result);

        Boolean exists = redisTemplate.opsForSet().isMember("key","111");
        System.out.println("exists: "+exists);

        Long count = redisTemplate.opsForSet().size("key");
        System.out.println("count: "+count);

        Long ret = redisTemplate.opsForSet().remove("key","111","222");
        result = redisTemplate.opsForSet().members("key");
        System.out.println("result: "+result);
        return "Set OK";
    }
    @RequestMapping("testHash")
    public String testHash() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return  null;
        });
        redisTemplate.opsForHash().put("key","f1","111");
        redisTemplate.opsForHash().put("key","f2","333");
        redisTemplate.opsForHash().put("key","f3","222");
        String value = (String)redisTemplate.opsForHash().get("key","f1");
        System.out.println("value :"+value);

        Boolean exists = redisTemplate.opsForHash().hasKey("key","f1");
        System.out.println("exists: "+exists);

        Long size = redisTemplate.opsForHash().size("key");
        System.out.println("size: "+size);
        return "Hash Ok";
    }
    @RequestMapping("/testZSet")
    public String testZSet() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return "null";
        });
        redisTemplate.opsForZSet().add("key","zhangsan",10);
        redisTemplate.opsForZSet().add("key","lisi",20);
        redisTemplate.opsForZSet().add("key","wangwu",30);
        Set<String> members = redisTemplate.opsForZSet().range("key",0,-1);
        System.out.println("members: "+members);

        Set<ZSetOperations.TypedTuple<String>> memberWithScore = redisTemplate.opsForZSet().rangeWithScores("key",0,-1);
        System.out.println("memberWithScore: "+memberWithScore);

        Double score =  redisTemplate.opsForZSet().score("key","zhangsan");
        System.out.println("score: "+score);

        Long size = redisTemplate.opsForZSet().size("key");
        System.out.println("size: "+size);

        Long rank = redisTemplate.opsForZSet().rank("key","lisi");
        System.out.println("rank: "+rank);
        return "ZSet OK";
    }
}
