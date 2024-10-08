package com.example.productproject.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class ProductDTO {
    private Long id;
    @NotNull @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @Size(max = 255)
    private String description;
    @Min(0)
    private Float price;
}
