package com.bjpowernode;

import redis.clients.jedis.Jedis;

public class App {
    public static void main( String[] args ) {
        // 获取一个redis链接对象
        Jedis jedis = new Jedis("127.0.0.1",6379);
        // 删除redis中的所有数据
        jedis.flushAll();
        // 执行redis命令
        jedis.set("username","zhangsan");
        jedis.append("username","123");
        String username = jedis.get("username");
        System.out.println(username);
    }
}
