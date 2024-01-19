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
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        //create product and check expectations
        Category category = categoryRepository.findById(1L).get();
        Vendor vendor = vendorRepository.findById(1L).get();
        Image image = imageRepository.findById(1L).get();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ProductDTO.class, new ProductDTOSerializer());
        objectMapper.registerModule(simpleModule);

        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("productTestName")
                .price(100.00)
                .quantity(1000L)
                .build();

        MvcResult result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is((int)productDTO.getQuantity())))
                .andReturn();

        //delete created product
        String content = result.getResponse().getContentAsString();
        String productName = JsonPath.parse(content).read("$.name");
        Optional<Product> product = productRepository.findByName(productName);
        Long id = product.get().getId();
        productRepository.deleteById(id);
    }

    @Test
    public void givenUpdatedProduct_whenUpdateProduct_thenReturnUpdatedProductObject() throws Exception {
        //create product and get id
        Category category = categoryRepository.findById(1L).get();
        Vendor vendor = vendorRepository.findById(1L).get();
        Image image = imageRepository.findById(1L).get();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ProductDTO.class, new ProductDTOSerializer());
        objectMapper.registerModule(simpleModule);

        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("productTestName")
                .price(100.00)
                .quantity(1000L)
                .build();

        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String productName = JsonPath.parse(content).read("$.name");
        Optional<Product> product = productRepository.findByName(productName);
        Long id = product.get().getId();

        //update product and check expectations
        ProductDTO updatedProductDTO = ProductDTO.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("UpdatedProductTest")
                .price(200)
                .quantity(2000)
                .build();

        ResultActions response = mockMvc.perform(put("/api/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedProductDTO.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(updatedProductDTO.getPrice())));

        //delete created product
        productRepository.deleteById(id);
    }

    @Test
    public void givenProductId_whenDeleteProduct_thenReturn200() throws Exception {
        //create product and get id
        Category category = categoryRepository.findById(1L).get();
        Vendor vendor = vendorRepository.findById(1L).get();
        Image image = imageRepository.findById(1L).get();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ProductDTO.class, new ProductDTOSerializer());
        objectMapper.registerModule(simpleModule);

        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("productTestName")
                .price(100.00)
                .quantity(1000L)
                .build();

        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String productName = JsonPath.parse(content).read("$.name");
        Optional<Product> product = productRepository.findByName(productName);
        Long id = product.get().getId();

        //delete product by id and check expectations
        ResultActions response = mockMvc.perform(delete("/api/products/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }


}
