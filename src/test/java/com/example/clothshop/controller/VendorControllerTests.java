package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.VendorDTO;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.service.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VendorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    public void givenVendorDTOObject_whenCreateVendor_thenReturnSavedVendor() throws Exception {
        VendorDTO vendorDTO = VendorDTO.builder()
                //.products(null)
                .name("VendorTestName")
                .build();

        BDDMockito.given(mapStructMapper.vendorToVendorDTO(vendorService.saveNewVendor(ArgumentMatchers.any(VendorDTO.class))))
                .willReturn(vendorDTO);

        BDDMockito.willDoNothing().given(vendorService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(post("/api/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendorDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(vendorDTO.getName())));
    }

    @Test
    public void givenUpdatedVendor_whenUpdateVendor_thenReturnUpdatedVendorObject() throws Exception {
        long vendorId = 1L;
        VendorDTO updatedVendorDTO = VendorDTO.builder()
                //.products(null)
                .name("VendorUpdatedTestName")
                .build();

        BDDMockito.given(mapStructMapper.vendorToVendorDTO(vendorService.updateVendor(updatedVendorDTO, vendorId)))
                .willReturn(updatedVendorDTO);

        BDDMockito.willDoNothing().given(vendorService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(put("/api/vendors/{id}", vendorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedVendorDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedVendorDTO.getName())));

    }

    @Test
    public void givenListOfVendors_whenGetAllVendors_thenReturnVendorsList() throws Exception {
        List<Vendor> vendorList = new ArrayList<>();
        Vendor vendor1 = Vendor.builder()
                .id(1L)
                .products(null)
                .name("VendorTestName1")
                .build();
        Vendor vendor2 = Vendor.builder()
                .id(2L)
                .products(null)
                .name("VendorTestName2")
                .build();
        vendorList.add(vendor1);
        vendorList.add(vendor2);

        List<VendorDTO> vendorDTOList = new ArrayList<>();
        VendorDTO vendorDTO1 = VendorDTO.builder()
                .id(1L)
                //.products(null)
                .name("VendorTestName1")
                .build();
        VendorDTO vendorDTO2 = VendorDTO.builder()
                .id(2L)
                //.products(null)
                .name("VendorTestName2")
                .build();
        vendorDTOList.add(vendorDTO1);
        vendorDTOList.add(vendorDTO2);

        BDDMockito.given(vendorService.getVendors()).willReturn(vendorList);

        BDDMockito.given(mapStructMapper.vendorToVendorDTO(ArgumentMatchers.any(Vendor.class)))
                .willReturn(vendorDTO1, vendorDTO2);

        ResultActions response = mockMvc.perform(get("/api/vendors"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(vendorDTOList.size())));
    }


    @Test
    public void givenVendorId_whenGetVendorById_thenReturnVendorObject() throws Exception {
        long vendorId = 1L;
        VendorDTO vendorDTO = VendorDTO.builder()
                //.products(null)
                .name("VendorTestName")
                .build();

        BDDMockito.given(mapStructMapper.vendorToVendorDTO(vendorService.getVendorById(vendorId)))
                .willReturn(vendorDTO);

        ResultActions response = mockMvc.perform(get("/api/vendors/{id}", vendorId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(vendorDTO.getName())));
    }


    @Test
    public void givenVendorId_whenDeleteVendor_thenReturn200() throws Exception {
        long vendorId = 1L;
        BDDMockito.willDoNothing().given(vendorService).deleteVendor(vendorId);

        ResultActions response = mockMvc.perform(delete("/api/vendors/{id}", vendorId));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
