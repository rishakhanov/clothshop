package com.example.clothshop.controller;


import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.PersonDTO;
import com.example.clothshop.dto.PersonUpdateDTO;
import com.example.clothshop.dto.ProductDTO;
import com.example.clothshop.service.PersonService;
import com.example.clothshop.service.ProductService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenUpdatedPerson_whenUpdatePerson_thenReturnUpdatedPerson() throws Exception {
        long personId = 1L;
        PersonUpdateDTO updatedPersonDTO = PersonUpdateDTO.builder()
                .firstname("FirstNameTestUpdate")
                .lastname("LastNameTestUpdate")
                .email("email@gmail.com")
                .password("PasswordTestUpdate")
                .phone("1-22-333")
                .build();

        PersonDTO personDTO = PersonDTO.builder()
                .username("UsernameTest")
                .firstname("FirstNameTestUpdate")
                .lastname("LastNameTestUpdate")
                .email("email@gmail.com")
                .password("PasswordTestUpdate")
                .phone("1-22-333")
                .orders(null)
                .build();

        BDDMockito.given(mapStructMapper.personToPersonDTO(personService.updatePerson(updatedPersonDTO, personId)))
                .willReturn(personDTO);

        BDDMockito.willDoNothing().given(personService).checkForValidationErrors(bindingResult);

        ResultActions response = mockMvc.perform(put("/api/users/{id}", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPersonDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.username", CoreMatchers.is(personDTO.getUsername())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonId_whenGetPersonById_thenReturnPersonObject() throws Exception {
        long personId = 1L;
        PersonDTO personDTO = PersonDTO.builder()
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("EmailTest")
                .password("PasswordTest")
                .phone("PhoneTest")
                .orders(null)
                .build();

        BDDMockito.given(mapStructMapper.personToPersonDTO(personService.getPersonById(personId)))
                .willReturn(personDTO);

        ResultActions response = mockMvc.perform(get("/api/users/id/{id}", personId));

        response.andDo(print())
                .andExpect(jsonPath("$.username", CoreMatchers.is(personDTO.getUsername())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonUsername_whenGetPersonByUsername_thenReturnPersonObject() throws Exception {
        String personUsername = "UsernameTest";
        PersonDTO personDTO = PersonDTO.builder()
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("EmailTest")
                .password("PasswordTest")
                .phone("PhoneTest")
                .orders(null)
                .build();

        BDDMockito.given(mapStructMapper.personToPersonDTO(personService.findByUsername(personUsername)))
                .willReturn(personDTO);

        ResultActions response = mockMvc.perform(get("/api/users/{username}", personUsername));

        response.andDo(print())
                .andExpect(jsonPath("$.username", CoreMatchers.is(personDTO.getUsername())));
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonId_whenDeletePerson_thenReturn200() throws Exception {
        long personId = 1L;
        BDDMockito.willDoNothing().given(personService).deleteUser(personId);

        ResultActions response = mockMvc.perform(delete("/api/users/{id}", personId));

        response.andExpect(status().isOk())
                .andDo(print());
    }

/*
    @Test
    public void givenPersonDTOObject_whenCreatePerson_thenReturnSavedPerson() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .category(null)
                .vendor(null)
                .image(null)
                .name("ProductTest")
                .price(77)
                .quantity(307)
                .build();

        BDDMockito.given(mapStructMapper.productToProductDTO(productService.saveNewProduct(ArgumentMatchers.any(ProductDTO.class))))
                .willReturn(productDTO);

        BDDMockito.willDoNothing().given(productService).checkForValidationErrors(bindingResult);



        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is((int)productDTO.getQuantity())));

                 
    }

*/

}
