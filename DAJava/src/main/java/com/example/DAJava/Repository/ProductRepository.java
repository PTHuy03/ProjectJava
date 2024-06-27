package com.example.DAJava.Repository;

import com.example.DAJava.Model.Product;
import com.example.DAJava.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContaining(String name);
    List<Product> findByCategory_Name(String categoryName);
}