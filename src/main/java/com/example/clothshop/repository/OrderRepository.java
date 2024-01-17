package com.example.clothshop.repository;

import com.example.clothshop.entity.Orders;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByPersonId(long id);

    Optional<Orders> findByCreatedAt(LocalDate shipDate);
}

