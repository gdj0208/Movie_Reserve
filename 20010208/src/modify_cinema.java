import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_cinema extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField cinemaIdField, cinemaSeatField, cinemaStatusField, cinemaSeatXField, cinemaSeatYField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_cinema() {
        setTitle("Cinema Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Seat", "Status", "Seat X", "Seat Y"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        cinemaIdField = new JTextField();
        cinemaSeatField = new JTextField();
        cinemaStatusField = new JTextField();
        cinemaSeatXField = new JTextField();
        cinemaSeatYField = new JTextField();
        formPanel.add(new JLabel("Cinema ID:"));
        formPanel.add(cinemaIdField);
        formPanel.add(new JLabel("Cinema Seat:"));
        formPanel.add(cinemaSeatField);
        formPanel.add(new JLabel("Cinema Status:"));
        formPanel.add(cinemaStatusField);
        formPanel.add(new JLabel("Cinema Seat X:"));
        formPanel.add(cinemaSeatXField);
        formPanel.add(new JLabel("Cinema Seat Y:"));
        formPanel.add(cinemaSeatYField);
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
                addCinema();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCinema();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCinema();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    cinemaIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    cinemaSeatField.setText(model.getValueAt(selectedRow, 1).toString());
                    cinemaStatusField.setText(model.getValueAt(selectedRow, 2).toString());
                    cinemaSeatXField.setText(model.getValueAt(selectedRow, 3).toString());
                    cinemaSeatYField.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

       
        setSize(800, 600);
        setVisible(true);

        loadCinemas();
    }

    private void loadCinemas() {
        try {
            String query = "SELECT * FROM db1.cinema";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("cinema_id"),
                    rs.getInt("cinema_seat"),
                    rs.getBoolean("cinema_status"),
                    rs.getInt("cinema_seat_x"),
                    rs.getInt("cinema_seat_y")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCinema() {
        try {
            String query = "INSERT INTO db1.cinema (cinema_id, cinema_seat, cinema_status, cinema_seat_x, cinema_seat_y) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(cinemaIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(cinemaSeatField.getText()));
            pstmt.setBoolean(3, Boolean.parseBoolean(cinemaStatusField.getText()));
            pstmt.setInt(4, Integer.parseInt(cinemaSeatXField.getText()));
            pstmt.setInt(5, Integer.parseInt(cinemaSeatYField.getText()));
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(cinemaIdField.getText()),
                Integer.parseInt(cinemaSeatField.getText()),
                Boolean.parseBoolean(cinemaStatusField.getText()),
                Integer.parseInt(cinemaSeatXField.getText()),
                Integer.parseInt(cinemaSeatYField.getText())
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCinema() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int cinemaId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.cinema WHERE cinema_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, cinemaId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCinema() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int cinemaId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.cinema SET cinema_seat = ?, cinema_status = ?, cinema_seat_x = ?, cinema_seat_y = ? WHERE cinema_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(cinemaSeatField.getText()));
                pstmt.setBoolean(2, Boolean.parseBoolean(cinemaStatusField.getText()));
                pstmt.setInt(3, Integer.parseInt(cinemaSeatXField.getText()));
                pstmt.setInt(4, Integer.parseInt(cinemaSeatYField.getText()));
                pstmt.setInt(5, cinemaId);
                pstmt.executeUpdate();
                model.setValueAt(Integer.parseInt(cinemaSeatField.getText()), selectedRow, 1);
                model.setValueAt(Boolean.parseBoolean(cinemaStatusField.getText()), selectedRow, 2);
                model.setValueAt(Integer.parseInt(cinemaSeatXField.getText()), selectedRow, 3);
                model.setValueAt(Integer.parseInt(cinemaSeatYField.getText()), selectedRow, 4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_cinema());
    }
}
