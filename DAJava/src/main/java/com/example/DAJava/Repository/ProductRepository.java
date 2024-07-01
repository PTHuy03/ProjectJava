package com.example.DAJava.Repository;

import com.example.DAJava.Model.Product;
import com.example.DAJava.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryName(String categoryName, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName AND p.name LIKE %:productName%")
    Page<Product> findByCategoryNameAndProductNameContaining(@Param("categoryName") String categoryName, @Param("productName") String productName, Pageable pageable);
}