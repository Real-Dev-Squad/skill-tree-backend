package com.RDS.skilltree.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest
class UserServiceImplTest {

    private final UserService userService;

    @Autowired
    public UserServiceImplTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void testCreateUser() throws MalformedURLException {
        UserDRO user1 = new UserDRO("12345abcd","John", "Doe",new URL("https://example.com"),UserRole.USER);
        userService.createUser(user1);
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void addSkill() {
    }
}