
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PayrollProcessor {

    public static double convertToDecimal(String time) {

        String[] parts = time.split(":");

        return Integer.parseInt(parts[0])
                + (Integer.parseInt(parts[1]) / 60.0);
    }

    public static double computeHoursWorked(
            String logIn,
            String logOut) {

        double inTime = convertToDecimal(logIn);
        double outTime = convertToDecimal(logOut);

        // 10-minute grace period
        if (inTime > 8.0 && inTime <= 8.1667) {

            inTime = 8.0;
        }

        // Only count time between 8 AM and 5 PM
        double start = Math.max(inTime, 8.0);
        double end = Math.min(outTime, 17.0);

        double hours = end - start;

        if (hours < 0) {

            return 0;
        }

        // Deduct lunch break
        if (hours > 4) {

            hours -= 1;
        }

        return hours;
    }
    
    public static void processAttendance(
        String employeeNumber,
        double[] cutoff1,
        double[] cutoff2) {

    try {

        BufferedReader br =
                new BufferedReader(
                        new FileReader(
                                "AttendanceRecord.csv"));

        String line;

        br.readLine(); // Skip header

        while ((line = br.readLine()) != null) {

            String[] attendanceData =
                    line.split(",");

            if (!attendanceData[0]
                    .equals(employeeNumber)) {

                continue;
            }

            String[] dateParts =
                    attendanceData[3].split("/");

            int month =
                    Integer.parseInt(dateParts[0]);

            int day =
                    Integer.parseInt(dateParts[1]);

            double hoursWorked =
                    computeHoursWorked(
                            attendanceData[4],
                            attendanceData[5]);

            if (month >= 6 && month <= 12) {

                if (day <= 15) {

                    cutoff1[month]
                            += hoursWorked;

                } else {

                    cutoff2[month]
                            += hoursWorked;
                }
            }
        }

        br.close();

    } catch (IOException e) {

        System.out.println(
                "Error reading attendance file.");
    }
    }
    
    public static String[] findEmployeeByNumber(
        String employeeNumber) {

    for (Employee employee
            : EmployeeDataHandler.loadEmployees()) {

        if (employee.getEmployeeNumber()
                .equals(employeeNumber)) {

            return employee.getData();
        }
    }

    return null;

    }
    
    
    public static String getMonthName(int month) {

    switch (month) {

        case 6:
            return "June";

        case 7:
            return "July";

        case 8:
            return "August";

        case 9:
            return "September";

        case 10:
            return "October";

        case 11:
            return "November";

        case 12:
            return "December";

        default:
            return "";
    }
    }
    
    public static String generateEmployeePayslip(
        String employeeNumber) {

    String[] employeeData =
            findEmployeeByNumber(employeeNumber);

    if (employeeData == null) {

        return "Employee not found.";
    }

    double[] cutoff1 =
            new double[13];

    double[] cutoff2 =
            new double[13];

    processAttendance(
            employeeNumber,
            cutoff1,
            cutoff2);

    String employeeName =
            employeeData[2] + " "
            + employeeData[1];

    double hourlyRate =
            Double.parseDouble(
                    employeeData[18]);

    double riceSubsidy =
        Double.parseDouble(
                employeeData[14]
                        .replace(",", ""));

    double phoneAllowance =
        Double.parseDouble(
                employeeData[15]
                        .replace(",", ""));

    double clothingAllowance =
        Double.parseDouble(
                employeeData[16]
                        .replace(",", ""));

    StringBuilder report =
            new StringBuilder();

    report.append(
            "========================================\n");

    report.append(
            "Employee #: ")
            .append(employeeNumber)
            .append("\n");

    report.append(
            "Employee Name: ")
            .append(employeeName)
            .append("\n");

    report.append(
            "========================================\n\n");

    for (int month = 6;
            month <= 12;
            month++) {

        double gross1 =
                cutoff1[month]
                * hourlyRate;

        double gross2 =
                cutoff2[month]
                * hourlyRate;

        double monthlyGross =
                gross1 + gross2;

        double sss =
                SalaryComputationModule
                        .computeSSS(
                                monthlyGross);

        double philHealth =
                SalaryComputationModule
                        .computePhilHealth(
                                monthlyGross);

        double pagIBIG =
                SalaryComputationModule
                        .computePagIBIG(
                                monthlyGross);

        double taxableIncome =
                monthlyGross
                - sss
                - philHealth
                - pagIBIG;

        double tax =
                SalaryComputationModule
                        .computeWithholdingTax(
                                taxableIncome);

        double totalDeductions =
                sss
                + philHealth
                + pagIBIG
                + tax;

        double netSalary =
                gross2
                + riceSubsidy
                + phoneAllowance
                + clothingAllowance
                - totalDeductions;

        report.append(
                "Month: ")
                .append(
                        getMonthName(month))
                .append("\n\n");

        report.append(
                "1-15\n");

        report.append(
                "Hours Worked: ")
                .append(cutoff1[month])
                .append("\n");

        report.append(
                "Gross Salary: ")
                .append(
                        String.format(
                                "%.2f",
                                gross1))
                .append("\n\n");

        report.append(
                "16-End of Month\n");

        report.append(
                "Hours Worked: ")
                .append(cutoff2[month])
                .append("\n");

        report.append(
                "Gross Salary: ")
                .append(
                        String.format(
                                "%.2f",
                                gross2))
                .append("\n\n");

        report.append(
                "Allowances\n");

        report.append(
                "Rice Subsidy: ")
                .append(riceSubsidy)
                .append("\n");

        report.append(
                "Phone Allowance: ")
                .append(phoneAllowance)
                .append("\n");

        report.append(
                "Clothing Allowance: ")
                .append(clothingAllowance)
                .append("\n\n");

        report.append(
                "Deductions\n");

        report.append(
                "SSS: ")
                .append(
                        String.format(
                                "%.2f",
                                sss))
                .append("\n");

        report.append(
                "PhilHealth: ")
                .append(
                        String.format(
                                "%.2f",
                                philHealth))
                .append("\n");

        report.append(
                "Pag-IBIG: ")
                .append(
                        String.format(
                                "%.2f",
                                pagIBIG))
                .append("\n");

        report.append(
                "Tax: ")
                .append(
                        String.format(
                                "%.2f",
                                tax))
                .append("\n\n");

        report.append(
                "Net Salary: ")
                .append(
                        String.format(
                                "%.2f",
                                netSalary))
                .append("\n");

        report.append(
                "\n----------------------------------------\n\n");
    }

    return report.toString();
}
    
    public static String testAttendance(
        String employeeNumber) {

    double[] cutoff1 =
            new double[13];

    double[] cutoff2 =
            new double[13];

    processAttendance(
            employeeNumber,
            cutoff1,
            cutoff2);

    StringBuilder report =
            new StringBuilder();

    for (int month = 6;
            month <= 12;
            month++) {

        report.append("Month ")
              .append(month)
              .append("\n");

        report.append("1-15 Hours: ")
              .append(cutoff1[month])
              .append("\n");

        report.append("16-End Hours: ")
              .append(cutoff2[month])
              .append("\n\n");
    }

    return report.toString();
    }
}