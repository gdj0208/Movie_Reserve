import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inquiry_Panel extends JPanel implements ActionListener {

	private JPanel[] Search_Panel;
	private JLabel title;
	private JButton search_button;
	
	private JLabel movie_name_label;
	private JLabel director_name_label;
	private JLabel actor_name_label;
	private JLabel genre_label;
	
	private JTextField movie_name_field;
	private JTextField director_name_field;
	private JTextField actor_name_field;
	private JTextField genre_field;
	
	public Inquiry_Panel() {
		set_panel();
		init_components();
		init_search_panel();
		add_components();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void set_panel() {
		setPreferredSize(new Dimension(250, 300));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	private void init_components() {
		title = new JLabel("영화조회");
		search_button = new JButton("검색");
		
		// 라벨들 추가 
		movie_name_label = new JLabel("영화명");
		director_name_label = new JLabel("감독");
		actor_name_label = new JLabel("배우");
		genre_label = new JLabel("장르");
		
		// 버튼들 추가 
		movie_name_field = new JTextField();
		director_name_field = new JTextField();
		actor_name_field = new JTextField();
		genre_field = new JTextField();
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
		            label = director_name_label;
		            field = director_name_field;
		            break;
		        case 2:
		            label = actor_name_label;
		            field = actor_name_field;
		            break;
		        case 3:
		            label = genre_label;
		            field = genre_field;
		            break;
		        default:
		            break;
		    }
		    
		    label.setSize(8,1);
		    field.setColumns(8);
		    
		    Search_Panel[i].add(label);
		    Search_Panel[i].add(field);
		}

		// 이후에 Search_Panel을 필요한 곳에 추가하거나 활용할 수 있습니다.

		
	}

	private void add_components() {
		add(title);
		for(int i = 0; i < Search_Panel.length; i++) { add(Search_Panel[i]); }
		add(search_button);
	}
}
