package woodpecker;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = WebServers.createWebServer(9996)
                .add("/hello", new HelloWorldHandler())
                .add("/quote", new ExceptionHandlerWrapper(new QuoteHandler()))
                .uncaughtExceptionHandler(new WebServerUncaughtExceptionHandler());
        Future future = webServer.start();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Listening on " + webServer.getUri());
    }
}
