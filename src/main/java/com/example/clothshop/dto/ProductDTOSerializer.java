package com.example.clothshop.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ProductDTOSerializer extends StdSerializer<ProductDTO> {

    private static final long serialVersionUID = 1L;

    public ProductDTOSerializer() {
        this(null);
    }

    public ProductDTOSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(ProductDTO productDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("category", productDTO.getCategory());
        jsonGenerator.writeObjectField("vendor", productDTO.getVendor());
        jsonGenerator.writeObjectField("image", productDTO.getImage());
        jsonGenerator.writeStringField("name", productDTO.getName());
        jsonGenerator.writeNumberField("price", productDTO.getPrice());
        jsonGenerator.writeNumberField("quantity", productDTO.getQuantity());
        jsonGenerator.writeEndObject();
    }
}
