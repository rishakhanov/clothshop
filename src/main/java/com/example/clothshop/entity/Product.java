package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Vendor vendor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductOrders> productOrders;

//    @ManyToMany(mappedBy = "orderedProducts")
//    List<Orders> products;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    @Column(name = "price")
    @Min(value = 0, message = "Price should be greater than or equal to 0")
    private double price;

    @Column(name = "quantity")
    @Min(value = 0, message = "Quantity should be greater than or equal to 0")
    private long quantity;

    public Long getId() {
        return id;
    }

    @JsonBackReference(value = "product-category")
    public Category getCategory() {
        return category;
    }

    @JsonBackReference(value = "product-vendor")
    public Vendor getVendor() {
        return vendor;
    }

    @JsonBackReference(value = "product-image")
    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @JsonManagedReference(value = "product-orders-product")
    public List<ProductOrders> getProductOrders() {
        return productOrders;
    }

    //    public List<Orders> getProducts() {
//        return products;
//    }
}
