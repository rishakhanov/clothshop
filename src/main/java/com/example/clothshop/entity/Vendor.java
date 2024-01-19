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
@Table(name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@JsonManagedReference
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Product> products;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    //@JsonManagedReference(value = "product-vendor")
    @JsonBackReference(value = "product-vendor")
    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }
}
