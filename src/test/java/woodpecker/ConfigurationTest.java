package woodpecker;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigurationTest {
    private final Configuration config;

    public ConfigurationTest() throws IOException {
        config = new Configuration("src/test/resources/woodpecker/config/test-unit.properties");
    }

    @Test(expected = IllegalStateException.class)
    public void getString_should_fail_if_property_missing() {
        config.getString("abc");
    }

    @Test
    public void getString_should_return_value_for_property_that_exists() {
        //execute
        String value = config.getString("test.unit.property");

        //verify
        assertEquals("property-value", value);
    }
}