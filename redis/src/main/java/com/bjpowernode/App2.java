package com.bjpowernode;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App2 {
    public static void main(String[] args) {
        JedisPool pool = null;

        pool = RedisUtils.open("127.0.0.1", 6379);
        Jedis jedis = pool.getResource();
        jedis.flushAll();
        jedis.hset("website","baidu","www.baidu.com");
        String webStr=jedis.hget("website","baidu");
        System.out.println(webStr);

        Map<String,String> map=new HashMap<>();
        map.put("username","tom");
        map.put("age","10");
        map.put("sex","male");

        jedis.hmset("student",map);
        List<String> sList=jedis.hmget("student","username","age","sex");
        for(String str:sList){
            System.out.println(str);
        }

    }
}
