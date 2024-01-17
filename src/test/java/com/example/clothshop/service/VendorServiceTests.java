package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.VendorDTO;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.VendorRepository;
import com.example.clothshop.util.exception.VendorNotCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTests {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private MapStructMapper mapStructMapper;

    @InjectMocks
    private VendorService vendorService;

    private Vendor vendor;

    private VendorDTO vendorDTO;

    @BeforeEach
    public void setup() {

        vendor = Vendor.builder()
                .id(1L)
                .products(new ArrayList<>())
                .name("VendorTest")
                .build();

        vendorDTO = VendorDTO.builder()
                .id(1L)
                //.products(new ArrayList<>())
                .name("VendorTest")
                .build();
    }

    @Test
    public void givenVendorObject_whenSaveVendor_thenReturnVendorObject() {
        given(vendorRepository.findByName(vendorDTO.getName())).willReturn(Optional.empty());
        given(mapStructMapper.vendorDTOToVendor(vendorDTO)).willReturn(vendor);
        given(vendorRepository.save(vendor)).willReturn(vendor);

        Vendor savedVendor = vendorService.saveNewVendor(vendorDTO);

        assertThat(savedVendor).isNotNull();
    }

    @Test
    public void givenExistingVendor_whenSaveVendor_thenThrowsException() {
        given(vendorRepository.findByName(vendorDTO.getName())).willReturn(Optional.of(vendor));

        org.junit.jupiter.api.Assertions.assertThrows(VendorNotCreatedException.class, () -> {
            vendorService.saveNewVendor(vendorDTO);
        });

        verify(vendorRepository, never()).save(any(Vendor.class));
    }

    @Test
    public void givenVendorsList_whenGetAllVendors_thenReturnVendorsList() {
        Vendor vendor2 = Vendor.builder()
                .id(2L)
                .products(new ArrayList<>())
                .name("VendorTest2")
                .build();

        given(vendorRepository.findAll()).willReturn(List.of(vendor, vendor2));

        List<Vendor> vendorList = vendorService.getVendors();

        assertThat(vendorList).isNotNull();
        assertThat(vendorList).size().isEqualTo(2);
    }

    @Test
    public void givenVendorId_whenGetVendorById_thenReturnVendorObject() {
        given(vendorRepository.findById(1L)).willReturn(Optional.of(vendor));

        Vendor savedVendor = vendorService.getVendorById(vendor.getId());

        assertThat(savedVendor).isNotNull();
    }

    @Test
    public void givenVendorDTOObject_whenUpdateVendor_thenReturnUpdatedVendor() {
        given(mapStructMapper.vendorDTOToVendor(vendorDTO)).willReturn(vendor);
        given(vendorRepository.save(vendor)).willReturn(vendor);
        vendor.setName("UpdatedVendorName");

        Vendor updatedVendor = vendorService.updateVendor(vendorDTO, 1L);

        assertThat(updatedVendor.getName()).isEqualTo("UpdatedVendorName");
    }

    @Test
    public void givenVendorId_whenDeleteVendor_thenNothing() {
        long vendorId = 1L;
        given(vendorRepository.findById(vendorId)).willReturn(Optional.of(vendor));
        willDoNothing().given(vendorRepository).deleteById(vendorId);

        vendorService.deleteVendor(vendorId);

        verify(vendorRepository, times(1)).deleteById(vendorId);
    }
}
