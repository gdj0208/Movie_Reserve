import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_seat extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField seatIdField, seatNumField, seatCinemaField;
    private JCheckBox seatStatusField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_seat() {
        setTitle("Seat Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Number", "Cinema", "Status"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        seatIdField = new JTextField();
        seatNumField = new JTextField();
        seatCinemaField = new JTextField();
        seatStatusField = new JCheckBox();
        formPanel.add(new JLabel("Seat ID:"));
        formPanel.add(seatIdField);
        formPanel.add(new JLabel("Seat Number:"));
        formPanel.add(seatNumField);
        formPanel.add(new JLabel("Cinema ID:"));
        formPanel.add(seatCinemaField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(seatStatusField);
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
                addSeat();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSeat();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSeat();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    seatIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    seatNumField.setText(model.getValueAt(selectedRow, 1).toString());
                    seatCinemaField.setText(model.getValueAt(selectedRow, 2).toString());
                    seatStatusField.setSelected((boolean) model.getValueAt(selectedRow, 3));
                }
            }
        });

     
        setSize(800, 600);
        setVisible(true);

        loadSeats();
    }

    private void loadSeats() {
        try {
            String query = "SELECT * FROM db1.seat";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("seat_id"),
                    rs.getInt("seat_num"),
                    rs.getInt("seat_cinema"),
                    rs.getBoolean("seat_status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSeat() {
        try {
            String query = "INSERT INTO db1.seat (seat_id, seat_num, seat_cinema, seat_status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(seatIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(seatNumField.getText()));
            pstmt.setInt(3, Integer.parseInt(seatCinemaField.getText()));
            pstmt.setBoolean(4, seatStatusField.isSelected());
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(seatIdField.getText()),
                Integer.parseInt(seatNumField.getText()),
                Integer.parseInt(seatCinemaField.getText()),
                seatStatusField.isSelected()
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSeat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int seatId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.seat WHERE seat_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, seatId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSeat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int seatId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.seat SET seat_num = ?, seat_cinema = ?, seat_status = ? WHERE seat_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(seatNumField.getText()));
                pstmt.setInt(2, Integer.parseInt(seatCinemaField.getText()));
                pstmt.setBoolean(3, seatStatusField.isSelected());
                pstmt.setInt(4, seatId);
                pstmt.executeUpdate();
                model.setValueAt(Integer.parseInt(seatNumField.getText()), selectedRow, 1);
                model.setValueAt(Integer.parseInt(seatCinemaField.getText()), selectedRow, 2);
                model.setValueAt(seatStatusField.isSelected(), selectedRow, 3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_seat());
    }
}
