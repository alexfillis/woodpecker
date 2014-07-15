package woodpecker;

public class WebServerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
    }
}
