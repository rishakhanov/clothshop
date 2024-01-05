package com.example.clothshop.controller;

import com.example.clothshop.config.WebSecurityConfig;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.OrderDTO;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderDTOObject_whenCreateOrder_thenReturnSavedOrder() throws Exception {
        long orderId = 1L;

        OrderDTO orderDTO = OrderDTO.builder()
                .person(null)
                .productOrders(null)
                .createdAt(LocalDate.now())
                .shipDate(null)
                .status(OrdersStatus.NEW)
                .build();

        BDDMockito.given(mapStructMapper.orderToOrderDTO(orderService.saveNewOrder(orderId)))
                .willReturn(orderDTO);

        ResultActions response = mockMvc.perform(post("/api/orders/users/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(orderDTO.getStatus().toString())));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenCanceledOrder_whenCancelOrder_thenReturnCanceledOrderObject() throws Exception {
        long orderId = 1L;
        OrderDTO canceledOrderDTO = OrderDTO.builder()
                .person(null)
                .productOrders(null)
                .createdAt(LocalDate.now())
                .shipDate(null)
                .status(OrdersStatus.CANCELED)
                .build();

        BDDMockito.given(mapStructMapper.orderToOrderDTO(orderService.cancelOrder(orderId)))
                .willReturn(canceledOrderDTO);

        ResultActions response = mockMvc.perform(post("/api/orders/{id}/cancel", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(canceledOrderDTO)));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(canceledOrderDTO.getStatus().toString())));

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() throws Exception {
        long orderId = 1L;
        OrderDTO orderDTO = OrderDTO.builder()
                .person(null)
                .productOrders(null)
                .createdAt(LocalDate.now())
                .shipDate(null)
                .status(OrdersStatus.NEW)
                .build();

        BDDMockito.given(mapStructMapper.orderToOrderDTO(orderService.getOrderById(orderId)))
                .willReturn(orderDTO);

        ResultActions response = mockMvc.perform(get("/api/orders/oid/{id}", orderId));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(orderDTO.getStatus().toString())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void givenOrderId_whenDeleteOrder_thenReturn200() throws Exception {
        long orderId = 1L;
        BDDMockito.willDoNothing().given(orderService).deleteOrder(orderId);

        ResultActions response = mockMvc.perform(delete("/api/orders/oid/{id}", orderId));

        response.andExpect(status().isOk())
                .andDo(print());
    }

}
