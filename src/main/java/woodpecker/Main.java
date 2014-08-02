package woodpecker;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.*;
import org.elasticsearch.metrics.ElasticsearchReporter;
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

/**
 * java -cp /home/alex/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.6/b7f0fc8f61ecadeb3695f0b9464755eee44374d4/commons-codec-1.6.jar:/home/alex/.gradle/caches/modules-2/files-2.1/commons-logging/commons-logging/1.1.3/f6f66e966c70a83ffbdb6f17a0919eaf7c8aca7f/commons-logging-1.1.3.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/fluent-hc/4.3.4/51e9902595f5802e49b620869ebe249f5ff41f08/fluent-hc-4.3.4.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/17.0/9c6ef172e8de35fd8d4d8783e4821e57cdef7445/guava-17.0.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpclient/4.3.4/a9a1fef2faefed639ee0d0fba5b3b8e4eb2ff2d8/httpclient-4.3.4.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpcore/4.3.2/31fbbff1ddbf98f3aa7377c94d33b0447c646b6e/httpcore-4.3.2.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-annotations/2.2.3/527fece4f23a457070a36c371a26d6c0208e1c3/jackson-annotations-2.2.3.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-annotations/2.4.0/d6a66c7a5f01cf500377bd669507a08cfeba882a/jackson-annotations-2.4.0.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-core/2.2.3/1a0113da2cab5f4c216b4e5e7c1dbfaa67087e14/jackson-core-2.2.3.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-core/2.4.1.1/f1b322a9ff873aee0b710af5f33bc10a2eb019ba/jackson-core-2.4.1.1.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-databind/2.2.3/3ae380888029daefb91d3ecdca3a37d8cb92bc9/jackson-databind-2.2.3.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-databind/2.4.1.2/419ce28bcca554fd92989af9aceebd0b8d08fd13/jackson-databind-2.4.1.2.jar:/home/alex/.gradle/caches/modules-2/files-2.1/junit/junit/4.11/4e031bb61df09069aeb2bffb4019e7a5034a4ee0/junit-4.11.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.codahale.metrics/metrics-core/3.0.2/c6a7fb32776e984b64ff1a548e3044238ea5a931/metrics-core-3.0.2.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.elasticsearch/metrics-elasticsearch-reporter/2.0/399ff7b3378e94be017f475a114227acc41b7b31/metrics-elasticsearch-reporter-2.0.jar:/home/alex/.gradle/caches/modules-2/files-2.1/com.codahale.metrics/metrics-jvm/3.0.2/d0c6032905a3c6793c3ddcfade79e2b5f3ec1e25/metrics-jvm-3.0.2.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.mockito/mockito-core/1.9.5/c3264abeea62c4d2f367e21484fbb40c7e256393/mockito-core-1.9.5.jar:/home/alex/.gradle/caches/modules-2/files-2.1/io.netty/netty/3.5.5.Final/289c791f8c7d0fd930b4a08f60b2510cfb1f9cfe/netty-3.5.5.Final.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.objenesis/objenesis/1.0/9b473564e792c2bdf1449da1f0b1b5bff9805704/objenesis-1.0.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/1.7.7/2b8019b6249bb05d81d3a3094e468753e2b21311/slf4j-api-1.7.7.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-simple/1.7.7/8095d0b9f7e0a9cd79a663c740e0f8fb31d0e2c8/slf4j-simple-1.7.7.jar:/home/alex/.gradle/caches/modules-2/files-2.1/org.webbitserver/webbit/0.4.15/1177e68d904300b32f32a3a87aaaaff73bcd9503/webbit-0.4.15.jar:/home/alex/dev/woodpecker/out/production/woodpecker woodpecker.Main /home/alex/dev/woodpecker/out/production/woodpecker/woodpecker/config/woodpecker.properties;/home/alex/dev/woodpecker/out/production/woodpecker/woodpecker/config/prod.properties
 */
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
            ElasticsearchReporter esReporter = ElasticsearchReporter.forRegistry(registry)
                    .hosts("localhost:9200", "localhost:9201")
                    .convertRatesTo(SECONDS)
                    .convertDurationsTo(MILLISECONDS)
                    .build();
            esReporter.start(1, MINUTES);

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
