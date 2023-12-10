package com.example.clothshop.dto;

import com.example.clothshop.entity.OrdersStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonOrdersDTO {

    private Long id;
    private LocalDate createdAt;
    private LocalDate shipDate;
    private OrdersStatus status;
}
