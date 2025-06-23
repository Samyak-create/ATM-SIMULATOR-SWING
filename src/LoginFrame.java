import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField cardField;
    private JPasswordField pinField;

    public LoginFrame() {
        setTitle("ATM Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Card Number:"));
        cardField = new JTextField();
        add(cardField);

        add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        add(pinField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> authenticate());
        add(loginBtn);

        setVisible(true);
    }

    private void authenticate() {
        String card = cardField.getText();
        String pin = new String(pinField.getPassword());

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE card_number=? AND pin=?");
            ps.setString(1, card);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispose(); // close login
                new ATMFrame(card); // open dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card or PIN");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
