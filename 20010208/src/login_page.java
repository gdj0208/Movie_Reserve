import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login_page extends JFrame implements ActionListener {
    // Components of the login form
    private Container container;
    private JLabel userLabel;
    private JLabel password;
    private JTextField userTextField;
    private JPasswordField userPassword;
    private JButton loginButton;
    private JButton resetButton;
    private JLabel messageLabel;
    public static int user;
    public static String user_N;

    // Constructor to set up the GUI components
    public login_page() {
        setTitle("Login Form");
        setBounds(300, 90, 400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 30, 100, 30);
        container.add(userLabel);
        
        password = new JLabel("Password:");
        password.setBounds(50, 70, 100, 30);
        container.add(password);

        userTextField = new JTextField();
        userTextField.setBounds(150, 30, 150, 30);
        container.add(userTextField);
        
        userPassword = new JPasswordField();
        userPassword.setBounds(150, 70, 150, 30);
        container.add(userPassword);

        

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 120, 100, 30);
        loginButton.addActionListener(this);
        container.add(loginButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(200, 120, 100, 30);
        resetButton.addActionListener(this);
        container.add(resetButton);

        messageLabel = new JLabel("");
        messageLabel.setBounds(50, 150, 250, 30);
        container.add(messageLabel);

        setVisible(true);
    }
    
    void set_user(String name) {
    	String e = "SELECT user_id FROM db1.user WHERE user_name = '" + name + "'";
    	
    	try {
    		
			ResultSet result = connection.stmt.executeQuery(e);
			
			if(result.next()) {
			    user = result.getInt("user_id");
			    user_N = name;
			}
		}
    	catch (SQLException ex) { ex.printStackTrace(); }
    }

    // Action handler for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String input_name = userTextField.getText();
            String input_pw = new String(userPassword.getPassword());
            
            try {
            	ResultSet All_User = connection.stmt.executeQuery("select * from db1.user");
            	while(All_User.next()) {
            		String Name = All_User.getString("user_name");
            		String pw = All_User.getString("user_password");
                	int Manager = All_User.getInt("user_manager");
                	
                	if (Name.equals(input_name) && pw.equals(input_pw) && Manager == 1) {
                    	new manager_page();
                    	break;
                    }
                	
                	else if (Name.equals(input_name) && pw.equals(input_pw) && Manager == 0) {
                		set_user(input_name);
                    	new user_page();
                    	break;
                    }
                	
            	}
       
            } catch (SQLException error) {
                System.out.println("SQL 실행 오류");
                error.printStackTrace();
            } 
            
            

        } 
        
        else if (e.getSource() == resetButton) {
            userTextField.setText("");
            userPassword.setText("");
            messageLabel.setText("");
        }
    }

    public static void main(String[] args) {
        new login_page();
    }
}
