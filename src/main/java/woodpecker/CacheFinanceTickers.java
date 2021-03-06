package woodpecker;

import com.codahale.metrics.MetricRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheFinanceTickers implements FinanceTickers {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LoadingCache<String, byte[]> tickersCache;

    public CacheFinanceTickers(final FinanceTickers tickers, MetricRegistry metrics) {
        tickersCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, byte[]>() {
                            @Override
                            public byte[] load(String symbol) throws Exception {
                                return tickers.getTicker(symbol);
                            }
                        });
        metrics.registerAll(new CacheMetricSet<>(tickersCache, "tickers"));
    }

    @Override
    public byte[] getTicker(String symbol) {
        try {
            return tickersCache.get(symbol);
        } catch (ExecutionException | UncheckedExecutionException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
