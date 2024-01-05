package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.entity.Person;
import com.example.clothshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTests {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PersonService personService;
    @InjectMocks
    private OrderService orderService;

    private Person person;

    private Orders order;

    @BeforeEach
    public void setup() {

        person = Person.builder()
                .id(1L)
                .orders(null)
                .rolesList(null)
                .username("PersonTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("email@test.com")
                .password("passwordtest")
                .phone("1-12-123")
                .build();

        order = Orders.builder()
                .id(1L)
                .person(null)
                .productOrders(null)
                .createdAt(LocalDate.now())
                .shipDate(null)
                .status(OrdersStatus.NEW)
                .build();

    }


    @Test
    public void givenOrderObject_whenSaveOrder_thenReturnOrderObject() {
        given(personService.getPersonById(person.getId())).willReturn(person);
        order.setId(null);
        order.setPerson(person);
        given(orderRepository.save(order)).willReturn(order);

        Orders savedOrder = orderService.saveNewOrder(person.getId());

        assertThat(savedOrder).isNotNull();
    }


    @Test
    public void givenOrdersList_whenGetAllOrders_thenReturnOrdersList() {
        Orders order2 = Orders.builder()
                .id(2L)
                .person(null)
                .productOrders(null)
                .createdAt(LocalDate.now())
                .shipDate(null)
                .status(OrdersStatus.NEW)
                .build();

        given(orderRepository.findAll()).willReturn(List.of(order, order2));

        List<Orders> ordersList = orderService.getOrders();

        assertThat(ordersList).isNotNull();
        assertThat(ordersList).size().isEqualTo(2);
    }

    @Test
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() {
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        Orders savedOrder = orderService.getOrderById(order.getId());

        assertThat(savedOrder).isNotNull();
    }

    @Test
    public void givenOrderId_whenDeleteOrder_thenNothing() {
        long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        willDoNothing().given(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
