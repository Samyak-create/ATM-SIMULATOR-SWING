import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ATMFrame extends JFrame {
    private final ATM atm;

    public ATMFrame(String cardNumber) {
        this.atm = new ATM(cardNumber);
        setTitle("ATM Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton balanceBtn = new JButton("Check Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton statementBtn = new JButton("Mini Statement");
        JButton logoutBtn = new JButton("Logout");

        balanceBtn.addActionListener(e -> showBalance());
        depositBtn.addActionListener(e -> depositMoney());
        withdrawBtn.addActionListener(e -> withdrawMoney());
        statementBtn.addActionListener(e -> showStatement());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(balanceBtn);
        add(depositBtn);
        add(withdrawBtn);
        add(statementBtn);
        add(logoutBtn);

        setVisible(true);
    }

    private void showBalance() {
        try {
            double balance = atm.getBalance();
            JOptionPane.showMessageDialog(this, "Your balance is ₹" + balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        try {
            double amount = Double.parseDouble(input);
            atm.deposit(amount);
            JOptionPane.showMessageDialog(this, "₹" + amount + " deposited.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(input);
            boolean success = atm.withdraw(amount);
            JOptionPane.showMessageDialog(this, success ? "Withdrawn ₹" + amount : "Insufficient balance.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void showStatement() {
        try {
            var list = atm.getMiniStatement();
            JOptionPane.showMessageDialog(this, String.join("\n", list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

