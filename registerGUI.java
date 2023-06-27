import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class registerGUI extends JFrame implements ActionListener {
    JLabel label1, label2, label3, label4, label5,label6,label7,label8,label9,label10,label11,label12,label13,label14,label15;
    JTextField text1,text2,text3,text4,text5,text6,text8;
    JButton button;
    private JCheckBox checkbox;

    public registerGUI() {
        checkbox = new JCheckBox();
        setTitle("New User Register");
        setSize(900, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        label1 = new JLabel("FirstName:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(label1, constraints);

        text1 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(text1, constraints);

        label2 = new JLabel("LastName:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(label2, constraints);

        text2 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(text2, constraints);

        label3 = new JLabel("Mobile No:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(label3, constraints);

        text3 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(text3, constraints);

        label4 = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(label4, constraints);

        text4 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(text4, constraints);

        label5 = new JLabel("New password:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(label5, constraints);

        text5 = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(text5, constraints);

        label6 = new JLabel("Confirm password:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(label6, constraints);

        text6 = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(text6, constraints);

        button = new JButton("Register");
        button.addActionListener(this);
        constraints.gridx = 1;
        constraints.gridy = 8;
        panel.add(button, constraints);

        button = new JButton("back");
        button.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 12;
        panel.add(button, constraints);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginGUI reg = new LoginGUI();
                reg.setVisible(true);
                setVisible(false); 
            }
        });

        label7 = new JLabel("");
        label7.setForeground(Color.RED);
        constraints.gridx = 1;
        constraints.gridy = 6;
        panel.add(label7, constraints);

        label8 = new JLabel("");
        label8.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(label8, constraints);

        label9 = new JLabel("");
        label9.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(label9, constraints);

        label10 = new JLabel("");
        label10.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 2;
        panel.add(label10, constraints);

        label11 = new JLabel("");
        label11.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 3;
        panel.add(label11, constraints);

        label12 = new JLabel("");
        label12.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 4;
        panel.add(label12, constraints);

        label13 = new JLabel("");
        label13.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 5;
        panel.add(label13, constraints);

        label14 = new JLabel("");
        label14.setForeground(Color.RED);
        constraints.gridx = 2;
        constraints.gridy = 7;
        panel.add(label14, constraints);

        checkbox.setText("I agree to terms&conditions.");
        checkbox.setSelected(false);
        constraints.gridx = 1;
        constraints.gridy = 7;
        panel.add(checkbox,constraints);
        
        label15 = new JLabel("");
        label15.setForeground(Color.RED);
        constraints.gridx = 1;
        constraints.gridy = 10;
        panel.add(label15, constraints);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        int checkCount = 0;
        String FirstName = text1.getText();
        if(FirstName.length() != 0) {
            label9.setText("✓");
            label9.setForeground(Color.GREEN);
            checkCount++;
        }
        else {
            label9.setText("* Field cannot be empty!");
            label9.setForeground(Color.RED);
            checkCount--;
        }

        String LastName = text2.getText();
        if(LastName.length() == 0) {
            label8.setText("* Field cannot be empty!");
            label8.setForeground(Color.RED);
            checkCount--;
        }
        else {
            if(FirstName.equals(LastName)) {
                label8.setText("FirstName and LastName should be differnt");
                label8.setForeground(Color.RED);
                checkCount--;
            }
            else {
                label8.setText("✓");
                label8.setForeground(Color.GREEN);
                checkCount++;
            }
        }
        String MobileNo = text3.getText();
        int flagMobile = 0;
        if(MobileNo.length() != 10) {
            flagMobile = -1;
        }
        if(flagMobile != -1) {
            for (int i = 0; i < MobileNo.length(); i++) {
                if (Character.isDigit(MobileNo.charAt(i)) == false) {
                    flagMobile = -1;
                }
            }
        }
        if(flagMobile == -1) {
            label10.setText("* Invalid Mobile Number Entered!");
            label10.setForeground(Color.RED);
            checkCount--;
        }
        else {
            label10.setText("✓");
            label10.setForeground(Color.GREEN);
            checkCount++;
        }

        String UserName = text4.getText();
        if(UserName.length() == 0) {
            label11.setText("* Field cannot be empty!");
            label11.setForeground(Color.RED);
            checkCount--;
        }
        else {
            label11.setText("✓");
            label11.setForeground(Color.GREEN);
            checkCount++;
        }

        String NewPassword = new String(((JPasswordField) text5).getPassword());
        if(NewPassword.length() < 8) {
            label12.setText("* Minimum 8 characters required!");
            label12.setForeground(Color.RED);
            checkCount--;
        }
        else {
            label12.setText("✓");
            label12.setForeground(Color.GREEN);
            checkCount++;
        }

        String ConfirmPassword = text6.getText();
        if(ConfirmPassword.length() < 8) {
            label13.setText("* Minimum 8 characters required!");
            label13.setForeground(Color.RED);
            checkCount--;
        }
        else if(ConfirmPassword.equals(NewPassword)) {
            label13.setText("✓");
            label13.setForeground(Color.GREEN);
            checkCount++;
        }
        else {
            label13.setText("* Passwords didn't match!");
            label13.setForeground(Color.RED);
            checkCount--;
        }

        if(checkbox.isSelected()) {
            label14.setText("✓");
            label14.setForeground(Color.GREEN);
            checkCount++;
        }
        else {
            label14.setText("* please,tick the box!");
            label14.setForeground(Color.RED);
            checkCount--;
        }
        if(checkCount == 7) {
            try {
                registerUser(FirstName+" "+LastName, MobileNo, UserName, ConfirmPassword);
            }
            catch(SQLException sqle) {
                System.out.println("Error while registering the user!");
            }
            label15.setText("Congratulations! Registration Successful,You will be redirected to loginPage in 2 sec.");
            label15.setForeground(Color.GREEN);
            int delay = 2000;
            Timer timer = new Timer(delay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LoginGUI log = new LoginGUI();
                    log.setVisible(true);
                    setVisible(false);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        else {
            label15.setText("");
        }        
    }

    private void registerUser(String Name, String MobileNo, String Username, String Passcode) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?" +
                                               "user=root&password=root");

            // Prepare the statement to insert the message
            String sql = "CREATE TABLE "+ Username +" (Sender varchar(25) NOT NULL PRIMARY KEY, Message varchar(1000))";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
            
            sql = "INSERT INTO Credentials (Name, MobileNo, Username, Passcode) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Name); // Vasudeva Kilaru
            stmt.setString(2, MobileNo); // 9959826455
            stmt.setString(3, Username); // VasudevaK
            stmt.setString(4, Passcode); // DevaDeva

            // Execute the statement
            stmt.executeUpdate();
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
        registerGUI gui = new registerGUI();
        gui.setVisible(true);
    }
}