package com.ludens.assignment.auth.application;

import com.ludens.assignment.auth.presentation.dto.request.AuthRequest;
import com.ludens.assignment.auth.presentation.dto.response.TokenResponse;
import com.ludens.assignment.global.security.TokenProvider;
import com.ludens.assignment.user.domain.User;
import com.ludens.assignment.user.exception.UserException;
import com.ludens.assignment.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenResponse signup(AuthRequest request) {
        if (userRepository.existsByUsernameAndDeletedAtIsNull(request.getUsername())) {
            throw UserException.usernameAlreadyExists();
        }
        User user = User.create(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new TokenResponse(tokenProvider.generateToken(user.getId(), user.getRole()));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(AuthRequest request) {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(request.getUsername())
                .orElseThrow(UserException::invalidCredentials);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw UserException.invalidCredentials();
        }
        return new TokenResponse(tokenProvider.generateToken(user.getId(), user.getRole()));
    }

    @Transactional
    public void deleteMe(String userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(UserException::notFound);
        user.softDelete();
    }
}
