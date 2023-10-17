package com.example.clothshop.repository;

import com.example.clothshop.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);

    boolean existsByName(String name);
}
