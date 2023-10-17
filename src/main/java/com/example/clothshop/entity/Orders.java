package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@ToString
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ManyToMany
    @JoinTable(
            name = "product_orders",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    List<Product> orderedProducts;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "ship_date")
    private LocalDate shipDate;

    @Column(name = "status")
    private String status;

    @Column(name = "complete")
    private boolean complete;

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public boolean isComplete() {
        return complete;
    }
}
