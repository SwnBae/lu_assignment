package com.ludens.assignment.heart.application;

import com.ludens.assignment.heart.infrastructure.HeartCountRedisRepository;
import com.ludens.assignment.heart.infrastructure.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartBatchService {

    private final PostLikeRepository postLikeRepository;
    private final HeartCountRedisRepository heartCountRedisRepository;

    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    @Transactional(readOnly = true)
    public void reconcile() {
        List<Object[]> counts = postLikeRepository.countAllGroupByPostId();
        for (Object[] row : counts) {
            Long postId = (Long) row[0];
            Long count = (Long) row[1];
            heartCountRedisRepository.set(postId, count);
        }
        log.debug("Heart count batch reconciliation complete: {} posts updated", counts.size());
    }
}
