package woodpecker;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class QuoteHandler implements HttpHandler {
    private final YahooFinanceTickers tickers;

    public QuoteHandler(YahooFinanceTickers tickers) {
        this.tickers = tickers;
    }

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        String symbol = mandatoryParam(request, "symbol");
        byte[] content = tickers.getTicker(symbol);
        response.header("Content-type", "application/json")
                .content(content)
                .end();
    }

    private String mandatoryParam(HttpRequest request, String paramName) {
        String paramValue = request.queryParam(paramName);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(paramValue), "'%s' is required", paramName);
        return paramValue;
    }
}
