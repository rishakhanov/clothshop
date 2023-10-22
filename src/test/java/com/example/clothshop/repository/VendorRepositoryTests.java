package com.example.clothshop.repository;

import com.example.clothshop.entity.Product;
import com.example.clothshop.entity.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VendorRepositoryTests {

    @Autowired
    private VendorRepository vendorRepository;

    private Vendor vendor;

    @BeforeEach
    public void setup(){
        List<Product> productList = new ArrayList<>();

        vendor = Vendor.builder()
                .name("VendorTest")
                .products(productList)
                .build();
    }

    @Test
    public void givenVendorObject_whenSave_thenReturnSavedObject() {
        Vendor savedVendor = vendorRepository.save(vendor);

        assertThat(savedVendor).isNotNull();
        assertThat(savedVendor.getId()).isGreaterThan(0);
    }

    @Test
    public void givenVendorList_whenFindAll_thenVendorList() {
        int vendorSize = (int) vendorRepository.count();
        List<Vendor> vendorList = vendorRepository.findAll();

        assertThat(vendorList).isNotNull();
        assertThat(vendorList.size()).isEqualTo(vendorSize);
    }

    @Test
    public void givenVendorObject_whenFindById_thenReturnVendorObject() {
        Vendor savedVendor = vendorRepository.save(vendor);

        Vendor vendorDB = vendorRepository.findById(savedVendor.getId()).get();

        assertThat(vendorDB).isNotNull();
    }

    @Test
    public void givenVendorName_whenFindByName_thenReturnVendorObject() {
        Vendor savedVendor = vendorRepository.save(vendor);

        Vendor vendorDB = vendorRepository.findByName(savedVendor.getName()).get();

        assertThat(vendorDB).isNotNull();
    }

    @Test
    public void givenVendorObject_whenUpdateVendor_thenReturnUpdatedVendor() {
        vendorRepository.save(vendor);

        Vendor savedVendor = vendorRepository.findById(vendor.getId()).get();
        savedVendor.setName("UpdatedVendorTest");
        Vendor updatedVendor = vendorRepository.save(savedVendor);

        assertThat(updatedVendor.getName()).isEqualTo("UpdatedVendorTest");
    }

    @Test
    public void givenVendorObject_whenDelete_thenRemoveVendor() {
        vendorRepository.save(vendor);

        vendorRepository.delete(vendor);
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendor.getId());

        assertThat(vendorOptional).isEmpty();
    }
}
