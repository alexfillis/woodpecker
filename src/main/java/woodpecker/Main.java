package woodpecker;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.codahale.metrics.MetricRegistry.name;
import static java.util.concurrent.TimeUnit.*;

public class Main {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        try {
            String[] configFilePath = args[0].split(";");
            Configuration config = new Configuration(configFilePath);

            final MetricRegistry registry = new MetricRegistry();
            registry.register(name(getClass(), "buffers"), new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
            registry.register(name(getClass(), "files"), new FileDescriptorRatioGauge());
            registry.register(name(getClass(), "garbage"), new GarbageCollectorMetricSet());
            registry.register(name(getClass(), "memory"), new MemoryUsageGaugeSet());
            registry.register(name(getClass(), "threads"), new ThreadStatesGaugeSet());
            final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                    .convertRatesTo(SECONDS)
                    .convertDurationsTo(MILLISECONDS)
                    .build();
            reporter.start(1, MINUTES);

            CacheFinanceTickers tickers = new CacheFinanceTickers(new YahooFinanceTickers(config, registry));

            WebServer webServer = WebServers.createWebServer(config.getInt("http.port", 8080))
                    .add("/hello", new CounterHandlerWrapper(new HelloWorldHandler(), registry))
                    .add("/quote", new ExceptionHandlerWrapper(new CounterHandlerWrapper(new QuoteHandler(tickers), registry)))
                    .uncaughtExceptionHandler(new WebServerUncaughtExceptionHandler(registry));
            Future future = webServer.start();
            future.get();
            logger.info("Listening on {}", webServer.getUri());
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
