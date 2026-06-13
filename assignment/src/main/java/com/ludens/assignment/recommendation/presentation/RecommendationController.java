package com.ludens.assignment.recommendation.presentation;

import com.ludens.assignment.global.annotation.LoginMember;
import com.ludens.assignment.post.application.PostApplicationService;
import com.ludens.assignment.post.presentation.dto.response.RecommendPostResponse;
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
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final PostApplicationService postApplicationService;

    @GetMapping
    public ResponseEntity<RecommendPostResponse> getRecommendation(
            @LoginMember(required = false) String loginUserId,
            @RequestParam(defaultValue = "20") @Positive @Max(100) int limit
    ) {
        RecommendPostResponse response = postApplicationService.getRecommended(loginUserId, limit);
        return ResponseEntity.ok(response);
    }
}
