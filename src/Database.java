import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;

public class Database {
    private static Connection connection = null;

    /**
     * @return Database connection
     */
    public static Connection connection() throws SQLException{
        if(Database.connection == null) {
            String url = MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", Settings.HOSTNAME, Settings.MYSQL_PORT, Settings.DATABASE_NAME);
            String username = Settings.DATABASE_USERNAME;
            String password = Settings.DATABASE_PASSWORD;
			
            Connection connection = DriverManager.getConnection(url, username, password);
            Database.connection = connection;

            return connection;
        } else {
            return Database.connection;
        }
    }
}
