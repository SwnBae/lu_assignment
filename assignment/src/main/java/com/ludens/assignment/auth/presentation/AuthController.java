package com.ludens.assignment.auth.presentation;

import com.ludens.assignment.auth.application.AuthApplicationService;
import com.ludens.assignment.auth.presentation.dto.request.AuthRequest;
import com.ludens.assignment.auth.presentation.dto.response.TokenResponse;
import com.ludens.assignment.global.annotation.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authApplicationService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authApplicationService.login(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@LoginMember String userId) {
        authApplicationService.deleteMe(userId);
        return ResponseEntity.noContent().build();
    }
}
