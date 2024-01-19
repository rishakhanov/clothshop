package com.example.clothshop.dto;

import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Vendor;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
public class ProductDTO {

    //@JsonProperty("id")
    private Long id;

    //@JsonProperty("category")
    private Category category;
    //@JsonProperty("vendor")
    private Vendor vendor;
    //@JsonProperty("image")
    private Image image;

    //@JsonProperty("name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    //@JsonProperty("price")
    @Min(value = 0, message = "Price should be greater than or equal to 0")
    private double price;

    //@JsonProperty("quantity")
    @Min(value = 0, message = "Quantity should be greater than or equal to 0")
    private long quantity;
/*
    @JsonCreator
    public ProductDTO(
            @JsonProperty(namespace = "id", required = false) Long id,
            @JsonProperty(namespace = "category", required = true) Category category,
            @JsonProperty(namespace = "vendor", required = true) Vendor vendor,
            @JsonProperty(namespace = "image", required = true) Image image,
            @JsonProperty(namespace = "name", required = true) String name,
            @JsonProperty(namespace = "price", required = true) double price,
            @JsonProperty(namespace = "quantity") long quantity
    ) {
        this.id = id;
        this.category = category;
        this.vendor = vendor;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonBackReference(value = "product-category")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonBackReference(value = "product-vendor")
    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @JsonBackReference(value = "product-image")
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
