package com.example.clothshop.repository;

import com.example.clothshop.entity.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DiscountRepositoryTests {

    @Autowired
    private DiscountRepository discountRepository;

    private Discount discount;

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
    }

    @Test
    public void givenDiscountObject_whenSave_thenReturnSavedObject() {
        Discount savedDiscount = discountRepository.save(discount);

        assertThat(savedDiscount).isNotNull();
        assertThat(savedDiscount.getId()).isGreaterThan(0);
    }

    @Test
    public void givenDiscountList_whenFindAll_thenDiscountList() {
        int discountSize = (int) discountRepository.count();
        List<Discount> discountList = discountRepository.findAll();

        assertThat(discountList).isNotNull();
        assertThat(discountList.size()).isEqualTo(discountSize);

    }

    @Test
    public void givenDiscountObject_whenFindById_thenReturnDiscountObject() {
        Discount savedDiscount = discountRepository.save(discount);

        Discount discountDB = discountRepository.findById(savedDiscount.getId()).get();

        assertThat(discountDB).isNotNull();
    }

    @Test
    public void givenDiscountName_whenFindByName_thenReturnDiscountObject() {
        Discount savedDiscount = discountRepository.save(discount);

        Discount discountDB = discountRepository.findByName(savedDiscount.getName()).get();

        assertThat(discountDB).isNotNull();
    }

    @Test
    public void givenDiscountObject_whenUpdateDiscount_thenReturnUpdatedDiscount() {
        discountRepository.save(discount);

        Discount savedDiscount = discountRepository.findById(discount.getId()).get();
        savedDiscount.setName("UpdatedDiscountTest");
        Discount updatedDiscount = discountRepository.save(savedDiscount);

        assertThat(updatedDiscount.getName()).isEqualTo("UpdatedDiscountTest");
    }

    @Test
    public void givenDiscountObject_whenDelete_thenRemoveDiscount() {
        discountRepository.save(discount);

        discountRepository.delete(discount);
        Optional<Discount> discountOptional = discountRepository.findById(discount.getId());

        assertThat(discountOptional).isEmpty();
    }
}
