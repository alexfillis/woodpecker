package woodpecker;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        try {
            String configFilePath = args[0];
            Configuration config = new Configuration(configFilePath);
            WebServer webServer = WebServers.createWebServer(9996)
                    .add("/hello", new HelloWorldHandler())
                    .add("/quote", new ExceptionHandlerWrapper(new QuoteHandler(config)))
                    .uncaughtExceptionHandler(new WebServerUncaughtExceptionHandler());
            Future future = webServer.start();
            future.get();
            System.out.println("Listening on " + webServer.getUri());
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
