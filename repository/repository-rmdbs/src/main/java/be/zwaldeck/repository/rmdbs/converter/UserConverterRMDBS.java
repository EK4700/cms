package be.zwaldeck.repository.rmdbs.converter;

import be.zwaldeck.repository.rmdbs.domain.UserDB;
import be.zwaldeck.zcms.repository.api.model.User;

public class UserConverterRMDBS implements Converter{
    @Override
    public UserDB toDB(User entity, boolean update) {
        UserDB db = new UserDB();

        if (update) {
            db.setId(entity.getId());
            db.setCreateAt(entity.getCreateAt());
        }

        db.setUserName(entity.getUsername());
        db.setRoles(entity.getRoles());
        db.setPassword(entity.getPassword());
        return db;
    }

    @Override
    public User fromDB(UserDB userDB) {
        User entity = new User();
        entity.setId(userDB.getId());
        entity.setUserName(userDB.getUserName());
        entity.setPassword(userDB.getPassword());
        entity.setRoles(userDB.getRoles());
        entity.setCreateAt(userDB.getCreateAt());
        entity.setUpdateAt(userDB.getUpdateAt());

        return entity;
    }
}
