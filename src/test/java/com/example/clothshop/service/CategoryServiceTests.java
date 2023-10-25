package com.example.clothshop.service;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Category;
import com.example.clothshop.repository.CategoryRepository;
import com.example.clothshop.util.CategoryNotCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MapStructMapper mapStructMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setup() {

        category = Category.builder()
                .id(1L)
                .products(new ArrayList<>())
                .name("CategoryTest")
                .build();

        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .products(new ArrayList<>())
                .name("CategoryTest")
                .build();
    }

    @Test
    public void givenCategoryObject_whenSaveCategory_thenReturnCategoryObject() {
        given(categoryRepository.findByName(categoryDTO.getName())).willReturn(Optional.empty());
        given(mapStructMapper.categoryDTOToCategory(categoryDTO)).willReturn(category);
        given(categoryRepository.save(category)).willReturn(category);

        Category savedCategory = categoryService.saveNewCategory(categoryDTO);

        assertThat(savedCategory).isNotNull();
    }

    @Test
    public void givenExistingCategory_whenSaveCategory_thenThrowsException() {
        given(categoryRepository.findByName(categoryDTO.getName())).willReturn(Optional.of(category));

        org.junit.jupiter.api.Assertions.assertThrows(CategoryNotCreatedException.class, () -> {
            categoryService.saveNewCategory(categoryDTO);
        });

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void givenCategoryList_whenGetAllCategories_thenReturnCategoryList() {
        Category category2 = Category.builder()
                .id(2L)
                .products(new ArrayList<>())
                .name("CategoryTest2")
                .build();

        given(categoryRepository.findAll()).willReturn(List.of(category, category2));

        List<Category> categoryList = categoryService.getCategories();

        assertThat(categoryList).isNotNull();
        assertThat(categoryList).size().isEqualTo(2);
    }

    @Test
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryObject() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        Category savedCategory = categoryService.getCategoryById(category.getId());

        assertThat(savedCategory).isNotNull();
    }

    @Test
    public void givenCategoryDTOObject_whenUpdateCategory_thenReturnUpdatedCategory() {
        given(mapStructMapper.categoryDTOToCategory(categoryDTO)).willReturn(category);
        given(categoryRepository.save(category)).willReturn(category);
        category.setName("UpdatedCategoryName");

        Category updatedCategory = categoryService.updateCategory(categoryDTO, 1L);

        assertThat(updatedCategory.getName()).isEqualTo("UpdatedCategoryName");
    }

    @Test
    public void givenCategoryId_whenDeleteCategory_thenNothing() {
        long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        willDoNothing().given(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
