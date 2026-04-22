package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequestDto;
import com.project.ecommerce.dto.CategoryResponseDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Page<CategoryResponseDto> getAllCategories(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return categoryRepository.findAll(pageable)
                .map(c -> new CategoryResponseDto(c.getId(),c.getName()));

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
