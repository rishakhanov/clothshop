package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//@Data
@Setter
@ToString
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@JsonManagedReference(value = "product-category")
    //@JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    private Discount discount;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    @JsonBackReference(value = "product-category")
    //@JsonManagedReference(value = "product-category")
    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public Discount getDiscount() {
        return discount;
    }
}
