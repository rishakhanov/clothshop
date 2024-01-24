package com.example.clothshop.controller;

import com.example.clothshop.dto.*;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.service.OrderService;
import com.example.clothshop.service.ProductService;
import com.example.clothshop.service.UserDetailsImpl;
import com.example.clothshop.util.exception.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    private final MapStructMapper mapStructMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getOrders() {
        return orderService.getOrders().stream().map(e -> mapStructMapper.orderToOrderDTO(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/oid/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO getOrderById(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.getOrderById(id));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderDTO getOrder(@PathVariable("id") long id, Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, id);
        return convertToOrderDTO(orderService.getOrderById(id));
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<OrderProductDTO> getOrderItems(@PathVariable("id") long id, Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, id);
        List<Product> products = orderService.getOrderProducts(id);
        return products.stream().map(mapStructMapper::productToOrderProductDTO).collect(Collectors.toList());
    }

    @GetMapping("/{oid}/items/{iid}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProductDTO getItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid, Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, oid);
        Product product = orderService.getOrderItem(oid, iid);
        return mapStructMapper.productToProductDTO(product);
    }

    @PostMapping("/{id}/items")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderProductDTO addItemToOrder(@PathVariable("id") long id,
                                          @RequestBody @Valid OrderProductDTO orderProductDTO,
                                          BindingResult bindingResult, Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, id);

        Product product = productService.getProductById(orderProductDTO.getId());
        productService.checkForValidationErrors(bindingResult);
        orderService.addProductToOrder(id, product, orderProductDTO.getQuantity());

        orderProductDTO.setPrice(product.getPrice());
        orderProductDTO.setName(product.getName());
        return orderProductDTO;
    }

    @DeleteMapping("/oid/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public String deleteById(@PathVariable long id) {
        orderService.deleteOrder(id);
        return "Order with ID = " + id + " was deleted.";
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String delete(@PathVariable long id, Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, id);
        orderService.deleteOrder(id);
        return "Order with ID = " + id + " was deleted.";
    }

    @DeleteMapping("/{oid}/items/{iid}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String deleteItemOfOrder(@PathVariable("oid") long oid, @PathVariable("iid") long iid,
                                    Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, oid);
        orderService.deleteProductOfOrder(oid, iid);
        return "Product with ID = " + iid + " was deleted from order with ID = " + oid;
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getOrdersOfPerson(@PathVariable("id") long id) {
        List<Orders> ordersList = orderService.getOrdersOfUser(id);
        return ordersList.stream().map(mapStructMapper::orderToOrderDTO).collect(Collectors.toList());
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<PersonOrdersDTO> getOrdersOfUser(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long id = userPrincipal.getId();
        List<Orders> orders = orderService.getOrdersOfUser(id);
        return orders.stream().map(mapStructMapper::orderToPersonOrdersDTO).collect(Collectors.toList());
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrderForPerson(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.saveNewOrder(id));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderDTO createOrderForUser(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long id = userPrincipal.getId();
        return mapStructMapper.orderToOrderDTO(orderService.saveNewOrder(id));
    }

    @PostMapping("/{id}/purchase")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderDTO purchaseOrder(@PathVariable("id") long id, Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Long personId = userPrincipal.getId();
        orderService.checkAuthority(personId, id);
        return mapStructMapper.orderToOrderDTO(orderService.purchaseOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO cancelOrder(@PathVariable("id") long id) {
        return mapStructMapper.orderToOrderDTO(orderService.cancelOrder(id));
    }

    @PostMapping("/{id}/ship")
    @PreAuthorize("hasRole('ADMIN')")
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
    private ResponseEntity<OrderErrorResponse> handleOrderCouldNotBeDeletedException(OrderCouldNotBeDeletedException exception) {
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
        PersonErrorResponse response = new PersonErrorResponse("User with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonUnauthorizedException(PersonUnauthorizedException exception) {
        PersonErrorResponse response = new PersonErrorResponse("User is not authorized to perform the requested operation.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
