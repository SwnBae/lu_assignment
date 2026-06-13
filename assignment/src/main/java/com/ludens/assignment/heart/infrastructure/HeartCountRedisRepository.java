package com.ludens.assignment.heart.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HeartCountRedisRepository {

    private static final String KEY_PREFIX = "heart_count:";
    private final StringRedisTemplate redisTemplate;

    public void increment(Long postId) {
        redisTemplate.opsForValue().increment(key(postId));
    }

    public void decrement(Long postId) {
        Long result = redisTemplate.opsForValue().decrement(key(postId));
        if (result != null && result < 0) {
            redisTemplate.opsForValue().set(key(postId), "0");
        }
    }

    public void delete(Long postId) {
        redisTemplate.delete(key(postId));
    }

    public Long get(Long postId) {
        String value = redisTemplate.opsForValue().get(key(postId));
        return value != null ? Long.parseLong(value) : null;
    }

    public Map<Long, Long> mget(List<Long> postIds) {
        if (postIds.isEmpty()) return Collections.emptyMap();
        List<String> keys = postIds.stream().map(this::key).toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        Map<Long, Long> result = new HashMap<>();
        if (values == null) return result;
        for (int i = 0; i < postIds.size(); i++) {
            String value = values.get(i);
            if (value != null) {
                result.put(postIds.get(i), Long.parseLong(value));
            }
        }
        return result;
    }

    public void set(Long postId, long count) {
        redisTemplate.opsForValue().set(key(postId), String.valueOf(count));
    }

    private String key(Long postId) {
        return KEY_PREFIX + postId;
    }
}
