import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Seat_Panel extends JFrame {

	private int xpos, ypos, width, height;
	
	private String title, cinema, day;
	private String baseQuery;
	
	private JPanel Top_Panel;
	private JPanel Body_Panel;
	
	
	public Seat_Panel(String title, String cinema, String day) {
		System.out.println("panel out");
		init_data(title, cinema, day);
		init_page();
		add_seat();
		create_page();
	}
	
	
	private void init_data(String title, String cinema, String day) {
		this.title = title;
		this.cinema = cinema;
		this.day = day;
		
		this.xpos = 1100;
		this.ypos = 90;
		this.width = 250;
		this.height = 250;
	}
	
	private void add_seat() {
		baseQuery = "select *\n"
				+ "from db1.seat\n"
				+ "where seat.seat_cinema = " + cinema;
		
		try {
			//System.out.print(baseQuery);
			ResultSet lists = connection.stmt.executeQuery(baseQuery);
			
			while(lists.next()) {
				JButton seat_button = new JButton(lists.getString("seat_num"));
				Body_Panel.add(seat_button);
			}
		} catch (SQLException e1) { e1.printStackTrace(); }
	}
	
	private void init_page() {
        setTitle("Reserve Page");
        setBounds(xpos, ypos, width, height);
        setResizable(false);
        
        set_top_panel();
        set_body_panel();
	}

	private void set_top_panel() {
		Top_Panel = new JPanel();
		Top_Panel.setLayout(new FlowLayout());
		
		JLabel title_label = new JLabel(title);
		JLabel cinema_label = new JLabel(cinema);
		JLabel day_label = new JLabel(day);
		
		Top_Panel.add(title_label);
		Top_Panel.add(cinema_label);
		Top_Panel.add(day_label);
		
		add(Top_Panel);
	}
	
	private void set_body_panel() {
		Body_Panel = new JPanel();
		add(Body_Panel);
	}
	
	private void create_page() {
        setVisible(true);		// 창 보이게 설정 
	}
}
