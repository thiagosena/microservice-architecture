package com.thiagosena.productservice.repositories;

import com.thiagosena.productservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}