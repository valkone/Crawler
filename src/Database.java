import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;


public class Database {

    /**
     * @return Database connection
     */
    public static Connection connection() {
        String url = MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", Settings.HOSTNAME, Settings.MYSQL_PORT, Settings.DATABASE_NAME);
        String username = Settings.DATABASE_USERNAME;
        String password = Settings.DATABASE_PASSWORD;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connect success");
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
