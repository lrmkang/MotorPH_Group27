public class Employee {

    private String[] data;

    public Employee(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public String getEmployeeNumber() {
        return data[0];
    }

    public String getLastName() {
        return data[1];
    }

    public String getFirstName() {
        return data[2];
    }

    public String getFullName() {
        return data[2] + " " + data[1];
    }

    public double getBasicSalary() {
        return Double.parseDouble(
                data[13]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());
    }

    public double getRiceSubsidy() {
        return Double.parseDouble(
                data[14]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());
    }

    public double getPhoneAllowance() {
        return Double.parseDouble(
                data[15]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());
    }

    public double getClothingAllowance() {
        return Double.parseDouble(
                data[16]
                        .replace("\"", "")
                        .replace(",", "")
                        .trim());
    }

    public void setFirstName(String firstName) {
        data[2] = firstName;
    }

    public void setLastName(String lastName) {
        data[1] = lastName;
    }

    public void setBasicSalary(double salary) {
        data[13] = String.valueOf(salary);
    }

    public void setRiceSubsidy(double value) {
        data[14] = String.valueOf(value);
    }

    public void setPhoneAllowance(double value) {
        data[15] = String.valueOf(value);
    }

    public void setClothingAllowance(double value) {
        data[16] = String.valueOf(value);
    }
}
