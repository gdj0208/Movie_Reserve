import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_user extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField userIdField, userNameField, userPasswordField, userPhoneField, userEmailField;
    private JCheckBox userManagerField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_user() {
        setTitle("User Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Password", "Phone", "Email", "Manager"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        userIdField = new JTextField();
        userNameField = new JTextField();
        userPasswordField = new JTextField();
        userPhoneField = new JTextField();
        userEmailField = new JTextField();
        userManagerField = new JCheckBox();
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIdField);
        formPanel.add(new JLabel("User Name:"));
        formPanel.add(userNameField);
        formPanel.add(new JLabel("User Password:"));
        formPanel.add(userPasswordField);
        formPanel.add(new JLabel("User Phone:"));
        formPanel.add(userPhoneField);
        formPanel.add(new JLabel("User Email:"));
        formPanel.add(userEmailField);
        formPanel.add(new JLabel("User Manager:"));
        formPanel.add(userManagerField);
        add(formPanel, BorderLayout.NORTH);

        // Button setup
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    userIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    userNameField.setText(model.getValueAt(selectedRow, 1).toString());
                    userPasswordField.setText(model.getValueAt(selectedRow, 2).toString());
                    userPhoneField.setText(model.getValueAt(selectedRow, 3).toString());
                    userEmailField.setText(model.getValueAt(selectedRow, 4).toString());
                    userManagerField.setSelected((boolean) model.getValueAt(selectedRow, 5));
                }
            }
        });

       
        setSize(800, 600);
        setVisible(true);

        loadUsers();
    }

    private void loadUsers() {
        try {
            String query = "SELECT * FROM db1.user";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("user_password"),
                    rs.getString("user_phone"),
                    rs.getString("user_email"),
                    rs.getBoolean("user_manager")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
        try {
            String query = "INSERT INTO db1.user (user_id, user_name, user_password, user_phone, user_email, user_manager) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(userIdField.getText()));
            pstmt.setString(2, userNameField.getText());
            pstmt.setString(3, userPasswordField.getText());
            pstmt.setString(4, userPhoneField.getText());
            pstmt.setString(5, userEmailField.getText());
            pstmt.setBoolean(6, userManagerField.isSelected());
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(userIdField.getText()),
                userNameField.getText(),
                userPasswordField.getText(),
                userPhoneField.getText(),
                userEmailField.getText(),
                userManagerField.isSelected()
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.user WHERE user_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.user SET user_name = ?, user_password = ?, user_phone = ?, user_email = ?, user_manager = ? WHERE user_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, userNameField.getText());
                pstmt.setString(2, userPasswordField.getText());
                pstmt.setString(3, userPhoneField.getText());
                pstmt.setString(4, userEmailField.getText());
                pstmt.setBoolean(5, userManagerField.isSelected());
                pstmt.setInt(6, userId);
                pstmt.executeUpdate();
                model.setValueAt(userNameField.getText(), selectedRow, 1);
                model.setValueAt(userPasswordField.getText(), selectedRow, 2);
                model.setValueAt(userPhoneField.getText(), selectedRow, 3);
                model.setValueAt(userEmailField.getText(), selectedRow, 4);
                model.setValueAt(userManagerField.isSelected(), selectedRow, 5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_user());
    }
}
