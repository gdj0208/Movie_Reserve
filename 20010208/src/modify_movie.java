import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class modify_movie extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField movieIdField, movieNameField, movieRuntimeField, movieRankField, movieDirectorField, movieActorField, movieGenreField, movieIntroduceField, movieReleaseDateField, movieGradeField;
    private JButton addButton, deleteButton, updateButton;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet rs = null;

    public modify_movie() {
        setTitle("Movie Manager");
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Runtime", "Rank", "Director", "Actor", "Genre", "Introduce", "Release Date", "Grade"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form setup
        JPanel formPanel = new JPanel(new GridLayout(11, 2));
        movieIdField = new JTextField();
        movieNameField = new JTextField();
        movieRuntimeField = new JTextField();
        movieRankField = new JTextField();
        movieDirectorField = new JTextField();
        movieActorField = new JTextField();
        movieGenreField = new JTextField();
        movieIntroduceField = new JTextField();
        movieReleaseDateField = new JTextField();
        movieGradeField = new JTextField();
        formPanel.add(new JLabel("Movie ID:"));
        formPanel.add(movieIdField);
        formPanel.add(new JLabel("Movie Name:"));
        formPanel.add(movieNameField);
        formPanel.add(new JLabel("Movie Runtime:"));
        formPanel.add(movieRuntimeField);
        formPanel.add(new JLabel("Movie Rank:"));
        formPanel.add(movieRankField);
        formPanel.add(new JLabel("Movie Director:"));
        formPanel.add(movieDirectorField);
        formPanel.add(new JLabel("Movie Actor:"));
        formPanel.add(movieActorField);
        formPanel.add(new JLabel("Movie Genre:"));
        formPanel.add(movieGenreField);
        formPanel.add(new JLabel("Movie Introduce:"));
        formPanel.add(movieIntroduceField);
        formPanel.add(new JLabel("Movie Release Date:"));
        formPanel.add(movieReleaseDateField);
        formPanel.add(new JLabel("Movie Grade:"));
        formPanel.add(movieGradeField);
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
                addMovie();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMovie();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMovie();
            }
        });

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    movieIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    movieNameField.setText(model.getValueAt(selectedRow, 1).toString());
                    movieRuntimeField.setText(model.getValueAt(selectedRow, 2).toString());
                    movieRankField.setText(model.getValueAt(selectedRow, 3).toString());
                    movieDirectorField.setText(model.getValueAt(selectedRow, 4).toString());
                    movieActorField.setText(model.getValueAt(selectedRow, 5).toString());
                    movieGenreField.setText(model.getValueAt(selectedRow, 6).toString());
                    movieIntroduceField.setText(model.getValueAt(selectedRow, 7).toString());
                    movieReleaseDateField.setText(model.getValueAt(selectedRow, 8).toString());
                    movieGradeField.setText(model.getValueAt(selectedRow, 9).toString());
                }
            }
        });

        
        setSize(800, 600);
        setVisible(true);

        loadMovies();
    }

    private void loadMovies() {
        try {
            String query = "SELECT * FROM db1.movie";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("movie_id"),
                    rs.getString("movie_name"),
                    rs.getTime("movie_runtime"),
                    rs.getInt("movie_rank"),
                    rs.getString("movie_director"),
                    rs.getString("movie_actor"),
                    rs.getString("movie_genre"),
                    rs.getString("movie_introduce"),
                    rs.getDate("movie_release_date"),
                    rs.getInt("movie_grade")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMovie() {
        try {
            String query = "INSERT INTO db1.movie (movie_id, movie_name, movie_runtime, movie_rank, movie_director, movie_actor, movie_genre, movie_introduce, movie_release_date, movie_grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(movieIdField.getText()));
            pstmt.setString(2, movieNameField.getText());
            pstmt.setTime(3, Time.valueOf(movieRuntimeField.getText()));
            pstmt.setInt(4, Integer.parseInt(movieRankField.getText()));
            pstmt.setString(5, movieDirectorField.getText());
            pstmt.setString(6, movieActorField.getText());
            pstmt.setString(7, movieGenreField.getText());
            pstmt.setString(8, movieIntroduceField.getText());
            pstmt.setDate(9, Date.valueOf(movieReleaseDateField.getText()));
            pstmt.setInt(10, Integer.parseInt(movieGradeField.getText()));
            pstmt.executeUpdate();
            model.addRow(new Object[]{
                Integer.parseInt(movieIdField.getText()),
                movieNameField.getText(),
                Time.valueOf(movieRuntimeField.getText()),
                Integer.parseInt(movieRankField.getText()),
                movieDirectorField.getText(),
                movieActorField.getText(),
                movieGenreField.getText(),
                movieIntroduceField.getText(),
                Date.valueOf(movieReleaseDateField.getText()),
                Integer.parseInt(movieGradeField.getText())
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ID가 중복됐는지 확인해주세요!");
            System.out.println("Runtime은 Time 형식에 맞게 00:00:00 이어야 합니다!");
            System.out.println("Release Date는 YYYY-MM-DD 형식에 맞게 입력해야 합니다!");
        }
    }

    private void deleteMovie() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int movieId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM db1.movie WHERE movie_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, movieId);
                pstmt.executeUpdate();
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("해당 행을 참조하는 데이터가 있는지 확인해주세요!");
            }
        }
    }

    private void updateMovie() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int movieId = (int) model.getValueAt(selectedRow, 0);
            try {
                String query = "UPDATE db1.movie SET movie_name = ?, movie_runtime = ?, movie_rank = ?, movie_director = ?, movie_actor = ?, movie_genre = ?, movie_introduce = ?, movie_release_date = ?, movie_grade = ? WHERE movie_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, movieNameField.getText());
                pstmt.setTime(2, Time.valueOf(movieRuntimeField.getText()));
                pstmt.setInt(3, Integer.parseInt(movieRankField.getText()));
                pstmt.setString(4, movieDirectorField.getText());
                pstmt.setString(5, movieActorField.getText());
                pstmt.setString(6, movieGenreField.getText());
                pstmt.setString(7, movieIntroduceField.getText());
                pstmt.setDate(8, Date.valueOf(movieReleaseDateField.getText()));
                pstmt.setInt(9, Integer.parseInt(movieGradeField.getText()));
                pstmt.setInt(10, movieId);
                pstmt.executeUpdate();
                model.setValueAt(movieNameField.getText(), selectedRow, 1);
                model.setValueAt(Time.valueOf(movieRuntimeField.getText()), selectedRow, 2);
                model.setValueAt(Integer.parseInt(movieRankField.getText()), selectedRow, 3);
                model.setValueAt(movieDirectorField.getText(), selectedRow, 4);
                model.setValueAt(movieActorField.getText(), selectedRow, 5);
                model.setValueAt(movieGenreField.getText(), selectedRow, 6);
                model.setValueAt(movieIntroduceField.getText(), selectedRow, 7);
                model.setValueAt(Date.valueOf(movieReleaseDateField.getText()), selectedRow, 8);
                model.setValueAt(Integer.parseInt(movieGradeField.getText()), selectedRow, 9);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Runtime은 Time 형식에 맞게 00:00:00 이어야 합니다!");
                System.out.println("Release Date는 YYYY-MM-DD 형식에 맞게 입력해야 합니다!");
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new modify_movie());
    }
}
