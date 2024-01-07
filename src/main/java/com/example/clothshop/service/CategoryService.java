package com.example.clothshop.service;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Product;
import com.example.clothshop.repository.CategoryRepository;
import com.example.clothshop.util.exception.CategoryNotCreatedException;
import com.example.clothshop.util.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapStructMapper mapStructMapper;

    public CategoryService(CategoryRepository categoryRepository, MapStructMapper mapStructMapper) {
        this.categoryRepository = categoryRepository;
        this.mapStructMapper = mapStructMapper;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(long id) {
        Optional<Category> foundCategory = categoryRepository.findById(id);
        return foundCategory.orElseThrow(CategoryNotFoundException::new);
    }

    public List<Product> getProductsOfACategory(long id) {
        Optional<Category> foundCategory = categoryRepository.findById(id);
        if (foundCategory.isPresent()) {
            return foundCategory.get().getProducts();
        } else {
            throw new CategoryNotFoundException();
        }
    }

    public void checkForValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new CategoryNotCreatedException(errorMsg.toString());
        }
    }

    @Transactional
    public Category saveNewCategory(CategoryDTO categoryDTO) {
        Optional<Category> foundCategory = categoryRepository.findByName(categoryDTO.getName());

        if (foundCategory.isPresent()) {
            throw new CategoryNotCreatedException("The category already exists.");
        } else {
            Category category = mapStructMapper.categoryDTOToCategory(categoryDTO);
            return categoryRepository.save(category);
        }
    }

    @Transactional
    public void deleteCategory(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException();
        }
    }

    @Transactional
    public Category updateCategory(CategoryDTO categoryDTO, long id) {
        Category category = mapStructMapper.categoryDTOToCategory(categoryDTO);
        category.setId(id);
        categoryRepository.save(category);
        return category;
    }
}
