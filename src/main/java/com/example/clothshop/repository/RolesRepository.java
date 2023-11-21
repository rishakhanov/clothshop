package com.example.clothshop.repository;

import com.example.clothshop.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByName(String name);
}
