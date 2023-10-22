package com.example.clothshop.repository;

import com.example.clothshop.entity.Category;
import com.example.clothshop.entity.Image;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private ImageRepository imageRepository;

    private Product product;

    @BeforeEach
    public void setup() {
        Category category = categoryRepository.getReferenceById(1L);
        Vendor vendor = vendorRepository.getReferenceById(1L);
        Image image = imageRepository.getReferenceById(1L);

        product = Product.builder()
                .category(category)
                .vendor(vendor)
                .image(image)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();
    }

    //@DisplayName("JUnit test for save product operation")
    @Test
    public void givenProductObject_whenSave_thenReturnSavedObject() {
        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void givenProductList_whenFindAll_thenProductList() {
        int productSize = (int) productRepository.count();
        List<Product> productList = productRepository.findAll();

        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(productSize);
    }

    @Test
    public void givenProductObject_whenFindById_thenReturnProductObject() {
        Product savedProduct = productRepository.save(product);

        Product productDB = productRepository.findById(savedProduct.getId()).get();

        assertThat(productDB).isNotNull();
    }

    @Test
    public void givenProductName_whenFindByName_thenReturnProductObject() {
        Product savedProduct = productRepository.save(product);

        Product productDB = productRepository.findByName(product.getName()).get();

        assertThat(productDB).isNotNull();
    }

    @Test
    public void givenProductObject_whenUpdateProduct_thenReturnUpdatedProduct() {

        productRepository.save(product);

        Product savedProduct = productRepository.findById(product.getId()).get();
        savedProduct.setName("UpdatedProductTest");
        savedProduct.setPrice(88);
        Product updatedProduct = productRepository.save(savedProduct);

        assertThat(updatedProduct.getName()).isEqualTo("UpdatedProductTest");
        assertThat(updatedProduct.getPrice()).isEqualTo(88);
    }

    @Test
    public void givenProductObject_whenDelete_thenRemoveProduct() {
        productRepository.save(product);

        productRepository.delete(product);
        Optional<Product> productOptional = productRepository.findById(product.getId());

        assertThat(productOptional.isEmpty());
    }
}
