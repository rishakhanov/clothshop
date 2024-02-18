package com.example.clothshop.dao;

import com.example.clothshop.dto.ProductDiscountDTO;
import com.example.clothshop.entity.Product;

import java.util.List;

public interface ProductDAO {

    List<ProductDiscountDTO> getProductsByCategoryWithPersonDiscounts(boolean discountExists, long categoryId);

}
