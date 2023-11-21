package com.example.clothshop.dto;


import com.example.clothshop.entity.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
public class VendorDTO {

    private Long id;
    private List<Product> products;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    public Long getId() {
        return id;
    }

    //@JsonManagedReference(value = "product-vendor")
    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }
}
