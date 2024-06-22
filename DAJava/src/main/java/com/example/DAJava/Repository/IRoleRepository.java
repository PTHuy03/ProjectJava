package com.example.DAJava.Repository;

import com.example.DAJava.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);
}