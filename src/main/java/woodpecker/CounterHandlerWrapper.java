package woodpecker;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import static com.codahale.metrics.MetricRegistry.*;

public class CounterHandlerWrapper implements HttpHandler {

    private final HttpHandler handler;
    private final Counter successCounter;
    private final Counter failCounter;
    private final Timer requestTimer;

    public CounterHandlerWrapper(HttpHandler handler, MetricRegistry metricRegistry) {
        this.handler = handler;
        successCounter = metricRegistry.counter(name(handler.getClass(), "request", "success"));
        failCounter = metricRegistry.counter(name(handler.getClass(), "request", "fail"));
        requestTimer = metricRegistry.timer(name(handler.getClass(), "request", "time"));
    }

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        Timer.Context time = requestTimer.time();
        try {
            try {
                handler.handleHttpRequest(request, response, control);
                successCounter.inc();
            } catch (Exception e) {
                failCounter.inc();
                throw e;
            }
        } finally {
            time.stop();
        }
    }
}
