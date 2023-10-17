package com.example.clothshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ClothshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testAdd() {
        assertEquals(42, Integer.sum(19, 23));
    }

}
