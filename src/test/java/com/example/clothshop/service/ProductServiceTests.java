package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Product;
import com.example.clothshop.repository.ProductRepository;
import com.example.clothshop.util.exception.ProductNotCreatedException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MapStructMapper mapStructMapper;
    @InjectMocks
    private ProductService productService;
    private Product product;
    private ProductDTO productDTO;


    @BeforeEach
    public void setup(){
        product = Product.builder()
                .id(1L)
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();

        productDTO = ProductDTO.builder()
                .id(1L)
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();
    }

    @Test
    public void givenProductObject_whenSaveProduct_thenReturnProductObject() {
        given(productRepository.findByName(product.getName())).willReturn(Optional.empty());
        given(mapStructMapper.productDTOToProduct(productDTO)).willReturn(product);
        given(productRepository.save(product)).willReturn(product);

        Product savedProduct = productService.saveNewProduct(productDTO);

        assertThat(savedProduct).isNotNull();
    }

    @Test
    public void givenExistingProduct_whenSaveProduct_thenThrowsException() {
        given(productRepository.findByName(product.getName())).willReturn(Optional.of(product));

        org.junit.jupiter.api.Assertions.assertThrows(ProductNotCreatedException.class, () -> {
            productService.saveNewProduct(productDTO);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenProductList_whenGetAllProducts_thenReturnProductList() {
        Product product2 = Product.builder()
                .id(2L)
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest2")
                .price(88)
                .quantity(888)
                .build();

        given(productRepository.findAll()).willReturn(List.of(product, product2));

        List<Product> productList = productService.getProducts();

        assertThat(productList).isNotNull();
        assertThat(productList).size().isEqualTo(2);
    }

    @Test
    public void givenProductId_whenGetProductById_thenReturnProductObject() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        Product savedProduct = productService.getProductById(product.getId());

        assertThat(savedProduct).isNotNull();
    }

    @Test
    public void givenProductDTOObject_whenUpdateProduct_thenReturnUpdatedProduct() {
        given(mapStructMapper.productDTOToProduct(productDTO)).willReturn(product);
        given(productRepository.save(product)).willReturn(product);
        product.setName("UpdatedProductName");
        product.setQuantity(222);

        Product updatedProduct = productService.updateProduct(productDTO, 1L);

        assertThat(updatedProduct.getName()).isEqualTo("UpdatedProductName");
        assertThat(updatedProduct.getQuantity()).isEqualTo(222);
    }

    @DisplayName("JUnit test for deleteProduct method")
    @Test
    public void givenProductId_whenDeleteProduct_thenNothing() {
        long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        willDoNothing().given(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }



}
