package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

public class HawkerProfileController implements Initializable{
	private Hawker hawkerRow = HawkerLoginController.loggedInHawker;

	@FXML
    private Label adminAgencyName;

    @FXML
    private Label adminMobileLabel;

    @FXML
    private Label adminAddrLabel;

    @FXML
    private Label editAgencyNameTF;

    @FXML
    private Label editNameTF;

    @FXML
    private Label editHawkerCodeTF;

    @FXML
    private Label editMobileNumTF;

    @FXML
    private Label editBillCategoryTF;

	public void initialize(URL location, ResourceBundle resources) {
		setupBindings();
		
	}


	private void setupBindings() {
		populateBillCategory();
		editAgencyNameTF.setText(hawkerRow.getAgencyName());
		editNameTF.setText(hawkerRow.getName());
		editHawkerCodeTF.setText(hawkerRow.getHawkerCode());
		editMobileNumTF.setText(hawkerRow.getMobileNum());
		
	}


	@FXML
	private void changePasswordClicked(ActionEvent evt) {
		TextInputDialog changePwdDialog = new TextInputDialog();
		changePwdDialog.setTitle("Change password");
		changePwdDialog.setHeaderText("Please enter the new password. \nPassword must be atleast 5 characters long.");
		// changePwdDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
		// ButtonType.CANCEL);
		final Button btOk = (Button) changePwdDialog.getDialogPane().lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (changePwdDialog.getEditor().getText().isEmpty() || changePwdDialog.getEditor().getText().length() < 5) {
				Notifications.create().title("Empty password")
						.text("Password cannot be left empty and must be more than 5 characters. Try again.")
						.hideAfter(Duration.seconds(5)).showError();
				event.consume();
			}
		});
		Optional<String> result = changePwdDialog.showAndWait();
		if (result.isPresent()) {
			// changeHawkerPwd(hawkerRow,result.get());
			HawkerLoginController.loggedInHawker.setPassword(result.get());
			HawkerLoginController.loggedInHawker.updateHawkerRecord();
			Notifications.create().title("Password updated").text("Password was successfully updated")
					.hideAfter(Duration.seconds(5)).showInformation();
		}
	}

	@FXML
	private void changeMobileClicked(ActionEvent evt) {
		Notifications.create().title("Please login again")
		.text("If you update Hawker profile, then please logout and login again to see changes.")
		.hideAfter(Duration.seconds(5)).showInformation();
		AHawkerInfoTabController.showEditHawkerDialog(HawkerLoginController.loggedInHawker);

		
	}
	
	@FXML
	private void viewHawkerClicked(ActionEvent evt){
		try {

			Dialog<Hawker> editHawkerDialog = new Dialog<Hawker>();
			editHawkerDialog.setTitle("View hawker data");
			editHawkerDialog.setHeaderText("View the hawker data below");

			editHawkerDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editHawkerLoader = new FXMLLoader(getClass().getResource("EditHawker.fxml"));
			Parent editHawkerGrid = (Parent) editHawkerLoader.load();
			EditHawkerController editHawkerController = editHawkerLoader.<EditHawkerController> getController();

			editHawkerDialog.getDialogPane().setContent(editHawkerGrid);
			editHawkerController.setHawkerToEdit(HawkerLoginController.loggedInHawker);
			editHawkerController.setupBindings();
			editHawkerController.gridPane.setDisable(true);
			Optional<Hawker> updatedHawker = editHawkerDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void populateAdminHeaders() {

		HashMap<String, String> adminMap = Main.getAdminDetails();
		adminAgencyName.setText(adminMap.get("name"));
		adminMobileLabel.setText(adminMap.get("mobile"));
		adminAddrLabel.setText(adminMap.get("addr"));

	}

	private void populateBillCategory() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select bill_category from point_name where name =? order by bill_category";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				editBillCategoryTF.setText(rs.getString(1));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public void reloadData(){
		setupBindings();
		populateAdminHeaders();
	}

}
