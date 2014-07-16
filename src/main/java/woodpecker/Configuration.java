package woodpecker;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private final Properties properties;

    public Configuration(String configFilePath) throws IOException {
        this.properties = new Properties();
        this.properties.load(new FileReader(new File(configFilePath)));
    }

    public String getString(String name) {
        checkExists(name);
        return properties.getProperty(name);
    }

    public int getInt(String name) {
        return Integer.parseInt(getString(name));
    }

    public int getInt(String name, int defaultValue) {
        if (exists(name)) {
            return getInt(name);
        } else {
            return defaultValue;
        }
    }

    private boolean exists(String name) {
        return properties.containsKey(name);
    }

    private void checkExists(String name) {
        Preconditions.checkState(exists(name), "'%s' property not found", name);
    }
}
