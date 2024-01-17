package com.example.clothshop.integration;

import com.example.clothshop.dto.PersonDTO;
import com.example.clothshop.dto.PersonUpdateDTO;
import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.repository.PersonRepository;
import com.example.clothshop.service.RolesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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
public class PersonControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenListOfPersons_whenGetAllPersons_thenReturnPersonsList() throws Exception {
        int repositorySize = (int) personRepository.count();

        ResultActions response = mockMvc.perform(get("/api/users"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(repositorySize)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonId_whenGetPersonById_thenReturnPersonObject() throws Exception {
        long personId = 1L;
        Person person = personRepository.findById(personId).get();

        ResultActions response = mockMvc.perform(get("/api/users/id/{id}", personId));

        response.andDo(print())
                .andExpect(jsonPath("$.username", CoreMatchers.is(person.getUsername())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenUpdatedPerson_whenUpdatePerson_thenReturnUpdatedPerson() throws Exception {
        //create Person
        List<Roles> roles = new ArrayList<>();
        Roles role = rolesService.findByName("ROLE_USER");
        roles.add(role);

        Person person = Person.builder()
                .orders(null)
                .rolesList(roles)
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("email@gmail.com")
                .password(encoder.encode("PasswordTest"))
                .phone("1-22-333")
                .build();

        long personId = personRepository.save(person).getId();

        //update person and check expectations
        PersonUpdateDTO updatedPersonDTO = PersonUpdateDTO.builder()
                .firstname("FirstNameTestUpdate")
                .lastname("LastNameTestUpdate")
                .email("email@gmail.com")
                .password("PasswordTestUpdate")
                .phone("1-22-333")
                .build();

        ResultActions response = mockMvc.perform(put("/api/users/{id}", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPersonDTO)));

        response.andDo(print())
                .andExpect(jsonPath("$.firstname", CoreMatchers.is("FirstNameTestUpdate")));

        //delete created person
        personRepository.deleteById(personId);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonUsername_whenGetPersonByUsername_thenReturnPersonObject() throws Exception {
        //create Person
        List<Roles> roles = new ArrayList<>();
        Roles role = rolesService.findByName("ROLE_USER");
        roles.add(role);

        Person person = Person.builder()
                .orders(null)
                .rolesList(roles)
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("email@gmail.com")
                .password(encoder.encode("PasswordTest"))
                .phone("1-22-333")
                .build();

        long personId = personRepository.save(person).getId();

        //find person and check expectations
        ResultActions response = mockMvc.perform(get("/api/users/{username}", "UsernameTest"));

        response.andDo(print())
                .andExpect(jsonPath("$.username", CoreMatchers.is(person.getUsername())));

        //delete created person
        personRepository.deleteById(personId);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenPersonId_whenDeletePerson_thenReturn200() throws Exception {
        //create Person
        List<Roles> roles = new ArrayList<>();
        Roles role = rolesService.findByName("ROLE_USER");
        roles.add(role);

        Person person = Person.builder()
                .orders(null)
                .rolesList(roles)
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("email@gmail.com")
                .password(encoder.encode("PasswordTest"))
                .phone("1-22-333")
                .build();

        long personId = personRepository.save(person).getId();

        //delete person by id and check expectations
        ResultActions response = mockMvc.perform(delete("/api/users/{id}", personId));

        response.andExpect(status().isOk())
                .andDo(print());
    }


}
