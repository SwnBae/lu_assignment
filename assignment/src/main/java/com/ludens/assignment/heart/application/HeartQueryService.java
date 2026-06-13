package com.ludens.assignment.heart.application;

import com.ludens.assignment.heart.infrastructure.HeartCountRedisRepository;
import com.ludens.assignment.heart.infrastructure.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartQueryService {

    private final HeartCountRedisRepository heartCountRedisRepository;
    private final PostLikeRepository postLikeRepository;

    public long getHeartCount(Long postId) {
        Long cached = heartCountRedisRepository.get(postId);
        if (cached != null) return cached;
        long count = postLikeRepository.countByPostId(postId);
        heartCountRedisRepository.set(postId, count);
        return count;
    }

    public Map<Long, Long> getHeartCounts(List<Long> postIds) {
        if (postIds.isEmpty()) return Collections.emptyMap();
        Map<Long, Long> cached = heartCountRedisRepository.mget(postIds);
        List<Long> missIds = postIds.stream()
                .filter(id -> !cached.containsKey(id))
                .toList();
        Map<Long, Long> result = new HashMap<>(cached);
        if (!missIds.isEmpty()) {
            Map<Long, Long> dbCounts = postLikeRepository.countByPostIds(missIds).stream()
                    .collect(Collectors.toMap(
                            row -> (Long) row[0],
                            row -> (Long) row[1]
                    ));
            for (Long missId : missIds) {
                long count = dbCounts.getOrDefault(missId, 0L);
                result.put(missId, count);
                heartCountRedisRepository.set(missId, count);
            }
        }
        return result;
    }

    public Set<Long> getHeartedPostIds(String userId, List<Long> postIds) {
        if (userId == null || postIds.isEmpty()) return Collections.emptySet();
        return new HashSet<>(postLikeRepository.findHeartedPostIds(userId, postIds));
    }
}
