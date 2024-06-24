import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_ticket extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField ticketIdField, ticketScheduleField, ticketCinemaField, ticketSeatField, ticketBookingField, ticketPriceStdField, ticketPriceSellField;
    private JCheckBox ticketStatusField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_ticket() {
        setTitle("Ticket Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Schedule", "Cinema", "Seat", "Booking", "Status", "Price Std", "Price Sell"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        ticketIdField = new JTextField();
        ticketScheduleField = new JTextField();
        ticketCinemaField = new JTextField();
        ticketSeatField = new JTextField();
        ticketBookingField = new JTextField();
        ticketStatusField = new JCheckBox();
        ticketPriceStdField = new JTextField();
        ticketPriceSellField = new JTextField();
        formPanel.add(new JLabel("Ticket ID:"));
        formPanel.add(ticketIdField);
        formPanel.add(new JLabel("Schedule ID:"));
        formPanel.add(ticketScheduleField);
        formPanel.add(new JLabel("Cinema ID:"));
        formPanel.add(ticketCinemaField);
        formPanel.add(new JLabel("Seat ID:"));
        formPanel.add(ticketSeatField);
        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(ticketBookingField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(ticketStatusField);
        formPanel.add(new JLabel("Standard Price:"));
        formPanel.add(ticketPriceStdField);
        formPanel.add(new JLabel("Selling Price:"));
        formPanel.add(ticketPriceSellField);
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
                addTicket();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTicket();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTicket();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    ticketIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    ticketScheduleField.setText(model.getValueAt(selectedRow, 1).toString());
                    ticketCinemaField.setText(model.getValueAt(selectedRow, 2).toString());
                    ticketSeatField.setText(model.getValueAt(selectedRow, 3).toString());
                    ticketBookingField.setText(model.getValueAt(selectedRow, 4).toString());
                    ticketStatusField.setSelected((boolean) model.getValueAt(selectedRow, 5));
                    ticketPriceStdField.setText(model.getValueAt(selectedRow, 6).toString());
                    ticketPriceSellField.setText(model.getValueAt(selectedRow, 7).toString());
                }
            }
        });

       
        setSize(800, 600);
        setVisible(true);

        loadTickets();
    }

    private void loadTickets() {
        try {
            String query = "SELECT * FROM db1.ticket";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ticket_id"),
                    rs.getInt("ticket_schedule"),
                    rs.getInt("ticket_cinema"),
                    rs.getInt("ticket_seat"),
                    rs.getInt("ticket_booking"),
                    rs.getBoolean("ticket_status"),
                    rs.getInt("ticket_price_std"),
                    rs.getInt("ticket_price_sell")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTicket() {
        try {
            String query = "INSERT INTO db1.ticket (ticket_id, ticket_schedule, ticket_cinema, ticket_seat, ticket_booking, ticket_status, ticket_price_std, ticket_price_sell) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(ticketIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(ticketScheduleField.getText()));
            pstmt.setInt(3, Integer.parseInt(ticketCinemaField.getText()));
            pstmt.setInt(4, Integer.parseInt(ticketSeatField.getText()));
            pstmt.setInt(5, Integer.parseInt(ticketBookingField.getText()));
            pstmt.setBoolean(6, ticketStatusField.isSelected());
            pstmt.setInt(7, Integer.parseInt(ticketPriceStdField.getText()));
            pstmt.setInt(8, Integer.parseInt(ticketPriceSellField.getText()));
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(ticketIdField.getText()),
                Integer.parseInt(ticketScheduleField.getText()),
                Integer.parseInt(ticketCinemaField.getText()),
                Integer.parseInt(ticketSeatField.getText()),
                Integer.parseInt(ticketBookingField.getText()),
                ticketStatusField.isSelected(),
                Integer.parseInt(ticketPriceStdField.getText()),
                Integer.parseInt(ticketPriceSellField.getText())
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTicket() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.ticket WHERE ticket_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, ticketId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTicket() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.ticket SET ticket_schedule = ?, ticket_cinema = ?, ticket_seat = ?, ticket_booking = ?, ticket_status = ?, ticket_price_std = ?, ticket_price_sell = ? WHERE ticket_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(ticketScheduleField.getText()));
                pstmt.setInt(2, Integer.parseInt(ticketCinemaField.getText()));
                pstmt.setInt(3, Integer.parseInt(ticketSeatField.getText()));
                pstmt.setInt(4, Integer.parseInt(ticketBookingField.getText()));
                pstmt.setBoolean(5, ticketStatusField.isSelected());
                pstmt.setInt(6, Integer.parseInt(ticketPriceStdField.getText()));
                pstmt.setInt(7, Integer.parseInt(ticketPriceSellField.getText()));
                pstmt.setInt(8, ticketId);
                pstmt.executeUpdate();
                model.setValueAt(Integer.parseInt(ticketScheduleField.getText()), selectedRow, 1);
                model.setValueAt(Integer.parseInt(ticketCinemaField.getText()), selectedRow, 2);
                model.setValueAt(Integer.parseInt(ticketSeatField.getText()), selectedRow, 3);
                model.setValueAt(Integer.parseInt(ticketBookingField.getText()), selectedRow, 4);
                model.setValueAt(ticketStatusField.isSelected(), selectedRow, 5);
                model.setValueAt(Integer.parseInt(ticketPriceStdField.getText()), selectedRow, 6);
                model.setValueAt(Integer.parseInt(ticketPriceSellField.getText()), selectedRow, 7);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_ticket());
    }
}
