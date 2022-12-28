package com.thiagosena.productservice.services;

import com.thiagosena.productservice.controllers.payloads.ProductRequest;
import com.thiagosena.productservice.controllers.payloads.ProductResponse;
import com.thiagosena.productservice.models.Product;
import com.thiagosena.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.of(productRequest);
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        val products = productRepository.findAll();
        log.info("Getting all products");
        return products.stream().map(ProductResponse::of).toList();
    }
}