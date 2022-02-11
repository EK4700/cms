package be.zwaldeck.repository.rmdbs.service;

import be.zwaldeck.repository.rmdbs.converter.UserConverterRMDBS;
import be.zwaldeck.repository.rmdbs.dao.UserDAO;
import be.zwaldeck.repository.rmdbs.domain.UserDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceRMDBSTest {

    @Mock
    private UserDAO userDAO;

    private UserConverterRMDBS userConverter;

    private UserServiceRMDBS userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        userConverter = new UserConverterRMDBS();
        userService = new UserServiceRMDBS(userDAO, userConverter);
    }

    @Test
    void loadUserByUsername_success() {

    }

    @Test
    void loadUserByUsername_fail() {

    }

    @Test
    void getUserByUserName_success() {
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.of(getUserDB()));
        assertTrue(userService.getUserByUserName("user").isPresent());
    }

    private UserDB getUserDB() {
        UserDB db = new UserDB();
        db.setUserName("name");
        return db;
    }
}