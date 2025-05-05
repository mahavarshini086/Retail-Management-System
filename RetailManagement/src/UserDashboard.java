import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {
    public UserDashboard() {
        setTitle("Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Light purple background
        getContentPane().setBackground(new Color(230, 220, 250));

        JButton manageBtn = new JButton("Manage Products");
        JButton profileBtn = new JButton("User Profile");
        JButton logoutBtn = new JButton("Logout");

        // Styling buttons
        Color btnColor = new Color(180, 150, 220);
        Font btnFont = new Font("Arial", Font.BOLD, 13);

        JButton[] buttons = {manageBtn, profileBtn, logoutBtn};
        int y = 70;
        for (JButton btn : buttons) {
            btn.setBounds(100, y, 200, 35);
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            y += 50;
            add(btn);
        }

        manageBtn.addActionListener(e -> {
            new ManageProducts().setVisible(true);
            dispose();
        });

        profileBtn.addActionListener(e -> {
            new UserProfile().setVisible(true);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }
}
