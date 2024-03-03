package com.example.clothshop.service;

import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.repository.DiscountRepository;
import com.example.clothshop.util.exception.DiscountNotCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


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
                .id(1L)
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
        given(discountRepository.findByName(discountDTO.getName())).willReturn(Optional.empty());
        given(mapStructMapper.discountDTOToDiscount(discountDTO)).willReturn(discount);
        given(discountRepository.save(discount)).willReturn(discount);

        Discount savedDiscount = discountService.saveNewDiscount(discountDTO);

        assertThat(savedDiscount).isNotNull();
    }

    @Test
    public void givenExistingDiscount_whenSaveDiscount_thenThrowsException() {
        given(discountRepository.findByName(discountDTO.getName())).willReturn(Optional.of(discount));

        org.junit.jupiter.api.Assertions.assertThrows(DiscountNotCreatedException.class, () -> {
            discountService.saveNewDiscount(discountDTO);
        });

        verify(discountRepository, never()).save(any(Discount.class));
    }

    @Test
    public void givenDiscountsList_whenGetAllDiscounts_thenReturnDiscountsList() {
        Discount discount2 = Discount.builder()
                .id(2L)
                .categories(null)
                .personDiscounts(null)
                .name("DiscountTest2")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        given(discountRepository.findAll()).willReturn(List.of(discount, discount2));

        List<Discount> discountList = discountService.getDiscounts();

        assertThat(discountList).isNotNull();
        assertThat(discountList).size().isEqualTo(2);
    }

    @Test
    public void givenDiscountId_whenGetDiscountById_thenReturnDiscountObject() {
        given(discountRepository.findById(1L)).willReturn(Optional.of(discount));

        Discount savedDiscount = discountService.getDiscountById(discount.getId());

        assertThat(savedDiscount).isNotNull();
    }

    @Test
    public void givenDiscountId_whenDeleteDiscount_thenNothing() {
        long discountId = 1L;
        given(discountRepository.findById(discountId)).willReturn(Optional.of(discount));
        willDoNothing().given(discountRepository).deleteById(discountId);

        discountService.deleteDiscount(discountId);

        verify(discountRepository, times(1)).deleteById(discountId);
    }


}
