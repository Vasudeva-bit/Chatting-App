//  Connection to database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.ResultSet;
// Selection JList
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatInterface extends JFrame implements ActionListener {
    JTextField textbox = new JTextField();
    JTextField search = new JTextField();
    JButton sendButton = new JButton("Send");
    JTextArea chatArea = new JTextArea();
    Box vertical = Box.createVerticalBox();

    String Username;
    String Recipient;

    public ChatInterface(String Username) {
        super("Chat Interface");
        String[] contacts = null;
        this.Username = Username;
        try {
            contacts = getAllUsers();
        }
        catch(SQLException sqle) {
            System.out.println(sqle);
        }
        finally {
            // add some code later
        }
        // set the contacts to all the entries on the user table over the database
        JList<String> contactInfo = new JList<>(contacts);
        contactInfo.setBackground(Color.CYAN);
        contactInfo.setBounds(10, 40, 140, 527);
        contactInfo.setFixedCellHeight(44);
        contactInfo.setFixedCellWidth(150);
        contactInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane contactsScrollPane = new JScrollPane(contactInfo);
        contactsScrollPane.setBounds(10, 40, 140, 527);
        contactsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contactsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().setLayout(null);
        getContentPane().add(contactsScrollPane);

        contactInfo.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent evt) {
                // To avoid double value selected
                if (evt.getValueIsAdjusting())
                    return;
                System.out.println("Recipient changed");
                chatArea.setText(""); // refresh the box or chatArea
                // Select the recipient
                Recipient = (String) contactInfo.getSelectedValue();
                // Get the older messages from the database
                try {
                    String totChat = getOldChats();
                    String message = "";
                    for (int i = 0;i < totChat.length(); i++) {
                        char ch = totChat.charAt(i);

                        if (ch == '!') {
                            chatArea.setLayout(new BorderLayout());
                            JPanel p2 = formatLabel(message);
                            JPanel right = new JPanel(new BorderLayout());
                            right.add(p2, BorderLayout.LINE_END); // todo: changable to LINE_START
                            vertical.add(right);
                            vertical.add(Box.createVerticalStrut(3));
                            chatArea.add(vertical, BorderLayout.PAGE_END);
                            repaint();
                            invalidate();
                            validate();
                            message = "";
                        } else if (ch == '$') {
                            chatArea.setLayout(new BorderLayout());
                            JPanel p2 = formatLabel(message);
                            JPanel right = new JPanel(new BorderLayout());
                            right.add(p2, BorderLayout.LINE_START);
                            vertical.add(right);
                            vertical.add(Box.createVerticalStrut(3));
                            chatArea.add(vertical, BorderLayout.PAGE_END);
                            repaint();
                            invalidate();
                            validate();
                            message = "";
                        } else {
                            message += ch;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    // display a message, saying, error loading older messages
                }
            }
        });

        search.setEditable(true);
        search.setBounds(10, 5, 140, 30);
        search.setBackground(Color.ORANGE);
        add(search);

        chatArea.setEditable(false);
        chatArea.setBackground(Color.WHITE);
        chatArea.setBounds(163, 40, 727, 496);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBounds(163, 40, 727, 496);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().setLayout(null);
        getContentPane().add(chatScrollPane);

        textbox.addActionListener(this);
        textbox.setBackground(Color.ORANGE);
        textbox.setBounds(163, 540, 680, 30);
        add(textbox);

        sendButton.addActionListener(this);
        sendButton.setBounds(835, 543, 60, 24);
        add(sendButton);

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    public String[] getAllUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?" +
                    "user=root&password=root");

            // Prepare the statement to insert the message
            // Insert into both the tables later on
            String sql = "SELECT * FROM Credentials";
            stmt = conn.prepareStatement(sql);
            // Execute the statement
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String temp = rs.getString("Username");
                if(!temp.equals(Username))
                    users.add(temp);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // Close the statement and connection
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        Object[] objArr = users.toArray(); 
        String[] str = Arrays.copyOf(objArr, objArr.length, String[].class);
        return str;
    }

    public String getOldChats() throws SQLException {
        System.out.println("Syncing older messages");
        Connection conn = null;
        PreparedStatement stmt = null;
        String oldChat = "";
        try {
            // Get a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?" +
                    "user=root&password=root");

            // Prepare the statement to insert the message
            String sql = "SELECT * FROM " + Username;
            stmt = conn.prepareStatement(sql);
            // Execute the statement
            ResultSet rs = stmt.executeQuery();
            boolean flag = true;
            while(rs.next()) {
                if(Recipient.equals(rs.getString("Sender"))) {
                    flag = false;
                    oldChat = rs.getString("Message");
                    break;
                }
            }
            stmt.close();
            if(flag) {
                sql = "INSERT INTO "+Username+" (Sender, Message) VALUES (?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, Recipient);
                stmt.setString(2, "");
                stmt.executeUpdate();
                // Close of statement
                stmt.close();

                sql = "INSERT INTO "+Recipient+" (Sender, Message) VALUES (?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, Username);
                stmt.setString(2, "");
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // Close the statement and connection
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return oldChat;
    }

    public void actionPerformed(ActionEvent event) {
        // Get the message from the message field
        String message = " " + textbox.getText() + " ";
        if (message.length() > 2) {
            chatArea.setLayout(new BorderLayout());
            JPanel p2 = formatLabel(message);
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END); // todo: changable to LINE_START
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(3));
            chatArea.add(vertical, BorderLayout.PAGE_END);
            repaint();
            invalidate();
            validate();
            textbox.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Message cannot be empty", "alert!", JOptionPane.INFORMATION_MESSAGE);
        }
        // Storing the message on the database
        try {
            insertMessage(Username, Recipient, message);
        } catch (SQLException sqle) {
            System.out.println("Error while sending the message to the user (message didn't get saved to database)");
        }
    }

    public static JPanel formatLabel(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel output = new JLabel(message);
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(3, 119, 252));
        output.setOpaque(true);
        panel.add(output);
        return panel;
    }

    private void insertMessage(String sender, String recipient, String message) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?" +
                    "user=root&password=root");
            // Insert into both the tables later on
            String sql = "UPDATE "+sender+" set Message = CONCAT(Message, ?) where Sender = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, message+"!");
            stmt.setString(2, recipient);
            // Execute the statement
            stmt.executeUpdate();
            stmt.close();

            sql = "UPDATE "+recipient+" set Message = CONCAT(Message, ?) where Sender = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, message+"$");
            stmt.setString(2, sender);
            // Execute the statement
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
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
        new ChatInterface("VasudevaK");
    }
}