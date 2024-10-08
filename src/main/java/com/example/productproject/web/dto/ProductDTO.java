package com.example.productproject.web.dto;

import com.example.productproject.web.entity.Products;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate creation;

    public ProductDTO(Products products){
        this.id = products.getId();
        this.name = products.getName();
        this.description = products.getDescription();
        this.price = products.getPrice();
        this.creation = products.getCreation();
    }
}
