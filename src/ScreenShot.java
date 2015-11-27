
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ScreenShot{
	
	//public boolean stop;
	private BufferedImage screenShot;
	private Robot jobot;
	public void click() throws Exception{
				jobot = new Robot();
				screenShot=jobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				
				
	}
	public void close(){
	//	file.deleteOnExit();
		screenShot.flush();
	}
	public BufferedImage getImage(){
		return screenShot;
	}
}

