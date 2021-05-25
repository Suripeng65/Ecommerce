package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerUnitTest {

    private UserController userController;
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    private User user = new User();

    @Before
    public void initSetup(){
        userController = new UserController();
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", passwordEncoder);

        user.setId(0);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));
    }

    @Test
    public void createUserTest(){

        when(passwordEncoder.encode("testpassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testusername");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testusername", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }


    @Test
    public void findByIdTest() {
        ResponseEntity<User> user = userController.findById(0L);
        assertEquals(0, user.getBody().getId());
        user = userController.findById(1L);
        assertEquals(404, user.getStatusCodeValue());
    }

}
