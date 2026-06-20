import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GUI extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryArea;

    public GUI() {

        setTitle("MotorPH Employee Salary Computation");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton viewButton
                = new JButton("View Employees");

        JButton addButton
                = new JButton("Add Employee");

        JButton updateButton
                = new JButton("Update Employee");

        JButton deleteButton
                = new JButton("Delete Employee");

        JButton computeButton
                = new JButton("Compute Salaries");

        JButton clearButton
                = new JButton("Clear");

        JPanel topPanel = new JPanel();

        topPanel.add(viewButton);
        topPanel.add(addButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);        
        topPanel.add(computeButton);
        topPanel.add(clearButton);

        String[] columns = {
        "Employee #",
        "Name",
        "Position",
        "Status",
        "Salary",
        "SSS #",
        "PhilHealth #",
        "TIN #",
        "Pag-IBIG #"
        };

        tableModel = new DefaultTableModel(columns, 0);

        employeeTable = new JTable(tableModel);

        JScrollPane tableScrollPane
                = new JScrollPane(employeeTable);

        summaryArea = new JTextArea(10, 20);
        summaryArea.setEditable(false);

        JScrollPane summaryScrollPane
                = new JScrollPane(summaryArea);

        JSplitPane splitPane
                = new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        tableScrollPane,
                        summaryScrollPane);

        splitPane.setDividerLocation(300);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        viewButton.addActionListener(
                e -> viewEmployees());
        
        addButton.addActionListener(
                e -> addEmployee());

        updateButton.addActionListener(
                e -> updateEmployee());

        deleteButton.addActionListener(
                e -> deleteEmployee());
        
        computeButton.addActionListener(
                e -> computeSalaries());

        clearButton.addActionListener(e -> {

            tableModel.setRowCount(0);

            summaryArea.setText("");
        });

        employeeTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        showSelectedEmployee();
                    }
                });

        setVisible(true);
    }

    private void viewEmployees() {

        tableModel.setRowCount(0);

        EmployeeManager manager
                = new EmployeeManager();

        ArrayList<Employee> employees
                = manager.getEmployees();

        for (Employee employee : employees) {

            String[] data
                    = employee.getData();

            tableModel.addRow(new Object[]{
            data[0],                                   // Employee #
            employee.getFullName(),                    // Name
            data[11],                                  // Position
            data[10],                                  // Status
            data[13].replace("\"", ""),                // Salary
            data[6],                                   // SSS #
            data[7],                                   // PhilHealth #
            data[8],                                   // TIN #
            data[9]                                    // Pag-IBIG #
            });
        }
    }

    private void computeSalaries() {

    String[] options = {
        "Process All Employees",
        "Process Selected Employee",
        "Process Employee Number"
    };

    int choice = JOptionPane.showOptionDialog(
            this,
            "Choose Payroll Processing Option",
            "Payroll Processing",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

    if (choice == -1) {

        return;
    }

    if (choice == 0) {

        processAllEmployees();

    } else if (choice == 1) {

        processSelectedEmployee();

    } else if (choice == 2) {

        processEmployeeNumber();
    }
}

    private void saveSalaryResults(
            String[] employeeNames,
            double[] grossPays,
            double[] deductions,
            double[] netPays) {

        try (BufferedWriter writer
                = new BufferedWriter(
                        new FileWriter(
                                "SalaryResults.csv"))) {

            writer.write(
                    "Employee Name,Gross Pay,Deductions,Net Pay");

            writer.newLine();

            for (int i = 0; i < employeeNames.length; i++) {

                writer.write(
                        employeeNames[i] + ","
                        + String.format("%.2f",
                                grossPays[i]) + ","
                        + String.format("%.2f",
                                deductions[i]) + ","
                        + String.format("%.2f",
                                netPays[i]));

                writer.newLine();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "SalaryResults.csv created successfully!");

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error saving CSV: "
                    + e.getMessage());
        }
    }
    
       private void updateEmployee() {

    EmployeeManager manager = new EmployeeManager();
    Employee employee = null;
    String employeeNumber;

    int selectedRow = employeeTable.getSelectedRow();

    // If an employee is selected in the table
    if (selectedRow != -1) {

        String selectedEmployeeNumber =
                tableModel.getValueAt(selectedRow, 0).toString();

        employee = manager.findEmployeeByNumber(selectedEmployeeNumber);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Update employee information for "
                + employee.getFullName() + "?",
                "Update Employee",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            employeeNumber = selectedEmployeeNumber;

        } else {

            employeeNumber = JOptionPane.showInputDialog(
                    this,
                    "Enter Employee Number to Update:");

            if (employeeNumber == null
                    || employeeNumber.trim().isEmpty()) {

                return;
            }

            employee = manager.findEmployeeByNumber(employeeNumber);
        }

    } else {

        employeeNumber = JOptionPane.showInputDialog(
                this,
                "Enter Employee Number to Update:");

        if (employeeNumber == null
                || employeeNumber.trim().isEmpty()) {

            return;
        }

        employee = manager.findEmployeeByNumber(employeeNumber);
    }

    if (employee == null) {

        JOptionPane.showMessageDialog(
                this,
                "Employee not found.");

        return;
    }

    EmployeeFormDialog dialog =
            new EmployeeFormDialog(
                    this,
                    "Update Employee",
                    employee,
                    manager);

    dialog.setVisible(true);

    if (!dialog.isConfirmed()) {

        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to update Employee #"
            + employee.getEmployeeNumber() + "?",
            "Confirm Update",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {

        return;
    }

    boolean updated =
            manager.updateRecord(
                    employee.getEmployeeNumber(),
                    dialog.getFirstName(),
                    dialog.getLastName(),
                    dialog.getBirthday(),
                    dialog.getAddress(),
                    dialog.getPhoneNumber(),
                    dialog.getSssNumber(),
                    dialog.getPhilHealthNumber(),
                    dialog.getTinNumber(),
                    dialog.getPagibigNumber(),
                    dialog.getStatus(),
                    dialog.getPosition(),
                    dialog.getBasicSalary(),
                    dialog.getRiceSubsidy(),
                    dialog.getPhoneAllowance(),
                    dialog.getClothingAllowance());

    if (updated) {

        manager.saveChangesToFile();

        JOptionPane.showMessageDialog(
                this,
                "Employee updated successfully.");

        viewEmployees();

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Update failed.");
    }
    }

    private void deleteEmployee() {

        String employeeNumber = JOptionPane.showInputDialog(
                this,
                "Enter Employee Number to Delete:");

        if (employeeNumber == null
                || employeeNumber.trim().isEmpty()) {

            return;
        }
        
        EmployeeManager manager
                = new EmployeeManager();

        Employee employee
                = manager.findEmployeeByNumber(
                        employeeNumber);

        if (employee == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee not found.");

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Employee #"
                + employeeNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {

            return;
        }        

        boolean deleted
                = manager.deleteRecord(employeeNumber);

        if (deleted) {

            manager.saveChangesToFile();            

            JOptionPane.showMessageDialog(
                    this,
                    "Employee deleted successfully.");

            viewEmployees();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee not found.");
        }
    }
    
    private void addEmployee() {

        EmployeeManager manager
                = new EmployeeManager();

        EmployeeFormDialog dialog
                = new EmployeeFormDialog(
                        this,
                        "Add Employee",
                        null, manager);

        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {

            return;
        }

        boolean added
                = manager.addEmployee(
                        dialog.getEmployeeNumber(),
                        dialog.getLastName(),
                        dialog.getFirstName(),
                        dialog.getBirthday(),
                        dialog.getAddress(),
                        dialog.getPhoneNumber(),
                        dialog.getSssNumber(),
                        dialog.getPhilHealthNumber(),
                        dialog.getTinNumber(),
                        dialog.getPagibigNumber(),
                        dialog.getStatus(),
                        dialog.getPosition(),
                        "",
                        dialog.getBasicSalary(),
                        dialog.getRiceSubsidy(),
                        dialog.getPhoneAllowance(),
                        dialog.getClothingAllowance());

        if (!added) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee Number already exists.");

            return;
        }

        manager.saveChangesToFile();

        JOptionPane.showMessageDialog(
                this,
                "Employee added successfully.");

        viewEmployees();
    }        
    
    private void processAllEmployees() {

    ArrayList<Employee> employees =
            EmployeeDataHandler.loadEmployees();

    StringBuilder results =
            new StringBuilder();

    for (Employee employee : employees) {

        results.append(
                PayrollProcessor.generateEmployeePayslip(
                        employee.getEmployeeNumber()));

        results.append("\n\n");
    }

    summaryArea.setText(
            results.toString());
    }

    private void processSelectedEmployee() {

    int selectedRow =
            employeeTable.getSelectedRow();

    if (selectedRow == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Please select an employee from the table first.");

        return;
    }

    String employeeNumber =
            tableModel.getValueAt(
                    selectedRow,
                    0).toString();

    summaryArea.setText(
            PayrollProcessor.generateEmployeePayslip(
                    employeeNumber));
    }

    private void processEmployeeNumber() {

    String employeeNumber =
            JOptionPane.showInputDialog(
                    this,
                    "Enter Employee Number:");

    if (employeeNumber == null
            || employeeNumber.trim().isEmpty()) {

        return;
    }

    String payslip =
            PayrollProcessor.generateEmployeePayslip(
                    employeeNumber.trim());

    summaryArea.setText(
            payslip);
    }
    
    private void showSelectedEmployee() {

        int row
                = employeeTable.getSelectedRow();

        if (row == -1) {

            return;
        }

        String employeeNumber
                = tableModel.getValueAt(row, 0)
                        .toString();

        EmployeeManager manager
                = new EmployeeManager();

        Employee employee
                = manager.findEmployeeByNumber(
                        employeeNumber);

        if (employee == null) {

            return;
        }

        String[] data
                = employee.getData();

        summaryArea.setText(
                "========== EMPLOYEE SUMMARY ==========\n\n"
                + "Employee Number: "
                + data[0] + "\n"
                + "Name: "
                + employee.getFullName() + "\n\n"
                + "Birthday: "
                + data[3] + "\n"
                + "Address: "
                + data[4].replace("\"", "") + "\n"
                + "Phone Number: "
                + data[5] + "\n\n"
                + "SSS Number: "
                + data[6] + "\n"
                + "PhilHealth Number: "
                + data[7] + "\n"
                + "TIN Number: "
                + data[8] + "\n"
                + "Pag-IBIG Number: "
                + data[9] + "\n\n"
                + "Status: "
                + data[10] + "\n"
                + "Position: "
                + data[11] + "\n\n"
                + "Basic Salary: "
                + data[13].replace("\"", "") + "\n"
                + "Rice Subsidy: "
                + data[14].replace("\"", "") + "\n"
                + "Phone Allowance: "
                + data[15].replace("\"", "") + "\n"
                + "Clothing Allowance: "
                + data[16].replace("\"", ""));
    }
}    
