import com.sun.deploy.net.HttpResponse;
import sun.net.www.http.HttpClient;

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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void craw(String url) throws IllegalArgumentException {
        Connection connection = Database.connection();

        try {
            Crawler.getUrlContent(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        /*Pattern pattern = Pattern.compile("((?!w+\\.)[a-zA-Z]+(\\.[a-z-A-Z]+)+.*)");
        Matcher match = pattern.matcher(url);

        if(!match.find()) {
            throw new IllegalArgumentException("Invalid url");
        }

        try {
            Crawler.stmt = connection.createStatement();
            String checkIfUrlIsCrawled = MessageFormat.format("SELECT COUNT(*) as row_count FROM" +
                    " crawled_data WHERE url = '{0}'", match.group());
            rs = stmt.executeQuery(checkIfUrlIsCrawled);

            if(rs.getInt("row_count") == 0) {

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/

    }

    private static void getUrlContent(String url) throws IOException {
        Map<String, String> request = Crawler.sendGet(url);
        System.out.println(request.get("responseCode"));
        System.out.println(request.get("response"));
        System.out.println(request.get("newLocation"));
    }

    private static Map<String, String> sendGet(String url) throws IOException{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // follow the url if is moved
        con.setInstanceFollowRedirects(true);

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
