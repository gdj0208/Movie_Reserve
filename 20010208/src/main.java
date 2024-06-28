public class main {
    public static void main(String[] args) {
        // Run the login page
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	connection.main();
                new login_page();
                System.out.println("check");
            }
        });
    }
}
