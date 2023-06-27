import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame implements ActionListener {
    JLabel label1, label2, label3;
    JTextField text1;
    JPasswordField text2;
    JButton button;

    public LoginGUI() {
        setTitle("Chat Interface");
        setSize(900, 600);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        label1 = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(label1, constraints);

        text1 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(text1, constraints);

        label2 = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(label2, constraints);

        text2 = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(text2, constraints);

        button = new JButton("Login");
        button.addActionListener(this);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(button, constraints);

        button = new JButton("Register");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerGUI reg = new registerGUI();
                reg.setVisible(true);
                setVisible(false); 
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(button, constraints);

        label3 = new JLabel("");
        label3.setForeground(Color.RED);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(label3, constraints);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        String username = text1.getText();
        String password = new String(text2.getPassword());
        try {
            verifyUser(username, password);
        }
        catch(SQLException sqle) {
            label3.setText("Error while verifying the credentials, try loggin in later!");
            label3.setForeground(Color.RED);
        }
    }

    private void verifyUser(String Username, String Passcode) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?" +
                                               "user=root&password=root");

            // Prepare the statement to insert the message
            String sql = "SELECT * FROM Credentials WHERE Username = ? AND Passcode = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Username);
            stmt.setString(2, Passcode);

            // Execute the statement
            ResultSet rs = stmt.executeQuery();
            boolean flag = true;
            while (rs.next()) {
                flag = false;
                label3.setText("Login successful, will be redirected in 2 seconds");
                label3.setForeground(Color.GREEN);
                int delay = 2000;
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // send the username
                        ChatInterface chatI = new ChatInterface(Username);
                        chatI.setVisible(true);
                        setVisible(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();                
            }
            if(flag) {
                label3.setText("Invalid Credentials, try again!");
                label3.setForeground(Color.ORANGE);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            // Close the statement and connection
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        LoginGUI gui = new LoginGUI();
        gui.setVisible(true);
    }
}
