package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequestDto;
import com.project.ecommerce.dto.CategoryResponseDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> categoryResponseDtos = categories.stream()
                .map(c -> new CategoryResponseDto(c.getId(),c.getName()))
                .toList();

        return categoryResponseDtos;

    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new CategoryResponseDto(category.getId(),category.getName());
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDto(savedCategory.getId(),savedCategory.getName());

    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        category.setName(categoryRequestDto.getName());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDto(savedCategory.getId(),savedCategory.getName());

    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        categoryRepository.delete(category);
    }
}
