import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class manager_page extends JFrame implements ActionListener {
    private Container container;
    private JButton initTableButton;
    private JButton movie;
    private JButton booking;
    private JButton cinema;
    private JButton schedule;
    private JButton seat;
    private JButton ticket;
    private JButton user;
    private JButton viewTableButton;
    private JTextArea outputArea;

    public manager_page() {
        setTitle("Admin Dashboard");
        setBounds(300, 90, 800, 600);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        initTableButton = new JButton("Initialize Schema");
        initTableButton.setBounds(50, 50, 150, 30);
        initTableButton.addActionListener(this);
        container.add(initTableButton);

        movie = new JButton("Modify Movie");
        movie.setBounds(50, 100, 150, 30);
        movie.addActionListener(this);
        container.add(movie);

        booking = new JButton("Modify Booking");
        booking.setBounds(50, 150, 150, 30);
        booking.addActionListener(this);
        container.add(booking);

        cinema = new JButton("Modify Cinema");
        cinema.setBounds(50, 200, 150, 30);
        cinema.addActionListener(this);
        container.add(cinema);

        schedule = new JButton("Modify Schedule");
        schedule.setBounds(50, 250, 150, 30);
        schedule.addActionListener(this);
        container.add(schedule);

        seat = new JButton("Modify Seat");
        seat.setBounds(50, 300, 150, 30);
        seat.addActionListener(this);
        container.add(seat);

        ticket = new JButton("Modify Ticket");
        ticket.setBounds(50, 350, 150, 30);
        ticket.addActionListener(this);
        container.add(ticket);

        user = new JButton("Modify User");
        user.setBounds(50, 400, 150, 30);
        user.addActionListener(this);
        container.add(user);

        viewTableButton = new JButton("View Table");
        viewTableButton.setBounds(50, 450, 150, 30);
        viewTableButton.addActionListener(this);
        container.add(viewTableButton);

        outputArea = new JTextArea();
        outputArea.setBounds(250, 50, 500, 500);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(250, 50, 500, 500);
        container.add(scrollPane);
       
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == initTableButton) {
            initializeTable();
        } 
        else if (e.getSource() == movie) {
           modify_movie.main();
        }
        else if (e.getSource() == booking) {
            modify_booking.main();
         }
        else if (e.getSource() == cinema) {
            modify_cinema.main();
         }
        else if (e.getSource() == schedule) {
            modify_schedule.main();
         }
        else if (e.getSource() == seat) {
            modify_seat.main();
         }
        else if (e.getSource() == ticket) {
            modify_ticket.main();
         }
        else if (e.getSource() == user) {
            modify_user.main();
         }
        
        else if (e.getSource() == viewTableButton) {
            viewTable();
        }
    }

    private void initializeTable() {
        String result = initialize_schema.main();
        outputArea.append(result);
    }


    
    

    private void viewTable() {
    	String result;
		try {
			result = show_table.main();
			outputArea.append(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        
                new manager_page();
         
    }
}
