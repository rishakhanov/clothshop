package com.example.clothshop.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler
    private ResponseEntity<VendorErrorResponse> handleVendorNotFoundException(VendorNotFoundException exception) {
        VendorErrorResponse response = new VendorErrorResponse("Vendor with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<VendorErrorResponse> handleVendorNotCreatedException(VendorNotCreatedException exception) {
        VendorErrorResponse response = new VendorErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<CategoryErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        CategoryErrorResponse response = new CategoryErrorResponse("Category with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<CategoryErrorResponse> handleCategoryNotCreatedException(CategoryNotCreatedException exception) {
        CategoryErrorResponse response = new CategoryErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<DiscountErrorResponse> handleDiscountNotFoundException(DiscountNotFoundException exception) {
        DiscountErrorResponse response = new DiscountErrorResponse("Discount with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
    private ResponseEntity<PersonErrorResponse> handlePersonUnauthorizedException(PersonUnauthorizedException exception) {
        PersonErrorResponse response = new PersonErrorResponse("User is not authorized to perform the requested operation.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotFoundException(PersonNotFoundException exception) {
        PersonErrorResponse response = new PersonErrorResponse("User with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotCreatedException(PersonNotCreatedException exception) {
        PersonErrorResponse response = new PersonErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ResponseEntity<RoleErrorResponse> handleRoleNotFoundException(RoleNotFoundException exception) {
        RoleErrorResponse response = new RoleErrorResponse("Role wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ProductErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ProductErrorResponse response = new ProductErrorResponse("Product with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ImageErrorResponse> handleImageNotFoundException(ImageNotFoundException exception) {
        ImageErrorResponse response = new ImageErrorResponse("Image with this id wasn't found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ProductErrorResponse> handleProductNotCreatedException(ProductNotCreatedException exception) {
        ProductErrorResponse response = new ProductErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
