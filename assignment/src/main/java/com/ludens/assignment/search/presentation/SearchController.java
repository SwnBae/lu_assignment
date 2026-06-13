package com.ludens.assignment.search.presentation;

import com.ludens.assignment.global.annotation.LoginMember;
import com.ludens.assignment.post.application.PostApplicationService;
import com.ludens.assignment.post.presentation.dto.response.PostPageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final PostApplicationService postApplicationService;

    @GetMapping("/posts")
    public ResponseEntity<PostPageResponse> searchPosts(
            @LoginMember(required = false) String loginUserId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "20") @Positive @Max(100) int limit
    ) {
        PostPageResponse response = postApplicationService.searchPosts(loginUserId, q, page, limit);
        return ResponseEntity.ok(response);
    }
}
