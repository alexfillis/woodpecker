package woodpecker;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class ExceptionHandlerWrapper implements HttpHandler {

    private final HttpHandler handler;

    public ExceptionHandlerWrapper(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        try {
            handler.handleHttpRequest(request, response, control);
        } catch (Exception e) {
            handle(e, response);
            response.end();
        }
    }

    private void handle(Exception e, HttpResponse response) throws Exception {
        if (e instanceof IllegalArgumentException) {
            response.status(404);
        } else {
            throw e;
        }
    }
}
