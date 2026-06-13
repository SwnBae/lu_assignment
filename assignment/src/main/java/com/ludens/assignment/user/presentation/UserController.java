package com.ludens.assignment.user.presentation;

import com.ludens.assignment.global.annotation.LoginMember;
import com.ludens.assignment.heart.application.HeartApplicationService;
import com.ludens.assignment.post.application.PostApplicationService;
import com.ludens.assignment.post.presentation.dto.response.PostPageResponse;
import com.ludens.assignment.user.application.UserService;
import com.ludens.assignment.user.domain.User;
import com.ludens.assignment.user.infrastructure.PostCountRedisRepository;
import com.ludens.assignment.user.presentation.dto.response.UserProfileResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostCountRedisRepository postCountRedisRepository;
    private final PostApplicationService postApplicationService;
    private final HeartApplicationService heartApplicationService;

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, UserProfileResponse>> getUserProfile(
            @PathVariable String username
    ) {
        User user = userService.findByUsername(username);
        Long cached = postCountRedisRepository.get(user.getId());
        long postCount = cached != null ? cached : fetchAndCachePostCount(user);
        return ResponseEntity.ok(Map.of("user", UserProfileResponse.of(user, postCount)));
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<PostPageResponse> getUserPosts(
            @PathVariable String username,
            @LoginMember(required = false) String loginUserId,
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "20") @Positive @Max(100) int limit
    ) {
        PostPageResponse response = postApplicationService.getPostsByAuthor(loginUserId, username, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/hearts")
    public ResponseEntity<PostPageResponse> getUserHearts(
            @PathVariable String username,
            @LoginMember String loginUserId,
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "20") @Positive @Max(100) int limit
    ) {
        PostPageResponse response = heartApplicationService.getUserHearts(loginUserId, username, page, limit);
        return ResponseEntity.ok(response);
    }

    private long fetchAndCachePostCount(User user) {
        long count = userService.countPosts(user.getId());
        postCountRedisRepository.set(user.getId(), count);
        return count;
    }
}
