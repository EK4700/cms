package be.zwaldeck.repository.rmdbs.converter;

import be.zwaldeck.repository.rmdbs.domain.UserDB;
import be.zwaldeck.zcms.repository.api.model.User;

public interface Converter {
    UserDB toDB(User entity, boolean update);
    User fromDB(UserDB userDB);
}
