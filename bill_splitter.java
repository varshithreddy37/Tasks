import java.util.*;

public class BillSplitter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Ask total bill
        System.out.print("Enter total bill amount: ");
        double totalBill = sc.nextDouble();

        // Ask number of people
        System.out.print("Enter number of people: ");
        int n = sc.nextInt();

        // Ask split type
        System.out.print("Split by percent or amount? (type 'percent' or 'amount'): ");
        String type = sc.next().toLowerCase();

        double[] splits = new double[n];

        if (type.equals("percent")) {
            double percentTotal = 0;

            for (int i = 0; i < n; i++) {
                System.out.print("Enter percent for person " + (i + 1) + ": ");
                double percent = sc.nextDouble();
                splits[i] = (percent / 100.0) * totalBill;
                percentTotal += percent;
            }

            // Validate total percentage
            if (percentTotal != 100) {
                System.out.println("Error: Total percent must be 100%. You entered: " + percentTotal);
                return;
            }

        } else if (type.equals("amount")) {
            double amountTotal = 0;

            for (int i = 0; i < n; i++) {
                System.out.print("Enter amount for person " + (i + 1) + ": ");
                double amount = sc.nextDouble();
                splits[i] = amount;
                amountTotal += amount;
            }

            // Validate total amount
            if (amountTotal != totalBill) {
                System.out.println("Error: Total amount must equal bill amount. You entered: " + amountTotal);
                return;
            }

        } else {
            System.out.println("Invalid option. Please type 'percent' or 'amount'.");
            return;
        }

        // Output results
        System.out.println("\n--- Split Summary ---");
        for (int i = 0; i < n; i++) {
            System.out.println("Person " + (i + 1) + " pays: " + splits[i]);
        }
    }
}
