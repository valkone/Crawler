import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static Statement stmt;
    private static ResultSet rs;

    public static void craw(String url)
            throws IllegalArgumentException, SQLException, IOException {
        Connection connection = Database.connection();

        Pattern pattern = Pattern.compile("((?!w+\\.)[a-zA-Z0-9-]+(\\.[a-z-A-Z0-9-]+)+.*)");
        Matcher match = pattern.matcher(url);

        if(!match.find()) {
            throw new IllegalArgumentException(url + " - is invalid url");
        }

        Crawler.stmt = connection.createStatement();
        String checkIfUrlIsCrawled = "SELECT COUNT(*) as row_count FROM" +
                " crawled_data WHERE url = '"+ url +"'";
        rs = stmt.executeQuery(checkIfUrlIsCrawled);

        if(rs.next() && rs.getInt("row_count") == 0) {
            String urlContent = getUrlContent(url);
            urlContent = urlContent.replace("'", "&#39;");
            String insertDataSql = "INSERT INTO crawled_data(content, url)" +
                    "VALUES('" + urlContent + "', '" + url + "')";

            stmt.executeUpdate(insertDataSql);
            System.out.println("Inserted : " + url);

            List<String> allUrls = getAllUrls(urlContent);
            for(String u : allUrls) {
                Crawler.craw(u);
            }
        } else {
            System.out.println("Skipped: "  + url);
        }
    }

    public static List<String> getAllUrls(String content) {
        List<String> allUrls = new ArrayList<>();

        Pattern urlMatch = Pattern.compile("(href|src)=('|\")((http://|https://|www)(www)*[^'\"]+)");
        Matcher urls = urlMatch.matcher(content);
        while (urls.find()) {
            allUrls.add(urls.group(3));
        }

        return allUrls;
    }

    private static String getUrlContent(String url) throws IOException {
        Pattern protocolPattern = Pattern.compile("http://|https://");
        Matcher match = protocolPattern.matcher(url);

        if(!match.find()) {
            url = "http://" + url;
        }

        Map<String, String> request = Crawler.sendGet(url);
        if(request.get("newLocation") != null) {
            return getUrlContent(request.get("newLocation"));
        }

        return request.get("response");
    }

    private static Map<String, String> sendGet(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", Crawler.USER_AGENT);


        String responseCode = Integer.toString(con.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Map<String, String> requestResponse = new HashMap<>();
        requestResponse.put("response", response.toString());
        if(responseCode.equals(GlobalConstants.HTTP_REDIRECT_CODE)) {
            requestResponse.put("responseCode", responseCode);
            requestResponse.put("newLocation", con.getHeaderField("Location").toString());
        }

        return requestResponse;
    }
}