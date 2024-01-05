package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.CategoryRepository;
import com.example.clothshop.repository.ImageRepository;
import com.example.clothshop.repository.VendorRepository;
import com.example.clothshop.service.ImageService;
import com.example.clothshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    public void givenProductDTOObject_whenCreateProduct_thenReturnSavedProduct() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();

        BDDMockito.given(mapStructMapper.productToProductDTO(productService.saveNewProduct(ArgumentMatchers.any(ProductDTO.class))))
                .willReturn(productDTO);

        BDDMockito.willDoNothing().given(productService).checkForValidationErrors(bindingResult);
/*
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is((int)productDTO.getQuantity())))
                .andReturn();
*/
        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is((int)productDTO.getQuantity())));
    }

    @Test
    public void givenUpdatedProduct_whenUpdateProduct_thenReturnUpdatedProductObject() throws Exception {
        long productId = 1L;
        ProductDTO productDTO = ProductDTO.builder()
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();
        ProductDTO updatedProductDTO = ProductDTO.builder()
                .category(null)
                .vendor(null)
                .image(null)
                .name("UpdatedProductTest")
                .price(80)
                .quantity(400)
                .build();

        BDDMockito.given(mapStructMapper.productToProductDTO(productService.updateProduct(updatedProductDTO, productId)))
                .willReturn(updatedProductDTO);

        BDDMockito.willDoNothing().given(productService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedProductDTO.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(updatedProductDTO.getPrice())));
    }

    @Test
    public void givenListOfProducts_whenGetAllProducts_thenReturnProductsList() throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        ProductDTO productDTO1 = ProductDTO.builder()
                .id(1L)
                .category(null)
                .vendor(null)
                .image(null)
                .name("Product1Test")
                .price(10.0)
                .quantity(100L)
                .build();
        ProductDTO productDTO2 = ProductDTO.builder()
                .id(2L)
                .category(null)
                .vendor(null)
                .image(null)
                .name("Product2Test")
                .price(20.0)
                .quantity(200L)
                .build();
        productDTOList.add(productDTO1);
        productDTOList.add(productDTO2);

        List<Product> productList = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .category(null)
                .vendor(null)
                .image(null)
                .productOrders(null)
                .name("Product1Test")
                .price(10.0)
                .quantity(100L)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .category(null)
                .vendor(null)
                .image(null)
                .productOrders(null)
                .name("Product2Test")
                .price(20.0)
                .quantity(200L)
                .build();
        productList.add(product1);
        productList.add(product2);

        BDDMockito.given(productService.getProducts()).willReturn(productList);

        BDDMockito.given(mapStructMapper.productToProductDTO(ArgumentMatchers.any(Product.class)))
                .willReturn(productDTO1, productDTO2);

        ResultActions response = mockMvc.perform(get("/api/products"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(productDTOList.size())));
    }


    @Test
    public void givenProductId_whenGetProductById_thenReturnProductObject() throws Exception {
        long productId = 1L;
        ProductDTO productDTO = ProductDTO.builder()
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();

        BDDMockito.given(mapStructMapper.productToProductDTO(productService.getProductById(productId)))
                .willReturn(productDTO);

        ResultActions response = mockMvc.perform(get("/api/products/{id}", productId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDTO.getName())));
    }

    @Test
    public void givenProductId_whenDeleteProduct_thenReturn200() throws Exception {
        long productId = 1L;
        BDDMockito.willDoNothing().given(productService).deleteProduct(productId);

        ResultActions response = mockMvc.perform(delete("/api/products/{id}", productId));

        response.andExpect(status().isOk())
                .andDo(print());
    }




}
