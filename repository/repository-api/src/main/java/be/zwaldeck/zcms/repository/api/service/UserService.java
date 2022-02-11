package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> getUserByUserName(String username);
}
