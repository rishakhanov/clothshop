package com.example.clothshop.repository;

import com.example.clothshop.entity.ProductOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrdersRepository extends JpaRepository<ProductOrders, Long> {

    List<ProductOrders> findAllByOrdersId(long id);
}
