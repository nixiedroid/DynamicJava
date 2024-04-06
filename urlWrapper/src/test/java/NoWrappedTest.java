import java.net.*;

public class NoWrappedTest {
    public static void main(String[] args) throws Exception {
        URLConnection connection = new URL("https://google.com/?q=funny%20cats").openConnection(Proxy.NO_PROXY);
        connection.setConnectTimeout(10000);
        System.out.println(connection.getURL());
    }
}
