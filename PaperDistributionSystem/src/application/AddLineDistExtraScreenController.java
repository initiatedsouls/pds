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
import javafx.util.Duration;

public class AddLineDistExtraScreenController implements Initializable {

	// Edit Customer Fields FXML
	@FXML
	private TextField addNameTF;
	@FXML
	private TextField addMobileNumTF;
	@FXML
	private ComboBox<String> addHwkCode;
	@FXML
	private ComboBox<String> addLineNumField;
	@FXML
	private TextField addOldHouseNumTF;
	@FXML
	private TextField addNewHouseNumTF;
	@FXML
	private TextField addAddrLine1;
	@FXML
	private TextField addAddrLine2;
	@FXML
	private TextField addLocalityTF;
	@FXML
	private TextField addCityTF;
	@FXML
	private ComboBox<String> addStateLOV;
	@FXML
	private ComboBox<String> addProfile1TF;
	@FXML
	private ComboBox<String> addProfile2TF;
	@FXML
	private TextField addProfile3TF;
	@FXML
	private TextField initialsTF;
	@FXML
	private TextField addBldgStreetTF;
	@FXML
	private ComboBox<String> addEmploymentLOV;
	@FXML
	private TextField addCommentsTF;
	private ObservableList<String> lineNumData = FXCollections.observableArrayList();
	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setupBindings() {
		addStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");

		populateHawkerCodes();
		populateEmploymentValues();
		populateProfileValues();
		addHwkCode.getItems().addAll(hawkerCodeData);
		new AutoCompleteComboBoxListener<>(addHwkCode);

		if (HawkerLoginController.loggedInHawker == null) {

			addHwkCode.setDisable(false);
		} else {
			addHwkCode.getSelectionModel().select(HawkerLoginController.loggedInHawker.getHawkerCode());
			addHwkCode.setDisable(true);
			populateLineNumbersForHawkerCode(addHwkCode.getSelectionModel().getSelectedItem());
		}

		addHwkCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				addLineNumField.setDisable(false);
				addLineNumField.getItems().clear();
				populateLineNumbersForHawkerCode(newValue);
			}

		});
		// addLineNumField.getItems().addAll(lineNumData);
		addProfile1TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(addProfile1TF);
		addProfile2TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(addProfile2TF);
		addEmploymentLOV.getItems().addAll(employmentData);
		new AutoCompleteComboBoxListener<>(addEmploymentLOV);
		initialsTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					initialsTF.setText(oldValue);
			}
		});
		addNameTF.requestFocus();
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
			addLineNumField.getItems().clear();
			addLineNumField.setItems(lineNumData);
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

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

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	public boolean isValid() {
		boolean validate = true;
		if (addNameTF.getText() == null) {
			Notifications.create().title("Empty Name").text("Name cannot be empty. Please enter value for name.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addMobileNumTF.getText() == null) {
			Notifications.create().title("Empty Mobile").text("Mobile cannot be empty. Please enter value for mobile.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addLineNumField.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Empty Line Number")
					.text("Line Number cannot be empty. Please enter value for Line Number.").showError();

		}
		if (lineDistForLineExists(addLineNumField.getSelectionModel().getSelectedItem().split(" ")[0].trim())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Line Distributor exists")
					.text("Line Distributor already exists for this line number").showError();
		}
		if (addProfile3TF.getText() != null && checkExistingProfileValue(addProfile3TF.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		// if (addMobileNumTF.getText().length()!=10) {
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// validate = false;
		// }
		// try {
		// Integer.parseInt(addMobileNumTF.getText());
		// } catch (NumberFormatException e) {
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// validate = false;
		// Main._logger.debug("Error :",e); e.printStackTrace();
		// }
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

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return false;
	}

	public void addLineDistributor() {

		try {

			if (isValid()) {
				PreparedStatement insertLineNum = null;
				String insertStatement = "INSERT INTO LINE_DISTRIBUTOR(NAME, MOBILE_NUM, LINE_NUM,HAWKER_ID,old_house_num, new_house_num, address_line1, address_line2, locality, city, state,profile1,profile2,profile3,initials,LINE_ID) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Connection con = Main.dbConnection;
				try {
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					insertLineNum = con.prepareStatement(insertStatement);
					long hawkerId;
					if (HawkerLoginController.loggedInHawker != null)
						hawkerId = HawkerLoginController.loggedInHawker.getHawkerId();
					else
						hawkerId = hawkerIdForCode(addHwkCode.getSelectionModel().getSelectedItem());
					insertLineNum.setString(1, addNameTF.getText());
					insertLineNum.setString(2, addMobileNumTF.getText());
					insertLineNum.setString(3,
							addLineNumField.getSelectionModel().getSelectedItem().split(" ")[0].trim());
					insertLineNum.setLong(4, hawkerId);
					insertLineNum.setString(5, addOldHouseNumTF.getText());
					insertLineNum.setString(6, addNewHouseNumTF.getText());
					insertLineNum.setString(7, addAddrLine1.getText());
					insertLineNum.setString(8, addAddrLine2.getText());
					insertLineNum.setString(9, addLocalityTF.getText());
					insertLineNum.setString(10, addCityTF.getText());
					insertLineNum.setString(11, addStateLOV.getSelectionModel().getSelectedItem());
					insertLineNum.setString(12, addProfile1TF.getSelectionModel().getSelectedItem());
					insertLineNum.setString(13, addProfile2TF.getSelectionModel().getSelectedItem());
					insertLineNum.setString(14,
							addProfile3TF.getText() == null ? null : addProfile3TF.getText().toLowerCase());
					insertLineNum.setString(15, initialsTF.getText());
					insertLineNum.setLong(16, ACustomerInfoTabController.lineIdForNumHwkCode(Integer.parseInt(addLineNumField.getSelectionModel().getSelectedItem().split(" ")[0].trim()), addHwkCode.getSelectionModel().getSelectedItem()));
					insertLineNum.execute();
				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
			}

		} catch (NumberFormatException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
					.text("Please enter proper numeric value in Line Number field").showError();
		}

	}

	public boolean lineDistForLineExists(String line_num) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con
					.prepareStatement("select count(*) from line_distributor where hawker_id = ? and line_num=?");
			stmt.setLong(1, hawkerIdForCode(addHwkCode.getSelectionModel().getSelectedItem()));
			stmt.setString(2, line_num);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return false;
	}

	private void populateProfileValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			profileValues.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PROFILE_VALUES' order by seq");
			while (rs.next()) {
				if(profileValues!=null && !profileValues.contains(rs.getString(1)))
				profileValues.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	private void populateEmploymentValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			employmentData.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='EMPLOYMENT_STATUS' order by seq");
			while (rs.next()) {
				if(employmentData!=null && !employmentData.contains(rs.getString(1)))
				employmentData.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	private void populateHawkerCodes() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerCodeData.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct hawker_code from hawker_info order by hawker_code");
			while (rs.next()) {
				if(hawkerCodeData!=null && !hawkerCodeData.contains(rs.getString(1)))
				hawkerCodeData.add(rs.getString(1));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	public void releaseVariables() {
		hawkerCodeData = null;
		employmentData = null;
		profileValues = null;
		lineNumData = null;
	}

}
