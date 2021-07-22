package com.bjpowernode;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
    //定义连接池对象
    private static JedisPool pool=null;

    //创建连接池
    public static JedisPool open(String host,int port){
        if(pool==null){
            //创建连接池配置对象
            JedisPoolConfig config=new JedisPoolConfig();
            config.setMaxTotal(10); //最大连接数
            config.setMaxIdle(2); //最大空闲连接，保证有任务可以马上得到连接
            config.setTestOnBorrow(true); //检查连接池是否密码

            pool=new JedisPool(config,host,port);

        }

        return pool;
    }

    //关闭连接池
    public static void close(){
        if(pool!=null){
            pool.close();
        }
    }
}
