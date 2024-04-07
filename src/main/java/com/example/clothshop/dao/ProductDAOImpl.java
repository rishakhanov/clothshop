package com.example.clothshop.dao;

import com.example.clothshop.dto.ProductDiscountDTO;
import com.example.clothshop.entity.Product;
import jakarta.persistence.Converter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ProductDiscountDTO> getProductsByCategoryWithPersonDiscounts(long categoryId) {
        LocalDate today = LocalDate.now();
        //var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String dateF = today.format(dtf);
        Query query = entityManager.createQuery("SELECT NEW com.example.clothshop.dto.ProductDiscountDTO(" +
                    "p.id, " +
                    "p.name, " +
                    "p.price, " +
                    "(p.price - p.price * dt.value), " +
                    "p.quantity) " +
                    "FROM Product p " +
                    "LEFT JOIN Category as ct on p.category.id = ct.id " +
                    "LEFT JOIN Discount as dt on ct.discount.id = dt.id " +
                    "WHERE (p.category.id = " + categoryId + ") " +
                    "AND " + "'" + today +"'" + " between dt.startDate and dt.endDate" );

        List<ProductDiscountDTO> products = (List<ProductDiscountDTO>) query.getResultList();
        return products;
    }

}
