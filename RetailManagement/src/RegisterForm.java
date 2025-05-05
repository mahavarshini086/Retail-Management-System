import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;

    public RegisterForm() {
        setTitle("Register - Retail Management");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 220, 255));  // Light purple

        JLabel userLabel = new JLabel("New Username:");
        JLabel passLabel = new JLabel("New Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton registerBtn = new JButton("Register");

        userLabel.setBounds(50, 50, 100, 30);
        usernameField.setBounds(160, 50, 180, 30);
        passLabel.setBounds(50, 100, 100, 30);
        passwordField.setBounds(160, 100, 180, 30);
        registerBtn.setBounds(140, 160, 120, 30);

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        userLabel.setFont(labelFont);
        passLabel.setFont(labelFont);

        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        registerBtn.setFont(buttonFont);
        registerBtn.setBackground(new Color(160, 130, 200));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        add(userLabel); add(usernameField);
        add(passLabel); add(passwordField);
        add(registerBtn);

        registerBtn.addActionListener(e -> register());
    }

    private void register() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usernameField.getText());
            ps.setString(2, new String(passwordField.getPassword()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Registered!");
            new LoginForm().setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        }
    }

    public static void main(String[] args) {
        new RegisterForm().setVisible(true);
    }
}
