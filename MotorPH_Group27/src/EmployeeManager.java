import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeManager {

    private ArrayList<Employee> employees;

    public EmployeeManager() {

        employees = EmployeeDataHandler.loadEmployees();
    }

    public ArrayList<Employee> getEmployees() {

        return employees;
    }

    public Employee findEmployeeByNumber(String employeeNumber) {

        for (Employee employee : employees) {

            if (employee.getEmployeeNumber().equals(employeeNumber)) {

                return employee;
            }
        }

        return null;
    }

    public boolean employeeExists(String employeeNumber) {

        return findEmployeeByNumber(employeeNumber) != null;
    }

    public boolean addEmployee(
            String employeeNumber,
            String lastName,
            String firstName,
            String status,
            String position) {

        return addEmployee(
                employeeNumber,
                lastName,
                firstName,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                status,
                position,
                "",
                0,
                0,
                0,
                0);
    }

    public boolean addEmployee(
            String employeeNumber,
            String lastName,
            String firstName,
            String birthday,
            String address,
            String phoneNumber,
            String sssNumber,
            String philHealthNumber,
            String tinNumber,
            String pagibigNumber,
            String status,
            String position,
            String immediateSupervisor,
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance) {

        if (employeeExists(employeeNumber)) {

            return false;
        }

        String[] data = new String[19];

        data[0] = employeeNumber;
        data[1] = lastName;
        data[2] = firstName;
        data[3] = birthday;
        data[4] = address;
        data[5] = phoneNumber;
        data[6] = sssNumber;
        data[7] = philHealthNumber;
        data[8] = tinNumber;
        data[9] = pagibigNumber;
        data[10] = status;
        data[11] = position;
        data[12] = immediateSupervisor;

        Employee employee = new Employee(data);

        employee.setBasicSalary(basicSalary);
        employee.setRiceSubsidy(riceSubsidy);
        employee.setPhoneAllowance(phoneAllowance);
        employee.setClothingAllowance(clothingAllowance);

        data[17] = "0";
        data[18] = "0";

        employees.add(employee);

        return true;
    }

    public boolean updateRecord(
            String employeeNumber,
            String firstName,
            String lastName,
            String birthday,
            String address,
            String phoneNumber,
            String sssNumber,
            String philHealthNumber,
            String tinNumber,
            String pagibigNumber,
            String status,
            String position,
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance) {

        Employee employee
                = findEmployeeByNumber(employeeNumber);

        if (employee == null) {

            return false;
        }

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.getData()[3] = birthday;
        employee.getData()[4] = address;
        employee.getData()[5] = phoneNumber;
        employee.getData()[6] = sssNumber;
        employee.getData()[7] = philHealthNumber;
        employee.getData()[8] = tinNumber;
        employee.getData()[9] = pagibigNumber;
        employee.getData()[10] = status;
        employee.getData()[11] = position;
        employee.setBasicSalary(basicSalary);
        employee.setRiceSubsidy(riceSubsidy);
        employee.setPhoneAllowance(phoneAllowance);
        employee.setClothingAllowance(clothingAllowance);

        return true;
    }

    public boolean deleteRecord(String employeeNumber) {

        Employee employee
                = findEmployeeByNumber(employeeNumber);

        if (employee == null) {

            return false;
        }

        employees.remove(employee);

        return true;
    }

    public void saveChangesToFile() {

        try (BufferedWriter writer
                = new BufferedWriter(
                        new FileWriter(
                                "EmployeeDetails.csv"))) {

            writer.write(
                    "Employee #,Last Name,First Name,Birthday,Address,Phone Number,"
                    + "SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,"
                    + "Immediate Supervisor,Basic Salary,Rice Subsidy,"
                    + "Phone Allowance,Clothing Allowance,"
                    + "Gross Semi-monthly Rate,Hourly Rate");

            writer.newLine();

            for (Employee employee : employees) {

                String[] data
                        = employee.getData();

                String[] escapedData
                        = new String[data.length];

                for (int i = 0; i < data.length; i++) {

                    escapedData[i] = toCsvField(data[i]);
                }

                writer.write(
                        String.join(",", escapedData));

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving employee records: "
                    + e.getMessage());
        }
    }

    /**
     * Prepares a single field for writing to a CSV file.
     *
     * If the field contains a comma, a double-quote, or a newline,
     * it is wrapped in double-quotes and any internal double-quotes
     * are escaped by doubling them (""), per standard CSV rules.
     *
     * Without this, fields such as an address or supervisor name
     * containing a comma (e.g. "Garcia, Manuel III") would be split
     * into extra unquoted columns on save, shifting every column
     * after it out of alignment.
     */
    private static String toCsvField(String value) {

        if (value == null) {

            return "";
        }

        boolean needsQuoting
                = value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r");

        if (!needsQuoting) {

            return value;
        }

        String escaped
                = value.replace("\"", "\"\"");

        return "\"" + escaped + "\"";
    }
}
