package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.OrderDTO;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.ProductOrders;
import com.example.clothshop.repository.OrderRepository;
import com.example.clothshop.repository.ProductOrdersRepository;
import com.example.clothshop.util.exception.OrderNotCreatedException;
import com.example.clothshop.util.exception.OrderNotFoundException;
import com.example.clothshop.util.exception.OrderNotFulfilledException;
import com.example.clothshop.util.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final MapStructMapper mapStructMapper;
    private final ProductOrdersRepository productOrdersRepository;
    private final ProductService productService;


    public OrderService(OrderRepository orderRepository, PersonService personService,
                        MapStructMapper mapStructMapper, ProductOrdersRepository productOrdersRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.personService = personService;
        this.mapStructMapper = mapStructMapper;
        this.productOrdersRepository = productOrdersRepository;
        this.productService = productService;
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
        List<Product> products = new ArrayList<>();
        Product product;
        List<ProductOrders> productOrders = order.getProductOrders();
        for (ProductOrders item : productOrders) {
            product = item.getProduct();
            product.setQuantity(item.getQuantity());
            products.add(product);
        }
        return products;
        //return productOrders.stream().map(ProductOrders::getProduct).toList();
        //return productOrdersService.getProductsByOrderId(id);
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
    public Product addProductToOrder(long orderId, Product product, long quantity) {
        Orders order = getOrderById(orderId);
        List<ProductOrders> productOrders = order.getProductOrders();
        for (ProductOrders item : productOrders) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(quantity);
                order.setProductOrders(productOrders);
                orderRepository.save(order);
                return product;
            }
        }

        ProductOrders productOrdersNew = new ProductOrders();
        productOrdersNew.setOrders(order);
        productOrdersNew.setProduct(product);
        productOrdersNew.setQuantity(quantity);
        productOrders.add(productOrdersNew);
        order.setProductOrders(productOrders);
        orderRepository.save(order);
        return product;
        //return productOrdersService.addProductToOrder(order, product, quantity);
    }

    @Transactional
    public boolean deleteOrder(long id) {
        Orders order = getOrderById(id);
        if (order.getStatus().getCode().matches("P|CD|CE")) {
            return false;
        } else {
            orderRepository.delete(order);
            return true;
        }
    }

    @Transactional
    public void deleteProductOfOrder(long orderId, long productId) {
        Orders order = getOrderById(orderId);
        List<ProductOrders> productOrders = order.getProductOrders();

        //productOrdersService.deleteProductOfOrder(order, productId);

        List<ProductOrders> updatedProductsList = new ArrayList<>();
        boolean productNotFound = true;

        for (ProductOrders item : productOrders) {
            if (item.getProduct().getId().equals(productId)) {
                productOrdersRepository.delete(item);
                productNotFound = false;
                break;
            } else {
                updatedProductsList.add(item);
            }
        }

        if (productNotFound) {
            throw new ProductNotFoundException();
        }

        order.setProductOrders(updatedProductsList);
        orderRepository.save(order);

    }

    public List<Orders> getOrdersOfUser(long userId) {
        personService.getPersonById(userId);
        List<Orders> orders = orderRepository.findAllByPersonId(userId);
        return orders;
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

            throw new OrderNotCreatedException(errorMsg.toString());
        }
    }

    @Transactional
    public Orders saveNewOrder(OrderDTO orderDTO, long userId) {
        personService.getPersonById(userId);
        Orders order = mapStructMapper.orderDTOToOrder(orderDTO);
        order.setShipDate(null);
        order.setStatus(OrdersStatus.NEW);
        return orderRepository.save(order);
    }

    @Transactional
    public Orders purchaseOrder(long orderId) {
        Orders order = getOrderById(orderId);

        if (!order.getStatus().equals(OrdersStatus.NEW)) {
            throw new OrderNotFulfilledException("Order could not be fulfilled: order status is not NEW");
        }

        List<ProductOrders> productOrders = order.getProductOrders();
        checkOrderQuantities(productOrders);
        Product product;
        for (ProductOrders productOrder : productOrders) {
            product = productService.getProductById(productOrder.getProduct().getId());
            product.setQuantity(product.getQuantity() - productOrder.getQuantity());
            productService.saveProduct(product);
        }
        order.setStatus(OrdersStatus.PAID);
        return orderRepository.save(order);
    }

    private void checkOrderQuantities(List<ProductOrders> productOrders) {
        Product product;
        for (ProductOrders productOrder : productOrders) {
            product = productService.getProductById(productOrder.getProduct().getId());
            if (product.getQuantity() < productOrder.getQuantity()) {
                throw new OrderNotFulfilledException("Order could not be fulfilled: requested order quantity "
                        + "of product " + product.getName() + " exceed available amount.");
            }
        }
    }

    @Transactional
    public Orders cancelOrder(long orderId) {
        Orders order = getOrderById(orderId);

        if (!order.getStatus().equals(OrdersStatus.PAID)) {
            throw new OrderNotFulfilledException("Order could not be fulfilled: order status is not PAID");
        }

        List<ProductOrders> productOrders = order.getProductOrders();
        Product product;

        for (ProductOrders productOrder : productOrders) {
            product = productService.getProductById(productOrder.getProduct().getId());
            product.setQuantity(product.getQuantity() + productOrder.getQuantity());
            productService.saveProduct(product);
        }

        order.setStatus(OrdersStatus.CANCELED);
        return orderRepository.save(order);
    }

    @Transactional
    public Orders shipOrder(long orderId) {
        Orders order = getOrderById(orderId);

        if (!order.getStatus().equals(OrdersStatus.PAID)) {
            throw new OrderNotFulfilledException("Order could not be fulfilled: order status is not PAID");
        }

        order.setShipDate(LocalDate.now());
        order.setStatus(OrdersStatus.COMPLETE);
        return orderRepository.save(order);
    }

    //        if (quantity > product.getQuantity()) {
//            throw new OrderNotCreatedException("Order cannot be created. " + product.getName()
//                    + " order quantity (" + quantity + ") exceeds the available stock level ("
//                    + product.getQuantity() + ")");
//        }
}
