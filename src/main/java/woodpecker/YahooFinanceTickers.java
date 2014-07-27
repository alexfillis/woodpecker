package woodpecker;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.codahale.metrics.MetricRegistry.*;
import static java.lang.String.format;

public class YahooFinanceTickers implements FinanceTickers {
    private static final String scheme = "http";
    private static final String path = "/v1/public/yql";
    private static final String yqlParam = "q=select * from yahoo.finance.quotes where symbol in (\"%s\")";
    private static final String envParam = "env=store://datatables.org/alltableswithkeys";
    private static final String formatParam = "format=json";

    private final String host;
    private final int port;
    private final Counter successRequestCount;
    private final Counter failRequestCount;

    public YahooFinanceTickers(Configuration config, MetricRegistry metricRegistry) {
        host = config.getString("yahoo.yql.host");
        port = config.getInt("yahoo.yql.port", 8080);
        successRequestCount = metricRegistry.counter(name(getClass(), "request", "success"));
        failRequestCount = metricRegistry.counter(name(getClass(), "request", "fail"));
    }

    @Override
    public byte[] getTicker(String symbol) {
        try {
            URI uri = target(queries(format(yqlParam, symbol), envParam, formatParam));
            Response wsResponse = Request.Get(uri).execute();
            successRequestCount.inc();
            return wsResponse.returnContent().asBytes();
        } catch (URISyntaxException | IOException e) {
            failRequestCount.inc();
            throw new TickerException(e);
        }
    }

    private URI target(String query) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, query, null);
    }

    private String queries(String... queries) {
        String fullQuery = "";
        for (int i = 0; i < queries.length; i++) {
            if (i != 0) {
                fullQuery += "&";
            }
            fullQuery += queries[i];
        }
        return fullQuery;
    }
}
