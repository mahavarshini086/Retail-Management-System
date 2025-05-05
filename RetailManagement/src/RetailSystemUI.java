import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RetailSystemUI extends JFrame {
    private JTextField nameField, priceField, qtyField, searchField;
    private JTable table;
    private DefaultTableModel model;

    public RetailSystemUI() {
        setTitle("Retail Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 220, 255)); // Light purple

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel qtyLabel = new JLabel("Quantity:");
        nameLabel.setFont(labelFont);
        priceLabel.setFont(labelFont);
        qtyLabel.setFont(labelFont);

        nameField = new JTextField();
        priceField = new JTextField();
        qtyField = new JTextField();
        searchField = new JTextField();
        nameField.setFont(fieldFont);
        priceField.setFont(fieldFont);
        qtyField.setFont(fieldFont);
        searchField.setFont(fieldFont);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        Color btnColor = new Color(160, 130, 200);
        Color btnTextColor = Color.WHITE;

        JButton[] buttons = {addBtn, updateBtn, deleteBtn};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(btnColor);
            btn.setForeground(btnTextColor);
            btn.setFocusPainted(false);
        }

        model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty"}, 0);
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(table);

        nameLabel.setBounds(20, 20, 80, 25);
        nameField.setBounds(100, 20, 150, 25);
        priceLabel.setBounds(20, 60, 80, 25);
        priceField.setBounds(100, 60, 150, 25);
        qtyLabel.setBounds(20, 100, 80, 25);
        qtyField.setBounds(100, 100, 150, 25);
        addBtn.setBounds(270, 20, 100, 30);
        updateBtn.setBounds(270, 60, 100, 30);
        deleteBtn.setBounds(270, 100, 100, 30);
        searchField.setBounds(400, 20, 250, 25);
        scrollPane.setBounds(20, 150, 650, 300);

        add(nameLabel); add(nameField);
        add(priceLabel); add(priceField);
        add(qtyLabel); add(qtyField);
        add(addBtn); add(updateBtn); add(deleteBtn);
        add(searchField); add(scrollPane);

        loadProducts();

        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchProducts(searchField.getText());
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                nameField.setText(model.getValueAt(row, 1).toString());
                priceField.setText(model.getValueAt(row, 2).toString());
                qtyField.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void loadProducts() {
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

    private void addProduct() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO products(name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt(qtyField.getText()));
            ps.executeUpdate();
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) model.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE products SET name=?, price=?, quantity=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt(qtyField.getText()));
            ps.setInt(4, id);
            ps.executeUpdate();
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) model.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchProducts(String keyword) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM products WHERE name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
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

    public static void main(String[] args) {
        new RetailSystemUI().setVisible(true);
    }
}
