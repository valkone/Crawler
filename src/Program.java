import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

public class Program {
    public static boolean isRunning = true;

    public static void main(String[] args) {
        while(isRunning) {
            String input = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                input = br.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            Map<String, String> parsedInput = Engine.inputParser(input);
            try {
                Engine.executeCommand(parsedInput);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
