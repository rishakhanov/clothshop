package com.example.clothshop.controller;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.service.CategoryService;
import com.example.clothshop.service.DiscountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private DiscountService discountService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    public void givenCategoryDTOObject_whenCreateCategory_thenReturnSavedCategory() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                //.products(null)
                .name("CategoryNameTest")
                .build();

        BDDMockito.given(mapStructMapper.categoryToCategoryDTO(categoryService.saveNewCategory(ArgumentMatchers.any(CategoryDTO.class))))
                .willReturn(categoryDTO);

        BDDMockito.willDoNothing().given(categoryService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(categoryDTO.getName())));
    }

    @Test
    public void givenDiscountDTOObject_whenCreateDiscount_thenReturnSavedDiscount() throws Exception {
        DiscountDTO discountDTO = DiscountDTO.builder()
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        BDDMockito.given(mapStructMapper.discountToDiscountDTO(discountService.saveNewDiscount(discountDTO)))
                .willReturn(discountDTO);

        BDDMockito.willDoNothing().given(discountService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(post("/api/categories/discounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(discountDTO)));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(discountDTO.getName())));
    }

    @Test
    public void givenUpdateCategory_whenUpdateCategory_thenReturnUpdatedCategoryObject() throws Exception {
        long categoryId = 1L;
        //CategoryDTO categoryDTO = CategoryDTO.builder()
                //.products(null)
                //.name("CategoryTestName")
                //.build();
        CategoryDTO updatedCategoryDTO = CategoryDTO.builder()
                //.products(null)
                .name("CategoryTestNameUpdated")
                .build();

        BDDMockito.given(mapStructMapper.categoryToCategoryDTO(categoryService.updateCategory(updatedCategoryDTO, categoryId)))
                .willReturn(updatedCategoryDTO);

        BDDMockito.willDoNothing().given(categoryService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(put("/api/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategoryDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedCategoryDTO.getName())));
    }

    @Test
    public void givenListOfCategories_whenGetAllCategories_thenReturnCategoriesList() throws Exception {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = Category.builder()
                .id(1L)
                .products(null)
                .name("CategoryTestName1")
                .build();
        Category category2 = Category.builder()
                .id(1L)
                .products(null)
                .name("CategoryTestName2")
                .build();
        categoryList.add(category1);
        categoryList.add(category2);

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        CategoryDTO categoryDTO1 = CategoryDTO.builder()
                .id(1L)
                //.products(null)
                .name("CategoryTestName1")
                .build();
        CategoryDTO categoryDTO2 = CategoryDTO.builder()
                .id(2L)
                //.products(null)
                .name("CategoryTestName2")
                .build();
        categoryDTOList.add(categoryDTO1);
        categoryDTOList.add(categoryDTO2);

        BDDMockito.given(categoryService.getCategories()).willReturn(categoryList);

        BDDMockito.given(mapStructMapper.categoryToCategoryDTO(ArgumentMatchers.any(Category.class)))
                .willReturn(categoryDTO1, categoryDTO2);

        ResultActions response = mockMvc.perform(get("/api/categories"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(categoryDTOList.size())));
    }

    @Test
    public void givenListOfDiscounts_whenGetAllDiscounts_thenReturnDiscountsList() throws Exception {
        List<Discount> discountList = new ArrayList<>();
        Discount discount1 = Discount.builder()
                .id(1L)
                .categories(null)
                .personDiscounts(null)
                .name("DiscountTest1")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        Discount discount2 = Discount.builder()
                .id(1L)
                .categories(null)
                .personDiscounts(null)
                .name("DiscountTest2")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        discountList.add(discount1);
        discountList.add(discount2);

        List<DiscountDTO> discountDTOList = new ArrayList<>();
        DiscountDTO discountDTO1 = DiscountDTO.builder()
                .name("DiscountDTOTest1")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        DiscountDTO discountDTO2 = DiscountDTO.builder()
                .name("DiscountDTOTest2")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        discountDTOList.add(discountDTO1);
        discountDTOList.add(discountDTO2);

        BDDMockito.given(discountService.getDiscounts()).willReturn(discountList);

        BDDMockito.given(mapStructMapper.discountToDiscountDTO(ArgumentMatchers.any(Discount.class)))
                .willReturn(discountDTO1, discountDTO2);

        ResultActions response = mockMvc.perform(get("/api/categories/discounts"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(discountDTOList.size())));

    }



    @Test
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryObject() throws Exception {
        long categoryId = 1L;
        CategoryDTO categoryDTO = CategoryDTO.builder()
                //.products(null)
                .name("CategoryNameTest")
                .build();

        BDDMockito.given(mapStructMapper.categoryToCategoryDTO(categoryService.getCategoryById(categoryId)))
                .willReturn(categoryDTO);

        ResultActions response = mockMvc.perform(get("/api/categories/{id}", categoryId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(categoryDTO.getName())));
    }

    @Test
    public void givenDiscountId_whenGetDiscountById_thenReturnDiscountObject() throws Exception {
        long discountId = 1L;
        DiscountDTO discountDTO = DiscountDTO.builder()
                .name("DiscountTest")
                .value(0)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2025-01-01"))
                .valid(true)
                .build();

        BDDMockito.given(mapStructMapper.discountToDiscountDTO(discountService.getDiscountById(discountId)))
                .willReturn(discountDTO);

        ResultActions response = mockMvc.perform(get("/api/categories/discounts/{id}", discountId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(discountDTO.getName())));
    }

    @Test
    public void givenCategoryId_whenDeleteCategory_thenReturn200() throws Exception {
        long categoryId = 1L;
        BDDMockito.willDoNothing().given(categoryService).deleteCategory(categoryId);

        ResultActions response = mockMvc.perform(delete("/api/categories/{id}", categoryId));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenDiscountId_whenDeleteDiscount_thenReturn200() throws Exception {
        long discountId = 1L;
        BDDMockito.willDoNothing().given(discountService).deleteDiscount(discountId);

        ResultActions response = mockMvc.perform(delete("/api/categories/discounts/{id}", discountId));

        response.andExpect(status().isOk())
                .andDo(print());
    }


}
