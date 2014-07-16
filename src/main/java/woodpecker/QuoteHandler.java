package woodpecker;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

public class QuoteHandler implements HttpHandler {
    private static final String scheme = "http";
    private static final String path = "/v1/public/yql";
    private static final String yqlParam = "q=select * from yahoo.finance.quotes where symbol in (\"%s\")";
    private static final String envParam = "env=store://datatables.org/alltableswithkeys";
    private static final String formatParam = "format=json";

    private final String host;
    private final int port;

    public QuoteHandler(Configuration config) {
        host = config.getString("yahoo.yql.host");
        port = config.getInt("yahoo.yql.port", 8080);
    }


    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        URI symbol = target(queries(format(yqlParam, mandatoryParam(request, "symbol")), envParam, formatParam));
        Response wsResponse = Request.Get(symbol).execute();
        byte[] content = wsResponse.returnContent().asBytes();
        response.header("Content-type", "application/json")
                .content(content)
                .end();
    }

    private String mandatoryParam(HttpRequest request, String paramName) {
        String paramValue = request.queryParam(paramName);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(paramValue), "'%s' is required", paramName);
        return paramValue;
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
