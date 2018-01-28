package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.controlsfx.control.Notifications;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.util.Base64;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class HawkerLoginController implements Initializable {


    @FXML
    private Label adminAgencyName;

    @FXML
    private Label adminMobileLabel;

    @FXML
    private Label adminAddrLabel;

	@FXML
	private Button loginButton;
	@FXML
	private Button adminLoginButton;
	@FXML
	private TextField mobileNum;
	@FXML
	private TextField password;
	@FXML
	Button registerButton;
	// Stage stage;
	Parent root;

	final private static String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
	final private static String SECRET = "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";
	public static Hawker loggedInHawker;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/*
		 * AmazonRDSClient rdsClient = new AmazonRDSClient(new
		 * BasicAWSCredentials(ACCESS_KEY, SECRET)); DescribeDBInstancesResult
		 * dbinstancesresult = rdsClient.describeDBInstances(); List<DBInstance>
		 * dbInstanceList = dbinstancesresult.getDBInstances(); if
		 * (!dbInstanceList.isEmpty()) { DBInstance dbInstance =
		 * dbInstanceList.get(0); Endpoint endPoint = dbInstance.getEndpoint();
		 * 
		 * 
		 * }
		 */
		populateAdminHeaders();
		password.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					loginClicked(new ActionEvent());
				}
			}
		});

		mobileNum.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					loginClicked(new ActionEvent());
				}
			}
		});
		adminLoginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					adminLoginClicked(new ActionEvent());
				}
			}
		});
		loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					loginClicked(new ActionEvent());
				}
			}
		});

		registerButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					addHawkerExtraClicked(new ActionEvent());
				}
			}
		});

	}
	

	@FXML
	public void loginClicked(ActionEvent event) {
//		callLoginFunction();
		System.out.println("Hawker Login button clicked");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}

			PreparedStatement existsStmt = con
					.prepareStatement("select hawker_code from hawker_info where mobile_num = ? and password=?");
			existsStmt.setString(1, mobileNum.getText());
			existsStmt.setString(2, password.getText());
			if (existsStmt.executeQuery().next()) {
				PreparedStatement stmt = con.prepareStatement(
						"select hawker_id,name,hawker_code, mobile_num, agency_name, active_flag, fee, old_house_num, new_house_num, addr_line1, addr_line2, locality, city, state,customer_access, billing_access, line_info_access, line_dist_access, paused_cust_access, product_access, reports_access,profile1,profile2,profile3,initials,password, employment, comments, point_name, building_street,bank_ac_no,bank_name,ifsc_code,stop_history_access,logo,ben_name,total_due  from hawker_info where mobile_num = ?");
				stmt.setString(1, mobileNum.getText());
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					if (rs.getString(6).equalsIgnoreCase("Y")) {
						loggedInHawker = new Hawker(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6).equalsIgnoreCase("Y"), rs.getDouble(7),
								rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
								rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
								rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24),
								rs.getString(25), rs.getString(26), rs.getString(27), rs.getString(28),
								rs.getString(29), rs.getString(30), rs.getString(31), rs.getString(32),
								rs.getString(33), rs.getString(34), rs.getBlob(35), rs.getString(36), rs.getDouble(37));
//						callLineInfoListFunction();
						Notifications.create().hideAfter(Duration.seconds(5)).title("Logged in")
								.text("Login successful").showInformation();
						// stage = (Stage) loginButton.getScene().getWindow();
						// load up OTHER FXML document
						root = FXMLLoader.load(HawkerLoginController.class.getResource("HawkerHome.fxml"));
						Scene scene = new Scene(root);
						Main.primaryStage.setScene(scene);
						Main.primaryStage.setMaximized(true);
						Main.primaryStage.show();
					} else {
						Notifications.create().hideAfter(Duration.seconds(5)).title("Inactive Hawker")
								.text("Hawker with given mobile number is not activated yet. Please contact administrator.")
								.showError();
					}
				}
			} else {
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid login details")
						.text("Invalid mobile number or password").showError();
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (IOException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	@FXML
	public void adminLoginClicked(ActionEvent event) {
		try {
			// get reference to the button's stage
			// stage = (Stage) adminLoginButton.getScene().getWindow();
			// load up OTHER FXML document
			root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
			/*
			 * else{ stage=(Stage) btn2.getScene().getWindow(); root =
			 * FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")); }
			 */
			// create a new scene with root and set the stage
			Scene scene = new Scene(root);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.setMaximized(true);
			Main.primaryStage.show();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	@FXML
	private void addHawkerExtraClicked(ActionEvent event) {
		try {

			Dialog<String> addHawkerDialog = new Dialog<String>();
			addHawkerDialog.setTitle("Add new hawker");
			addHawkerDialog.setHeaderText("Add new Hawker data below.");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			addHawkerDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CLOSE);
			Button saveButton = (Button) addHawkerDialog.getDialogPane().lookupButton(saveButtonType);
			FXMLLoader addHawkerLoader = new FXMLLoader(getClass().getResource("AddHawkersExtraScreen.fxml"));
			Parent addHawkerGrid = (Parent) addHawkerLoader.load();
			AddHawkerExtraScreenController addHwkController = addHawkerLoader
					.<AddHawkerExtraScreenController> getController();

			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addHwkController.isValid()) {
					addHwkController.addHawker();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker created")
							.text("Hawker created successfully. You can now try to login").showInformation();
				} else {
					btnEvent.consume();
				}
			});

			/*
			 * saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> { if
			 * (addHwkController.isValid()) { addHwkController.addHawker();
			 * Notifications.create().hideAfter(Duration.seconds(5)).title(
			 * "Hawker created") .text(
			 * "Hawker created successfully. You can now try to login"
			 * ).showInformation(); } else { btnEvent.consume(); }
			 * 
			 * });
			 */
			addHawkerDialog.getDialogPane().setContent(addHawkerGrid);
			addHwkController.setupBindings();

			addHawkerDialog.setResultConverter(dialogButton -> {
				if (dialogButton != saveButtonType) {
					return null;
				}
				return null;
			});

			Optional<String> updatedHawker = addHawkerDialog.showAndWait();
			// refreshHawkerTable();

			updatedHawker.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					// addHawkerDialog.showAndWait();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}


	private void populateAdminHeaders() {
		HashMap<String,String> adminMap = Main.getAdminDetails();
		adminAgencyName.setText(adminMap.get("name"));
		adminMobileLabel.setText(adminMap.get("mobile"));
		adminAddrLabel.setText(adminMap.get("addr"));
		
	}
	
	private void callLineInfoListFunction() {
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

        lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        try {
            InvokeRequest invokeRequest = new InvokeRequest();
            invokeRequest.setFunctionName("LineInfoList");
            invokeRequest.setPayload(ByteBuffer.wrap(gson.toJson(loggedInHawker.getHawkerId()).getBytes(StringUtils.UTF8)));
            InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);
            
            if (invokeResult.getLogResult() != null) { 
                System.out.println(" log: " 
                        + new String(Base64.decode(invokeResult.getLogResult()))); 
            } 
     
            if (invokeResult.getFunctionError() != null) { 
                throw new LambdaFunctionException(invokeResult.getFunctionError(), 
                        false, new String(invokeResult.getPayload().array())); 
            } 
     
            if (invokeResult.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT ) { 
                return; 
            } 
//            System.out.println(gson.fromJson(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))), ArrayList.class));
            System.out.println(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))));
            
        } catch (Exception e) {
            e.printStackTrace();

        }
	}
	
	private void callLoginFunction(){
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

        lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        try {
            InvokeRequest invokeRequest = new InvokeRequest();
            invokeRequest.setFunctionName("Login");
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("Mobile", mobileNum.getText());
            map.put("Password",password.getText());
            invokeRequest.setPayload(ByteBuffer.wrap(gson.toJson(map).getBytes(StringUtils.UTF8)));
            InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);
            
            if (invokeResult.getLogResult() != null) { 
                System.out.println(" log: " 
                        + new String(Base64.decode(invokeResult.getLogResult()))); 
            } 
     
            if (invokeResult.getFunctionError() != null) { 
                throw new LambdaFunctionException(invokeResult.getFunctionError(), 
                        false, new String(invokeResult.getPayload().array())); 
            } 
     
            if (invokeResult.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT ) { 
                return; 
            } 
            System.out.println(gson.fromJson(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))), Boolean.class));
            
        } catch (Exception e) {
            e.printStackTrace();

        }
	}

}
