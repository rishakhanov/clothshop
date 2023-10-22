package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.ProductRepository;
import com.example.clothshop.util.ProductNotCreatedException;
import com.example.clothshop.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final VendorService vendorService;
    private final ImageService imageService;
    private MapStructMapper mapStructMapper;
    public ProductService(ProductRepository productRepository, CategoryService categoryService,
                          VendorService vendorService, ImageService imageService,
                          MapStructMapper mapStructMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.vendorService = vendorService;
        this.imageService = imageService;
        this.mapStructMapper = mapStructMapper;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.orElseThrow(ProductNotFoundException::new);
    }

    public Product getProductByName(String name) {
        Optional<Product> foundProduct = productRepository.findByName(name);
        return foundProduct.orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    public Product saveNewProduct(ProductDTO productDTO) {
        Optional<Product> foundProduct = productRepository.findByName(productDTO.getName());
        if (foundProduct.isPresent()) {
            throw new ProductNotCreatedException("The product already exists.");
        } else {
            checkForExistenceCategoryVendorImage(productDTO);
            Product product = mapStructMapper.productDTOToProduct(productDTO);
            return productRepository.save(product);
        }
    }

    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Transactional
    public void updatePhoto(Product product, Image image) {
        product.setImage(image);
        productRepository.save(product);

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

            throw new ProductNotCreatedException(errorMsg.toString());
        }
    }

    public void checkForExistenceCategoryVendorImage(ProductDTO productDTO) {
        categoryService.getCategoryById(productDTO.getCategory().getId());
        vendorService.getVendorById(productDTO.getVendor().getId());
        imageService.getImageById(productDTO.getImage().getId());
    }

    @Transactional
    public Product updateProduct(ProductDTO productDTO, long id) {
        Product product = mapStructMapper.productDTOToProduct(productDTO);
        product.setId(id);
        productRepository.save(product);
        return product;
    }

}