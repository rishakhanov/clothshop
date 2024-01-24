package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.repository.ProductRepository;
import com.example.clothshop.util.exception.ProductNotCreatedException;
import com.example.clothshop.util.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private MapStructMapper mapStructMapper;
    public ProductService(ProductRepository productRepository, MapStructMapper mapStructMapper) {
        this.productRepository = productRepository;
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

    @Transactional
    public Product updateProduct(ProductDTO productDTO, long id) {
        Product product = mapStructMapper.productDTOToProduct(productDTO);
        product.setId(id);
        productRepository.save(product);
        return product;
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

}
