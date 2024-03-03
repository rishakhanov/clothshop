package com.example.clothshop.controller;

import com.example.clothshop.dto.CategoryDTO;
import com.example.clothshop.dto.DiscountDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.entity.Product;
import com.example.clothshop.service.CategoryService;
import com.example.clothshop.service.DiscountService;
import com.example.clothshop.util.exception.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final DiscountService discountService;

    private MapStructMapper mapStructMapper;

    @GetMapping()
    public List<CategoryDTO> getCategories() {
        return categoryService.getCategories().stream().map(e -> mapStructMapper.categoryToCategoryDTO(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategory(@PathVariable("id") long id) {
        return mapStructMapper.categoryToCategoryDTO(categoryService.getCategoryById(id));
    }

    @GetMapping("/{id}/products")
    public List<ProductDTO> getProducts(@PathVariable("id") long id) {
        List<Product> productList = categoryService.getProductsOfACategory(id);
        return productList.stream().map(item -> mapStructMapper.productToProductDTO(item))
                .collect(Collectors.toList());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        categoryService.checkForValidationErrors(bindingResult);
        return mapStructMapper.categoryToCategoryDTO(categoryService.saveNewCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable("id") long id, @RequestBody @Valid CategoryDTO categoryDTO,
                              BindingResult bindingResult) {
        categoryService.checkForValidationErrors(bindingResult);
        return mapStructMapper.categoryToCategoryDTO(categoryService.updateCategory(categoryDTO, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return "Category with ID = " + id + " was deleted.";
    }

    @GetMapping("/discounts")
    public List<DiscountDTO> getDiscounts() {
        return discountService.getDiscounts().stream().map(e -> mapStructMapper.discountToDiscountDTO(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/discounts/{id}")
    public DiscountDTO getDiscount(@PathVariable("id") long id) {
        return mapStructMapper.discountToDiscountDTO(discountService.getDiscountById(id));
    }

    @PostMapping("/discounts")
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountDTO createDiscount(@RequestBody @Valid DiscountDTO discountDTO, BindingResult bindingResult) {
        discountService.checkForValidationErrors(bindingResult);
        return mapStructMapper.discountToDiscountDTO(discountService.saveNewDiscount(discountDTO));
    }

    @DeleteMapping("/discounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDiscount(@PathVariable("id") long id) {
        discountService.deleteDiscount(id);
        return "Discount with ID = " + id + " was deleted.";
    }

    @PutMapping("/{cid}/discount/{did}")
    public CategoryDTO changeDiscount(@PathVariable("cid") long categoryId, @PathVariable("did") long discountId) {
        return mapStructMapper.categoryToCategoryDTO(categoryService.changeDiscount(categoryId, discountId));
    }

    /*
    @ExceptionHandler
    private ResponseEntity<CategoryErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        CategoryErrorResponse response = new CategoryErrorResponse("Category with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<CategoryErrorResponse> handleCategoryNotCreatedException(CategoryNotCreatedException exception) {
        CategoryErrorResponse response = new CategoryErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<DiscountErrorResponse> handleDiscountNotFoundException(DiscountNotFoundException exception) {
        DiscountErrorResponse response = new DiscountErrorResponse("Discount with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

     */


}
