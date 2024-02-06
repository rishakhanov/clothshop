package com.example.clothshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discount")
    private List<Category> categories;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discount")
    private List<PersonDiscount> personDiscounts;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    @Column(name = "value")
    @Min(value = 0, message = "Discount should be greater than or equal to 0")
    private double value;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "valid")
    private boolean valid;

    public Long getId() {
        return id;
    }

    //@JsonBackReference(value = "category-discount")
    public List<Category> getCategories() {
        return categories;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isValid() {
        return valid;
    }

    public List<PersonDiscount> getPersonDiscounts() {
        return personDiscounts;
    }
}
