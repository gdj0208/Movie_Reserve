
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class user_page extends JFrame implements ActionListener {
	
	private Container container;
	private JPanel MainFrame;
	private JPanel TopPanel;
    
	private JButton Reserve_Movie_Button;
    private JButton Check_Tickets_Button;

    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet resultSet = null;
    
    public JPanel MainSearchPanel;
    private JPanel SearchPanel;				// 좌측 검색창
    private JPanel MiniSearchPanel;
    private JPanel ListPanel;				// 중앙 조회
    ShowingPanel SeatPanel;				// 우측 좌석창 
    
    Ticket_page newPage;
    
    private JLabel SearchLabel;
    private JButton searchButton = new JButton();
    private JTextField[] searchText = new JTextField[4];
    private JLabel[] serachLabel = new JLabel[4];
    
    private String movie, director, actors, genre;
    
    private JLabel users;
    private JLabel date;
    // Database connection details
 
    
    private int width = 800;
    private int height = 600;
    
    private boolean showingMainSearch = false;
    
    List<Schedule_data> datalist = new ArrayList<>();

    public user_page() {
    	users = new JLabel(login_page.user_N);
    	date = new JLabel("2024-06-07");
    	getContentPane().add(users);
    	getContentPane().add(date);
    	// 창 만들기 
    	init_window();
        
        // top 패널 생성 
        init_top_panel();
        
        // 창 보이게 설정 
        setVisible(true);
    }

    // 액션 목록 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.getSource());
		if (e.getSource() == Reserve_Movie_Button) { Reserve_Movie_Button(); } 
		else if(e.getSource() == Check_Tickets_Button) { Check_Tickets_Button(); }
		//else if(e.getSource() == searchButton) { searchButton();  System.out.println("c3"); }
		else { System.out.println("Unknown source: " + e.getSource()); }
	}
	
	// ===== [[ 유저 대시 보드 초기화 ]] =================================================================== 
	private void init_window() {
        setTitle("User Dashboard");
        setBounds(300, 90, width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // 유저보드에 있는 메인 컨테이너
        // top_panel, main_seach_panel로 구성 
        container = getContentPane();
        container.setLayout(new FlowLayout());
        
        MainFrame = new JPanel();
        MainFrame.setLayout(new BoxLayout(MainFrame, BoxLayout.Y_AXIS));
        
        container.add(MainFrame);
	}
	
	
	
	// ----- [ 상단 패널 생성 (영화 검색 버튼 / 조회 버튼) ] ------------------------------------------------
    private void init_top_panel() {
    	TopPanel = new JPanel();
    	
    	// 맨 위 좌측 버튼 
        Reserve_Movie_Button = new JButton("Reserve Movie");
        Reserve_Movie_Button.addActionListener(this);

        // 맨 위 오른쪽 버튼 
        Check_Tickets_Button = new JButton("My Bookings");
        Check_Tickets_Button.addActionListener(this);
        
        TopPanel.add(Reserve_Movie_Button);
        TopPanel.add(Check_Tickets_Button);
        
        MainFrame.add(TopPanel);
        
    }
    
    
    
    // ----- [[ 중앙 창 ]] ----------------------------------------------------------------------
    private void init_main_search_panel() {
    	// main_search_panel
    	MainSearchPanel = new JPanel(new FlowLayout());
    	MainSearchPanel.setSize((int)(width * 0.9), (int)(height * 0.9));
    	
    	// main_search_paeel 안에 있는 panel들의 크기 
        Dimension panelSize = new Dimension(MainSearchPanel.getWidth() / 3, (int)(MainSearchPanel.getHeight() * 0.8));

        MainSearchPanel.setBackground(Color.white);

        // main_search_panel 내의 패널들 
        init_search_panel(panelSize);
        init_list_panel(panelSize);
        init_seat_panel(panelSize);

        MainFrame.add(MainSearchPanel);
        MainFrame.revalidate(); // Revalidate to refresh the layout
        MainFrame.repaint();    // Repaint to update the display
    }
    
    
    // 좌측 검색창 띄우기 
	private void init_search_panel(Dimension panelSize) {
		// SearchPanel 초기화
		SearchPanel = new JPanel(); // 먼저 JPanel을 초기화
		SearchPanel.setLayout(new BoxLayout(SearchPanel, BoxLayout.Y_AXIS)); // 그 후에 BoxLayout 설정

		// 패널 크기 설정
		SearchPanel.setSize(MainSearchPanel.WIDTH / 3, (int)(MainSearchPanel.HEIGHT * 0.8));
		SearchPanel.setPreferredSize(panelSize);

		// 검색 버튼 텍스트 설정
		searchButton.setText("검색");

		// 라벨과 미니 서치 패널 초기화
		SearchLabel = new JLabel("영화조회");
		MiniSearchPanel = new JPanel(new GridLayout(4, 2));

		// 검색 버튼 및 라벨 초기화
		init_search_buttons_and_labels();
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { searchButton();  }
		});
		
		// 컴포넌트 추가
		SearchPanel.add(SearchLabel);
		SearchPanel.add(MiniSearchPanel);
		SearchPanel.add(searchButton);

		// 메인 패널에 서치 패널 추가
		MainSearchPanel.add(SearchPanel);
	}

	
	// 좌측 검색창 검색 목록 띄우기 
	private void init_search_buttons_and_labels() {
		serachLabel[0] = new JLabel("영화명");
		serachLabel[1] = new JLabel("감독명");
		serachLabel[2] = new JLabel("배우명");
		serachLabel[3] = new JLabel("장르");
		
		searchText[0] = new JTextField();
		searchText[1] = new JTextField();
		searchText[2] = new JTextField();
		searchText[3] = new JTextField();
		
		MiniSearchPanel.add(serachLabel[0]);
		MiniSearchPanel.add(searchText[0]);
		MiniSearchPanel.add(serachLabel[1]);
		MiniSearchPanel.add(searchText[1]);
		MiniSearchPanel.add(serachLabel[2]);
		MiniSearchPanel.add(searchText[2]);
		MiniSearchPanel.add(serachLabel[3]);
		MiniSearchPanel.add(searchText[3]);
	}
	
	
	// 중앙 영화 목록 띄우기 
	private void init_list_panel(Dimension panelSize) {
    	ListPanel = new JPanel();
    	ListPanel.setLayout(new BoxLayout(ListPanel, BoxLayout.Y_AXIS));
    	
    	ListPanel.setSize(MainSearchPanel.WIDTH / 3, (int)(MainSearchPanel.HEIGHT * 0.8));
        ListPanel.setPreferredSize(panelSize);
        //ListPanel.setBackground(Color.gray);'

        ListPanel.add(new JLabel("영화 목록"));
        
        MainSearchPanel.add(ListPanel);
        MainFrame.revalidate(); // Revalidate to refresh the layout
        MainFrame.repaint();    // Repaint to update the display
	}
	
	
	// 우측 좌석 패널 띄우기 
	private void init_seat_panel(Dimension panelSize) {
    	SeatPanel = new ShowingPanel(MainSearchPanel, panelSize);
	}
	
	
    // ----- [[ 중앙 창 ]] ----------------------------------------------------------------------
	
	
	
	// ====== [[ 버튼들 액션 목록 ]] ===================================================================
	
	// 상단 좌측 영화 검색하기 버튼 (수정됨)
    public void Reserve_Movie_Button() {
		MainFrame.removeAll();
		MainFrame.add(TopPanel);
    	if(showingMainSearch == false) {
    		MainFrame.revalidate();
    		MainFrame.repaint();
    		
        	init_main_search_panel();
        	showingMainSearch = true;
    	}
    	/*
    	 */
    }
    
    // 검색항 영화들의 목록 보여주기 (수정됨)
    public void Check_Tickets_Button() {
    	//System.out.print(showingMainSearch);
		MainFrame.removeAll();
		MainFrame.add(TopPanel);
    	if(showingMainSearch == true) {
    		MainFrame.revalidate();
    		MainFrame.repaint();
    		showingMainSearch = false;
    	}

		newPage = new Ticket_page(this, MainFrame);
    }
    
    // 검색 버튼을 클릭했을 시 텍스트창에서 데이터 불러오
    private void searchButton() {
    	movie = searchText[0].getText();
    	director = searchText[1].getText();
    	actors = searchText[2].getText();
    	genre = searchText[3].getText();
    	
    	
    	String baseQuery 
    	= "SELECT db1.movie.movie_id, db1.movie.movie_name, db1.schedule.schedule_cinema, db1.schedule.schedule_day, db1.schedule.schedule_id \n"
    	+ "FROM db1.schedule \n"
    	+ "inner join db1.movie ON db1.schedule.schedule_movie = db1.movie.movie_id \n";
    	
    	try {

        	boolean condition = false;
        	
    		// 쿼리문 작성 
            if(!movie.isEmpty() || !director.isEmpty() || !actors.isEmpty() || !genre.isEmpty()) {
            	baseQuery = baseQuery + "WHERE ";
            	if(!movie.isBlank()) { 
            		baseQuery = baseQuery + "db1.movie.movie_name = \"" + movie + "\"";
            		condition = true; }
            	if(!director.isBlank()) {
            		if(condition == true) { baseQuery = baseQuery + " and "; }
            		baseQuery += "db1.movie.movie_director like \"%" + director + "%\"";
            		condition = true;
            	}
            	if(!actors.isBlank()) {
            		if(condition == true) { baseQuery = baseQuery + " and "; }
            		baseQuery += "db1.movie.movie_actor like \"%" + actors + "%\"";
            		condition = true;
            	}
            	if(!genre.isBlank()) {
            		if(condition == true) { baseQuery = baseQuery + " and "; }
            		baseQuery += "db1.movie.movie_genre like \"%" + genre + "%\"";
            		condition = true;
            	}
            }
            
            // 쿼리문 실행 
            //stmt = conn.createStatement();
            resultSet = stmt.executeQuery(baseQuery);
            datalist.clear();

            ResetListPanel(resultSet);

           // MainFrame.revalidate(); // Revalidate to refresh the layout
           // MainFrame.repaint();    // Repaint to update the display
            
        } 
    	catch (SQLException ex) { ex.printStackTrace(); }
    }
    
    public void ResetListPanel(ResultSet resultSet) throws SQLException {

    	ListPanel.removeAll();
    	ListPanel.add(new JLabel("영화 목록"));
        
    	// 쿼리문 결과 
        while (resultSet.next()) {
        	int movie_id = resultSet.getInt("movie_id");
        	String movie_name = resultSet.getString("movie_name");
        	String cinema = resultSet.getString("schedule_cinema");
        	String day = resultSet.getString("schedule_day");
        	int schedule_id = resultSet.getInt("schedule_id");
        
        	JPanel newpanel = new JPanel();
    		newpanel.setLayout(new BoxLayout(newpanel, BoxLayout.Y_AXIS));
    		newpanel.setBackground(Color.white);
    		
    		newpanel.add( new JLabel("영화명 : " + movie_name) );
    		newpanel.add( new JLabel("상영일 : " + day) );
    		newpanel.add( new JLabel("상영관 : " + cinema) );
    		newpanel.add( new JLabel("\n") );
    		
    		newpanel.addMouseListener(new MouseAdapter() { 
    			public void mouseClicked(MouseEvent e) { 
    				SeatPanel.Reset_Seat_Info(cinema, movie_id, schedule_id);
    			}
    		});
    		
    		ListPanel.add(newpanel);
        }
        

        MainFrame.revalidate(); // Revalidate to refresh the layout
        MainFrame.repaint();    // Repaint to update the display
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new user_page();
            }
        });
    }
}
