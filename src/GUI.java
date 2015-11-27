
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener, WindowListener {
	JFrame copy;
	JLabel user, pass, message, llogo;
	JTextField userField;
	JPasswordField passField;
	JButton Blogin;
	dbConnection mydb = new dbConnection();
	ImageIcon Logo;
	boolean u=false,p=false;

	public GUI(JFrame wrapper) throws Exception {

		mydb.initialize();// initializing the DB and logging in credentials
		wrapper.getContentPane().removeAll();// or remove(JComponent)
		JPanel banner = new JPanel();
		banner.setLayout(new BorderLayout());
		banner.setPreferredSize(new Dimension(400, 200));
		banner.setBackground(Color.LIGHT_GRAY);
		banner.setBorder(null);

		JPanel username = new JPanel();
		username.setPreferredSize(new Dimension(400, 50));
		username.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		username.setBackground(Color.LIGHT_GRAY);
		username.setBorder(null);

		JPanel password = new JPanel();
		password.setBackground(Color.LIGHT_GRAY);
		password.setBorder(null);
		password.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		password.setPreferredSize(new Dimension(400, 50));

		JPanel login = new JPanel();
		login.setBackground(Color.LIGHT_GRAY);
		login.setBorder(null);
		login.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		login.setPreferredSize(new Dimension(400, 50));

		JPanel info = new JPanel();
		info.setBackground(Color.LIGHT_GRAY);
		info.setBorder(null);
		info.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
		info.setPreferredSize(new Dimension(400, 250));
		

		wrapper.setTitle("JoBot");
		//wrapper.pack();//adds title bar
		wrapper.getContentPane();
		wrapper.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		//Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//dim.height/2-this.getSize().height/2
		wrapper.setLocation(400,100);
		//setLocationRelativeTo(null);
		wrapper.setResizable(false);
		wrapper.setSize(400, 600);
		wrapper.addWindowListener(this);// for closelistener

		// Displaying the logo
		File file = new File("images/jobot.png");
		BufferedImage b=ImageIO.read(file);
		ImageIO.write(b, "JPEG", file);
		// JLabel displays the text in the screen
		llogo = new JLabel(Logo);
		llogo.setIcon(new ImageIcon(b.getScaledInstance(140, 200,Image.SCALE_AREA_AVERAGING)));
		llogo.setForeground(Color.BLACK);

		message = new JLabel("");
		message.setFont(new Font("Serif", Font.BOLD, 16));
		message.setForeground(Color.GREEN);

		Blogin = new JButton("Login");
		
		Blogin.setPreferredSize(new Dimension(100, 40));
		Blogin.addActionListener(this);
		
	
		userField = new JTextField(15);
		userField.setText("Username");
		userField.setFont(new Font("Serif", Font.ROMAN_BASELINE, 20));
		userField.setForeground(Color.GRAY);
		userField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(u==false){
					userField.setText("");
					u=true;
				}
				
			}
		});

		passField = new JPasswordField(15);
		passField.setText("Password");
		passField.setFont(new Font("Serif", Font.ROMAN_BASELINE, 20));
		passField.setForeground(Color.GRAY);
		passField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(p==false){
					passField.setText("");
					p=true;
				}
				
			}
		});


		// Adding the JItems in the 5Graphical Display

		banner.add(llogo, BorderLayout.CENTER);
		username.add(userField);
		password.add(passField);

		login.add(Blogin);
		info.add(message);

		wrapper.add(banner);
		wrapper.add(username);
		wrapper.add(password);
		wrapper.add(login);
		wrapper.add(info);
		wrapper.setVisible(true);
		wrapper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		copy=wrapper;
		
	}

	public static void main(String args[]) throws Exception {
		JFrame start=new JFrame();
		GUI app = new GUI(start);

		// main method
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(Blogin)) {
			try {
				mydb.select(userField.getText(), passField.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (mydb.getCheck() == true) {
				message.setText("Correct Password.");
				message.setForeground(Color.green);
				try {
					mydb.updateorDelete("UPDATE commands SET commandNumber='12' where username='"+mydb.getUsername()+"';");
					picGUI pic=new picGUI(copy, mydb);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				message.setText("Incorrect Password. Try Again");
				message.setForeground(Color.red);
			}
		}

	}

	public void windowClosing(WindowEvent e) {
		
		try {
			mydb.updateorDelete("UPDATE commands SET commandNumber='12' where username='"+mydb.getUsername()+"';");//signalling turned off
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mydb.closeConnection();// to close all the connections
		dispose();
		System.exit(0);
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
