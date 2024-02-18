package com.example.clothshop.dto;

public class ProductDiscountDTO {

    private Long id;

    private String name;

    private double price;

    private double discountedPrice;

    private long quantity;

    public ProductDiscountDTO(Long id, String name, double price, double discountedPrice, long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
