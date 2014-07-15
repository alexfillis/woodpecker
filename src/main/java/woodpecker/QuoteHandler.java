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
    private final String scheme = "http";
    private final String host = "query.yahooapis.com";
    private final String path = "/v1/public/yql";
    private final String yqlParam = "q=select * from yahoo.finance.quotes where symbol in (\"%s\")";
    private final String envParam = "env=store://datatables.org/alltableswithkeys";
    private final String formatParam = "format=json";

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        URI symbol = target(mandatoryParam(request, "symbol"));
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

    private URI target(String symbol) throws URISyntaxException {
        return new URI(scheme, host, path, queries(format(yqlParam, symbol), envParam, formatParam), null);
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
