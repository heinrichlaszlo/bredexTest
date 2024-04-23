package com.example.bredexTest.user.dto.response;

import lombok.*;
/**
 * DTO for user login response
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginResponseDTO {

    private String accessToken;

    private String refreshToken;
}
