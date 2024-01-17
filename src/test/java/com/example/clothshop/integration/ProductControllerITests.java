package com.example.clothshop.integration;

import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.dto.ProductDTOSerializer;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.CategoryRepository;
import com.example.clothshop.repository.ImageRepository;
import com.example.clothshop.repository.ProductRepository;
import com.example.clothshop.repository.VendorRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void givenListOfProducts_whenGetAllProducts_thenReturnProductsList() throws Exception {
        int repositorySize = (int) productRepository.count();

        ResultActions response = mockMvc.perform(get("/api/products"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(repositorySize)));
    }

    @Test
    public void givenProductId_whenGetProductById_thenReturnProductObject() throws Exception {
        long productId = 1L;
        Product product = productRepository.findById(productId).get();

        ResultActions response = mockMvc.perform(get("/api/products/{id}", productId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(product.getName())));
    }

    @Test
    public void givenProductDTOObject_whenCreateProduct_thenReturnSavedProduct() throws Exception {
        Category category = categoryRepository.findById(1L).get();
        Vendor vendor = vendorRepository.findById(1L).get();
        Image image = imageRepository.findById(1L).get();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ProductDTO.class, new ProductDTOSerializer());
        objectMapper.registerModule(simpleModule);

        //objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("productTestName")
                .price(100)
                .quantity(1000)
                .build();

        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is((int)productDTO.getQuantity())));
    }

}
