package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDiscountDTO;
import com.example.clothshop.entity.*;
import com.example.clothshop.repository.OrderRepository;
import com.example.clothshop.repository.PersonDiscountRepository;
import com.example.clothshop.repository.ProductOrdersRepository;
import com.example.clothshop.util.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final ProductOrdersRepository productOrdersRepository;
    private final ProductService productService;
    private final PersonDiscountRepository personDiscountRepository;
    private final MapStructMapper mapStructMapper;



    public OrderService(OrderRepository orderRepository, PersonService personService,
                        ProductOrdersRepository productOrdersRepository, ProductService productService, PersonDiscountRepository personDiscountRepository, MapStructMapper mapStructMapper) {
        this.orderRepository = orderRepository;
        this.personService = personService;
        this.productOrdersRepository = productOrdersRepository;
        this.productService = productService;
        this.personDiscountRepository = personDiscountRepository;
        this.mapStructMapper = mapStructMapper;
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
    public double addProductToOrder(long orderId, Product product, long quantity, Long personId) {
        Orders order = getOrderById(orderId);
        List<ProductOrders> productOrders = order.getProductOrders();
        double discountedPrice = calculateDiscountedPrice(product, personId);

        for (ProductOrders item : productOrders) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(quantity);
                item.setDiscountedPrice(discountedPrice);
                order.setProductOrders(productOrders);
                orderRepository.save(order);
                return discountedPrice;
            }
        }

        ProductOrders productOrdersNew = new ProductOrders();
        productOrdersNew.setOrders(order);
        productOrdersNew.setProduct(product);
        productOrdersNew.setQuantity(quantity);
        productOrdersNew.setDiscountedPrice(discountedPrice);
        productOrders.add(productOrdersNew);
        order.setProductOrders(productOrders);
        orderRepository.save(order);
        return discountedPrice;
    }

    private double calculateDiscountedPrice(Product product, Long personId) {
        List<PersonDiscount> personDiscountList = personDiscountRepository.findPersonDiscountsByPersonId(personId);
        if (!personDiscountList.isEmpty()) {
            Category category = product.getCategory();
            Discount discount = category.getDiscount();
            LocalDate today = LocalDate.now();
            if (today.isBefore(discount.getStartDate()) || today.isAfter(discount.getEndDate())) {
                return product.getPrice();
            }
            Long discountId = discount.getId();
            for (PersonDiscount personDiscount : personDiscountList) {
                if (personDiscount.getDiscount().getId().equals(discountId)) {
                    return product.getPrice() - product.getPrice() * discount.getValue();
                }
            }
        }
        return product.getPrice();
    }


    @Transactional
    public void deleteOrder(long id) {
        Orders order = getOrderById(id);

        if (order.getStatus().getCode().matches("P|CD|CE")) {
            throw new OrderCouldNotBeDeletedException("Order with ID = " + id + " was paid, canceled or complete and cannot be deleted.");
        } else {
            orderRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteProductOfOrder(long orderId, long productId) {
        Orders order = getOrderById(orderId);
        List<ProductOrders> productOrders = order.getProductOrders();
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
    public Orders saveNewOrder(long userId) {
        Person person = personService.getPersonById(userId);
        Orders order = createOrder(person);
        return orderRepository.save(order);
    }

    private Orders createOrder(Person person) {
        Orders order = new Orders();
        order.setPerson(person);
        order.setProductOrders(new ArrayList<>());
        order.setCreatedAt(LocalDate.now());
        order.setShipDate(null);
        order.setStatus(OrdersStatus.NEW);
        return order;
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

    public void checkAuthority(long personId, long orderId) {
        Person person = personService.getPersonById(personId);
        List<Roles> rolesList = person.getRolesList();
        for (Roles role : rolesList) {
            if (role.getName().equals("ROLE_ADMIN")) {
                return;
            }
        }
        Optional<Orders> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            if (order.get().getPerson().getId() == personId) {
                return;
            } else {
                throw new PersonUnauthorizedException();
            }
        }
    }

    public List<ProductDiscountDTO> getProducts(Long personId) {//delete
        List<PersonDiscount> personDiscountList = personDiscountRepository.findPersonDiscountsByPersonId(personId);
        if (personDiscountList.isEmpty()) {
            return getProductsWithoutDiscounts();
        } else {
                return getProductsWithDiscounts(personId, personDiscountList);
        }
    }

    private List<ProductDiscountDTO> getProductsWithoutDiscounts() {//delete
        return productService
                .getProducts()
                .stream()
                .map(e -> mapStructMapper.productToProductDiscountDTO(e))
                .collect(Collectors.toList());
    }

    private List<ProductDiscountDTO> getProductsWithDiscounts(Long personId, List<PersonDiscount> personDiscountList) {//delete
    return null;

    }



}
