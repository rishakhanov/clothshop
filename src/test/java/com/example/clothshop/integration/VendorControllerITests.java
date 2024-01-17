package com.example.clothshop.integration;

import com.example.clothshop.dto.VendorDTO;
import com.example.clothshop.entity.Vendor;
import com.example.clothshop.repository.VendorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VendorControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenListOfVendors_whenGetAllVendors_thenReturnVendorsList() throws Exception {
        int repositorySize = (int) vendorRepository.count();

        ResultActions response = mockMvc.perform(get("/api/vendors"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(repositorySize)));
    }

    @Test
    public void givenVendorId_whenGetVendorById_thenReturnVendorObject() throws Exception {
        long vendorId = 1L;
        Vendor vendor = vendorRepository.findById(vendorId).get();

        ResultActions response = mockMvc.perform(get("/api/vendors/{id}", vendorId));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(vendor.getName())));
    }

    @Test
    public void givenVendorDTOObject_whenCreateVendor_thenReturnSavedVendor() throws Exception {
        //create vendor and check expectations
        VendorDTO vendorDTO = VendorDTO.builder()
                .name("vendorTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendorDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(vendorDTO.getName())))
                .andReturn();

        //delete created vendor
        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());
        vendorRepository.deleteById(id);
    }

    @Test
    public void givenUpdatedVendor_whenUpdateVendor_thenReturnUpdatedVendorObject() throws Exception {
        //create vendor and get id
        VendorDTO vendorDTO = VendorDTO.builder()
                .name("vendorTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendorDTO)))
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        //update vendor and check expectations
        VendorDTO updatedVendorDTO = VendorDTO.builder()
                .name("vendorTestNameUpdated")
                .build();

        ResultActions response = mockMvc.perform(put("/api/vendors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedVendorDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedVendorDTO.getName())));

        //delete created vendor
        vendorRepository.deleteById(id);
    }

    @Test
    public void givenVendorId_whenDeleteVendor_thenReturn200() throws Exception {
        //create vendor and get id
        VendorDTO vendorDTO = VendorDTO.builder()
                .name("vendorTestName")
                .build();

        MvcResult result = mockMvc.perform(post("/api/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendorDTO)))
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long id = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        //delete created vendor by id and check expectations
        ResultActions response = mockMvc.perform(delete("/api/vendors/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print());
    }

}
