package com.revature.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.revature.model.User;
import com.revature.service.UserService;

public class UserServiceTest {

    @Test
    void testUserRegistration() {
        UserService service = new UserService();
        User user = new User("Test","test@mail.com","1234","BUYER");
        assertTrue(service.register(user));
    }
}

