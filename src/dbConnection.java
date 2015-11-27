import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

public class dbConnection {
	public Connection connect = null;
	public Statement statement = null;
	public PreparedStatement preparedStatement = null;
	public ResultSet resultSet = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	Timestamp time = new Timestamp(date.getTime());
	private boolean check;
	private String username;

	// to initialize the jdbc driver and connect the jdbc to mysql using
	// necessary credentials
	public void initialize() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection(
				"",//you will to put your host name and user name here
				"");//and your password
		statement = connect.createStatement();
	}

	public void insertImage(String username, File f) throws Exception {
		preparedStatement = connect
				.prepareStatement("INSERT INTO pictures (username, image, DateReceived) "
						+ " VALUES (?, ?,'" + dateFormat.format(time) + "');");
		FileInputStream im = new FileInputStream(f);
		preparedStatement.setString(1, username);
		preparedStatement.setBinaryStream(2, im, (int) f.length());
		preparedStatement.executeUpdate();

	}

	// to modify or delete row/rows
	public void updateorDelete(String query) throws SQLException, IOException {
		preparedStatement = connect.prepareStatement(query);
		preparedStatement.executeUpdate();
	}
	
	public int selectCommand(String u) throws SQLException{
		int ret=0;
		resultSet = statement.executeQuery("select commandNumber from commands where username='"+u+"';");
		while (resultSet.next()) {
			ret = resultSet.getInt("commandNumber");
		}
		return ret;
	}
	
	public String selectCommandProgram(String u) throws SQLException{
		String ret="";
		resultSet = statement.executeQuery("select program from commands where username='"+u+"';");
		while (resultSet.next()) {
			ret = resultSet.getString("program");
		}
		return ret;
	}

	public void select(String u, String p) throws Exception {
		check = false;
		username = u;
		resultSet = statement
				.executeQuery("select * from Credentials where username= '" + u
						+ "' and password= '" + p + "';");
		viewSelect(resultSet);
	}

	public void selectAll() throws SQLException, IOException {
		resultSet = statement.executeQuery("select image from Credentials;");
		viewSelect(resultSet);
	}

	public BufferedImage viewBlob(String username) throws Exception {
		BufferedImage bi = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyMMdd hh mm ss a");
		Calendar currentCal = Calendar.getInstance();
		File image;
		resultSet = statement
				.executeQuery("select image from pictures where username='"
						+ username + "';");
		while (resultSet.next()) {
			image = new File(formatter.format(currentCal.getTime()) + ".jpeg");
			FileOutputStream fos = new FileOutputStream(image);

			byte[] buffer = new byte[1];
			InputStream is = resultSet.getBinaryStream(1);
			while (is.read(buffer) > 0) {
				fos.write(buffer);
			}
			bi = ImageIO.read(image);
			image.deleteOnExit();
			fos.close();
		}
		return bi;
	}

	public void viewSelect(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			String u = resultSet.getString("username");
			check = true;
			username = u;
		}
	}

	public boolean getCheck() {
		return check;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String s) {
		username=s;
	}

	// to close the connections
	public void closeConnection() {
		try {
			if (statement != null) {
				statement.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
