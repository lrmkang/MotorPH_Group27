import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataHandler {

    private static final String FILE_NAME = "EmployeeDetails.csv";

    public static ArrayList<Employee> loadEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(FILE_NAME))) {

            br.readLine(); // Skip header

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {

                    continue;
                }

                String[] data
                        = parseCsvLine(line);

                Employee employee
                        = new Employee(data);

                employees.add(employee);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading employees: "
                    + e.getMessage());
        }

        return employees;
    }

    private static String[] parseCsvLine(String line) {

        List<String> fields = new ArrayList<>();

        StringBuilder current = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (insideQuotes) {

                if (c == '"') {

                    boolean nextIsQuote
                            = (i + 1 < line.length())
                            && (line.charAt(i + 1) == '"');

                    if (nextIsQuote) {

                        // Escaped quote ("") inside a quoted field
                        current.append('"');
                        i++;

                    } else {

                        // Closing quote
                        insideQuotes = false;
                    }

                } else {

                    current.append(c);
                }

            } else {

                if (c == '"') {

                    insideQuotes = true;

                } else if (c == ',') {

                    fields.add(current.toString());
                    current.setLength(0);

                } else {

                    current.append(c);
                }
            }
        }

        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }
}
