package be.zwaldeck.repository.rmdbs.service;

import be.zwaldeck.repository.rmdbs.converter.UserConverterRMDBS;
import be.zwaldeck.repository.rmdbs.dao.UserDAO;
import be.zwaldeck.repository.rmdbs.domain.UserDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.of(getUserDB()));

        assertNotNull(userService.loadUserByUsername("user"));
    }

    @Test
    void loadUserByUsername_fail() {
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.empty());

        Throwable t = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user"));
        assertEquals("No user found with username 'user'", t.getMessage());
    }

    @Test
    void getUserByUserName_success() {
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.of(getUserDB()));
        assertTrue(userService.getUserByUserName("user").isPresent());
    }

    @Test
    void getUserByUserName_fail() {
        when(userDAO.findByUsername(anyString())).thenReturn(Optional.of(getUserDB()));
        assertTrue(userService.getUserByUserName("user").isPresent());
    }

    private UserDB getUserDB() {
        UserDB db = new UserDB();
        db.setUserName("name");
        return db;
    }
}