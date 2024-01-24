package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.service.ImageService;
import com.example.clothshop.service.ProductService;
import com.example.clothshop.util.exception.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;
    private MapStructMapper mapStructMapper;


    @GetMapping()
    public List<ProductDTO> getProducts() {
        return productService
                .getProducts()
                .stream()
                .map(e -> mapStructMapper.productToProductDTO(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable("id") long id) {
        return mapStructMapper.productToProductDTO(productService.getProductById(id));
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> retrievePhoto(@PathVariable("id") long id) {
        Product product = productService.getProductById(id);
        Image image = imageService.getImage(product.getImage().getName());
        var body =  new ByteArrayResource(image.getData());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, image.getType())
                .body(body);
    }

    @PutMapping ("/{id}/photo")
    public ImageSaveResponse uploadPhoto(@PathVariable("id") long id, @RequestPart MultipartFile file) {
        try {
            Product product = productService.getProductById(id);
            Image image = imageService.save(file);
            productService.updatePhoto(product, image);
            return ImageSaveResponse.builder()
                    .success(true)
                    .filename(file.getOriginalFilename())
                    .build();
        } catch (Exception e) {
            log.error("Failed to save image", e);
            return ImageSaveResponse.builder().success(false).filename(file.getOriginalFilename()).build();
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult) {
        productService.checkForValidationErrors(bindingResult);
        ProductDTO productDTO1 = mapStructMapper.productToProductDTO(productService.saveNewProduct(productDTO));
        //return mapStructMapper.productToProductDTO(productService.saveNewProduct(productDTO));
        return productDTO1;
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable("id") long id, @RequestBody @Valid ProductDTO productDTO,
                                             BindingResult bindingResult) {
        productService.checkForValidationErrors(bindingResult);
        return mapStructMapper.productToProductDTO(productService.updateProduct(productDTO, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "Product with ID = " + id + " was deleted.";
    }

    @ExceptionHandler
    private ResponseEntity<ProductErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ProductErrorResponse response = new ProductErrorResponse("Product with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ImageErrorResponse> handleImageNotFoundException(ImageNotFoundException exception) {
        ImageErrorResponse response = new ImageErrorResponse("Image with this id wasn't found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ProductErrorResponse> handleProductNotCreatedException(ProductNotCreatedException exception) {
        ProductErrorResponse response = new ProductErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<CategoryErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        CategoryErrorResponse response = new CategoryErrorResponse("Category with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<VendorErrorResponse> handleVendorNotFoundException(VendorNotFoundException exception) {
        VendorErrorResponse response = new VendorErrorResponse("Vendor with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
