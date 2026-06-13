package com.ludens.assignment.user.application;

import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.user.infrastructure.PostCountRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCountBatchService {

    private final PostRepository postRepository;
    private final PostCountRedisRepository postCountRedisRepository;

    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    @Transactional(readOnly = true)
    public void reconcile() {
        List<Object[]> counts = postRepository.countAllGroupByAuthorId();
        for (Object[] row : counts) {
            String authorId = (String) row[0];
            Long count = (Long) row[1];
            postCountRedisRepository.set(authorId, count);
        }
        log.debug("Post count batch reconciliation complete: {} users updated", counts.size());
    }
}
