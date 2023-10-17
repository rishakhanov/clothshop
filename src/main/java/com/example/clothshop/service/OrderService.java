package com.example.clothshop.service;

import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Product;
import com.example.clothshop.repository.OrderRepository;
import com.example.clothshop.util.OrderNotFoundException;
import com.example.clothshop.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> getOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders;
    }

    public Orders getOrderById(long id) {
        Optional<Orders> foundOrder = orderRepository.findById(id);
        return foundOrder.orElseThrow(OrderNotFoundException::new);
    }

    public List<Product> getOrderProducts(long id) {
        Orders order = getOrderById(id);
        return order.getOrderedProducts();
//        Optional<Orders> foundOrder = orderRepository.findById(id);
//        if (foundOrder.isPresent()) {
//            return foundOrder.get().getOrderedProducts();
//        } else {
//            throw new OrderNotFoundException();
//        }
    }

    public Product getOrderItem(long oid, long iid) {
        List<Product> products = getOrderProducts(oid);
        for (Product product : products) {
            if (product.getId().equals(iid)) {
                return product;
            }
        }
        throw new ProductNotFoundException();
    }

    @Transactional
    public void addProductToOrder(long orderId, Product product) {
        Orders order = getOrderById(orderId);
        order.getOrderedProducts().add(product);
        orderRepository.save(order);
    }

    @Transactional
    public boolean deleteOrder(long id) {
        Orders order = getOrderById(id);
        if (order.getStatus().equals("paid")) {
            return false;
        } else {
            orderRepository.delete(order);
            return true;
        }
    }

    @Transactional
    public void deleteProductOfOrder(long orderId, long productId) {
        Orders order = getOrderById(orderId);
        List<Product> updatedProductsList = new ArrayList<>();
        boolean productNotFound = true;

        for (Product product : order.getOrderedProducts()) {
            if (product.getId().equals(productId)) {
                productNotFound = false;
            } else {
                updatedProductsList.add(product);
            }
        }

        if (productNotFound) {
            throw new ProductNotFoundException();
        }

        order.setOrderedProducts(updatedProductsList);
        orderRepository.save(order);
    }
}
