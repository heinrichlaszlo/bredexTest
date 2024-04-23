package com.example.bredexTest.user.dto.request;

import lombok.*;
/**
 * DTO for user login
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDTO {

    private String email;
    private String password;
}
