package com.example.clothshop.dao;

import com.example.clothshop.dto.ProductDiscountDTO;
import com.example.clothshop.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ProductDiscountDTO> getProductsByCategoryWithPersonDiscounts(long categoryId) {
        Query query = entityManager.createQuery("SELECT NEW com.example.clothshop.dto.ProductDiscountDTO(" +
                    "p.id, " +
                    "p.name, " +
                    "p.price, " +
                    "(p.price - p.price * dt.value), " +
                    "p.quantity) " +
                    "FROM Product p " +
                    "LEFT JOIN Category as ct on p.category.id = ct.id " +
                    "LEFT JOIN Discount as dt on ct.discount.id = dt.id " +
                    "WHERE p.category.id = " + categoryId);

        List<ProductDiscountDTO> products = (List<ProductDiscountDTO>) query.getResultList();
        return products;
    }

}
