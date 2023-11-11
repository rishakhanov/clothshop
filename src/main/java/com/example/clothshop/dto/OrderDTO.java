package com.example.clothshop.dto;

import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public class OrderDTO {

    private Long id;
    private Person person;
    private List<Product> orderedProducts;
    private LocalDate createdAt;
    private LocalDate shipDate;
    //private String status;
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

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    //public String getStatus() {
    public OrdersStatus getStatus() {
        return status;
    }

    //public void setStatus(String status) {
    public void setStatus(OrdersStatus status) {
        this.status = status;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
}
