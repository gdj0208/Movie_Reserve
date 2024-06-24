import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ticket_page {

    private user_page UserPage;
    public JPanel OuterPanel; // 부모 패널
    public JPanel MainPanel; // 이 패널의 메인 페널
    private JFrame popUp;
    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet resultSet = null;

    List<Ticket_Data> datalist = new ArrayList<>();

    // 기본 페이지
    public Ticket_page(user_page UserPage, JPanel OuterPanel) {

        this.UserPage = UserPage;
        this.OuterPanel = OuterPanel;

        // 메인 패널
        MainPanel = new JPanel();
        MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
        MainPanel.setBackground(Color.black);

        // 유저의 티켓들을 보여주는 함수
        Show_My_Ticket_List();

        // 부모 패널에 메인 패널 넣기
        OuterPanel.revalidate();
        OuterPanel.repaint();
    }

    // 유저의 티켓들을 보여주는 함수 (수정)
    public void Show_My_Ticket_List() {
        int cnt = 0;

        // 유저의 티켓 정보들을 보여주는 쿼리문 (수정됨) 
        String baseQuery =
                "SELECT db1.ticket.ticket_id, db1.movie.movie_name, db1.schedule.schedule_day, db1.ticket.ticket_cinema, db1.ticket.ticket_seat, db1.ticket.ticket_price_std, db1.ticket.ticket_booking " +
                        "FROM db1.ticket " +
                        "INNER JOIN db1.schedule ON db1.ticket.ticket_schedule = db1.schedule.schedule_id " +
                        "INNER JOIN db1.movie ON db1.movie.movie_id = db1.schedule.schedule_movie " +
                        "INNER JOIN db1.booking ON db1.booking.booking_id = db1.ticket.ticket_booking " +
                        "where db1.booking.booking_user = " + login_page.user;

        // DB에 연결하기
        try {
            // 티켓 정보들 도출
            resultSet = stmt.executeQuery(baseQuery);

            OuterPanel.remove(MainPanel);
            MainPanel.removeAll();

            // 영화 티켓 정보들 읽기
            while (resultSet.next()) {
                cnt += 1;
                int ticket_id = resultSet.getInt("ticket_id");
                String movie_name = resultSet.getString("movie_name");
                String schedule_day = resultSet.getString("schedule_day");
                int schedule_cinema = resultSet.getInt("ticket_cinema");
                int seat_id = resultSet.getInt("ticket_seat");
                int ticket_price = resultSet.getInt("ticket_price_std");
                int booking_num = resultSet.getInt("ticket_booking");		// (수정됨) 

                // 영화 정보들을 보여주는 패널
                JPanel moviePanel = new JPanel();
                moviePanel.setLayout(new BorderLayout());
                moviePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                moviePanel.setBackground(Color.white);

                // 좌측에 영화 정보를 보여주는 패널
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.white);
                infoPanel.add(new JLabel("영화명 : " + movie_name));
                infoPanel.add(new JLabel("상영일 : " + schedule_day));
                infoPanel.add(new JLabel("상영관 : " + schedule_cinema));
                infoPanel.add(new JLabel("좌석번호 : " + seat_id));
                infoPanel.add(new JLabel("가격 : " + ticket_price));
                infoPanel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                    	
                    	// 수정 대상 
                        Show_Schedule_Info(ticket_id);
                    }
                });

                // 우측에 버튼 패널
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                buttonPanel.setBackground(Color.white);
                JButton cancelButton = new JButton("Cancel");
                JButton changeMovieButton = new JButton("Change Movie");
                JButton changeScheduleButton = new JButton("Change Schedule");

                cancelButton.putClientProperty("ticket_id", ticket_id);
                changeMovieButton.putClientProperty("ticket_id", ticket_id);
                changeScheduleButton.putClientProperty("ticket_id", ticket_id);
                
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Cancle_Schedule((int) cancelButton.getClientProperty("ticket_id"));
                    }
                });
                changeMovieButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        change_Movie((int) changeMovieButton.getClientProperty("ticket_id"));
                    }
                });
                changeScheduleButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Adjust_Schedule((int) changeScheduleButton.getClientProperty("ticket_id"));
                    }
                });

                buttonPanel.add(cancelButton);
                buttonPanel.add(changeMovieButton);
                buttonPanel.add(changeScheduleButton);

                moviePanel.add(infoPanel, BorderLayout.CENTER);
                moviePanel.add(buttonPanel, BorderLayout.EAST);

                MainPanel.add(moviePanel);
            }

            OuterPanel.add(MainPanel);
            OuterPanel.revalidate();
            OuterPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 상영 정보 보여주기 (수정됨)
    public void Show_Schedule_Info(int ticket_id) {
    	//상영일정(schedule_day), 상영관(ticket_cinema), 티켓(ticket_seat, ticket_price_std, ticket_price_sell, ticket_status, ticket_booking)
    	
    	JPanel newPanel;
    	JPanel moviePanel = null;

        String baseQuery = "select db1.schedule.schedule_day, db1.ticket.ticket_cinema, db1.ticket.ticket_seat, db1.ticket.ticket_price_std, \n"
        		+ "db1.ticket.ticket_price_sell, db1.ticket.ticket_status, db1.ticket.ticket_booking \n"
        		+ "from db1.ticket \n"
        		+ "inner join db1.schedule on db1.schedule.schedule_id = db1.ticket.ticket_schedule \n"
        		+ "WHERE db1.ticket.ticket_id = " + Integer.toString(ticket_id);
        
        popUp = new JFrame("Schedule Info");
        popUp.setSize(200, 250);

        // 새 패널 생성
        newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));

        try {
            // 티켓 정보들 도출
            resultSet = stmt.executeQuery(baseQuery);
            while (resultSet.next()) {
            	moviePanel = new JPanel();
            	moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
            	
                JLabel day = new JLabel("상영일 : " + resultSet.getString("schedule_day"));
                JLabel cinema = new JLabel("영화관 : " + resultSet.getInt("ticket_cinema"));
                JLabel seat = new JLabel("좌석 : " + resultSet.getInt("ticket_seat"));
                JLabel user = new JLabel("사용자 : " + login_page.user);
                JLabel stdPrice = new JLabel("가격 : " + resultSet.getInt("ticket_price_std"));
                JLabel sellPrice = new JLabel("판매가 : " + resultSet.getInt("ticket_price_sell"));
                JLabel status = new JLabel("지불여불 : " + resultSet.getBoolean("ticket_status"));
                JLabel booking = new JLabel("예약상태 : " + resultSet.getBoolean("ticket_booking"));
                
                moviePanel.add(day);
                moviePanel.add(cinema);
                moviePanel.add(seat);
                moviePanel.add(user);
                moviePanel.add(stdPrice);
                moviePanel.add(sellPrice);
                moviePanel.add(status);
                moviePanel.add(booking);
            }
            
            newPanel.add(moviePanel);
        }
        catch (SQLException ex) { ex.printStackTrace(); }

        popUp.add(newPanel);
        popUp.setVisible(true);
    }

    // 영화 예매 취소 (수정됨 )
    public void Cancle_Schedule(int ticket_id) { 
        String ticketQuery = "DELETE FROM db1.ticket WHERE db1.ticket.ticket_id = " + ticket_id;
        String bookingQuery = "DELETE FROM db1.booking WHERE db1.booking.booking_id = " + Find_Booking_id(ticket_id);
        String seatQuery = "UPDATE db1.seat \n"
        		+ "SET db1.seat.seat_status = 1 \n"
        		+ "WHERE db1.seat.seat_id =  \n"
        		+ "(SELECT db1.ticket.ticket_seat FROM db1.ticket WHERE db1.ticket.ticket_id = " + ticket_id + ");";

        try {
            stmt.executeUpdate(seatQuery);
            stmt.executeUpdate(ticketQuery);
            stmt.executeUpdate(bookingQuery);
            Show_My_Ticket_List();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int Find_Booking_id(int ticket_id) {
        int booking_id = -1;

        String baseQuery = "SELECT db1.booking.booking_id " +
                "FROM db1.ticket " +
                "INNER JOIN db1.booking ON db1.booking.booking_id = db1.ticket.ticket_booking " +
                "WHERE db1.ticket.ticket_id = " + ticket_id;
        
        try {
            ResultSet tmp = stmt.executeQuery(baseQuery);
            while (tmp.next()) {
                booking_id = tmp.getInt("booking_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return booking_id;
    }

    // 영화 바꾸기 (수정)
    public void change_Movie(int ticket_id) {
        UserPage.Reserve_Movie_Button();
        Cancle_Schedule(ticket_id);
    }

    // 영화 스케줄 바꾸기
    public void Adjust_Schedule(int ticket_id) {
        UserPage.Reserve_Movie_Button();

        String findingQuery = "SELECT db1.movie.movie_id, db1.movie.movie_name, db1.schedule.schedule_cinema, db1.schedule.schedule_day, db1.schedule.schedule_id " +
                "FROM db1.movie " +
                "INNER JOIN db1.schedule ON db1.schedule.schedule_movie = db1.movie.movie_id " +
                "INNER JOIN db1.ticket ON db1.ticket.ticket_schedule = db1.movie.movie_id " +
                "WHERE db1.ticket.ticket_id = " + ticket_id;
        String seatQuery = "UPDATE db1.seat \n"
        		+ "SET db1.seat.seat_status = 1 \n"
        		+ "WHERE db1.seat.seat_id =  \n"
        		+ "(SELECT db1.ticket.ticket_seat FROM db1.ticket WHERE db1.ticket.ticket_id = " + ticket_id + ");";

        try {
            resultSet = stmt.executeQuery(findingQuery);
            UserPage.ResetListPanel(resultSet);
            stmt.executeUpdate(seatQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cancle_Schedule(ticket_id);
    }
}
