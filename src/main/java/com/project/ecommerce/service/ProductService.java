package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryResponseDto;
import com.project.ecommerce.dto.ProductRequestDto;
import com.project.ecommerce.dto.ProductResponseDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public Page<ProductResponseDto> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page,size);

        return productRepository.findAll(pageable)
                .map(p -> new ProductResponseDto(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getCategory().getId(),
                        p.getCategory().getName()
                ));
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {

        Category category = categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(EntityNotFoundException::new);

        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getName()
        );
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Category category = categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(EntityNotFoundException::new);

        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);

        return new ProductResponseDto(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getStock(),
                updatedProduct.getCategory().getId(),
                updatedProduct.getCategory().getName()
        );
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        productRepository.delete(product);
    }
}
