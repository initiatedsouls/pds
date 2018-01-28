package application;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Main extends Application {
	public static Connection dbConnection = null;
	public static String dateFormat = "dd/MM/yyyy";
	public static AmazonS3 s3logoclient;
	public static StringConverter<LocalDate> dateConvertor = new StringConverter<LocalDate>() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Main.dateFormat);

		@Override
		public String toString(LocalDate date) {
			if (date != null) {
				return dateFormatter.format(date);
			} else {
				return "";
			}
		}

		@Override
		public LocalDate fromString(String string) {
			if (string != null && !string.isEmpty()) {
				return LocalDate.parse(string, dateFormatter);
			} else {
				return null;
			}
		}
	};

	private static String dbConnectionString = "jdbc:oracle:thin:@lateefahmedpds.c3in7ocqfbfv.ap-southeast-1.rds.amazonaws.com:1521:ORCL";
	public static Stage primaryStage;
	public static DecimalFormat df = new DecimalFormat("#.##");
	
	
	public static final Logger _logger = LogManager.getLogger(Main.class.getName());
	
	@Override
	public void start(Stage primaryStage) {
		Main._logger.debug("Start method ");
		Main.primaryStage=primaryStage;
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("HawkerLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.primaryStage.setScene(scene);
			Main.primaryStage.setMaximized(true);
			Main.primaryStage.show();
		} catch (Exception e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main._logger.debug("Entered Main method");
		df.setRoundingMode(RoundingMode.CEILING);
		try {
			Properties prop = new Properties();
			String propFileName = "application/config.properties";
 
			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
 
			// get the property value and print it out
			String access_key = prop.getProperty("ACCESS_KEY");
			String secret = prop.getProperty("SECRET");
			// step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			AWSCredentials credentials = new BasicAWSCredentials(access_key, secret);
			AmazonRDS client = AmazonRDSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("ap-northeast-1").build();
			DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
			req.setDBInstanceIdentifier("lateefahmedpds");
			DescribeDBInstancesResult result = client.describeDBInstances();
			DBInstance dbInstance = (DBInstance) result.getDBInstances().toArray()[0];
			String address=dbInstance.getEndpoint().getAddress();
			s3logoclient = new AmazonS3Client(credentials);
			// step2 create the connection object
			dbConnectionString = "jdbc:oracle:thin:@"+dbInstance.getEndpoint().getAddress()+":"+dbInstance.getEndpoint().getPort()+":ORCL";
			dbConnection = DriverManager.getConnection(dbConnectionString, "admin", "LateefAhmedPDS");

		} catch (Exception e) {
			System.out.println(e);
		}
		launch(args);
	}

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		dbConnection.close();
	}

	public static Connection reconnect() {
		try {
			Notifications.create().title("Checking connection!").text("Checking database connection.")
					.showInformation();
			if (dbConnection.isClosed()) {
				Notifications.create().title("Connection Error!").text("Database connection lost, reconnecting")
						.showError();
				// step1 load the driver class
				Class.forName("oracle.jdbc.driver.OracleDriver");

				// step2 create the connection object
				dbConnection = DriverManager.getConnection(dbConnectionString, "admin", "LateefAhmedPDS");

				Notifications.create().title("Connection Success!").text("Database reconnected").show();
			} else {
				Notifications.create().title("All good!").text("Dabatase connection seems fine, please try again.")
						.showInformation();
			}

		} catch (Exception e) {
			System.out.println(e);
			Notifications.create().title("Connection Error!")
					.text("Cannot connect to amazon database, contact administrator!").showError();
		}

		return dbConnection;
	}
	
	public static HashMap<String,String> getAdminDetails(){
		HashMap<String,String> retMap = new HashMap<String,String>();
		try {
			Properties prop = new Properties();
			String propFileName = "application/config.properties";
 
			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
 
			// get the property value and print it out
			String access_key = prop.getProperty("ACCESS_KEY");
			String secret = prop.getProperty("SECRET");
			Connection con = Main.dbConnection;
			if(con==null){
				Class.forName("oracle.jdbc.driver.OracleDriver");
				AWSCredentials credentials = new BasicAWSCredentials(access_key, secret);
				AmazonRDS client = AmazonRDSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("ap-northeast-1").build();
				DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
				req.setDBInstanceIdentifier("lateefahmedpds");
				DescribeDBInstancesResult result = client.describeDBInstances();
				DBInstance dbInstance = (DBInstance) result.getDBInstances().toArray()[0];
				String address=dbInstance.getEndpoint().getAddress();
				
				// step2 create the connection object
				con = DriverManager.getConnection("jdbc:oracle:thin:@"+dbInstance.getEndpoint().getAddress()+":"+dbInstance.getEndpoint().getPort()+":ORCL", "admin", "LateefAhmedPDS");

			}
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select company_name,company_mobile,company_addr from admin_login where username ='admin' ";
			PreparedStatement stmt = con.prepareStatement(query);
//			stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				retMap.put("name", rs.getString(1));
				retMap.put("mobile",rs.getString(2));
				retMap.put("addr",rs.getString(3));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return retMap;
	}
}
