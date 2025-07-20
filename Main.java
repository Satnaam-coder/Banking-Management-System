import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Welcome to Java Bank ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    service.createAccount();
                    break;
                case 2:
                    Customer customer = service.login();
                    if (customer != null) {
                        boolean session = true;
                        while (session) {
                            System.out.println("\n--- Menu ---");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Logout");
                            System.out.print("Choose: ");
                            int opt = sc.nextInt();
                            switch (opt) {
                                case 1:
                                    service.deposit(customer);
                                    break;
                                case 2:
                                    service.withdraw(customer);
                                    break;
                                case 3:
                                    service.checkBalance(customer);
                                    break;
                                case 4:
                                    session = false;
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Thank you for using Java Bank!");
                    System.exit(0);
            }
        }
    }
}