package be.zwaldeck.repository.rmdbs.dao;

import be.zwaldeck.repository.rmdbs.domain.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<UserDB, String> {
    Optional<UserDB> findByUsername(String username);
}
