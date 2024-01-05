package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.dto.VendorDTO;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.service.VendorService;
import com.example.clothshop.util.exception.VendorErrorResponse;
import com.example.clothshop.util.exception.VendorNotCreatedException;
import com.example.clothshop.util.exception.VendorNotFoundException;
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
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    private final MapStructMapper mapStructMapper;

    @GetMapping()
    public List<VendorDTO> getVendors() {
        return vendorService.getVendors().stream().map(this::convertToVendorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VendorDTO getVendor(@PathVariable("id") long id) {
        return mapStructMapper.vendorToVendorDTO(vendorService.getVendorById(id));
    }

    @GetMapping("/{id}/products")
    public List<ProductDTO> getProducts(@PathVariable("id") long id) {
        List<Product> productList = vendorService.getProductsOfAVendor(id);
        return productList.stream().map(mapStructMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO create(@RequestBody @Valid VendorDTO vendorDTO, BindingResult bindingResult) {
        vendorService.checkForValidationErrors(bindingResult);
        return mapStructMapper.vendorToVendorDTO(vendorService.saveNewVendor(vendorDTO));
    }

    @PutMapping("/{id}")
    public VendorDTO update(@PathVariable("id") long id, @RequestBody @Valid VendorDTO vendorDTO,
                            BindingResult bindingResult) {
        vendorService.checkForValidationErrors(bindingResult);
        return mapStructMapper.vendorToVendorDTO(vendorService.updateVendor(vendorDTO, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable long id) {
        vendorService.deleteVendor(id);
        return "Vendor with ID = " + id + " was deleted.";
    }

    private VendorDTO convertToVendorDTO(Vendor vendor) {
        return mapStructMapper.vendorToVendorDTO(vendor);
    }

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
}
