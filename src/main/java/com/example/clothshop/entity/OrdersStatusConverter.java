package com.example.clothshop.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrdersStatusConverter implements AttributeConverter<OrdersStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrdersStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public OrdersStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(OrdersStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
