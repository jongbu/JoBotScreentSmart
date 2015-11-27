import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class picGUI extends JFrame implements ActionListener, WindowListener {
	JFileChooser fileChooser;
	JLabel wel, message, llogo, prog, value;
	JPanel banner, welcome, Settings, Sync, Stop,progress, LogOut,info,gap,gap1;
	JButton BSync, BStop, BLogout,Bup, Bdown;
	dbConnection mydb = new dbConnection();
	BufferedImage pic, sendBrowsedPic;
	int timeReq=10000;
	ImageIcon Logo,Bar;
	private boolean threadStop,startClicked;

	public picGUI(final JFrame j,dbConnection d) throws Exception {
		
		threadStop = false;// Sync on
		startClicked=false;
		j.getContentPane().removeAll();// or remove(JComponent)
		mydb = d;

		banner = new JPanel();
		banner.setLayout(new BorderLayout());
		banner.setPreferredSize(new Dimension(400, 200));
		banner.setBackground(Color.LIGHT_GRAY);
		banner.setBorder(null);

		welcome = new JPanel();
		welcome.setPreferredSize(new Dimension(400, 50));
		welcome.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		welcome.setBackground(Color.LIGHT_GRAY);
		welcome.setBorder(null);
		
		Settings = new JPanel();
		Settings.setLayout(new GridLayout(1, 7));
		Settings.setPreferredSize(new Dimension(400, 20));
		Settings.setBackground(Color.LIGHT_GRAY);
		Settings.setBorder(null);
		
		gap = new JPanel();
		gap.setBackground(Color.LIGHT_GRAY);
		gap.setBorder(null);
		gap.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		gap.setPreferredSize(new Dimension(400, 30));
		
		Sync = new JPanel();
		Sync.setBackground(Color.LIGHT_GRAY);
		Sync.setBorder(null);
		Sync.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Sync.setPreferredSize(new Dimension(400, 50));
		
		Stop = new JPanel();
		Stop.setBackground(Color.LIGHT_GRAY);
		Stop.setBorder(null);
		Stop.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Stop.setPreferredSize(new Dimension(400, 50));

		gap1 = new JPanel();
		gap1.setBackground(Color.LIGHT_GRAY);
		gap1.setBorder(null);
		gap1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		gap1.setPreferredSize(new Dimension(400, 30));
		
		progress = new JPanel();
		progress.setBackground(Color.LIGHT_GRAY);
		progress.setBorder(null);
		progress.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		progress.setPreferredSize(new Dimension(400, 20));

		LogOut = new JPanel();
		LogOut.setBackground(Color.LIGHT_GRAY);
		LogOut.setBorder(null);
		LogOut.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		LogOut.setPreferredSize(new Dimension(400, 50));
				
		info = new JPanel();
		info.setBackground(Color.LIGHT_GRAY);
		info.setBorder(null);
		info.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
		info.setPreferredSize(new Dimension(400, 100));

		j.setTitle("JoBot");

		value=new JLabel("  10000");
		value.setFont(new Font("Serif", Font.BOLD, 16));
		value.setForeground(Color.GREEN);
		value.setPreferredSize(new Dimension(50, 20));
		
		Bup = new JButton("+");
		Bup.setPreferredSize(new Dimension(20, 20));
		Bup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource().equals(Bup)) {
					if(timeReq<15000){
						timeReq+=1000;
						value.setText("  "+Integer.toString(timeReq));
					}
				}

			}
		});
		
		Bdown = new JButton("-");
		Bdown.setPreferredSize(new Dimension(20,20));
		Bdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource().equals(Bdown)) {
					if(timeReq>5000){
						timeReq-=1000;
					value.setText("  "+Integer.toString(timeReq));
				}
				}

			}
		});
		
		
		Bar = new ImageIcon("images/ProgressBar.gif");
		prog = new JLabel(Bar);
		prog.setVisible(false);
		
		// Displaying the logo
		Logo = new ImageIcon("images/JoBot.jpg");

		// JLabel displays the text in the screen
		llogo = new JLabel(Logo);

		wel = new JLabel("Welcome, "+ mydb.getUsername());
		wel.setFont(new Font("Serif", Font.BOLD, 24));
		wel.setForeground(Color.blue);
		
		message = new JLabel("");
		message.setFont(new Font("Serif", Font.BOLD, 16));
		message.setForeground(Color.GREEN);

		BSync = new JButton("Start Sync");
		BSync.setPreferredSize(new Dimension(150, 40));
		BSync.addActionListener(this);

		BStop = new JButton("Stop Sync");
		BStop.setPreferredSize(new Dimension(150, 40));
		BStop.addActionListener(this);

		BLogout = new JButton("Log Out");
		BLogout.setPreferredSize(new Dimension(150, 40));
		BLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource().equals(BLogout)) {
					try {
						startClicked=false;
						threadStop = true;
						mydb.closeConnection();// to close all the connections
						GUI G=new GUI(j);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

		
		banner.add(llogo, BorderLayout.CENTER);
		welcome.add(wel);
		Settings.add(new JLabel(""), 0);
		Settings.add(new JLabel(""), 1);
		Settings.add(Bdown,2);
		Settings.add(value,3);
		Settings.add(Bup,4);
		Settings.add(new JLabel(""), 5);
		Settings.add(new JLabel(""), 6);
		Sync.add(BSync);
		Stop.add(BStop);
		progress.add(prog);
		LogOut.add(BLogout);
		info.add(message);
		
		j.add(banner);
		j.add(welcome);
		j.add(Settings);
		j.add(gap);
		j.add(Sync);
		j.add(Stop);
		j.add(LogOut);
		j.add(gap1);
		j.add(progress);
		j.add(info);

		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			
	}
	
	public void getDBCommand(){
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				int lastCall=0;
					while(threadStop==false){
					try {
						
						int i = mydb.selectCommand(mydb.getUsername());
						
						if (i > 0 && i < 12 && i!=lastCall) {
							lastCall=i;
							waitForCommand(i);
						}
						Thread.sleep(4000);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
		});
		t1.start();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 if (e.getSource().equals(BSync)) {
			if(startClicked==false){
				startClicked=true;
			threadStop = false;// sets the synchronization on
			getDBCommand();

			// to buffer image and save picture after every 5 seconds
			Thread t2 = new Thread(new Runnable() {
				public void run() {
					// code goes here.
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyMMdd hh mm ss a");

					Calendar currentCal = Calendar.getInstance();
					while (threadStop == false) {
						
						try {
							File file;
							ScreenShot sc = new ScreenShot();
							sc.click();
							pic = sc.getImage();

							file = new File(formatter.format(currentCal.getTime()) + ".jpeg");
							ImageIO.write(pic, "JPEG", file);

							mydb.updateorDelete("Delete from pictures where username='" + mydb.getUsername() + "';");
							mydb.insertImage(mydb.getUsername(), file);
							message.setText("New Image Saved");
							Thread.sleep(timeReq);//time it takes to upload image
							message.setText("");
							file.deleteOnExit();
							sc.close();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

				}
			});
			t2.start();
			// Displaying the progress bar
			prog.setVisible(true);
			message.setText("Synchronzing . . .");
			Timer infoTimer = new Timer(2000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					message.setText("");
				}
			});
			infoTimer.setRepeats(false);
			infoTimer.start();
			}

		} else if (e.getSource().equals(BStop)) {
			prog.setVisible(false);
			startClicked=false;
			threadStop = true;
			message.setText("Synchronization Paused");
			Timer infoTimer = new Timer(5000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					message.setText("");
				}
			});
			infoTimer.setRepeats(false);
			infoTimer.start();
		}
	}

	public void windowClosing(WindowEvent e) {
		try {
			mydb.updateorDelete("UPDATE commands SET commandNumber='0' where username='" + mydb.getUsername() + "';");// signalling
																														// turned
																														// off
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		threadStop = true;
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

	public static void waitForCommand(int i)
			throws RuntimeException, IOException, SQLException, InterruptedException {
		String command = "", para = "";
		String os = System.getProperty("os.name");
		os = os.toLowerCase();
		 System.out.println("os:"+os);
		if (os.contains("linux") || os.contains("mac")) {
			if (i == 1)
				command = "shutdown -h now";
			else if (i == 2)
				command = "shutdown -r now";
			else if (i == 3)
				command = "osascript -e 'tell application \"System Events\" to log out'";
		} else if (os.contains("windows")) {
			if      (i == 1)
				command = "shutdown.exe -s -f";
			else if (i == 2)
				command = "shutdown.exe -r -f";
			else if (i == 3)
				command = "shutdown.exe -l -f";
			else if (i == 4) 
				command = "TASKKILL /F /IM chrome.exe";
			else if (i == 5) 
				command = "TASKKILL /F /IM firefox.exe";
			else if (i == 6) 
				command = "TASKKILL /F /IM iexplore.exe";
			else if (i == 7) 
				command = "TASKKILL /F /IM notepad.exe";
			else if (i == 8) 
				command = "TASKKILL /F /IM winword.exe";
			else if (i == 9) 
				command = "TASKKILL /F /IM excel.exe";
			else if (i == 10) 
				command = "TASKKILL /F /IM powerpnt.exe";
			
		} else {
			throw new RuntimeException("Unsupported operating system.");
		}
		Runtime.getRuntime().exec(command);
	}
}
