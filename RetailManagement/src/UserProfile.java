import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserProfile extends JFrame {
    JLabel userLabel = new JLabel("Username: ");

    public UserProfile() {
        setTitle("User Profile");
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Light purple background
        getContentPane().setBackground(new Color(230, 220, 250));

        userLabel.setBounds(30, 50, 240, 30);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(new Color(80, 0, 100));
        add(userLabel);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(90, 100, 100, 30);
        backBtn.setBackground(new Color(180, 150, 220));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        add(backBtn);

        backBtn.addActionListener(e -> {
            new UserDashboard().setVisible(true);
            dispose();
        });

        // Use Session data if available
        String username = Session.getLoggedInUsername();
        if (username != null) {
            userLabel.setText("Username: " + username);
        } else {
            try (Connection conn = DBConnection.getConnection()) {
                ResultSet rs = conn.createStatement().executeQuery("SELECT username FROM users LIMIT 1");
                if (rs.next()) userLabel.setText("Username: " + rs.getString("username"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
