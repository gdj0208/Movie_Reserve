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

public class Schedule_Panel  extends JPanel implements ActionListener {

	private Container container;
	private JLabel test;
	private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JButton select_button;
    private Seat_Panel seat_panel;

	boolean first_condition = true;
	
	private String baseQuery;
	private String conditionQuery;
	
	private String movie_name;
	private String movie_director;
	private String movie_actor;
	private String movie_genre;
	
	Object movie_name_obj;
	Object cinema_obj;
    Object schedule_day_obj;
	
	
    // 기본 초기화 
	public Schedule_Panel(String movie_name, String movie_director, String movie_actor, String movie_genre) {
		set_panel();
		set_search_conditions(movie_name, movie_director, movie_actor,  movie_genre);

		set_components();
		add_movie_lists();
		add_components();
	}
	
	public Schedule_Panel() {
		set_panel();
		//init_components();
		set_components();
		add_components();
	}

	
	public void set_search_conditions(String movie_name, String movie_director, String movie_actor, String movie_genre) {
		this.movie_name = movie_name;
		this.movie_director = movie_director;
		this.movie_actor = movie_actor;
		this.movie_genre = movie_genre;
	}
	
	public void show_search_lists() {
		add_movie_lists();
		add_components();
	}
	
	
	// 패널 초기 설정 
 	private void set_panel() {
		setPreferredSize(new Dimension(250, 300));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	
	// 컴포넌트들 설
	private void set_components() {
		//test = new JLabel("영화 목록");
		/*
		select_button = new JButton("선택");
		select_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 System.out.println(movie_name_obj + " " + cinema_obj + " " + schedule_day_obj);
			}
		});
		*/
		model = new DefaultTableModel(
				new String[]{
						"Name",
						"Director",
						"Genre",
						"Cinema",
						"Day"
						}
				, 0);
		
		table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                int movie_name_col = 0;
                int cinema_col = 3;
                int schedule_day_col = 4;

                if (row != -1 && col != -1) { // 유효한 행인지 확인
                    movie_name_obj = table.getValueAt(row, movie_name_col);
                    cinema_obj = table.getValueAt(row, cinema_col);
                    schedule_day_obj = table.getValueAt(row, schedule_day_col);

                    seat_panel = new Seat_Panel(movie_name_obj.toString(), cinema_obj.toString(), schedule_day_obj.toString());
                    //System.out.println("check1\n");
                    //System.out.println(movie_name_obj + " " + cinema_obj + " " + schedule_day_obj);
                }
            }
        });
        
		scrollPane = new JScrollPane(table);
		
		baseQuery = "SELECT db1.movie.movie_name, \n"
				+ "	db1.movie.movie_director, \n"
				+ "	db1.movie.movie_genre, \n"
				+ "	db1.schedule.schedule_cinema, \n"
				+ "	db1.schedule.schedule_day\n"
				+ "FROM db1.movie\n"
				+ "	INNER JOIN db1.schedule";
	}
	
	private void add_movie_lists() {
		try {
			add_conditions_on_query();
			//System.out.print(baseQuery);
			ResultSet lists = connection.stmt.executeQuery(baseQuery);
			
			while(lists.next()) {
				model.addRow(new Object[] {
						lists.getString("movie_name"),
						lists.getString("movie_director"),
						lists.getString("movie_genre"),
						lists.getInt("schedule_cinema"),
						lists.getString("schedule_day")
				});
			}
		} catch (SQLException e1) { e1.printStackTrace(); }
	}
	
	private void add_conditions_on_query() {
		String tmpQuery;
		conditionQuery = "\nWHERE \n	";
		
		add_name_condition();
		add_director_condition();
		add_actor_condition();
		add_genre_condition();
		
		if(first_condition == false) { baseQuery += conditionQuery; }
	}
	
	private void add_name_condition() {
		if(!movie_name.isBlank()) { 
			String tmpQuery = "";
			tmpQuery = "db1.movie.movie_name LIKE \"%" + movie_name + "%\" \n";
			
			first_condition = false;
			conditionQuery += tmpQuery;
		}
	}
	
	private void add_director_condition() {
		if(!movie_director.isBlank()) {
			String tmpQuery = "";
			if(!first_condition) { tmpQuery = "and "; }
			
			tmpQuery += "db1.movie.movie_director LIKE \"%" + movie_director + "%\" \n";
			
			first_condition = false;
			conditionQuery += tmpQuery;
		}
	}
	
	private void add_actor_condition() {
		if(!movie_actor.isBlank()) {
			String tmpQuery = "";
			if(!first_condition) { tmpQuery = "and "; }
		
			tmpQuery += "db1.movie.movie_actor LIKE \"%" + movie_actor + "%\" \n";
			
			first_condition = false;
			conditionQuery += tmpQuery;
		}
	}
	
	private void add_genre_condition() {
		if(!movie_genre.isBlank()) {
			String tmpQuery = "";
			if(!first_condition) { tmpQuery = "and "; }
		
			tmpQuery += "db1.movie.movie_genre LIKE \"%" + movie_genre + "%\" \n";
			
			first_condition = false;
			conditionQuery += tmpQuery;
		}
	}
	
	
	
	private void add_components() {
		//add(test);
		add(scrollPane);
		//add(select_button);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
