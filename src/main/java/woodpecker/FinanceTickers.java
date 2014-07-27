package woodpecker;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by alex on 7/27/14.
 */
public interface FinanceTickers {
    byte[] getTicker(String symbol) throws URISyntaxException, IOException;
}
