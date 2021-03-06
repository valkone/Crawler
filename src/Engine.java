import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Engine {
    public static Map<String, String> inputParser(String input) {
        String[] lineSplit = input.split(" ");
        String command = lineSplit[0];
        String arguments = null;

        if(lineSplit.length > 1) {
            String[] argumentsArray = Arrays.copyOfRange(lineSplit, 1, lineSplit.length);
            arguments = String.join(",", argumentsArray);
        }

        Map<String, String> result = new HashMap<>();
        result.put("command", command);
        result.put("parameters", arguments);

        return result;
    }

    public static void executeCommand(Map<String, String> input)
            throws IllegalArgumentException, SQLException, IOException {
        switch (input.get("command")) {
            case "exit":
                Program.isRunning = false;
                break;

            case "crawl":
                String[] parameters = input.get("parameters").split(" ");
                Crawler.craw(parameters[0]);
                break;

            default:
                System.out.println("Invalid command!");
                break;
        }
    }
}