package com.example.clothshop.integration;

import com.example.clothshop.dto.OrderDTO;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.repository.OrderRepository;
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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Sql("/sql/order_init.sql")
public class OrderControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenListOfOrders_whenGetAllOrders_thenReturnOrdersList() throws Exception {
        int repositorySize = (int) orderRepository.count();

        ResultActions response = mockMvc.perform(get("/api/orders"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(repositorySize)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() throws Exception {
        long orderId = 1L;

        Orders order = orderRepository.findById(orderId).get();

        ResultActions response = mockMvc.perform(get("/api/orders/oid/{id}", orderId));

        response.andDo(print())
                .andExpect(jsonPath("$.status", CoreMatchers.is(order.getStatus().toString())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderDTOObject_whenCreateOrder_thenReturnSavedOrder() throws Exception {
        long userId = 2L;

        MvcResult result = mockMvc.perform(post("/api/orders/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", CoreMatchers.is(OrdersStatus.NEW.toString())))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderId_whenDeleteOrder_thenReturn200() throws Exception {
        long userId = 2L;
        MvcResult result = mockMvc.perform(post("/api/orders/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Long orderId = Long.parseLong(JsonPath.parse(content).read("$.id").toString());

        ResultActions response = mockMvc.perform(delete("/api/orders/oid/{id}", orderId));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
