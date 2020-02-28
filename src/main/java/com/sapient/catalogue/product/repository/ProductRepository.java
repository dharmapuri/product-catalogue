package com.sapient.catalogue.product.repository;

import com.sapient.catalogue.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{
}
