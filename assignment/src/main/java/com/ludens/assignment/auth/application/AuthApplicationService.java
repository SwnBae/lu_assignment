package com.ludens.assignment.auth.application;

import com.ludens.assignment.auth.presentation.dto.request.AuthRequest;
import com.ludens.assignment.auth.presentation.dto.response.TokenResponse;
import com.ludens.assignment.heart.infrastructure.HeartCountRedisRepository;
import com.ludens.assignment.user.infrastructure.PostCountRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final AuthService authService;
    private final HeartCountRedisRepository heartCountRedisRepository;
    private final PostCountRedisRepository postCountRedisRepository;

    public TokenResponse signup(AuthRequest request) {
        return authService.signup(request);
    }

    public TokenResponse login(AuthRequest request) {
        return authService.login(request);
    }

    public void deleteMe(String userId) {
        List<Long> affectedPostIds = authService.deleteMe(userId);
        affectedPostIds.forEach(heartCountRedisRepository::delete);
        postCountRedisRepository.delete(userId);
    }
}
