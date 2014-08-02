package woodpecker;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.codahale.metrics.MetricRegistry.name;

public class CacheMetricSet<K, V> implements MetricSet {
    private final Cache<K, V> cache;
    private final String name;

    public CacheMetricSet(Cache<K, V> cache, String name) {
        this.cache = cache;
        this.name = name;
    }

    @Override
    public Map<String, Metric> getMetrics() {
        final CacheStats stats = cache.stats();
        Map<String, Metric> metrics = new HashMap<>();
        metrics.put(name(cache.getClass(), name, "requestCount"), gauge(stats.requestCount()));
        metrics.put(name(cache.getClass(), name, "hitCount"), gauge(stats.hitCount()));
        metrics.put(name(cache.getClass(), name, "hitRate"), gauge(stats.hitRate()));
        metrics.put(name(cache.getClass(), name, "missCount"), gauge(stats.missCount()));
        metrics.put(name(cache.getClass(), name, "missRate"), gauge(stats.missRate()));
        metrics.put(name(cache.getClass(), name, "loadCount"), gauge(stats.loadCount()));
        metrics.put(name(cache.getClass(), name, "loadSuccessCount"), gauge(stats.loadSuccessCount()));
        metrics.put(name(cache.getClass(), name, "loadExceptionCount"), gauge(stats.loadExceptionCount()));
        metrics.put(name(cache.getClass(), name, "loadExceptionRate"), gauge(stats.loadExceptionRate()));
        metrics.put(name(cache.getClass(), name, "totalLoadTime"), gauge(stats.totalLoadTime()));
        metrics.put(name(cache.getClass(), name, "averageLoadPenalty"), gauge(stats.averageLoadPenalty()));
        metrics.put(name(cache.getClass(), name, "evictionCount"), gauge(stats.evictionCount()));
        return Collections.unmodifiableMap(metrics);
    }

    private <T> Gauge gauge(final T metric) {
        return new Gauge<T>() {
            @Override
            public T getValue() {
                return metric;
            }
        };
    }
}
