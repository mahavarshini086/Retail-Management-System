import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageProducts extends JFrame {
    JTable table;
    DefaultTableModel model;
    JTextField nameField, priceField, quantityField;

    public ManageProducts() {
        setTitle("Product Management");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 220, 255));  // Light purple

        model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity"}, 0);
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 10, 560, 150);
        add(pane);

        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        nameField.setBounds(10, 180, 130, 30);
        priceField.setBounds(150, 180, 130, 30);
        quantityField.setBounds(290, 180, 130, 30);

        addBtn.setBounds(430, 180, 100, 30);
        updateBtn.setBounds(430, 220, 100, 30);
        deleteBtn.setBounds(430, 260, 100, 30);
        backBtn.setBounds(430, 300, 100, 30);

        // Style fields and buttons
        JTextField[] fields = {nameField, priceField, quantityField};
        for (JTextField f : fields) {
            f.setBackground(Color.WHITE);
            f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }

        JButton[] buttons = {addBtn, updateBtn, deleteBtn, backBtn};
        for (JButton b : buttons) {
            b.setBackground(new Color(160, 130, 200)); // Medium purple
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        add(nameField); add(priceField); add(quantityField);
        add(addBtn); add(updateBtn); add(deleteBtn); add(backBtn);

        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        backBtn.addActionListener(e -> {
            new UserDashboard().setVisible(true);
            dispose();
        });

        loadProducts();

        table.getSelectionModel().addListSelectionListener(e -> fillFormFields());
    }

    void loadProducts() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM products");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addProduct() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt(quantityField.getText()));
            ps.executeUpdate();
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateProduct() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE products SET name=?, price=?, quantity=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setDouble(2, Double.parseDouble(priceField.getText()));
                ps.setInt(3, Integer.parseInt(quantityField.getText()));
                ps.setInt(4, id);
                ps.executeUpdate();
                loadProducts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void deleteProduct() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            try (Connection conn = DBConnection.getConnection()) {
                conn.createStatement().executeUpdate("DELETE FROM products WHERE id=" + id);
                loadProducts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void fillFormFields() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            nameField.setText(model.getValueAt(row, 1).toString());
            priceField.setText(model.getValueAt(row, 2).toString());
            quantityField.setText(model.getValueAt(row, 3).toString());
        }
    }

    public static void main(String[] args) {
        new ManageProducts().setVisible(true);
    }
}
