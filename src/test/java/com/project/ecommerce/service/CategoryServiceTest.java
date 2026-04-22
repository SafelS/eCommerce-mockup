package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequestDto;
import com.project.ecommerce.dto.CategoryResponseDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories_shouldReturnListOfCategories(){

        //ARRANGE
        Category category = new Category(1L, "Electronics", null);
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        //ACT
        Page<CategoryResponseDto> result = categoryService.getAllCategories(0, 10);

        //ASSERT
        assertThat(result).hasSize(1);
        assertThat(result.toList().get(0).getName()).isEqualTo("Electronics");

    }

    @Test
    void getCategoryById_shouldReturnCategory(){

        Category category = new Category(1L, "test", null);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        CategoryResponseDto result = categoryService.getCategoryById(category.getId());

        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getName()).isEqualTo(category.getName());


    }

    @Test
    void getCategoryById_shouldThrowException_whenNotFound(){

        assertThatThrownBy(() -> categoryService.getCategoryById(99L))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    void createCategory_shouldCreateNewCategory(){
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("new");

        Category category = new Category(1L, categoryRequestDto.getName(), null);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto result = categoryService.createCategory(categoryRequestDto);

        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getId()).isEqualTo(category.getId());

    }

    @Test
    void updateCategory_shouldUpdateCategoryName(){

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("new");
        Category category = new Category(1L, "old", null);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto result = categoryService.updateCategory(category.getId(), categoryRequestDto);

        assertThat(category.getName()).isEqualTo(result.getName());

    }

    @Test
    void deleteCategory_shouldDeleteCategory(){

        Category category = new Category(1L, "test", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }



}
