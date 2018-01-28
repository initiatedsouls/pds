package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class AdminLoginController implements Initializable {


    @FXML
    private Label adminAgencyName;

    @FXML
    private Label adminMobileLabel;

    @FXML
    private Label adminAddrLabel;

	@FXML
	private Button adminLoginButton;
	@FXML
	private TextField adminUsername;
	@FXML
	private PasswordField adminPassword;
	@FXML
	private Button backButton;

	// Stage stage;
	Parent root;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateAdminHeaders();
		adminUsername.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					loginClicked(new ActionEvent());
				}
			}
		});

		adminPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					loginClicked(new ActionEvent());
				}
			}
		});
		backButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.ENTER) {
					try {
						backButtonClicked(new ActionEvent());
					} catch (IOException e) {

						Main._logger.debug("Error :",e);
						e.printStackTrace();
					}
				}
			}
		});
	}

	@FXML
	private void loginClicked(ActionEvent event) {
		System.out.println("Admin Login button clicked");
//		PrepareBillingXLS.PrepareBillingXLS();
//		ExportToExcel.exportDataToExcel();
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}

			PreparedStatement existsStmt = con
					.prepareStatement("select username,password from admin_login where username = ? and password=?");
			existsStmt.setString(1, adminUsername.getText());
			existsStmt.setString(2, adminPassword.getText());
			if (existsStmt.executeQuery().next()) {
				// stage = (Stage) adminLoginButton.getScene().getWindow();
				// load up OTHER FXML document
				root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));

				Scene scene = new Scene(root);
				// scene.set
				Main.primaryStage.setScene(scene);
				Main.primaryStage.setMaximized(true);
				Main.primaryStage.show();
			} else {
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid login details")
						.text("Invalid Administrator username or password").showError();
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
	private void backButtonClicked(ActionEvent event) throws IOException {

		// stage = (Stage) adminLoginButton.getScene().getWindow();
		// load up OTHER FXML document
		root = FXMLLoader.load(getClass().getResource("HawkerLogin.fxml"));

		Scene scene = new Scene(root);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.setMaximized(true);
		Main.primaryStage.show();
	}
	
	private void populateAdminHeaders() {
		HashMap<String,String> adminMap = Main.getAdminDetails();
		adminAgencyName.setText(adminMap.get("name"));
		adminMobileLabel.setText(adminMap.get("mobile"));
		adminAddrLabel.setText(adminMap.get("addr"));
		
	}


}
