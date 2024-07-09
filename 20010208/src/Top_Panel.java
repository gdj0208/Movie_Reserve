import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Top_Panel extends JPanel{
	
	private JButton reserveButton, inquiryButton;
	private JPanel MainPanel;
	private Inquiry_Panel InquiryPanel;
	
	public Top_Panel() {
		set_panel();
		init_buttons();
		add_buttons();
	}
	
	public void get_inquiry_panel(Inquiry_Panel InquiryPanel) { this.InquiryPanel = InquiryPanel; }
	public void get_main_panel(JPanel MainPanel) { this.MainPanel = MainPanel;	}

	// 패널 설정 
	private void set_panel() {
		setPreferredSize(new Dimension(800, 100));
		setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	
	// 버튼들 설정 
	private void init_buttons() {
		// 버튼 생성 
		reserveButton = new JButton("Reserve movie");
		inquiryButton = new JButton("My Booking");

		// 버튼 정렬 
		reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		inquiryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// reserveButton 클릭시 영화 조회하는 기능 추
		add_reserve_button_action_listener();
	}
	
	private void add_reserve_button_action_listener() {
		reserveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { show_inquiry_panel(); }
		});
	}
	
	private void show_inquiry_panel() {
		
		// 기존 Inquiry Panel은 지기 
		if(InquiryPanel != null) { MainPanel.remove(InquiryPanel); } 
		
		// 새로운 Inquiry Panel 추가 
		InquiryPanel = new Inquiry_Panel(MainPanel);
		MainPanel.add(InquiryPanel, BorderLayout.WEST);
		
		// MainPanel 다시 그리기 
		MainPanel.revalidate();
		MainPanel.repaint();
	}
	
	
	// 버튼들 넣기 
	private void add_buttons() {
		add(Box.createVerticalGlue());
		add(reserveButton);
		add(Box.createVerticalStrut(10));
		add(inquiryButton);
		add(Box.createVerticalGlue());
	}


}
