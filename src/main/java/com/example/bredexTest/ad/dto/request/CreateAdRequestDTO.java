package com.example.bredexTest.ad.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
/**
 * DTO for creating an ad
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdRequestDTO {

    @Size(max = 20)
    private String brand;

    @Size(max = 20)
    private String type;

    @Size(max = 200)
    private String description;

    @Max(9999999999L)
    private Long price;
}
