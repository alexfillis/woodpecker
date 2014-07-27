package woodpecker;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codahale.metrics.MetricRegistry.name;

public class WebServerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Counter counter;

    public WebServerUncaughtExceptionHandler(MetricRegistry metricRegistry) {
        counter = metricRegistry.counter(name(getClass()));
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        counter.inc();
        logger.error(throwable.getMessage(), throwable);
    }
}
