package com.example.DAJava.Repository;

import com.example.DAJava.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}