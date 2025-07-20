import java.sql.*;
import java.util.Scanner;

public class BankService {
    Scanner sc = new Scanner(System.in);

    public void createAccount() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String sql = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Account created successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer login() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String sql = "SELECT * FROM customers WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful. Welcome " + rs.getString("name") + "!");
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        email,
                        password,
                        rs.getDouble("balance")
                );
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deposit(Customer customer) {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE customers SET balance = balance + ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, amount);
            pst.setInt(2, customer.getId());
            pst.executeUpdate();

            String tx = "INSERT INTO transactions (customer_id, type, amount) VALUES (?, 'Deposit', ?)";
            PreparedStatement pstTx = conn.prepareStatement(tx);
            pstTx.setInt(1, customer.getId());
            pstTx.setDouble(2, amount);
            pstTx.executeUpdate();

            System.out.println("Deposit successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(Customer customer) {
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        if (customer.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE customers SET balance = balance - ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, amount);
            pst.setInt(2, customer.getId());
            pst.executeUpdate();

            String tx = "INSERT INTO transactions (customer_id, type, amount) VALUES (?, 'Withdraw', ?)";
            PreparedStatement pstTx = conn.prepareStatement(tx);
            pstTx.setInt(1, customer.getId());
            pstTx.setDouble(2, amount);
            pstTx.executeUpdate();

            System.out.println("Withdrawal successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkBalance(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT balance FROM customers WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, customer.getId());

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Your balance is: ₹" + rs.getDouble("balance"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
