package woodpecker;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import static com.codahale.metrics.MetricRegistry.*;

public class CounterHandlerWrapper implements HttpHandler {

    private final HttpHandler handler;
    private final Counter successCounter;
    private final Counter failCounter;

    public CounterHandlerWrapper(HttpHandler handler, MetricRegistry metricRegistry) {
        this.handler = handler;
        successCounter = metricRegistry.counter(name(handler.getClass(), "request", "success"));
        failCounter = metricRegistry.counter(name(handler.getClass(), "request", "fail"));
    }

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        try {
            handler.handleHttpRequest(request, response, control);
            successCounter.inc();
        } catch (Exception e) {
            failCounter.inc();
        }
    }
}
