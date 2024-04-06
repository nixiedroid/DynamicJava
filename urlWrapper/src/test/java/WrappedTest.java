
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;


public class WrappedTest
{
    public static void main(String[] args) throws Exception
    {
        URLConnection con = new URL("https://google.com/?q=funny%20cats").openConnection(Proxy.NO_PROXY);
      //  con.connect();
        System.out.println(con.getURL().toString());
    }
}
