package com.example.clothshop.controller;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.service.CategoryService;
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
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    public void givenCategoryDTOObject_whenCreateCategory_thenReturnSavedCategory() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .products(null)
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
    public void givenUpdateCategory_whenUpdateCategory_thenReturnUpdatedCategoryObject() throws Exception {
        long categoryId = 1L;
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .products(null)
                .name("CategoryTestName")
                .build();
        CategoryDTO updatedCategoryDTO = CategoryDTO.builder()
                .products(null)
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
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryObject() throws Exception {
        long categoryId = 1L;
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .products(null)
                .name("CategoryNameTest")
                .build();

        BDDMockito.given(mapStructMapper.categoryToCategoryDTO(categoryService.getCategoryById(categoryId)))
                .willReturn(categoryDTO);

        ResultActions response = mockMvc.perform(get("/api/categories/{id}", categoryId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(categoryDTO.getName())));
    }


    @Test
    public void givenCategoryId_whenDeleteCategory_thenReturn200() throws Exception {
        long categoryId = 1L;
        BDDMockito.willDoNothing().given(categoryService).deleteCategory(categoryId);

        ResultActions response = mockMvc.perform(delete("/api/categories/{id}", categoryId));

        response.andExpect(status().isOk())
                .andDo(print());
    }

}
