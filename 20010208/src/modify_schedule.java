import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_schedule extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField scheduleIdField, scheduleMovieField, scheduleCinemaField, scheduleReleaseField, scheduleDayField, scheduleCountField, scheduleGenreField, scheduleStartField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_schedule() {
        setTitle("Schedule Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Movie", "Cinema", "Release Date", "Day", "Count", "Genre", "Start"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        scheduleIdField = new JTextField();
        scheduleMovieField = new JTextField();
        scheduleCinemaField = new JTextField();
        scheduleReleaseField = new JTextField();
        scheduleDayField = new JTextField();
        scheduleCountField = new JTextField();
        scheduleGenreField = new JTextField();
        scheduleStartField = new JTextField();
        formPanel.add(new JLabel("Schedule ID:"));
        formPanel.add(scheduleIdField);
        formPanel.add(new JLabel("Movie ID:"));
        formPanel.add(scheduleMovieField);
        formPanel.add(new JLabel("Cinema ID:"));
        formPanel.add(scheduleCinemaField);
        formPanel.add(new JLabel("Release Date:"));
        formPanel.add(scheduleReleaseField);
        formPanel.add(new JLabel("Day:"));
        formPanel.add(scheduleDayField);
        formPanel.add(new JLabel("Count:"));
        formPanel.add(scheduleCountField);
        formPanel.add(new JLabel("Genre:"));
        formPanel.add(scheduleGenreField);
        formPanel.add(new JLabel("Start Time:"));
        formPanel.add(scheduleStartField);
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
                addSchedule();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSchedule();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSchedule();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    scheduleIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    scheduleMovieField.setText(model.getValueAt(selectedRow, 1).toString());
                    scheduleCinemaField.setText(model.getValueAt(selectedRow, 2).toString());
                    scheduleReleaseField.setText(model.getValueAt(selectedRow, 3).toString());
                    scheduleDayField.setText(model.getValueAt(selectedRow, 4).toString());
                    scheduleCountField.setText(model.getValueAt(selectedRow, 5).toString());
                    scheduleGenreField.setText(model.getValueAt(selectedRow, 6).toString());
                    scheduleStartField.setText(model.getValueAt(selectedRow, 7).toString());
                }
            }
        });

      
        setSize(800, 600);
        setVisible(true);

        loadSchedules();
    }

    private void loadSchedules() {
        try {
            String query = "SELECT * FROM db1.schedule";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("schedule_id"),
                    rs.getInt("schedule_movie"),
                    rs.getInt("schedule_cinema"),
                    rs.getDate("schedule_release"),
                    rs.getString("schedule_day"),
                    rs.getInt("schedule_count"),
                    rs.getString("schedule_genre"),
                    rs.getInt("schedule_start")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSchedule() {
        try {
            String query = "INSERT INTO db1.schedule (schedule_id, schedule_movie, schedule_cinema, schedule_release, schedule_day, schedule_count, schedule_genre, schedule_start) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(scheduleIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(scheduleMovieField.getText()));
            pstmt.setInt(3, Integer.parseInt(scheduleCinemaField.getText()));
            pstmt.setDate(4, Date.valueOf(scheduleReleaseField.getText()));
            pstmt.setString(5, scheduleDayField.getText());
            pstmt.setInt(6, Integer.parseInt(scheduleCountField.getText()));
            pstmt.setString(7, scheduleGenreField.getText());
            pstmt.setInt(8, Integer.parseInt(scheduleStartField.getText()));
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(scheduleIdField.getText()),
                Integer.parseInt(scheduleMovieField.getText()),
                Integer.parseInt(scheduleCinemaField.getText()),
                Date.valueOf(scheduleReleaseField.getText()),
                scheduleDayField.getText(),
                Integer.parseInt(scheduleCountField.getText()),
                scheduleGenreField.getText(),
                Integer.parseInt(scheduleStartField.getText())
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSchedule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int scheduleId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.schedule WHERE schedule_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, scheduleId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSchedule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int scheduleId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.schedule SET schedule_movie = ?, schedule_cinema = ?, schedule_release = ?, schedule_day = ?, schedule_count = ?, schedule_genre = ?, schedule_start = ? WHERE schedule_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(scheduleMovieField.getText()));
                pstmt.setInt(2, Integer.parseInt(scheduleCinemaField.getText()));
                pstmt.setDate(3, Date.valueOf(scheduleReleaseField.getText()));
                pstmt.setString(4, scheduleDayField.getText());
                pstmt.setInt(5, Integer.parseInt(scheduleCountField.getText()));
                pstmt.setString(6, scheduleGenreField.getText());
                pstmt.setInt(7, Integer.parseInt(scheduleStartField.getText()));
                pstmt.setInt(8, scheduleId);
                pstmt.executeUpdate();
                model.setValueAt(Integer.parseInt(scheduleMovieField.getText()), selectedRow, 1);
                model.setValueAt(Integer.parseInt(scheduleCinemaField.getText()), selectedRow, 2);
                model.setValueAt(Date.valueOf(scheduleReleaseField.getText()), selectedRow, 3);
                model.setValueAt(scheduleDayField.getText(), selectedRow, 4);
                model.setValueAt(Integer.parseInt(scheduleCountField.getText()), selectedRow, 5);
                model.setValueAt(scheduleGenreField.getText(), selectedRow, 6);
                model.setValueAt(Integer.parseInt(scheduleStartField.getText()), selectedRow, 7);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_schedule());
    }
}
