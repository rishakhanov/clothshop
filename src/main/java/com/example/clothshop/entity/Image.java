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
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@JsonManagedReference
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "image")
    private List<Product> products;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "data")
    private byte[] data;

    public Long getId() {
        return id;
    }

    @JsonManagedReference(value = "product-image")
    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
