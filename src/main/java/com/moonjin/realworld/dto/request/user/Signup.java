package com.moonjin.realworld.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
public class Signup {
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    private String username;

    @Builder
    public Signup(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
