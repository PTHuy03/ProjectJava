package com.example.DAJava.Repository;

import com.example.DAJava.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaterogyRepository extends JpaRepository<Category, Long> {
}
