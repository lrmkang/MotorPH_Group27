import javax.swing.*;
import java.awt.*;

public class EmployeeGUI extends JFrame {

    private String employeeNumber;
    private JTextArea displayArea;

    public EmployeeGUI(String employeeNumber) {

        this.employeeNumber = employeeNumber;

        setTitle("MotorPH Employee Portal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton infoButton =
                new JButton("View My Information");

        JButton payslipButton =
                new JButton("View My Payslip");

        JButton logoutButton =
                new JButton("Logout");

        JPanel topPanel = new JPanel();

        topPanel.add(infoButton);
        topPanel.add(payslipButton);
        topPanel.add(logoutButton);

        displayArea = new JTextArea();

        displayArea.setEditable(false);

        JScrollPane scrollPane =
                new JScrollPane(displayArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        infoButton.addActionListener(
                e -> viewMyInformation());

        payslipButton.addActionListener(
                e -> viewMyPayslip());

        logoutButton.addActionListener(
                e -> logout());

        setVisible(true);
    }

    private void viewMyInformation() {

        String[] data =
                PayrollProcessor.findEmployeeByNumber(
                        employeeNumber);

        if (data == null) {

            displayArea.setText(
                    "Employee not found.");

            return;
        }

        displayArea.setText(
                "========== EMPLOYEE INFORMATION ==========\n\n"
                + "Employee Number: " + data[0] + "\n"
                + "Name: " + data[2] + " " + data[1] + "\n\n"
                + "Birthday: " + data[3] + "\n"
                + "Address: " + data[4] + "\n"
                + "Phone Number: " + data[5] + "\n\n"
                + "SSS Number: " + data[6] + "\n"
                + "PhilHealth Number: " + data[7] + "\n"
                + "TIN Number: " + data[8] + "\n"
                + "Pag-IBIG Number: " + data[9] + "\n\n"
                + "Status: " + data[10] + "\n"
                + "Position: " + data[11]);
    }

    private void viewMyPayslip() {

        displayArea.setText(
                PayrollProcessor.generateEmployeePayslip(
                        employeeNumber));
    }

    private void logout() {

        dispose();

        new LoginGUI();
    }
}