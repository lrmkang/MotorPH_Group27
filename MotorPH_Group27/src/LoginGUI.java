import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI {

    private JFrame frame;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;

    public LoginGUI() {

        frame = new JFrame("MotorPH Login");

        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JLabel titleLabel = new JLabel(
        "MotorPH Payroll System",
        SwingConstants.CENTER);

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(
        new Dimension(120, 35));

        panel.add(lblUsername);
        panel.add(txtUsername);

        panel.add(lblPassword);
        panel.add(txtPassword);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);

        JPanel mainPanel = new JPanel(
        new BorderLayout(10, 10));

        mainPanel.setBorder(
        BorderFactory.createEmptyBorder(
                20, 20, 20, 20));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                authenticate();
            }
        });

        frame.setVisible(true);
    }

    private void authenticate() {

        String username = txtUsername.getText();

        String password =
                new String(txtPassword.getPassword());

        if (!password.equals("12345")
                || (!username.equals("employee")
                && !username.equals("payroll_staff"))) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Incorrect username and/or password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        JOptionPane.showMessageDialog(
                frame,
                "Login Successful!");

        frame.dispose();

        if (username.equals("payroll_staff")) {

            new GUI();

        } else {

        String employeeNumber =
            JOptionPane.showInputDialog(
                    frame,
                    "Enter Employee Number:");

        if (employeeNumber == null
            || employeeNumber.trim().isEmpty()) {

        return;
        }

        new EmployeeGUI(
            employeeNumber.trim());
        }
        }
}