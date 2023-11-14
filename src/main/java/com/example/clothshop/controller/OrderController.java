package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.OrderDTO;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.dto.OrderProductDTO;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Product;
import com.example.clothshop.service.OrderService;
import com.example.clothshop.service.ProductService;
import com.example.clothshop.util.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    private final MapStructMapper mapStructMapper;

    @GetMapping()
    public List<OrderDTO> getOrders() {
        return orderService.getOrders().stream().map(this::convertToOrderDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable("id") long id) {
        return convertToOrderDTO(orderService.getOrderById(id));
    }

    @GetMapping("/{id}/items")
    public List<OrderProductDTO> getOrderItems(@PathVariable("id") long id) {
        List<Product> products = orderService.getOrderProducts(id);
        return products.stream().map(mapStructMapper::productToOrderProductDTO).collect(Collectors.toList());
    }

    @GetMapping("/{oid}/items/{iid}")
    public ProductDTO getItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid) {
        Product product = orderService.getOrderItem(oid, iid);
        return mapStructMapper.productToProductDTO(product);
    }

    @PostMapping("/{id}/items")
    public OrderProductDTO addItemToOrder(@PathVariable("id") long id,
                                          @RequestBody @Valid OrderProductDTO orderProductDTO,
                                          BindingResult bindingResult) {
        Product product = productService.getProductById(orderProductDTO.getId());
        productService.checkForValidationErrors(bindingResult);
        orderService.addProductToOrder(id, product, orderProductDTO.getQuantity());

        orderProductDTO.setPrice(product.getPrice());
        orderProductDTO.setName(product.getName());
        return orderProductDTO;
//        return mapStructMapper.productToProductDTO(
//                orderService.addProductToOrder(id, product, productDTO.getQuantity()));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        if (orderService.deleteOrder(id)) {
            return "Order with ID = " + id + " was deleted.";
        } else {
            return "Order with ID = " + id + " was paid, canceled or complete and cannot be deleted.";
        }
    }

    @DeleteMapping("/{oid}/items/{iid}")
    public String deleteItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid) {
        orderService.deleteProductOfOrder(oid, iid);
        return "Product with ID = " + iid + " was deleted from order with ID = " + oid;
    }

    @GetMapping("/users/{id}")
    public List<OrderDTO> getOrdersOfPerson(@PathVariable("id") long id) {
        List<Orders> ordersList = orderService.getOrdersOfUser(id);
        return ordersList.stream().map(mapStructMapper::orderToOrderDTO).collect(Collectors.toList());
    }

    @PostMapping("/users/{id}")
    public OrderDTO createOrderForPerson(@PathVariable("id") long id, @RequestBody @Valid OrderDTO orderDTO,
                                         BindingResult bindingResult) {
        orderService.checkForValidationErrors(bindingResult);
        return mapStructMapper.orderToOrderDTO(orderService.saveNewOrder(orderDTO, id));
    }

    @PostMapping("/{id}/purchase")
    public OrderDTO purchaseOrder(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.purchaseOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public OrderDTO cancelOrder(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.cancelOrder(id));
    }

    @PostMapping("/{id}/ship")
    public OrderDTO shipOrder(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.shipOrder(id));
    }

    private OrderDTO convertToOrderDTO(Orders order) {
        return mapStructMapper.orderToOrderDTO(order);
    }

    @ExceptionHandler
    private ResponseEntity<OrderErrorResponse> handleOrderNotFoundException(OrderNotFoundException exception) {
        OrderErrorResponse response = new OrderErrorResponse("Order with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<OrderErrorResponse> handleOrderNotCreatedException(OrderNotCreatedException exception) {
        OrderErrorResponse response = new OrderErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<OrderErrorResponse> handleOrderNotFulfilledException(OrderNotFulfilledException exception) {
        OrderErrorResponse response = new OrderErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler
    private ResponseEntity<ProductErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ProductErrorResponse productErrorResponse = new ProductErrorResponse("Product with this id wasn't found!");
        return new ResponseEntity<>(productErrorResponse, HttpStatus.NOT_FOUND);
   }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotFoundException(PersonNotFoundException exception) {
        PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
