
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowingPanel {
	public int booking_id;
	public int ticket_id;
	public int movie_id;
	public int cinema_num;
	public int seat_id;
	public boolean seat_status;
	public int schedule_id;
	public int payment;

    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet resultSet = null;
    
	public JPanel OuterPanel;
	public JPanel MainPanel;
	public JPanel SeatPanel;
	private JFrame popUp;
	
	private String changeQueryFront;
	private String changeQueryBack;

    List<SeatData> datalist = new ArrayList<>();
    List<JButton> buttonlist = new ArrayList<>();
	
	public ShowingPanel(JPanel OuterPanel, Dimension panelSize) {
		this.OuterPanel = OuterPanel;
		
		MainPanel = new JPanel();
		
		MainPanel.setSize(OuterPanel.WIDTH / 3, (int)(OuterPanel.HEIGHT * 0.8));
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
		MainPanel.setPreferredSize(panelSize);
		//MainPanel.setBackground(Color.blue);
        
		MainPanel.removeAll();
		MainPanel.add(new JLabel("좌석 목록"));
		//MainPanel.add(new JButton("좌석 예매"));
        MainPanel.add(SeatPanel = new JPanel());
        
		OuterPanel.add(MainPanel);
		OuterPanel.revalidate();
		OuterPanel.repaint();
	}
	
	public void Reset_Seat_Info(String cinema_num, int movie_id, int schedule_id) {
		this.movie_id = movie_id;
		this.cinema_num = Integer.parseInt(cinema_num);
		this.schedule_id = schedule_id;
		
		SeatPanel.removeAll();
		SeatPanel = new JPanel(new GridLayout(10, 10));
		
		String baseQuery = "select * from db1.seat where db1.seat.seat_cinema = " + Integer.parseInt(cinema_num);
		changeQueryFront = "UPDATE db1.seat set db1.seat.seat_status = ";
		changeQueryBack = "where db1.seat.seat_cinema = " + Integer.parseInt(cinema_num) + " and db1.seat.seat_id = ";

    	try {
    		//Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
    		//Statement statement = connection.createStatement();
        		
    		resultSet = stmt.executeQuery(baseQuery);
    		while (resultSet.next()) {
    			JButton newButton = new JButton();
    			
            	int seat_id = resultSet.getInt("seat_id");
            	boolean seat_status = resultSet.getBoolean("seat_status");
            	
            	newButton.setText(Integer.toString(seat_id));
            	if (!seat_status) {
            		newButton.setBackground(Color.gray);
            	}
           
            	newButton.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
            			String Query;
            			
            			// 좌석 예약 
            			try
            			{ 
            				
                	    	
                			if(seat_status) {
                				// 예약되지 않은 좌석 
                				
                				Query = changeQueryFront + "FALSE " + changeQueryBack + Integer.toString(seat_id);
                				make_reserve();
                				reserve_ticket(seat_id);
                				newButton.setBackground(Color.gray);
                    			stmt.executeUpdate(Query); 
                    			newButton.setEnabled(false);
                    			showInfo(true);
                    		}
                			else {
                				showInfo(false);
                			}
                			
                		}
            			catch(SQLException ex) { ex.printStackTrace(); }
            		}
            	} );
            	
            	datalist.add(new SeatData(seat_id, seat_status));
          	
            	buttonlist.add(newButton);
            	
            	SeatPanel.add(newButton);
            }
    		MainPanel.add(SeatPanel);
    		OuterPanel.revalidate(); // Revalidate to refresh the layout
    		OuterPanel.repaint();    // Repaint to update the display
            
        } 
    	catch (SQLException ex) { ex.printStackTrace(); }
	}
	
	public void showInfo(boolean reserved) {
		JLabel label;
		JPanel newPanel;
		
		popUp = new JFrame("New Window");
		popUp.setSize(300, 150);
		
		// 새 패널 생성
        newPanel = new JPanel();
		if(reserved) { 
			label = new JLabel("Successfully reserved!!!"); 
		}
		else { label = new JLabel("seat is full!!!"); }
        newPanel.add(label);
        
        // 새 패널을 새 프레임에 추가
        popUp.add(newPanel);
        
        // 새 프레임을 화면에 표시
        popUp.setVisible(true);
	}

	public void reserve_ticket(int seat_id) {
		// 티켓 id, schedule_id, cinema_id( cinema_num ), seat_id ( seat_id ), 
		// booking id, ticket_status, ticket_price_std, ticket_price_sell
		
		String baseQuery = "SELECT MAX(ticket_id) AS max FROM db1.ticket";
		try {
	
			ResultSet resultSet = stmt.executeQuery(baseQuery);
			
			if(resultSet.next()) {
			    ticket_id = resultSet.getInt("max")+1;
			}
		}
    	catch (SQLException ex) { ex.printStackTrace(); }
		
		baseQuery
		= "INSERT \n"
		+ "INTO db1.ticket \n"
		+ "VALUES(" + ticket_id + ", " + schedule_id + ", " + cinema_num + ", " + seat_id
		+ ", " + booking_id + ", " + true + ", " + payment + ", " + payment + ")";
		System.out.println(baseQuery);
		try {
		
    		stmt.executeUpdate(baseQuery);
		}
    	catch (SQLException ex) { ex.printStackTrace(); }
	}
	
	public void make_reserve() {
		ResultSet resultSet;
		
		String how = "Credit Card";
		int payStatus = 1;
		payment = 25000;
		int user = login_page.user;
		String day = "2024-05-15";
		
		String baseQuery = "SELECT MAX(booking_id) AS max FROM db1.booking";
		try {
	
			resultSet = stmt.executeQuery(baseQuery);
			
			if(resultSet.next()) {
			    booking_id = resultSet.getInt("max")+1;
			}
		}
    	catch (SQLException ex) { ex.printStackTrace(); }

		try {
		
    		baseQuery
   		 	= "INSERT \n"
   			+ "INTO db1.booking \n"
   			+ "VALUES("
   			+ booking_id +",'Credit Card', " + payStatus + ", " + Integer.toString(payment) + "," +user+", '2024-05-15');";
    		stmt.executeUpdate(baseQuery);
    		
    		// 티켓 예약 만들기
		}
		catch(SQLException ex) { ex.printStackTrace(); }
	}
}
