package be.zwaldeck.zcms.author.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(be.zwaldeck.repository.rmdbs.config.RMDBSConfig.class)
@Profile("RMDBS")
public class RMDBSConfig {
}
