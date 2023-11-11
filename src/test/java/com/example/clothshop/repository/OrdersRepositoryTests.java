package com.example.clothshop.repository;

import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.OrdersStatus;
import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Product;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrdersRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private  PersonRepository personRepository;

    private Orders orders;

    @BeforeEach
    public void setup() {
        Person person = personRepository.getReferenceById(1L);
        List<Product> orderedProducts = new ArrayList<>();


        orders = Orders.builder()
                .person(person)
                .orderedProducts(orderedProducts)
                .createdAt(LocalDate.parse("2023-01-01"))
                .shipDate(null)
                .status(OrdersStatus.NEW)
                .build();
    }

    @Test
    public void givenOrderObject_whenSave_thenReturnSavedObject() {
        Orders savedOrder = orderRepository.save(orders);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void givenOrderList_whenFindAll_thenOrderList() {
        int orderSize = (int) orderRepository.count();
        List<Orders> ordersList = orderRepository.findAll();

        assertThat(ordersList).isNotNull();
        assertThat(ordersList.size()).isEqualTo(orderSize);
    }

    @Test
    public void givenOrderObject_whenFindById_thenReturnOrderObject() {
        Orders savedOrder = orderRepository.save(orders);

        Orders orderDB = orderRepository.findById(savedOrder.getId()).get();

        assertThat(orderDB).isNotNull();

    }
}
