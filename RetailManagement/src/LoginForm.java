import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;

    public LoginForm() {
        setTitle("Login - Retail Management");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(80, 40, 120)); // Purple theme

        // Load icons
        ImageIcon userIcon = new ImageIcon("C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\RetailManagement\\icons\\user.png");
        ImageIcon lockIcon = new ImageIcon("icons/lock.png");
        ImageIcon loginIcon = new ImageIcon("icons/login.png");
        ImageIcon registerIcon = new ImageIcon("icons/register.png");
        

        JLabel titleLabel = new JLabel("Retail Management Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 10, 300, 30);
        add(titleLabel);

        JLabel userLabel = new JLabel("Username:", userIcon, JLabel.LEFT);
        userLabel.setForeground(Color.WHITE);
        JLabel passLabel = new JLabel("Password:", lockIcon, JLabel.LEFT);
        passLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login", loginIcon);
        JButton registerBtn = new JButton("Register", registerIcon);

        userLabel.setBounds(50, 50, 120, 30);
        usernameField.setBounds(170, 50, 160, 30);
        passLabel.setBounds(50, 100, 120, 30);
        passwordField.setBounds(170, 100, 160, 30);
        loginBtn.setBounds(50, 160, 120, 30);
        registerBtn.setBounds(210, 160, 120, 30);

        add(userLabel); add(usernameField);
        add(passLabel); add(passwordField);
        add(loginBtn); add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
    }

    private void login() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usernameField.getText());
            ps.setString(2, new String(passwordField.getPassword()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new UserDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}
