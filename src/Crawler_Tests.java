import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class Crawler_Tests {

    @Test
    public void Test_GetAllUrls_ShouldReturnCorrectUrls() {
        List<String> urls = Crawler.getAllUrls("<a href='http://google.com'>Link</a>");
        Assert.assertEquals("http://google.com", urls.get(0));

        urls = Crawler.getAllUrls("<a href='http://google.com'>Link</a><a href='http://microsoft.com'>Link</a>");
        Assert.assertEquals("http://google.com", urls.get(0));
        Assert.assertEquals("http://microsoft.com", urls.get(1));
    }

    @Test
    public void Test_GetAllUrls_ShouldReturnNoUrls() {
        List<String> urls = Crawler.getAllUrls("Content with no urls");
        Assert.assertEquals(0, urls.size());
    }
}
