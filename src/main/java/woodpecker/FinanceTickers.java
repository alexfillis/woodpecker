package woodpecker;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FinanceTickers {
    byte[] getTicker(String symbol) throws URISyntaxException, IOException;
}
