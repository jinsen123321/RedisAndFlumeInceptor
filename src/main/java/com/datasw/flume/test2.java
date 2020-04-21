package com.datasw.flume;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author jinsen
 * @create 2020-04-07 16:53
 */
public class test2 {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(test2.class);
        Jedis jedis = RedisUtil.getJedis();
//        jedis.sadd("user","senbaba2");
//        System.out.println("success");
        String str = "172.18.2.26 search 2020-04-02 00:00:00,393 DEBUG [scheduling-1] {} - <== Total: 4";
        String jedisReject = "module:reject:name";
        String jedisAllow = "module:allow:name";
        String[] split = str.split(" ");
        String keyword = split[1];
        //先判断是否被拒绝
        if (jedis.sismember(jedisReject, keyword)) {
            System.out.println("不能传入event");
        } else {
            if (jedis.sismember(jedisAllow, keyword)) {
                System.out.println("能放行");
            } else {
                ArrayList<String> list = testpost.selectDb();
                if (list.contains(keyword)) {
                    System.out.println("能放行,redis还没来的及添加");
                    jedis.sadd(jedisAllow, keyword);
                } else {
                    System.out.println("不能放行,且reject要多添加一个");
                    jedis.sadd(jedisReject, keyword);
                }
            }
        }
    }
}



