package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerUnitTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    private User user = new User();
    private Item item = new Item();
    private Cart cart = new Cart();

    List<Item> list_item = new ArrayList<Item>();

    @Before
    public void initSetup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
        TestUtils.injectObject(orderController, "userRepository", userRepo);

        user.setId(0L);
        user.setUsername("testusername");
        user.setPassword("testpassword");
        when(userRepo.findByUsername("testusername")).thenReturn(user);

        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(49.99));
        item.setName("oreo_cake");
        item.setDescription("This is oreo cake description.");
        list_item.add(item);

        cart.setId(0L);
        cart.setItems(list_item);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(49.99));
        user.setCart(cart);
    }

    @Test
    public void submitOrderSuccess(){
        ResponseEntity<UserOrder> response = orderController.submit("testusername");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(49.99), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    public void submitOrderUserNotFound(){
        ResponseEntity<UserOrder> response = orderController.submit("fakeuser");
        assertEquals(404, response.getStatusCodeValue());
    }
}