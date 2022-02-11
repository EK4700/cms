package be.zwaldeck.zcms.utils.config;

import be.zwaldeck.zcms.utils.config.exception.ConfigException.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonConfigUtilsTest {

    @Test
    void loadConfig_notExisting() {
        Throwable t = assertThrows(ConfigException.class,
                () -> JsonConfigUtils.loadConfig("doesntexists.json", Object.class)
                );
        assertEquals("File 'doesntexists.json' doesn't exists", t.getMessage());
    }

    @Test
    void loadConfig_directory() {
        Throwable t = assertThrows(ConfigException.class,
                () -> JsonConfigUtils.loadConfig(System.getProperty("user.dir"), Object.class));
        assertEquals("File '" + System.getProperty("user.dir") + "' is not a json file", t.getMessage());
    }

    @Test
    void loadConfig_noJson() {
        String filePath = (ClassLoader.getSystemResource("config.txt").getFile().split(":"))[1];
        Throwable t = assertThrows(ConfigException.class,
                () -> JsonConfigUtils.loadConfig(filePath, Object.class));
        assertEquals("File '" + filePath.toString() + "' is not a json file", t.getMessage());
    }

    @Test
    void loadConfig_invalidJson() {
        String filePath = (ClassLoader.getSystemResource("config_invalid.json").getFile().split(":"))[1];
        Throwable t = assertThrows(ConfigException.class,
                () -> JsonConfigUtils.loadConfig(filePath, Object.class));
        assertEquals("There went something wrong while parsing the config for '" + filePath + "'", t.getMessage());
    }

    @Test
    void loadConfig_validJson() throws Exception {
        String filePath = ClassLoader.getSystemResource("config_valid.json").getFile().split(":")[1];
        TestConfig config = JsonConfigUtils.loadConfig(filePath, TestConfig.class);
        assertEquals("zcms", config.getName());
    }

}