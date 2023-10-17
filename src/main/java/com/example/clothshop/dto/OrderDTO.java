package com.example.clothshop.dto;

import com.example.clothshop.entity.Person;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class OrderDTO {

    private Long id;
    private Person person;
    private LocalDate createdAt;
    private LocalDate shipDate;
    private String status;
    private boolean complete;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
