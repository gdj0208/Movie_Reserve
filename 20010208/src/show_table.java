import java.io.*;
import java.sql.*;

public class show_table {

    public static String formatResultSet(ResultSet rs) throws Exception {
        StringBuilder sb = new StringBuilder();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // Get column names and their max lengths
        String[] columnNames = new String[columnCount];
        int[] columnWidths = new int[columnCount];

        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = rsmd.getColumnName(i);
            columnWidths[i - 1] = columnNames[i - 1].length();
        }

        // Get data and calculate max widths
        rs.beforeFirst();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                if (value != null) {
                    columnWidths[i - 1] = Math.max(columnWidths[i - 1], value.length());
                }
            }
        }

        // Build column names row
        for (int i = 0; i < columnCount; i++) {
            sb.append(padRight(columnNames[i], columnWidths[i] + 2));
        }
        sb.append("\n");

        // Build separator row
        for (int i = 0; i < columnCount; i++) {
            sb.append(padRight("", columnWidths[i], '-'));
            sb.append("  ");
        }
        sb.append("\n");

        // Build data rows
        rs.beforeFirst();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                sb.append(padRight(value != null ? value : "NULL", columnWidths[i - 1] + 2));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static String padRight(String s, int n, char padChar) {
        StringBuilder padded = new StringBuilder(s);
        while (padded.length() < n) {
            padded.append(padChar);
        }
        return padded.toString();
    }

    public static String main() throws Exception {
        StringBuilder result = new StringBuilder();

        try {
            ResultSet rs;

            rs = connection.stmt.executeQuery("SELECT * FROM db1.user");
            result.append("\nUSER\n").append(formatResultSet(rs)).append("\n");

            rs = connection.stmt.executeQuery("SELECT * FROM db1.movie");
            result.append("\nMOVIE\n").append(formatResultSet(rs)).append("\n");

            rs = connection.stmt.executeQuery("SELECT * FROM db1.cinema");
            result.append("\nCINEMA\n").append(formatResultSet(rs)).append("\n");

            rs = connection.stmt.executeQuery("SELECT * FROM db1.schedule");
            result.append("\nSCHEDULE\n").append(formatResultSet(rs)).append("\n");

            rs = connection.stmt.executeQuery("SELECT * FROM db1.seat");
            result.append("\nSEAT\n").append(formatResultSet(rs)).append("\n");

            rs = connection.stmt.executeQuery("SELECT * FROM db1.ticket");
            result.append("\nTICKET\n").append(formatResultSet(rs)).append("\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving data from the database.";
        }

        return result.toString();
    }
}
