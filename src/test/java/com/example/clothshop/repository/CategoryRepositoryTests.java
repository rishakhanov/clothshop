package com.example.clothshop.repository;

import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DiscountRepository discountRepository;

    private Category category;

    @BeforeEach
    public void setup() {
        List<Product> productList = new ArrayList<>();
        Optional<Discount> discount = discountRepository.findById(1L);

        category = Category.builder()
                .name("CategoryTest")
                .products(productList)
                .discount(discount.get())
                .build();
    }

    @Test
    public void givenCategoryObject_whenSave_thenReturnSavedObject() {
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void givenCategoryList_whenFindAll_thenCategoryList() {
        int categorySize = (int) categoryRepository.count();
        List<Category> categoryList = categoryRepository.findAll();

        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(categorySize);
    }

    @Test
    public void givenCategoryObject_whenFindById_thenReturnCategoryObject() {
        Category savedCategory = categoryRepository.save(category);

        Category categoryDB = categoryRepository.findById(savedCategory.getId()).get();

        assertThat(categoryDB).isNotNull();
    }

    @Test
    public void givenCategoryName_whenFindByName_thenReturnCategoryObject() {
        Category savedCategory = categoryRepository.save(category);

        Category categoryDB = categoryRepository.findByName(savedCategory.getName()).get();

        assertThat(categoryDB).isNotNull();
    }

    @Test
    public void givenCategoryObject_whenUpdateCategory_thenReturnUpdatedCategory() {
        categoryRepository.save(category);

        Category savedCategory = categoryRepository.findById(category.getId()).get();
        savedCategory.setName("UpdatedCategoryTest");
        Category updatedCategory = categoryRepository.save(savedCategory);

        assertThat(updatedCategory.getName()).isEqualTo("UpdatedCategoryTest");
    }

    @Test
    public void givenCategoryObject_whenDelete_thenRemoveCategory() {
        categoryRepository.save(category);

        categoryRepository.delete(category);
        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());

        assertThat(categoryOptional.isEmpty());
    }
}
