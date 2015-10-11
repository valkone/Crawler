import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void craw(String url) throws IllegalArgumentException{
        Connection connection = Database.connection();

        Pattern pattern = Pattern.compile("((?!w+\\.)[a-zA-Z]+(\\.[a-z-A-Z]+)+.*)");
        Matcher match = pattern.matcher(url);

        if(!match.find()) {
            throw new IllegalArgumentException("Invalid url");
        }

        System.out.println(match.group());

        try {
            Crawler.stmt = connection.createStatement();
            //rs = stmt.executeQuery("SELECT url FROM crawledurls WHERE url = "'.url.'"");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
