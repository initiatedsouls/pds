package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.amazonaws.util.NumberUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AddCustomerExtraScreenController implements Initializable {

	// Edit Customer Fields FXML
	@FXML
	private TextField addNameTF;
	@FXML
	private TextField addMobileNumTF;
	@FXML
	private TextField addHouseSeqTF;
	@FXML
	private TextField addOldHouseNumTF;
	@FXML
	private TextField addNewHouseNumTF;
	@FXML
	private TextField addBldgStreetTF;
	@FXML
	private TextField addAddrLine1;
	@FXML
	private TextField addAddrLine2;
	@FXML
	private TextField addLocalityTF;
	@FXML
	private TextField addCityTF;
	@FXML
	private ComboBox<String> addProfile1TF;
	@FXML
	private ComboBox<String> addProfile2TF;
	@FXML
	private TextField addProfile3TF;
	@FXML
	private TextField addCommentsTF;
	@FXML
	private TextField initialsTF;
	@FXML
	private ComboBox<String> addHawkerCodeLOV;
	@FXML
	private ComboBox<String> addLineNumLOV;
	@FXML
	private ComboBox<String> addStateLOV;
	@FXML
	private ComboBox<String> addEmploymentLOV;

	private int seq = 0;

	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerLineNumData = FXCollections.observableArrayList();
	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();

	public boolean customerInserted = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main._logger.debug("Entered  initialize  method");

	}

	public void setupBindings() {
		Main._logger.debug("Entered  setupBindings  method");
		addStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		addLineNumLOV.getItems().clear();
		addHawkerCodeLOV.getItems().clear();
		populateHawkerCodes();
		populateEmploymentValues();
		populateProfileValues();
		addHawkerCodeLOV.getItems().addAll(hawkerCodeData);
		new AutoCompleteComboBoxListener<>(addHawkerCodeLOV);
		addNameTF.requestFocus();

		addLineNumLOV.getItems().addAll(hawkerLineNumData);

		if (HawkerLoginController.loggedInHawker == null) {

			addHawkerCodeLOV.setDisable(false);
		} else {
			addHawkerCodeLOV.getSelectionModel().select(HawkerLoginController.loggedInHawker.getHawkerCode());
			addHawkerCodeLOV.setDisable(true);
			populateLineNumbersForHawkerCode(addHawkerCodeLOV.getSelectionModel().getSelectedItem());
			addLineNumLOV.getItems().clear();
			addLineNumLOV.getItems().addAll(hawkerLineNumData);
		}

		addHawkerCodeLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue!=null) {
					addLineNumLOV.getItems().clear();
					populateLineNumbersForHawkerCode(newValue);
					addLineNumLOV.getItems().addAll(hawkerLineNumData);
					addLineNumLOV.getSelectionModel().clearSelection();
				}
			}
		});

		addLineNumLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				seq = maxSeq();
				seq = seq == 0 ? 1 : seq;
				addHouseSeqTF.setText(seq + "");
			}
		});
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
	}

	public void setupHawkerAndLine(String hawkerCode, String lineNum) {
		Main._logger.debug("Entered  setupHawkerAndLine  method");
		addHawkerCodeLOV.getSelectionModel().select(hawkerCode);
		addLineNumLOV.getSelectionModel().select(lineNum);
		addHawkerCodeLOV.setDisable(true);
		addLineNumLOV.setDisable(true);
		seq = maxSeq();
		seq = seq == 0 ? 1 : seq;
		addHouseSeqTF.setText(seq + "");
	}

	private int maxSeq() {
		Main._logger.debug("Entered  maxSeq  method");
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con
					.prepareStatement("select max(house_seq)+1 seq from customer where hawker_code=? and line_num=?");
			stmt.setString(1, addHawkerCodeLOV.getSelectionModel().getSelectedItem());
			stmt.setString(2, addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0]);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return 1;
	}

	private void populateLineNumbersForHawkerCode(String hawkerCode) {
		Main._logger.debug("Entered  populateLineNumbersForHawkerCode  method");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerLineNumData.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_num=ld.line_num(+) and li.hawker_id = ? and li.line_num<>0 order by li.line_num");
			stmt.setLong(1, hawkerIdForCode(hawkerCode));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				hawkerLineNumData.add(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private long hawkerIdForCode(String hawkerCode) {
		Main._logger.debug("Entered  hawkerIdForCode  method");

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

	private void populateHawkerCodes() {
		Main._logger.debug("Entered  populateHawkerCodes  method");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerCodeData.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct hawker_code from hawker_info order by hawker_code");
			while (rs.next()) {
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

	private void populateProfileValues() {
		Main._logger.debug("Entered  populateProfileValues  method");
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
		Main._logger.debug("Entered  populateEmploymentValues  method");
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

	public boolean isValid() {
		Main._logger.debug("Entered  isValid  method");
		boolean validate = true;
		if (addNameTF.getText() == null || addNameTF.getText().isEmpty()) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Customer Name not provided")
					.text("Please provide a customer name before adding the the customer").showError();
		}
		if (addHawkerCodeLOV.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
					.text("Please select a hawker before adding the the customer").showError();
		}
		if (addLineNumLOV.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Line number not selected")
					.text("Please select a line number before adding the the customer").showError();
		}
		if (NumberUtils.tryParseInt(addHouseSeqTF.getText()) == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid house number")
					.text("House sequence should not be empty and must be NUMBERS only").showError();
		}
		if (Integer.parseInt(addHouseSeqTF.getText()) > seq || Integer.parseInt(addHouseSeqTF.getText()) < 1) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid House Sequence")
					.text("House Sequence must be between 1 and " + seq).showError();
		}
		if (addProfile3TF.getText() != null && checkExistingProfileValue(addProfile3TF.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		return validate;
	}

	public void addCustomer() {
		Main._logger.debug("Entered  addCustomer  method");

		

		if (isValid()) {
			if (houseSequenceExistsInLine(addHawkerCodeLOV.getSelectionModel().getSelectedItem(),
					Integer.parseInt(addHouseSeqTF.getText()),
					Long.parseLong(addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0].trim()))) {
				ArrayList<Customer> custData = getCustomerDataToShift(
						addHawkerCodeLOV.getSelectionModel().getSelectedItem(),
						Integer.parseInt(addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0].trim()));
				shiftHouseSeqFrom(custData, Integer.parseInt(addHouseSeqTF.getText()));
			}

			PreparedStatement insertCustomer = null;
			String insertStatement = "INSERT INTO CUSTOMER(name,mobile_num,hawker_code, line_num, house_seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street,HAWKER_ID, LINE_ID) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = Main.dbConnection;
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertCustomer = con.prepareStatement(insertStatement);
				insertCustomer.setString(1, addNameTF.getText());
				insertCustomer.setString(2, addMobileNumTF.getText());
				insertCustomer.setString(3, addHawkerCodeLOV.getSelectionModel().getSelectedItem());
				// if (!addLineNumLOV.isDisabled())
				insertCustomer.setLong(4,
						Long.parseLong(addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0].trim()));
				// else
				// insertCustomer.setString(4, null);
				if (!addHouseSeqTF.isDisabled())
					insertCustomer.setInt(5, Integer.parseInt(addHouseSeqTF.getText()));
				else
					insertCustomer.setString(5, null);
				insertCustomer.setString(6, addOldHouseNumTF.getText());
				insertCustomer.setString(7, addNewHouseNumTF.getText());
				insertCustomer.setString(8, addAddrLine1.getText());
				insertCustomer.setString(9, addAddrLine2.getText());
				insertCustomer.setString(10, addLocalityTF.getText());
				insertCustomer.setString(11, addCityTF.getText());
				insertCustomer.setString(12, addStateLOV.getSelectionModel().getSelectedItem());
				insertCustomer.setString(13, addProfile1TF.getSelectionModel().getSelectedItem());
				insertCustomer.setString(14, addProfile2TF.getSelectionModel().getSelectedItem());
				insertCustomer.setString(15,
						addProfile3TF.getText() == null ? null : addProfile3TF.getText().toLowerCase());
				insertCustomer.setString(16, initialsTF.getText());
				insertCustomer.setString(17, addEmploymentLOV.getSelectionModel().getSelectedItem());
				insertCustomer.setString(18, addCommentsTF.getText());
				insertCustomer.setString(19, addBldgStreetTF.getText());
				insertCustomer.setLong(20,hawkerIdForCode(addHawkerCodeLOV.getSelectionModel().getSelectedItem()));
				insertCustomer.setLong(21,ACustomerInfoTabController.lineIdForNumHwkCode(Integer.parseInt(addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0].trim()), addHawkerCodeLOV.getSelectionModel().getSelectedItem()));
				
				customerInserted = insertCustomer.executeUpdate() > 0;
				if (customerInserted)
					Notifications.create().text("Customer inserted successfully")
							.text("Customer record was inserted successfully").showInformation();
				con.commit();
				// con.close();

			} catch (SQLException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
						.text("There has been some error during customer creation, please retry").showError();
				Main.reconnect();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}

		}

	}

	public boolean houseSequenceExistsInLine(String hawkerCode, int seq, Long lineNum) {
		Main._logger.debug("Entered  houseSequenceExistsInLine  method");
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials from customer where house_seq=? and line_num=? and hawker_code=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, seq);
			stmt.setLong(2, lineNum);
			stmt.setString(3, hawkerCode);
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

	public ArrayList<Customer> getCustomerDataToShift(String hawkerCode, int lineNum) {
		Main._logger.debug("Entered  getCustomerDataToShift  method");
		ArrayList<Customer> custData = new ArrayList<Customer>();

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_code=? and line_num=? order by house_seq";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, hawkerCode);
			stmt.setInt(2, lineNum);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				custData.add(new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getLong(6), rs.getInt(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19),
						rs.getString(20), rs.getString(21), rs.getDouble(22), rs.getLong(23), rs.getLong(24)));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

		return custData;
	}

	private void shiftHouseSeqFrom(ArrayList<Customer> custData, int seq) {
		Main._logger.debug("Entered  shiftHouseSeqFrom  method");

		for (int i = 0; i < custData.size(); i++) {
			Customer cust = custData.get(i);
			if (cust != null && cust.getHouseSeq() >= seq) {
				if (cust.getHouseSeq() == seq) {
					cust.setHouseSeq(cust.getHouseSeq() + 1);
					cust.updateCustomerRecord();
				} else if (cust.getHouseSeq() > seq) {
					if (i > 0 && custData.get(i - 1).getHouseSeq() == cust.getHouseSeq()) {
						cust.setHouseSeq(cust.getHouseSeq() + 1);
						cust.updateCustomerRecord();
					} else
						return;
				}

			}
		}
	}

	private boolean checkExistingProfileValue(String profileValue) {
		Main._logger.debug("Entered  checkExistingProfileValue  method");
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

	/*private long lineIdForNumHwkCode(int lineNum, String hwkCode) {
		Main._logger.debug("Entered lineIdForNum method");

		long lineId = -1;
		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement lineIdStatement = null;
			String lineIdQuery = "select line_id from line_info where hawker_id = ? and line_num=?";
			lineIdStatement = con.prepareStatement(lineIdQuery);
			lineIdStatement.setLong(1, BillingUtilityClass.hawkerForHwkCode(hwkCode).getHawkerId());
			lineIdStatement.setInt(2, lineNum);
			
			ResultSet lineIdRs = lineIdStatement.executeQuery();

			if (lineIdRs.next()) {
				lineId = lineIdRs.getLong(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return lineId;
	}
	*/
	public void reset() {
		Main._logger.debug("Entered reset   method");
		addNameTF.clear();
		addMobileNumTF.clear();
		addHouseSeqTF.clear();
		addOldHouseNumTF.clear();
		addNewHouseNumTF.clear();
		addBldgStreetTF.clear();
		addAddrLine1.clear();
		addAddrLine2.clear();
		addLocalityTF.clear();
		addCityTF.clear();
		addProfile1TF.getSelectionModel().clearSelection();
		addProfile2TF.getSelectionModel().clearSelection();
		addProfile3TF.clear();
		addCommentsTF.clear();
		initialsTF.clear();
		addHawkerCodeLOV.getSelectionModel().clearSelection();
		addLineNumLOV.getSelectionModel().clearSelection();
		// addStateLOV
		addEmploymentLOV.getSelectionModel().clearSelection();

	}

	public void releaseVariables() {
		Main._logger.debug("Entered  releaseVariables  method");

		hawkerCodeData = null;
		hawkerLineNumData = null;
		employmentData = null;
		profileValues = null;
	}

}
