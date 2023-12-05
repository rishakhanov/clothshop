package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.VendorDTO;
import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.VendorRepository;
import com.example.clothshop.util.exception.VendorNotCreatedException;
import com.example.clothshop.util.exception.VendorNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VendorService {

    private final VendorRepository vendorRepository;

    private final MapStructMapper mapStructMapper;

    public VendorService(VendorRepository vendorRepository, MapStructMapper mapStructMapper) {
        this.vendorRepository = vendorRepository;
        this.mapStructMapper = mapStructMapper;
    }

    public List<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    public Vendor getVendorById(long id) {
        Optional<Vendor> foundVendor = vendorRepository.findById(id);
        return foundVendor.orElseThrow(VendorNotFoundException::new);
    }

    public List<Product> getProductsOfAVendor(long id) {
        Optional<Vendor> foundVendor = vendorRepository.findById(id);
        if (foundVendor.isPresent()) {
            return foundVendor.get().getProducts();
        } else {
            throw new VendorNotFoundException();
        }
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

            throw new VendorNotCreatedException(errorMsg.toString());
        }
    }

    @Transactional
    public Vendor saveNewVendor(VendorDTO vendorDTO) {
        Optional<Vendor> foundVendor = vendorRepository.findByName(vendorDTO.getName());
        if (foundVendor.isPresent()) {
            throw new VendorNotCreatedException("The vendor already exists.");
        } else {
            Vendor vendor = mapStructMapper.vendorDTOToVendor(vendorDTO);
            return vendorRepository.save(vendor);
        }
    }

    @Transactional
    public Vendor updateVendor(VendorDTO vendorDTO, long id) {
        Vendor vendor = mapStructMapper.vendorDTOToVendor(vendorDTO);
        vendor.setId(id);
        vendorRepository.save(vendor);
        return vendor;
    }

    @Transactional
    public void deleteVendor(long id) {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        if (vendor.isPresent()) {
            vendorRepository.deleteById(id);
        } else {
            throw new VendorNotFoundException();
        }
    }
}
