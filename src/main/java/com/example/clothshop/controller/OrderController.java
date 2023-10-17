package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.OrderDTO;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Product;
import com.example.clothshop.service.OrderService;
import com.example.clothshop.service.ProductService;
import com.example.clothshop.util.OrderErrorResponse;
import com.example.clothshop.util.OrderNotFoundException;
import com.example.clothshop.util.ProductErrorResponse;
import com.example.clothshop.util.ProductNotFoundException;
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
    public List<ProductDTO> getOrderItems(@PathVariable("id") long id) {
        List<Product> products = orderService.getOrderProducts(id);
        return products.stream().map(mapStructMapper::productToProductDTO).collect(Collectors.toList());
    }

    @GetMapping("/{oid}/items/{iid}")
    public ProductDTO getItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid) {
        Product product = orderService.getOrderItem(oid, iid);
        return mapStructMapper.productToProductDTO(product);
    }

    @PostMapping("/{id}/items")
    public ProductDTO addItemToOrder(@PathVariable("id") long id, @RequestBody @Valid ProductDTO productDTO,
                                     BindingResult bindingResult) {
        Product product = productService.getProductByName(productDTO.getName());
        productService.checkForValidationErrors(bindingResult);
        orderService.addProductToOrder(id, product);
        return mapStructMapper.productToProductDTO(product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        if (orderService.deleteOrder(id)) {
            return "Order with ID = " + id + " was deleted.";
        } else {
            return "Order with ID = " + id + " was paid and cannot be deleted.";
        }
    }

    @DeleteMapping("/{oid}/items/{iid}")
    public String deleteItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid) {
        orderService.deleteProductOfOrder(oid, iid);
        return "Product with ID = " + iid + " was deleted from order with ID = " + oid;
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
    private ResponseEntity<ProductErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ProductErrorResponse productErrorResponse = new ProductErrorResponse("Product with this id wasn't found!");
        return new ResponseEntity<>(productErrorResponse, HttpStatus.NOT_FOUND);
   }
}
