import javax.swing.*;
import java.awt.*;

/**
 * A single modal form dialog containing every employee input field.
 * Used by both "Add Employee" and "Update Employee" in GUI.java.
 */
public class EmployeeFormDialog extends JDialog {

    private JTextField employeeNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField birthdayField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField sssNumberField;
    private JTextField philHealthNumberField;
    private JTextField tinNumberField;
    private JTextField pagibigNumberField;
    private JTextField statusField;
    private JTextField positionField;
    private JTextField basicSalaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;

    private boolean confirmed = false;
    private final boolean isUpdateMode;
    private EmployeeManager manager;

    public EmployeeFormDialog(JFrame owner, String title, Employee employee, EmployeeManager manager) {

        super(owner, title, true);
        
        this.manager = manager;

        this.isUpdateMode = (employee != null);

        buildForm(employee);

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void buildForm(Employee employee) {

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        GridBagConstraints fieldConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(4, 4, 4, 8);

        fieldConstraints.gridx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);

        int row = 0;

        employeeNumberField = new JTextField(20);
        lastNameField = new JTextField(20);
        firstNameField = new JTextField(20);
        birthdayField = new JTextField(20);
        addressField = new JTextField(20);
        phoneNumberField = new JTextField(20);
        sssNumberField = new JTextField(20);
        philHealthNumberField = new JTextField(20);
        tinNumberField = new JTextField(20);
        pagibigNumberField = new JTextField(20);
        statusField = new JTextField(20);
        positionField = new JTextField(20);
        basicSalaryField = new JTextField(20);
        riceSubsidyField = new JTextField(20);
        phoneAllowanceField = new JTextField(20);
        clothingAllowanceField = new JTextField(20);

        if (employee != null) {

            String[] data = employee.getData();

            employeeNumberField.setText(employee.getEmployeeNumber());
            lastNameField.setText(employee.getLastName());
            firstNameField.setText(employee.getFirstName());
            birthdayField.setText(data[3]);
            addressField.setText(data[4].replace("\"", ""));
            phoneNumberField.setText(data[5]);
            sssNumberField.setText(data[6]);
            philHealthNumberField.setText(data[7]);
            tinNumberField.setText(data[8]);
            pagibigNumberField.setText(data[9]);
            statusField.setText(data[10]);
            positionField.setText(data[11]);
            basicSalaryField.setText(String.valueOf(employee.getBasicSalary()));
            riceSubsidyField.setText(String.valueOf(employee.getRiceSubsidy()));
            phoneAllowanceField.setText(String.valueOf(employee.getPhoneAllowance()));
            clothingAllowanceField.setText(String.valueOf(employee.getClothingAllowance()));

            // Employee Number is the lookup key; don't allow editing it here.
            employeeNumberField.setEditable(false);

        } else {

            basicSalaryField.setText("0");
            riceSubsidyField.setText("0");
            phoneAllowanceField.setText("0");
            clothingAllowanceField.setText("0");
        }

        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Employee Number:", employeeNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Last Name:", lastNameField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "First Name:", firstNameField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Birthday (mm/dd/yyyy):", birthdayField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Address:", addressField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Phone Number:", phoneNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "SSS Number:", sssNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "PhilHealth Number:", philHealthNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "TIN Number:", tinNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Pag-IBIG Number:", pagibigNumberField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Status:", statusField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Position:", positionField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Basic Salary:", basicSalaryField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Rice Subsidy:", riceSubsidyField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Phone Allowance:", phoneAllowanceField);
        row = addField(formPanel, labelConstraints, fieldConstraints, row,
                "Clothing Allowance:", clothingAllowanceField);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setPreferredSize(new Dimension(480, 480));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    private int addField(
            JPanel panel,
            GridBagConstraints labelConstraints,
            GridBagConstraints fieldConstraints,
            int row,
            String labelText,
            JTextField field) {

        labelConstraints.gridy = row;
        fieldConstraints.gridy = row;

        panel.add(new JLabel(labelText), labelConstraints);
        panel.add(field, fieldConstraints);

        return row + 1;
    }

    private void onOk() {

        String validationError = validateFields();

        if (validationError != null) {

            JOptionPane.showMessageDialog(
                    this,
                    validationError,
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        confirmed = true;
        setVisible(false);
    }

    private void onCancel() {
        confirmed = false;
        setVisible(false);
    }

    private String validateFields() {

        StringBuilder errors = new StringBuilder();

        // Employee Number
        String empNo = employeeNumberField.getText().trim();

        if (empNo.isEmpty()) {

            errors.append("- Employee Number is required.\n");

        } else if (!empNo.matches("\\d+")) {

            errors.append("- Employee Number must contain digits only.\n");

        } else if (!isUpdateMode
                && manager.employeeExists(empNo)) {

            errors.append("- Employee Number already exists.\n");
        }

        // Required Fields
        requireField(lastNameField, "Last Name", errors);
        requireField(firstNameField, "First Name", errors);
        requireField(birthdayField, "Birthday", errors);
        requireField(phoneNumberField, "Phone Number", errors);
        requireField(sssNumberField, "SSS Number", errors);
        requireField(philHealthNumberField, "PhilHealth Number", errors);
        requireField(tinNumberField, "TIN Number", errors);
        requireField(pagibigNumberField, "Pag-IBIG Number", errors);
        requireField(statusField, "Status", errors);
        requireField(positionField, "Position", errors);

        // Birthday Format
        if (!birthdayField.getText()
                .trim()
                .matches("\\d{2}/\\d{2}/\\d{4}")) {

            errors.append("- Birthday must be MM/DD/YYYY.\n");
        }

        // Phone Number (Phone # Format Validation)
        requirePhoneFormat(phoneNumberField, errors);        
        
        // Status & Position (String Validation)
        requireLetters(statusField, "Status", errors);
        requireLetters(positionField, "Position", errors);

        // Government IDs (Digit Validation)
        requireDigits(sssNumberField, "SSS Number", errors);
        requireDigits(philHealthNumberField, "PhilHealth Number", errors);
        requireDigits(tinNumberField, "TIN Number", errors);
        requireDigits(pagibigNumberField, "Pag-IBIG Number", errors);        

        // Salary Fields (Numeric Validation)
        requireNumeric(basicSalaryField, "Basic Salary", errors);
        requireNumeric(riceSubsidyField, "Rice Subsidy", errors);
        requireNumeric(phoneAllowanceField, "Phone Allowance", errors);
        requireNumeric(clothingAllowanceField, "Clothing Allowance", errors);

        return errors.length() > 0
                ? errors.toString()
                : null;
    }

    private boolean isNumeric(String value) {

        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void requireField(
        JTextField field,
        String fieldName,
        StringBuilder errors) {

        if (field.getText().trim().isEmpty()) {

            errors.append("- ")
                  .append(fieldName)
                  .append(" is required.\n");
        }
    }

    private void requireLetters(
        JTextField field,
        String fieldName,
        StringBuilder errors) {

        String value = field.getText().trim();

        if (!value.isEmpty()
                && !value.matches(".*[a-zA-Z].*")) {

            errors.append("- ")
                  .append(fieldName)
                  .append(" must contain letters.\n");
        }
    }

    private void requireNumeric(
            JTextField field,
            String fieldName,
            StringBuilder errors) {

        String value = field.getText().trim();

        if (!isNumeric(value)) {

            errors.append("- ")
                  .append(fieldName)
                  .append(" must be numeric.\n");

        } else if (Double.parseDouble(value) < 0) {

            errors.append("- ")
                  .append(fieldName)
                  .append(" cannot be negative.\n");
        }
    }
    
    private void requireDigits(
        JTextField field,
        String fieldName,
        StringBuilder errors) {

        String value = field.getText().trim();

        if (!value.isEmpty()
                && !value.matches("\\d+")) {

            errors.append("- ")
                  .append(fieldName)
                  .append(" must contain digits only.\n");
        }
    }
    
    private void requirePhoneFormat(
            JTextField field,
            StringBuilder errors) {

        String value = field.getText().trim();

        if (!value.isEmpty()
                && !value.matches("\\d{3}-\\d{3}-\\d{3}")) {

            errors.append(
                    "- Phone Number must be in the format "
                    + "123-456-789.\n");
        }
    }    
    
    public boolean isConfirmed() {
        return confirmed;
    }

    public String getEmployeeNumber() {
        return employeeNumberField.getText().trim();
    }

    public String getLastName() {
        return lastNameField.getText().trim();
    }

    public String getFirstName() {
        return firstNameField.getText().trim();
    }

    public String getBirthday() {
        return birthdayField.getText().trim();
    }

    public String getAddress() {
        return addressField.getText().trim();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText().trim();
    }

    public String getSssNumber() {
        return sssNumberField.getText().trim();
    }

    public String getPhilHealthNumber() {
        return philHealthNumberField.getText().trim();
    }

    public String getTinNumber() {
        return tinNumberField.getText().trim();
    }

    public String getPagibigNumber() {
        return pagibigNumberField.getText().trim();
    }

    public String getStatus() {
        return statusField.getText().trim();
    }

    public String getPosition() {
        return positionField.getText().trim();
    }

    public double getBasicSalary() {
        return Double.parseDouble(basicSalaryField.getText().trim());
    }

    public double getRiceSubsidy() {
        return Double.parseDouble(riceSubsidyField.getText().trim());
    }

    public double getPhoneAllowance() {
        return Double.parseDouble(phoneAllowanceField.getText().trim());
    }

    public double getClothingAllowance() {
        return Double.parseDouble(clothingAllowanceField.getText().trim());
    }
}
