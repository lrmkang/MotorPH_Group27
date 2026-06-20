public class SalaryComputationModule {

    public static double computeGrossPay(double basicSalary) {
        return basicSalary;
    }

    public static double computeSSS(double salary) {

        if (salary < 3250) {
            return 135.00;
        } else if (salary < 3750) {
            return 157.50;
        } else if (salary < 4250) {
            return 180.00;
        } else if (salary < 4750) {
            return 202.50;
        } else if (salary < 5250) {
            return 225.00;
        } else if (salary < 5750) {
            return 247.50;
        } else if (salary < 6250) {
            return 270.00;
        } else if (salary < 6750) {
            return 292.50;
        } else if (salary < 7250) {
            return 315.00;
        } else if (salary < 7750) {
            return 337.50;
        } else if (salary < 8250) {
            return 360.00;
        } else if (salary < 8750) {
            return 382.50;
        } else if (salary < 9250) {
            return 405.00;
        } else if (salary < 9750) {
            return 427.50;
        } else if (salary < 10250) {
            return 450.00;
        } else if (salary < 10750) {
            return 472.50;
        } else if (salary < 11250) {
            return 495.00;
        } else if (salary < 11750) {
            return 517.50;
        } else if (salary < 12250) {
            return 540.00;
        } else if (salary < 12750) {
            return 562.50;
        } else if (salary < 13250) {
            return 585.00;
        } else if (salary < 13750) {
            return 607.50;
        } else if (salary < 14250) {
            return 630.00;
        } else if (salary < 14750) {
            return 652.50;
        } else if (salary < 15250) {
            return 675.00;
        } else if (salary < 15750) {
            return 697.50;
        } else if (salary < 16250) {
            return 720.00;
        } else if (salary < 16750) {
            return 742.50;
        } else if (salary < 17250) {
            return 765.00;
        } else if (salary < 17750) {
            return 787.50;
        } else if (salary < 18250) {
            return 810.00;
        } else if (salary < 18750) {
            return 832.50;
        } else if (salary < 19250) {
            return 855.00;
        } else if (salary < 19750) {
            return 877.50;
        } else if (salary < 20250) {
            return 900.00;
        } else if (salary < 20750) {
            return 922.50;
        } else if (salary < 21250) {
            return 945.00;
        } else if (salary < 21750) {
            return 967.50;
        } else if (salary < 22250) {
            return 990.00;
        } else if (salary < 22750) {
            return 1012.50;
        } else if (salary < 23250) {
            return 1035.00;
        } else if (salary < 23750) {
            return 1057.50;
        } else if (salary < 24250) {
            return 1080.00;
        } else if (salary < 24750) {
            return 1102.50;
        } else {
            return 1125.00;
        }
    }

    public static double computePhilHealth(double salary) {

        if (salary <= 10000) {
            return 150.00;
        } else if (salary < 60000) {
            return (salary * 0.03) / 2;
        } else {
            return 900.00;
        }
    }

    public static double computePagIBIG(double salary) {

        double contribution;

        if (salary <= 1500) {
            contribution = salary * 0.01;
        } else {
            contribution = salary * 0.02;
        }

        return Math.min(contribution, 100);
    }

    public static double computeWithholdingTax(double taxableIncome) {

        if (taxableIncome <= 20832) {
            return 0;
        } else if (taxableIncome < 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome < 66667) {
            return 2500 + ((taxableIncome - 33333) * 0.25);
        } else if (taxableIncome < 166667) {
            return 10833 + ((taxableIncome - 66667) * 0.30);
        } else if (taxableIncome < 666667) {
            return 40833.33 + ((taxableIncome - 166667) * 0.32);
        } else {
            return 200833.33 + ((taxableIncome - 666667) * 0.35);
        }
    }

    public static double computeDeductions(double grossPay) {

        double sss = computeSSS(grossPay);
        double philHealth = computePhilHealth(grossPay);
        double pagIBIG = computePagIBIG(grossPay);

        double taxableIncome
                = grossPay - sss - philHealth - pagIBIG;

        double withholdingTax
                = computeWithholdingTax(taxableIncome);

        return sss + philHealth + pagIBIG + withholdingTax;
    }

    public static double computeNetPay(double grossPay) {

        return grossPay - computeDeductions(grossPay);
    }
}
