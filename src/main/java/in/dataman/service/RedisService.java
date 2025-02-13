package in.dataman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveValue(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }


    public void testConnection() {
        redisTemplate.opsForValue().set("connectionTest", "working", 1, TimeUnit.MINUTES);
        System.out.println("Connection Test Value: " + redisTemplate.opsForValue().get("connectionTest"));
    }



}
