package com.project.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Please enter a Product name")
    private String name;

    @NotBlank(message = "Product description not valid")
    private String description;

    @PositiveOrZero(message = "Product price not valid")
    private Double price;

    @Positive(message = "Product stock not valid")
    private int stock;

    @Positive(message = "Category ID not valid")
    private Long categoryId;

}
