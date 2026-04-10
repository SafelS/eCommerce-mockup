package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private Long categoryId;
    private String categoryName;

}
