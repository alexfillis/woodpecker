package woodpecker;

import com.codahale.metrics.MetricRegistry;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CacheFinanceTickersTest {
    private final FinanceTickers financeTickers = mock(FinanceTickers.class);
    private final MetricRegistry metrics = mock(MetricRegistry.class);
    private final CacheFinanceTickers cacheFinanceTickers = new CacheFinanceTickers(financeTickers, metrics);

    @Test
    public void subsequent_consecutive_calls_should_return_same_value() {
        // given
        String symbol = "ABC";
        byte[] stubbedResult = "something".getBytes();
        when(financeTickers.getTicker(symbol)).thenReturn(stubbedResult);

        // when
        byte[] result1 = cacheFinanceTickers.getTicker(symbol);
        byte[] result2 = cacheFinanceTickers.getTicker(symbol);
        byte[] result3 = cacheFinanceTickers.getTicker(symbol);
        byte[] result4 = cacheFinanceTickers.getTicker(symbol);

        // then
        assertThat(result1, is(equalTo(stubbedResult)));
        assertThat(result2, is(equalTo(stubbedResult)));
        assertThat(result3, is(equalTo(stubbedResult)));
        assertThat(result4, is(equalTo(stubbedResult)));
    }

    @Test
    public void after_first_call_subsequent_calls_should_NOT_call_wrapped_FinanceTickers() {
        // given
        String symbol = "ABC";
        byte[] stubbedResult = "something".getBytes();
        when(financeTickers.getTicker(symbol)).thenReturn(stubbedResult);

        // when
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);
        cacheFinanceTickers.getTicker(symbol);

        // then
        verify(financeTickers, atMost(1)).getTicker(symbol);
    }

    @Test
    public void should_return_null_when_wrapper_impl_fails() throws ExecutionException {
        // given
        String symbol = "ABC";
        when(financeTickers.getTicker(symbol)).thenThrow(new TickerException(any(Exception.class)));

        // when
        byte[] result = cacheFinanceTickers.getTicker(symbol);

        // then
        verify(financeTickers, atMost(1)).getTicker(symbol);
        assertNull(result);
    }

}
