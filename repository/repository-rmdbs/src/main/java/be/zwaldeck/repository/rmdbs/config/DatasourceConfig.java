package be.zwaldeck.repository.rmdbs.config;

import lombok.Data;

@Data
public class DatasourceConfig {

    private DatabaseType databaseType;
    private String host;
    private int port;
    private String databaseName;
    private boolean useSSL;
    private String username;
    private String password;
    private String[] packagesToScan;
}
