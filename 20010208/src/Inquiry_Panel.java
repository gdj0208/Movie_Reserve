import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Inquiry_Panel extends JPanel implements ActionListener {

	private Schedule_Panel Schedule_Panel;
	private Container container;
	private String baseQuery;
	
	private String movie_name;
	private String movie_director;
	private String movie_actor;
	private String movie_genre;
	
	private JPanel Main_Panel;
	private JPanel[] Search_Panel;
	private JLabel title;
	private JButton search_button;
	
	private JLabel movie_name_label;
	private JLabel movie_director_label;
	private JLabel movie_actor_label;
	private JLabel movie_genre_label;
	
	private JTextField movie_name_field;
	private JTextField movie_director_field;
	private JTextField movie_actor_field;
	private JTextField movie_genre_field;
	
	public Inquiry_Panel(JPanel MainPanel) {
		set_panel(MainPanel);
		init_components();
		add_components();
	}
	
	private void set_main_panel(JPanel MainPanel) { this.Main_Panel = MainPanel; }
	private void set_panel(JPanel MainPanel) {
		this.Main_Panel = MainPanel;
		
		setPreferredSize(new Dimension(250, 300));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	public void set_schedule_panel(Schedule_Panel Schedule_Panel) { this.Schedule_Panel = Schedule_Panel; }
	
	
	private void init_components() {
		init_title_and_button();	// 맨 위 제목과 맨 아래 버튼 추가 
		init_labels();				// 라벨들 추가 
		init_fields();				// 버튼들 추가 
		init_search_panel();		// 검색패널들 추
	}
	
	private void init_title_and_button() {
		title = new JLabel("영화조회");
		search_button = new JButton("검색");
		search_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				get_field_datas();
				Schedule_Panel.set_search_conditions(movie_name, movie_director, movie_actor, movie_genre);
				Schedule_Panel.show_search_lists();
				//show_schedule_panel();
			}
		});
	}
	
	private void init_labels() {
		movie_name_label = new JLabel("영화명");
		movie_director_label = new JLabel("감독");
		movie_actor_label = new JLabel("배우");
		movie_genre_label = new JLabel("장르");
	}
	
	private void init_fields() {
		movie_name_field = new JTextField();
		movie_director_field = new JTextField();
		movie_actor_field = new JTextField();
		movie_genre_field = new JTextField();
	}
	
	private void init_search_panel() {
		Search_Panel = new JPanel[4];

		for (int i = 0; i < Search_Panel.length; i++) {
		    Search_Panel[i] = new JPanel();
		    Search_Panel[i].setLayout(new FlowLayout(FlowLayout.CENTER)); 
		    
		    JLabel label = null;
		    JTextField field = null;
		    
		    switch (i) {
		        case 0:
		            label = movie_name_label;
		            field = movie_name_field;
		            break;
		        case 1:
		            label = movie_director_label;
		            field = movie_director_field;
		            break;
		        case 2:
		            label = movie_actor_label;
		            field = movie_actor_field;
		            break;
		        case 3:
		            label = movie_genre_label;
		            field = movie_genre_field;
		            break;
		        default:
		            break;
		    }
		    
		    label.setSize(8,1);
		    field.setColumns(8);
		    
		    Search_Panel[i].add(label);
		    Search_Panel[i].add(field);
		}
	}

	
	
	
	private void add_components() {
		add(title);
		for(int i = 0; i < Search_Panel.length; i++) { add(Search_Panel[i]); }
		add(search_button);
	}

	private void show_schedule_panel() {
		// 기존 Inquiry Panel은 지기 
		if(Schedule_Panel != null) { Main_Panel.remove(Schedule_Panel); } 
		
		// 새로운 Inquiry Panel 추가 
		Schedule_Panel = new Schedule_Panel(movie_name, movie_director, movie_actor, movie_genre);
		Main_Panel.add(Schedule_Panel, BorderLayout.CENTER);
				
		// MainPanel 다시 그리기 
		Main_Panel.revalidate();
		Main_Panel.repaint();
	}

	private void get_field_datas() {
		movie_name = movie_name_field.getText();
		movie_director = movie_director_field.getText();
		movie_actor = movie_actor_field.getText();
		movie_genre = movie_genre_field.getText();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) { }
}
