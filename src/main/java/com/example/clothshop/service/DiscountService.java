package com.example.clothshop.service;

import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.repository.DiscountRepository;
import com.example.clothshop.util.exception.CategoryNotCreatedException;
import com.example.clothshop.util.exception.DiscountNotCreatedException;
import com.example.clothshop.util.exception.DiscountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class DiscountService {

    private final DiscountRepository discountRepository;

    private final MapStructMapper mapStructMapper;

    public DiscountService(DiscountRepository discountRepository, MapStructMapper mapStructMapper) {
        this.discountRepository = discountRepository;
        this.mapStructMapper = mapStructMapper;
    }

    public List<Discount> getDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(long id) {
        Optional<Discount> foundDiscount = discountRepository.findById(id);
        return foundDiscount.orElseThrow(DiscountNotFoundException::new);
    }


    public void checkForValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new DiscountNotCreatedException(errorMsg.toString());
        }
    }

    @Transactional
    public Discount saveNewDiscount(DiscountDTO discountDTO) {
        Optional<Discount> foundDiscount = discountRepository.findByName(discountDTO.getName());
        if (foundDiscount.isPresent()) {
            throw new DiscountNotCreatedException("The discount already exists.");
        } else {
            Discount discount = mapStructMapper.discountDTOToDiscount(discountDTO);
            return discountRepository.save(discount);
        }
    }

    @Transactional
    public void deleteDiscount(long id) {
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            discountRepository.deleteById(id);
        } else {
            throw new DiscountNotFoundException();
        }
    }



}
