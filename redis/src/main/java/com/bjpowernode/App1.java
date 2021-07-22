package com.bjpowernode;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class App1
{
    public static void main( String[] args ) {
        JedisPool pool=null;

        pool=RedisUtils.open("127.0.0.1",6379);
        Jedis jedis=pool.getResource();
        jedis.flushAll();
        jedis.set("username","tom");
        String username=jedis.get("username");
        System.out.println(username);

    }
}
