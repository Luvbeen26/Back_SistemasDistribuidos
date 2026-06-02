package banksoft.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppProperties {
    private static final String CONFIG_FILE = "/application.properties";
    private static final Properties PROPERTIES = loadProperties();

    private AppProperties() {
    }

    public static String getRequired(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta configurar la propiedad: " + key);
        }
        return value.trim();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = AppProperties.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("No se encontro " + CONFIG_FILE + " en el classpath");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar " + CONFIG_FILE, e);
        }
    }
}
