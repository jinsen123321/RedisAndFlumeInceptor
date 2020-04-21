package com.datasw.flume;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.ResourceBundle;

public class RedisUtil {


  /* //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;*/

    private static JedisPool jedisPool = null;

     private static String host;
     private static Integer port;
     private static String password;
     private static Integer max_idle;
     private static Integer min_idle;
     private static Integer max_wait;
     private static Integer max_active;
     private static Integer timeout;
    static {
        ResourceBundle rd=ResourceBundle.getBundle("redis");
        host=rd.getString("redis.host");
        port=Integer.parseInt(rd.getString("redis.port"));
        password=rd.getString("redis.password");
        max_idle=Integer.parseInt(rd.getString("redis.max_idle"));
        min_idle=Integer.parseInt(rd.getString("redis.min_idle"));
        max_wait=Integer.parseInt(rd.getString("redis.max_wait"));
        max_active=Integer.parseInt(rd.getString("redis.max_active"));
        timeout=Integer.parseInt(rd.getString("redis.timeout"));
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(max_active);
            config.setMaxIdle(max_idle);
            config.setMaxWaitMillis(max_wait);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestOnCreate(false);
            config.setBlockWhenExhausted(true);
            jedisPool = new JedisPool(config, host, port, timeout, password);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {

            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /***
     *
     * 释放资源
     */
    public static void returnResource(final Jedis jedis) {
        if(jedis != null) jedis.close();
    }
}
