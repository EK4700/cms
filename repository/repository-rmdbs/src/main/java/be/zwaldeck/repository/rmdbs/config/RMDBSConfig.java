package be.zwaldeck.repository.rmdbs.config;

import be.zwaldeck.zcms.utils.config.JsonConfigUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.util.*;

import static be.zwaldeck.repository.rmdbs.config.DatabaseType.MYSQL;
import static be.zwaldeck.repository.rmdbs.config.DatabaseType.POSTGRES;

@Configuration
@Profile("RMDBS")
@ComponentScan(basePackages = {"be.zwaldeck.repository.rmdbs"})
@EntityScan(basePackages = {"be.zwaldeck.repository.rmdbs.domain"})
@EnableJpaRepositories(basePackages = {"be.zwaldeck.repository.rmdbs.dao"})
public class RMDBSConfig {

    private DatasourceConfig config;

    @PostConstruct
    public void init() throws Exception {
        config = JsonConfigUtils.loadConfig("config/datasource.json", DatasourceConfig.class);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(buildConnectionUrl(config))
                .username(config.getUsername())
                .password(config.getPassword())
                .driverClassName(getDriver(config.getDatabaseType()))
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan(getEmfPackagesToScan(config));
        emf.setPersistenceUnitName("zcms");

        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        va.setShowSql(true);
        va.setGenerateDdl(false);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", getHibernateDialect(config.getDatabaseType()));
        // jpaProperties("hibernate.hbm2dll.auto", "create-drop");

        emf.setJpaVendorAdapter(va);
        emf.setJpaProperties(jpaProperties);
        emf.afterPropertiesSet();
        return emf;
    }

    private String getHibernateDialect(DatabaseType type) {

        switch (type) {
            case POSTGRES:
                return "org.hibernate.dialect.PostgreSQL95Dialect";
            case MYSQL:
            default:
                return "org.hibernate.dialect.MySQLDialect";
        }
    }

    @Bean(name = "rawSettingsMap")
    public Map<String, String> rawSettingsMap() {
        Map<String, String> settings = new HashMap<>();

        settings.put("connection.driver_class", getDriver(config.getDatabaseType()));
        settings.put("dialect", getHibernateDialect(config.getDatabaseType()));
        settings.put("hibernate.connection.url", buildConnectionUrl(config));
        settings.put("hibernate.connection.username", config.getUsername());
        settings.put("hibernate.connection.password", config.getPassword());
        settings.put("show_sql", "true");

        return settings;
     }
    private String[] getEmfPackagesToScan(DatasourceConfig config) {
        List<String> packagesToScan = Arrays.asList(config.getPackagesToScan());
        packagesToScan.add("be.zwaldeck.repository.rmdbs.domain");

        return packagesToScan.toArray(new String[0]);
    }

    private String getDriver(DatabaseType type) {
        switch (type) {
            case POSTGRES:
                return "org.postgres.Driver";
            case MYSQL:
            default:
                return "com.mysql.jdbc.Driver";
        }
    }

    private String buildConnectionUrl(DatasourceConfig config) {
        String connectionString = "jdbc:";
        connectionString += getConnectinoUrlType(config.getDatabaseType()) + "://";
        connectionString += config.getHost() + ":" + config.getPort();
        connectionString += "/" + config.getDatabaseName();
        connectionString += "?useSSL" + config.isUseSSL();
        return connectionString;
    }

    private String getConnectinoUrlType(DatabaseType databaseType) {
        switch (databaseType) {
            case POSTGRES:
                return "postgres";
            case MYSQL:
            default:
                return "mysql";
        }
    }
}
