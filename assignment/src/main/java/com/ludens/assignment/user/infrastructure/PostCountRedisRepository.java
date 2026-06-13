package com.ludens.assignment.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostCountRedisRepository {

    private static final String KEY_PREFIX = "post_count:";
    private final StringRedisTemplate redisTemplate;

    public void increment(String userId) {
        redisTemplate.opsForValue().increment(key(userId));
    }

    public void decrement(String userId) {
        Long result = redisTemplate.opsForValue().decrement(key(userId));
        if (result != null && result < 0) {
            redisTemplate.opsForValue().set(key(userId), "0");
        }
    }

    public void delete(String userId) {
        redisTemplate.delete(key(userId));
    }

    public Long get(String userId) {
        String value = redisTemplate.opsForValue().get(key(userId));
        return value != null ? Long.parseLong(value) : null;
    }

    public Map<String, Long> mget(List<String> userIds) {
        if (userIds.isEmpty()) return Collections.emptyMap();
        List<String> keys = userIds.stream().map(this::key).toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        Map<String, Long> result = new HashMap<>();
        if (values == null) return result;
        for (int i = 0; i < userIds.size(); i++) {
            String value = values.get(i);
            if (value != null) {
                result.put(userIds.get(i), Long.parseLong(value));
            }
        }
        return result;
    }

    public void set(String userId, long count) {
        redisTemplate.opsForValue().set(key(userId), String.valueOf(count));
    }

    private String key(String userId) {
        return KEY_PREFIX + userId;
    }
}
