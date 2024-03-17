package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
