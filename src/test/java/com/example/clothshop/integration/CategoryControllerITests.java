package com.example.clothshop.integration;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.repository.CategoryRepository;
import com.example.clothshop.repository.DiscountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenListOfCategories_whenGetAllCategories_thenReturnCategoriesList() throws Exception {
        int repositorySize = (int) categoryRepository.count();

        ResultActions response = mockMvc.perform(get("/api/categories"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(repositorySize)));
    }

    @Test
    public void givenListOfDiscounts_whenGetAllDiscounts_thenReturnDiscountsList() throws Exception {
        int repositorySize = (int) discountRepository.count();

        ResultActions response = mockMvc.perform(get("/api/categories/discounts"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(repositorySize)));
    }

    @Test
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryObject() throws Exception {
        long categoryId = 1L;
        Category category = categoryRepository.findById(categoryId).get();

        ResultActions response = mockMvc.perform(get("/api/categories/{id}", categoryId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(category.getName())));
    }

    @Test
    public void givenDiscountId_whenGetDiscountById_thenReturnDiscountObject() throws Exception {
        long discountId = 1L;
        Discount discount = discountRepository.findById(discountId).get();

        ResultActions response = mockMvc.perform(get("/api/categories/discounts/{id}", discountId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(discount.getName())));
    }



    @Test
    public void givenCategoryDTOObject_whenCreateCategory_thenReturnSavedCategory() throws Exception {
        //create category and check expectations
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("categoryTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(categoryDTO.getName())))
                .andReturn();

        //delete created category
        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());
        categoryRepository.deleteById(id);

    }

    @Test
    public void givenDiscountDTOObject_whenCreateDiscount_thenReturnSavedDiscount() throws Exception {
        //create discount and check expectations
        DiscountDTO discountDTO = DiscountDTO.builder()
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        MvcResult result = mockMvc.perform(post("/api/categories/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(discountDTO.getName())))
                .andReturn();

        //delete created discount
        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());
        discountRepository.deleteById(id);
    }

    @Test
    public void givenUpdateCategory_whenUpdateCategory_thenReturnUpdatedCategoryObject() throws Exception {
        //create category and get id
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("categoryTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        //update category and check expectations
        CategoryDTO updatedCategoryDTO = CategoryDTO.builder()
                .name("categoryTestNameUpdated")
                .build();

        ResultActions response = mockMvc.perform(put("/api/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategoryDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedCategoryDTO.getName())));

        //delete created category
        categoryRepository.deleteById(id);
    }

    @Test
    public void givenCategoryId_whenDeleteCategory_thenReturn200() throws Exception {
        //create category and get id
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("categoryTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        //delete category by id and check expectations
        ResultActions response = mockMvc.perform(delete("/api/categories/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenDiscountId_whenDeleteDiscount_thenReturn200() throws Exception {
        //create discount and get id
        DiscountDTO discountDTO = DiscountDTO.builder()
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        MvcResult result = mockMvc.perform(post("/api/categories/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDTO)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        //delete category by id and check expectations
        ResultActions response = mockMvc.perform(delete("/api/categories/discounts/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }

}
