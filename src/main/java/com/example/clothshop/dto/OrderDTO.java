package com.example.clothshop.dto;

import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.ProductOrders;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Setter
public class OrderDTO {

    private Long id;
    private Person person;
    private List<ProductOrders> productOrders;
    private LocalDate createdAt;
    private LocalDate shipDate;
    private OrdersStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonBackReference(value = "orders-person")
    public Person getPerson() {
        return person;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public OrdersStatus getStatus() {
        return status;
    }

    public List<ProductOrders> getProductOrders() {
        return productOrders;
    }

}
