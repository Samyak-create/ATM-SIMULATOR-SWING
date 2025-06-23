import java.sql.*;
import java.util.ArrayList;

public class ATM {
    private final String cardNumber;

    public ATM(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getBalance() throws SQLException {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT balance FROM users WHERE card_number=?");
        ps.setString(1, cardNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getDouble("balance");
        return 0;
    }

    public void deposit(double amount) throws SQLException {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE users SET balance = balance + ? WHERE card_number=?");
        ps.setDouble(1, amount);
        ps.setString(2, cardNumber);
        ps.executeUpdate();
        logTransaction("CREDIT", amount);
    }

    public boolean withdraw(double amount) throws SQLException {
        double bal = getBalance();
        if (bal < amount) return false;

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE users SET balance = balance - ? WHERE card_number=?");
        ps.setDouble(1, amount);
        ps.setString(2, cardNumber);
        ps.executeUpdate();
        logTransaction("DEBIT", amount);
        return true;
    }

    private void logTransaction(String type, double amount) throws SQLException {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO transactions(card_number, type, amount) VALUES (?, ?, ?)");
        ps.setString(1, cardNumber);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.executeUpdate();
    }

    public ArrayList<String> getMiniStatement() throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE card_number=? ORDER BY timestamp DESC LIMIT 5");
        ps.setString(1, cardNumber);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(rs.getTimestamp("timestamp") + " | " + rs.getString("type") + " | ₹" + rs.getDouble("amount"));
        }
        return list;
    }
}

