import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

public class modify_booking extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField IdField, paystrategyField, paystatusField, payamountField, userField, dateField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    

    public modify_booking() {
        setTitle("Booking Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "paystrategy", "paystatus", "payamount", "user", "date"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(11, 2));
        IdField = new JTextField();
        paystrategyField = new JTextField();
        paystatusField = new JTextField();
        payamountField = new JTextField();
        userField = new JTextField();
        dateField = new JTextField();
        
        formPanel.add(new JLabel("ID:"));
        formPanel.add(IdField);
        formPanel.add(new JLabel("paystrategy :"));
        formPanel.add(paystrategyField);
        formPanel.add(new JLabel("paystatus : "));
        formPanel.add(paystatusField);
        formPanel.add(new JLabel("payamount : "));
        formPanel.add(payamountField);
        formPanel.add(new JLabel("user"));
        formPanel.add(userField);
        formPanel.add(new JLabel("date:"));
        formPanel.add(dateField);
        
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
            	try {
            		addBooking();
            	} catch (Exception er) {
					// TODO: handle exception
            		er.printStackTrace();
				}
                
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBooking();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		updateBooking();
            	} catch (Exception er) {
					// TODO: handle exception
            		er.printStackTrace();
				}
                
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    IdField.setText(model.getValueAt(selectedRow, 0).toString());
                    paystrategyField.setText(model.getValueAt(selectedRow, 1).toString());
                    paystatusField.setText(model.getValueAt(selectedRow, 2).toString());
                    payamountField.setText(model.getValueAt(selectedRow, 3).toString());
                    userField.setText(model.getValueAt(selectedRow, 4).toString());
                    dateField.setText(model.getValueAt(selectedRow, 5).toString());
                }
            }
        });

       
        setSize(800, 600);
        setVisible(true);

        loadBooking();
    }

    private void loadBooking() {
        try  {
            String query = "SELECT * FROM db1.booking";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("booking_id"),
                    rs.getString("booking_pay_stretegy"),
                    rs.getBoolean("booking_pay_status"),
                    rs.getInt("booking_pay_amount"),
                    rs.getInt("booking_user"),
                    rs.getDate("booking_date"),
            
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBooking() throws ParseException {
        try  {
        	java.sql.Date sqlDate = new java.sql.Date(format.parse(dateField.getText()).getTime());

            String query = "INSERT INTO db1.booking (booking_id, booking_pay_stretegy, booking_pay_status, booking_pay_amount, booking_user,booking_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(IdField.getText()));
            pstmt.setString(2, paystrategyField.getText());
            pstmt.setBoolean(3, Boolean.parseBoolean(paystatusField.getText()));
            pstmt.setInt(4, Integer.parseInt(payamountField.getText()));
            pstmt.setInt(5, Integer.parseInt(userField.getText()));
            pstmt.setDate(6,sqlDate);
         
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(IdField.getText()),
                paystrategyField.getText(),
                Boolean.parseBoolean(paystatusField.getText()),
                Integer.parseInt(payamountField.getText()),
                Integer.parseInt(userField.getText()),
                sqlDate,
                
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("id가 중복됐나 확인해주세요!");
           
        }
    }

    private void deleteBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int Id = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.booking WHERE booking_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Id);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("해당 행을 참조하는 데이터가 있는지 확인해주세요!");
            }
        }
    }

    private void updateBooking() throws ParseException {
    	java.sql.Date sqlDate = new java.sql.Date(format.parse(dateField.getText()).getTime());
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int Id = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.booking SET booking_pay_stretegy = ?, booking_pay_status = ?, booking_pay_amount = ?, booking_user = ?, booking_date = ? WHERE booking_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, paystrategyField.getText());
                pstmt.setBoolean(2, Boolean.parseBoolean(paystatusField.getText()));
                pstmt.setInt(3, Integer.parseInt(payamountField.getText()));
                pstmt.setInt(4, Integer.parseInt(userField.getText()));
                pstmt.setDate(5,sqlDate);
        
                pstmt.setInt(6, Id);
                pstmt.executeUpdate();
                model.setValueAt(paystrategyField.getText(), selectedRow, 1);
                model.setValueAt(Boolean.parseBoolean(paystatusField.getText()), selectedRow, 2);
                model.setValueAt(Integer.parseInt(payamountField.getText()), selectedRow, 3);
                model.setValueAt(Integer.parseInt(userField.getText()), selectedRow, 4);
                model.setValueAt(sqlDate, selectedRow, 5);
                
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("runtume은 Time 형식에 맞게 00:00:00 이어야 합니다!");
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_booking());
    }
}
