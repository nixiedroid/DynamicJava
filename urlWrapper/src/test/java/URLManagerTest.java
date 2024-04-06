import com.nixiedroid.urlWrapper.URLPatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class URLManagerTest
{
    @BeforeAll
    public static void init(){
        URLPatcher.load("URLWrapper.urls");
    }

    @Test
    public void testUnknown() throws Exception
    {
        assertNull( URLPatcher.replace(new URL("https://example.com")),"Unknown URL found");
    }

    @Test
    public void testPlain() throws Exception
    {
        URL url = URLPatcher.replace(new URL("https://invalid.url/subserver/saveME"));
        assertNotNull( url,"Known URL not found");
        assertEquals( "http://valid.url/saveME", url.toString(),"Incorrect replacement");
    }

    @Test
    public void testRegex() throws Exception
    {
        URL url = URLPatcher.replace(new URL("https://invalid.url/subserver/?username=test"));
        assertNotNull(url,"Known URL not found");
        assertEquals( "http://valid.url/?username=test", url.toString(),"Incorrect replacement");
    }
}
