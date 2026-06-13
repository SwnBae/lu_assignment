package com.ludens.assignment.heart.presentation;

import com.ludens.assignment.global.annotation.LoginMember;
import com.ludens.assignment.heart.application.HeartApplicationService;
import com.ludens.assignment.heart.presentation.dto.response.HeartUsersPageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class HeartController {

    private final HeartApplicationService heartApplicationService;

    @PostMapping("/{id}/heart")
    public ResponseEntity<Void> heart(
            @PathVariable Long id,
            @LoginMember String userId
    ) {
        heartApplicationService.heart(userId, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/heart")
    public ResponseEntity<Void> unheart(
            @PathVariable Long id,
            @LoginMember String userId
    ) {
        heartApplicationService.unheart(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/hearts")
    public ResponseEntity<HeartUsersPageResponse> getPostHearts(
            @PathVariable Long id,
            @LoginMember String userId,
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "20") @Positive @Max(100) int limit
    ) {
        HeartUsersPageResponse response = heartApplicationService.getPostHearts(userId, id, page, limit);
        return ResponseEntity.ok(response);
    }
}
