package com.ludens.assignment.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthRequest {

    @NotBlank(message = "username은 필수입니다.")
    @Size(min = 3, max = 20, message = "username은 3~20자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "username은 영문자, 숫자, 밑줄(_)만 허용됩니다.")
    private String username;

    @NotBlank(message = "password는 필수입니다.")
    @Size(min = 6, message = "password는 최소 6자 이상이어야 합니다.")
    private String password;
}
