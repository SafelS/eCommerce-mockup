package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductRequestDto;
import com.project.ecommerce.dto.ProductResponseDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void getAllProducts_shouldReturnListOfProducts(){

        Category category = new Category(1L, "Electronics", null);
        Product product = new Product(1L, "Phone", "A Smartphone", 499.99, 10, category);

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDto> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(product.getId());
        assertThat(result.get(0).getName()).isEqualTo(product.getName());


    }

    @Test
    void getProductById_shouldReturnProduct(){

        Category category = new Category(1L, "Electronics", null);
        Product product = new Product(1L, "Phone", "A Smartphone", 499.99, 10, category);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductResponseDto result = productService.getProductById(product.getId());

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(product.getName());

    }

    @Test
    void getProductById_shouldThrowException_whenNotFound(){

        assertThatThrownBy(() -> productService.getProductById(10L))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    void createProduct_shouldCreateNewProduct(){

        Category category = new Category(1L, "Category", null);
        ProductRequestDto requestDto = new ProductRequestDto("New Product", "Description",
                10.99, 10, category.getId());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Product product = new Product(1L, requestDto.getName(), requestDto.getDescription(), requestDto.getPrice(),
                requestDto.getStock(), category);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto result = productService.createProduct(requestDto);

        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getCategoryName()).isEqualTo(category.getName());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());

    }

    @Test
    void updateProduct_shouldUpdateProduct(){



        Category category = new Category(1L, "Category", null);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        ProductRequestDto requestDto = new ProductRequestDto("new name", "new desc", 999.99, 99, category.getId());

        Product product = new Product(1L, "name", "description", 1.11, 11, category);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto result = productService.updateProduct(product.getId(), requestDto);

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(requestDto.getName());
        assertThat(result.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(result.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());
        assertThat(result.getCategoryName()).isEqualTo(category.getName());

    }

    @Test
    void deleteProduct_shouldDeleteProduct(){
        Category category = new Category(1L, "Category", null);
        Product product = new Product(1L, "name", "description", 1.11, 11, category);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.deleteProduct(product.getId());

        verify(productRepository).delete(product);
    }


}
