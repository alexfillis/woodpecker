package woodpecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
    }
}
