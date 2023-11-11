package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_orders")
public class ProductOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @Column(name = "quantity")
    @Min(value = 1, message = "Quantity should be greater than or equal to 1")
    private long quantity;

    public Long getId() {
        return id;
    }

    @JsonBackReference(value = "product-orders-product")
    public Product getProduct() {
        return product;
    }

    @JsonBackReference(value = "product-orders-orders")
    public Orders getOrders() {
        return orders;
    }

    public long getQuantity() {
        return quantity;
    }
}
