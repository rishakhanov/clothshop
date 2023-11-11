package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Builder
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orders",cascade = CascadeType.ALL)
    private List<ProductOrders> productOrders;

//    @ManyToMany
//    @JoinTable(
//            name = "product_orders",
//            joinColumns = @JoinColumn(name = "orders_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
//    private List<Product> orderedProducts;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "ship_date")
    private LocalDate shipDate;

    @Column(name = "status")
    private OrdersStatus status;

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

    public OrdersStatus getStatus() {
        return status;
    }

    @JsonManagedReference(value = "product-orders-orders")
    public List<ProductOrders> getProductOrders() {
        return productOrders;
    }

    //    public List<Product> getOrderedProducts() {
//        return orderedProducts;
//    }

}
