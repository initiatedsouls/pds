package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class EditLineDistributorController implements Initializable {
	private LineDistributor lineDistRow;

	@FXML
	public GridPane gridPane;
	// Edit Customer Fields FXML
	@FXML
	private TextField editNameTF;
	@FXML
	private TextField editMobileNumTF;
	@FXML
	private TextField editOldHouseNumTF;
	@FXML
	private TextField editNewHouseNumTF;
	@FXML
	private TextField editAddrLine1;
	@FXML
	private TextField editAddrLine2;
	@FXML
	private TextField editLocalityTF;
	@FXML
	private TextField editCityTF;
	@FXML
	private ComboBox<String> editStateLOV;
	@FXML
	private ComboBox<String> editHwkCode;
	@FXML
	private ComboBox<String> editLineLOV;
	@FXML
	private ComboBox<String> editProfile1TF;
	@FXML
	private ComboBox<String> editProfile2TF;
	@FXML
	private TextField editProfile3TF;
	@FXML
	private TextField initialsTF;
	@FXML
	private TextField editBldgStreetTF;
	@FXML
	private ComboBox<String> editEmploymentLOV;
	@FXML
	private TextField editCommentsTF;

	private ObservableList<String> lineNumData = FXCollections.observableArrayList();
	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setLineDistToEdit(LineDistributor lineDist) {
		this.lineDistRow = lineDist;
	}

	public void setupBindings() {
		editStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		editStateLOV.getSelectionModel().select(lineDistRow.getState());
		editLineLOV.getSelectionModel().clearSelection();
		editLineLOV.setDisable(true);
		populateHawkerCodes();
		populateEmploymentValues();
		populateProfileValues();
		editLineLOV.getItems().addAll(lineNumData);

		editNameTF.setText(lineDistRow.getName());
		editMobileNumTF.setText(lineDistRow.getMobileNum());
		editOldHouseNumTF.setText(lineDistRow.getOldHouseNum());
		editNewHouseNumTF.setText(lineDistRow.getNewHouseNum());
		editAddrLine1.setText(lineDistRow.getAddrLine1());
		editAddrLine2.setText(lineDistRow.getAddrLine2());
		editLocalityTF.setText(lineDistRow.getLocality());
		editCityTF.setText(lineDistRow.getCity());
		editProfile1TF.getSelectionModel().select(lineDistRow.getProfile1());
		editProfile2TF.getSelectionModel().select(lineDistRow.getProfile2());
		editProfile3TF.setText(lineDistRow.getProfile3());
		initialsTF.setText(lineDistRow.getInitials());
		editProfile1TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile1TF);
		editProfile2TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile2TF);
		editProfile1TF.getSelectionModel().select(lineDistRow.getProfile1());
		editProfile2TF.getSelectionModel().select(lineDistRow.getProfile2());
		editProfile3TF.setText(lineDistRow.getProfile3());
		editEmploymentLOV.getItems().addAll(employmentData);
		new AutoCompleteComboBoxListener<>(editEmploymentLOV);
		editEmploymentLOV.getSelectionModel().select(lineDistRow.getEmployment());
		editCommentsTF.setText(lineDistRow.getComments());
		editHwkCode.getItems().addAll(hawkerCodeData);
		editHwkCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				editLineLOV.setDisable(false);
				editLineLOV.getItems().clear();
				populateLineNumbersForHawkerCode(newValue);
				editLineLOV.getItems().addAll(lineNumData);
			}

		});
		editHwkCode.getSelectionModel().select(lineDistRow.getHawkerCode());
		editLineLOV.getSelectionModel().select("" + lineDistRow.getLineNum());
		initialsTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					initialsTF.setText(oldValue);
			}
		});
		editNameTF.requestFocus();
	}

	private void populateLineNumbers() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			lineNumData.clear();
			PreparedStatement stmt = con
					.prepareStatement("select distinct line_num from line_info where hawker_id = ? and line_num<>0");
			stmt.setLong(1, lineDistRow.getHawkerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				lineNumData.add(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public LineDistributor returnUpdatedLineDistributor() {
		if (isLineDistValid()) {
			lineDistRow.setName(editNameTF.getText());
			lineDistRow.setMobileNum(editMobileNumTF.getText());
			lineDistRow.setOldHouseNum(editOldHouseNumTF.getText());
			lineDistRow.setNewHouseNum(editNewHouseNumTF.getText());
			lineDistRow.setAddrLine1(editAddrLine1.getText());
			lineDistRow.setAddrLine2(editAddrLine2.getText());
			lineDistRow.setLocality(editLocalityTF.getText());
			lineDistRow.setCity(editCityTF.getText());
			lineDistRow.setState(editStateLOV.getSelectionModel().getSelectedItem());
			lineDistRow.setProfile1(editProfile1TF.getSelectionModel().getSelectedItem());
			lineDistRow.setProfile2(editProfile2TF.getSelectionModel().getSelectedItem());
			lineDistRow.setProfile3(editProfile3TF.getText());
			lineDistRow.setInitials(initialsTF.getText());
			lineDistRow.updateLineDistRecord();
			return lineDistRow;
		} else
			return null;
	}

	private void populateProfileValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			profileValues = FXCollections.observableArrayList();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PROFILE_VALUES' order by seq");
			while (rs.next()) {
				if (profileValues != null && !profileValues.contains(rs.getString(1)))
					profileValues.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private void populateEmploymentValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			employmentData = FXCollections.observableArrayList();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='EMPLOYMENT_STATUS' order by seq");
			while (rs.next()) {
				if (employmentData != null && !employmentData.contains(rs.getString(1)))
					employmentData.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private void populateHawkerCodes() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerCodeData=FXCollections.observableArrayList();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct hawker_code from hawker_info order by hawker_code");
			while (rs.next()) {
				if(hawkerCodeData!=null && !hawkerCodeData.contains(rs.getString(1)))
				hawkerCodeData.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public boolean lineDistForLineExists(String line_num) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con
					.prepareStatement("select count(*) from line_distributor where line_id = ? and line_dist_id <> ?");
			stmt.setLong(1, lineDistRow.getLineId());
			stmt.setLong(2, lineDistRow.getLineDistId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return false;
	}

	private long hawkerIdForCode(String hawkerCode) {

		long hawkerId = -1;
		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement hawkerIdStatement = null;
			String hawkerIdQuery = "select hawker_id from hawker_info where hawker_code = ?";
			hawkerIdStatement = con.prepareStatement(hawkerIdQuery);
			hawkerIdStatement.setString(1, hawkerCode);
			ResultSet hawkerIdRs = hawkerIdStatement.executeQuery();

			if (hawkerIdRs.next()) {
				hawkerId = hawkerIdRs.getLong(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	private void populateLineNumbersForHawkerCode(String hawkerCode) {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			lineNumData.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_num=ld.line_num(+) and li.hawker_id = ? and li.line_num<>0 order by li.line_num");
			stmt.setLong(1, hawkerIdForCode(hawkerCode));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				lineNumData.add(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public boolean isLineDistValid() {
		boolean validate = true;
		if (lineDistForLineExists(editLineLOV.getSelectionModel().getSelectedItem().split(" ")[0].trim())) {
			Notifications.create().title("Invalid Line Number")
					.text("A line distributor already exists for this line number.").hideAfter(Duration.seconds(5))
					.showError();
			validate = false;
		}
		if (editNameTF.getText().isEmpty()) {
			Notifications.create().title("Invalid Name").text("A line distributor cannot be created with empty name")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (editProfile3TF.getText() != null && checkExistingProfileValue(editProfile3TF.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		return validate;
	}

	private boolean checkExistingProfileValue(String profileValue) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con
					.prepareStatement("select value from lov_lookup where code = 'PROFILE_VALUES' AND lower(VALUE)=?");
			stmt.setString(1, profileValue.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return false;
	}

	public void releaseVariables() {
		hawkerCodeData = null;
		employmentData = null;
		profileValues = null;
		lineNumData = null;
	}

}
