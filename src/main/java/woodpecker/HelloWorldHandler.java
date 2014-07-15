package woodpecker;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class HelloWorldHandler implements HttpHandler {
    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        response.header("Content-type", "text/html")
                .content("<html><body>Hello world!</body></html>")
                .end();
    }
}
