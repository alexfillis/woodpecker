package woodpecker;

import com.codahale.metrics.MetricRegistry;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CacheFinanceTickersTest {
    private final FinanceTickers financeTickers = mock(FinanceTickers.class);
    private final MetricRegistry metrics = mock(MetricRegistry.class);
    private final CacheFinanceTickers cacheFinanceTickers = new CacheFinanceTickers(financeTickers, metrics);

    @Test
    public void wrapped_impl_should_be_called_when_NOT_in_cache() throws IOException, URISyntaxException {
        // given
        String symbol = "";
        when(financeTickers.getTicker(symbol)).thenReturn(new byte[] {});

        // when
        cacheFinanceTickers.getTicker(symbol);

        // then
        verify(financeTickers).getTicker(symbol);
    }

    @Test
    public void wrapped_impl_should_NOT_be_called_when_in_cache() throws IOException, URISyntaxException {
        // given
        String symbol = "";
        when(financeTickers.getTicker(symbol)).thenReturn(new byte[] {});

        // when
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);

        // then
        verify(financeTickers, atMost(1)).getTicker(symbol);
    }

    @Test
    public void should_return_null_when_wrapper_impl_fails() throws IOException, URISyntaxException {
        // given
        String symbol = "";
        when(financeTickers.getTicker(symbol)).thenThrow(new IOException());

        // when
        byte[] result = cacheFinanceTickers.getTicker(symbol);

        // then
        verify(financeTickers, atMost(1)).getTicker(symbol);
        assertNull(result);
    }

}
