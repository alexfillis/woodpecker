package woodpecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        try {
            String[] configFilePath = args[0].split(";");
            Configuration config = new Configuration(configFilePath);
            WebServer webServer = WebServers.createWebServer(config.getInt("http.port", 8080))
                    .add("/hello", new HelloWorldHandler())
                    .add("/quote", new ExceptionHandlerWrapper(new QuoteHandler(new YahooFinanceTickers(config))))
                    .uncaughtExceptionHandler(new WebServerUncaughtExceptionHandler());
            Future future = webServer.start();
            future.get();
            logger.info("Listening on {}", webServer.getUri());
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
