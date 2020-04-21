package com.datasw.flume;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import redis.clients.jedis.Jedis;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinsen
 * @create 2020-04-02 14:43
 */
public class LogETLInterceptor implements Interceptor {

    private final static String jedisReject = "module:reject:name";

    private final static String jedisAllow = "module:allow:name";

    Jedis jedis = null;

    private static ArrayList<String> list = null;
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        jedis = RedisUtil.getJedis();
        byte[] body = event.getBody();
        String log = new String(body,Charset.forName("UTF-8"));
        //"172.18.2.26 search 2020-04-02 00:00:00,393 DEBUG [scheduling-1] {} - <== Total: 4"
        //空格分开 关键字在第二个位置
        String[] split = log.split(" ");
        String keyword = split[1];
        //从拒绝组的缓存中判断，存在就直接拒绝，不执行后面代码，提高性能
        try {
            if (jedis.sismember(jedisReject,keyword)){
                return null;
            }else {
            //查看通过的组，存在就直接放行
                if (jedis.sismember(jedisAllow, keyword)) {
                    return event;
                } else {
            //没有就从数据库再查一次，存在就放行，不存在就再拒绝组添加
                    list = LogUtil.selectDb();
                    if (list.contains(keyword)) {
                        System.out.println("能放行,redis还没来的及添加");
                        jedis.sadd(jedisAllow, keyword);

                        return event;
                    } else {
                        System.out.println("不能放行,且reject要多添加一个");

                        jedis.sadd(jedisReject, keyword);

                        return null;
                    }
                }
            }
        } catch (Exception e) {
            if(jedis != null) {
                RedisUtil.returnResource(jedis);
            }
            e.printStackTrace();
            return null;
        }finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
    }

    @Override
    public List<Event> intercept(List<Event> events) {

        ArrayList<Event> interceptors = new ArrayList<>();

        for (Event event : events) {

            Event intercept1 = intercept(event);

            interceptors.add(intercept1);
        }

        return interceptors;
    }

    @Override
    public void close() {

    }
    public static class Builder implements Interceptor.Builder{


        @Override
        public Interceptor build() {

            return new LogETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
