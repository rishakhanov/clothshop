package com.example.clothshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Builder
public class DiscountDTO {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    @Min(value = 0, message = "Discount should be greater than or equal to 0")
    private double value;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean valid;

    public Long getId() {
        return id;
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

}
