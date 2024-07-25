
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

    private Connection conn = connection.conn;
    private Statement stmt = connection.stmt;
    private ResultSet resultSet = null;
    
    private JPanel MainPanel;
    private Top_Panel top_Panel;
    private Inquiry_Panel Inquiry_Panel;
    private Schedule_Panel Schedule_Panel;
    // Database connection details
 
    private int width = 800;
    private int height = 600;

    public user_page() {
    	init_page();									// 기본적인 페이지의 크기 등 설정 후 생성 
    	create_main_panel();							// 메인 패널 생성  
    	create_top_panel(MainPanel, Inquiry_Panel);		// top panel 생성  
    	create_page();									// 페이지 생성
    }

	
	private void init_page() {
        setTitle("User Page");
        setBounds(300, 90, width, height);
        setResizable(false);
	}
	
	private void create_main_panel() {
        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout());
	}

	private void create_top_panel(JPanel MainPanel, Inquiry_Panel InquiryPanel) {
    	top_Panel = new Top_Panel();
    	top_Panel.get_main_panel(MainPanel);
    	top_Panel.get_inquiry_panel(InquiryPanel);
    	MainPanel.add(top_Panel, BorderLayout.NORTH);
	}

	private void create_page() {
    	add(MainPanel);			// 매인 패널 추가 
        setVisible(true);		// 창 보이게 설정 
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
