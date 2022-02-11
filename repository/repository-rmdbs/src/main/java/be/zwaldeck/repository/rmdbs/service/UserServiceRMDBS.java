package be.zwaldeck.repository.rmdbs.service;

import be.zwaldeck.repository.rmdbs.converter.UserConverterRMDBS;
import be.zwaldeck.repository.rmdbs.dao.UserDAO;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserServiceRMDBS implements UserService {

    private final UserDAO userDAO;
    private final UserConverterRMDBS converter;

    @Autowired
    public UserServiceRMDBS(UserDAO userDAO, UserConverterRMDBS converter) {
        this.userDAO = userDAO;
        this.converter = converter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUserName(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username '" + username + "'"));
    }

    @Override
    public Optional<User> getUserByUserName(String username) {
        return userDAO.findByUsername(username).map(converter::fromDB);
    }
}
