package be.zwaldeck.repository.rmdbs.service;

import be.zwaldeck.repository.rmdbs.domain.UserDB;
import be.zwaldeck.zcms.repository.api.model.Role;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.api.service.SetupService;
import be.zwaldeck.zcms.repository.exception.RepositoryException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SetupServiceRMDBS implements SetupService {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceRMDBS userService;
    private final Map<String, String> rawSettings;

    private static final String[] TABLES_NEEDED = {
            "user_tbl",
            "user_roles_tbl"
    };


    @Autowired
    public SetupServiceRMDBS(DataSource dataSource,
                             PasswordEncoder passwordEncoder,
                             UserServiceRMDBS userService,
                             @Qualifier("rawSettingsMap")Map<String, String> rawSettings) {

        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.rawSettings = rawSettings;
    }

    @Override
    public boolean isRepositorySetup() throws RepositoryException {
        ArrayList<String> tablesInDb = new ArrayList<>();
        try {
            ResultSet resultSet = dataSource
                    .getConnection().
                    getMetaData().
                    getTables(null,null,"%",null);
            while (resultSet.next()) {
                tablesInDb.add(resultSet.getString("TABLE_NAME"));
            }


            // Check if the db contains tables
            for (String table : TABLES_NEEDED) {
                if (!tablesInDb.contains(table)) {
                    return false;
                }
            }

            // check if admin user exists
            Optional<User> admin = userService.getUserByUserName("admin");
            if (!admin.isPresent() || !admin.get().getRoles().contains(Role.ROLE_ADMIN)) {
                return false;
            }

        } catch (SQLException e) {
            throw new RepositoryException("There went something wrong", e);
        }

        return true;
    }

    @Override
    public void setupRepository() {
        MetadataSources metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(rawSettings).build()
        );
        metadataSources.addAnnotatedClass(UserDB.class);

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(false);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.execute(EnumSet.of(TargetType.DATABASE),
                SchemaExport.Action.BOTH,
                metadataSources.buildMetadata());

        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Arrays.asList(Role.values()));
        userService.create(admin);
    }
}
