package com.example.clothshop.service;

import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTests {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private MapStructMapper mapStructMapper;

    @InjectMocks
    private DiscountService discountService;

    private Discount discount;

    private DiscountDTO discountDTO;

    @BeforeEach
    public void setup() {

        discount = Discount.builder()
                .categories(null)
                .personDiscounts(null)
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        discountDTO = DiscountDTO.builder()
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();
    }

    @Test
    public void givenDiscountObject_whenSaveDiscount_thenReturnDiscountObject() {

    }

}
