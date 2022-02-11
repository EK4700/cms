package be.zwaldeck.zcms.utils.config;

import be.zwaldeck.zcms.utils.config.exception.ConfigException.ConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JsonConfigUtils {

    private JsonConfigUtils() {

    }

    public static <T> T loadConfig(String filePath, Class<T> clazz) throws ConfigException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new ConfigException("File '" + filePath + "' doesn't exists");
        }

        if (Files.isDirectory(path) || !FilenameUtils.getExtension(filePath).equalsIgnoreCase("json")) {
            throw new ConfigException("File '" + filePath + "' is not a json file");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(path.toFile(), clazz);
        } catch (IOException e) {
            throw new ConfigException("There went something wrong while parsing the config for '" + filePath + "'", e);
        }
    }

}
