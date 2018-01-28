package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import com.amazonaws.util.NumberUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class EditCustomerController implements Initializable {

	private Customer custRow;

	// Edit Customer Fields FXML
	@FXML
	private TextField editNameTF;
	@FXML
	private TextField editCustomerCodeTF;
	@FXML
	private TextField editMobileNumTF;
	@FXML
	private TextField editHouseSeqTF;
	@FXML
	private TextField editOldHouseNumTF;
	@FXML
	private TextField editNewHouseNumTF;
	@FXML
	private TextField editBldgStreetTF;
	@FXML
	private TextField editAddrLine1;
	@FXML
	private TextField editAddrLine2;
	@FXML
	private TextField editLocalityTF;
	@FXML
	private TextField editCityTF;
	@FXML
	private ComboBox<String> editProfile1TF;
	@FXML
	private ComboBox<String> editProfile2TF;
	@FXML
	private TextField editProfile3TF;
	@FXML
	private TextField editCommentsTF;
	@FXML
	private TextField initialsTF;
	@FXML
	private ComboBox<String> editHawkerCodeLOV;
	@FXML
	private ComboBox<String> editLineNumLOV;
	@FXML
	private ComboBox<String> editStateLOV;
	@FXML
	private ComboBox<String> editEmploymentLOV;
	@FXML
	public GridPane gridPane;

	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerLineNumData = FXCollections.observableArrayList();
	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private int seq;
	Gson gson = new GsonBuilder().serializeNulls().create();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// setupBindings();

	}

	public void setCustomerToEdit(Customer customer) {
		this.custRow = customer;
	}

	public void setupBindings() {
		editStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		new AutoCompleteComboBoxListener<>(editStateLOV);
		editStateLOV.getSelectionModel().select(custRow.getState());
		populateHawkerCodes();
		populateEmploymentValues();
		populateProfileValues();
		editHawkerCodeLOV.setItems(hawkerCodeData);
		new AutoCompleteComboBoxListener<>(editHawkerCodeLOV);

		editHawkerCodeLOV.getSelectionModel().select(custRow.getHawkerCode());
		editLineNumLOV.getItems().clear();
		populateLineNumbersForHawkerCode(custRow.getHawkerCode());
		editLineNumLOV.getItems().addAll(hawkerLineNumData);
		editLineNumLOV.getSelectionModel().select("" + custRow.getLineNum());
		if (HawkerLoginController.loggedInHawker == null)
			editHawkerCodeLOV.setDisable(false);
		editHawkerCodeLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				editLineNumLOV.getItems().clear();
				populateLineNumbersForHawkerCode(newValue);
				editLineNumLOV.getItems().addAll(hawkerLineNumData);
				editLineNumLOV.getSelectionModel().clearSelection();
			}
		});
		seq = maxSeq();
		editLineNumLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				seq = maxSeq();
			}
		});
		editNameTF.setText(custRow.getName());
		editCustomerCodeTF.setText("" + custRow.getCustomerCode());
		editMobileNumTF.setText(custRow.getMobileNum());
		editHouseSeqTF.setText("" + custRow.getHouseSeq());
		editOldHouseNumTF.setText(custRow.getOldHouseNum());
		editNewHouseNumTF.setText(custRow.getNewHouseNum());
		editAddrLine1.setText(custRow.getAddrLine1());
		editAddrLine2.setText(custRow.getAddrLine2());
		editLocalityTF.setText(custRow.getLocality());
		editCityTF.setText(custRow.getCity());
		editProfile1TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile1TF);
		editProfile2TF.getItems().addAll(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile2TF);
		editProfile1TF.getSelectionModel().select(custRow.getProfile1());
		editProfile2TF.getSelectionModel().select(custRow.getProfile2());
		editProfile3TF.setText(custRow.getProfile3());
		editEmploymentLOV.getItems().addAll(employmentData);
		new AutoCompleteComboBoxListener<>(editEmploymentLOV);
		editEmploymentLOV.getSelectionModel().select(custRow.getEmployment());
		editCommentsTF.setText(custRow.getComments());
		initialsTF.setText(custRow.getInitials());
		editBldgStreetTF.setText(custRow.getBuildingStreet());
		initialsTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					initialsTF.setText(oldValue);
			}
		});
		editNameTF.requestFocus();
	}

	private void populateLineNumbersForHawkerCode(String hawkerCode) {

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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	private void populateHawkerCodes() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerCodeData = FXCollections.observableArrayList();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select distinct hawker_code from hawker_info order by hawker_code");
			while (rs.next()) {
				if (hawkerCodeData != null && !hawkerCodeData.contains(rs.getString(1)))
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

	public boolean isValid() {
		boolean validate = true;
		if (editNameTF.getText() == null || editNameTF.getText().isEmpty()) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Customer Name not provided")
					.text("Please provide a customer name before adding the the customer").showError();
		}
		if (editHawkerCodeLOV.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
					.text("Please select a hawker before adding the the customer").showError();
		}
		if (editLineNumLOV.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Line number not selected")
					.text("Please select a line number before adding the the customer").showError();
		}
		if (NumberUtils.tryParseInt(editHouseSeqTF.getText()) == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid house number")
					.text("House sequence should not be empty and must be NUMBERS only").showError();
		}
		if (Integer.parseInt(editHouseSeqTF.getText()) > seq || Integer.parseInt(editHouseSeqTF.getText()) < 1) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid House Sequence")
					.text("House Sequence must be between 1 and " + seq).showError();
		}
		if (editProfile3TF.getText() != null && checkExistingProfileValue(editProfile3TF.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		return validate;
	}

	public Customer returnUpdatedCustomer() {
		if (isValid()) {
			shuffleHouseSequences();
			Customer edittedCustomer = new Customer(custRow);
			edittedCustomer.setCustomerCode(Long.parseLong(editCustomerCodeTF.getText()));
			edittedCustomer.setName(editNameTF.getText());
			edittedCustomer.setMobileNum(editMobileNumTF.getText());
			edittedCustomer.setHawkerCode(editHawkerCodeLOV.getSelectionModel().getSelectedItem());
			edittedCustomer
					.setLineNum(Integer.parseInt(editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0]));
			edittedCustomer.setHouseSeq(Integer.parseInt(editHouseSeqTF.getText().trim()));
			edittedCustomer.setOldHouseNum(editOldHouseNumTF.getText());
			edittedCustomer.setNewHouseNum(editNewHouseNumTF.getText());
			edittedCustomer.setAddrLine1(editAddrLine1.getText());
			edittedCustomer.setAddrLine2(editAddrLine2.getText());
			edittedCustomer.setLocality(editLocalityTF.getText());
			edittedCustomer.setCity(editCityTF.getText());
			edittedCustomer.setState(editStateLOV.getSelectionModel().getSelectedItem());
			edittedCustomer.setProfile1(editProfile1TF.getSelectionModel().getSelectedItem());
			edittedCustomer.setProfile2(editProfile2TF.getSelectionModel().getSelectedItem());
			edittedCustomer.setProfile3(editProfile3TF.getText());
			edittedCustomer.setInitials(initialsTF.getText());
			edittedCustomer.setBuildingStreet(editBldgStreetTF.getText());
			edittedCustomer.setEmployment(editEmploymentLOV.getSelectionModel().getSelectedItem());
			edittedCustomer.setComments(editCommentsTF.getText());
			edittedCustomer.setHawkerId(ACustomerInfoTabController
					.hawkerIdForCode(editHawkerCodeLOV.getSelectionModel().getSelectedItem()));
			edittedCustomer.setLineId(ACustomerInfoTabController.lineIdForNumHwkCode(
					Integer.parseInt(editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0]),
					editHawkerCodeLOV.getSelectionModel().getSelectedItem()));
//			editCustomerFunction();
			
			edittedCustomer.updateCustomerRecord();
			
			return edittedCustomer;
		} else
			return null;
	}
	private void editCustomerFunction() {

		String ACCESS_KEY = "AKIAIZ53M3FYTTP6B5GQ";
		String SECRET = "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";
//		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("customerId", custRow.getCustomerId().toString());
		map.put("customerCode", (new Long(custRow.getCustomerCode())).toString());
		map.put("name", custRow.getName());
		map.put("mobileNum", custRow.getMobileNum());
		map.put("hawkerCode", custRow.getHawkerCode());
		map.put("lineNum", custRow.getLineNum().toString());
		map.put("houseSeq", new Integer(custRow.getHouseSeq()).toString());
		map.put("oldHouseNum", custRow.getOldHouseNum());
		map.put("newHouseNum", custRow.getNewHouseNum());
		map.put("addrLine1", custRow.getAddrLine1());
		map.put("addrLine2", custRow.getAddrLine2());
		map.put("locality", custRow.getLocality());
		map.put("city", custRow.getCity());
		map.put("state", custRow.getState());
		map.put("profile1", custRow.getProfile1());
		map.put("profile2", custRow.getProfile2());
		map.put("profile3", custRow.getProfile3());
		map.put("initials", custRow.getInitials());
		map.put("employment", custRow.getEmployment());
		map.put("comments", custRow.getComments());
		map.put("buildingStreet", custRow.getBuildingStreet());
		map.put("totalDue", new Double(custRow.getTotalDue()).toString());
		map.put("hawkerId", custRow.getHawkerId().toString());
		map.put("lineId", custRow.getLineId().toString());
        lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        try {
            InvokeRequest invokeRequest = new InvokeRequest();
            invokeRequest.setFunctionName("EditCustomer");
//            HashMap<String,String> map = new HashMap<String,String>();
//            map.put("hawkerId", HawkerLoginController.loggedInHawker.getHawkerId().toString());
//            map.put("lineId",lineNumTable.getSelectionModel().getSelectedItem().getLineId().toString());
            invokeRequest.setPayload(ByteBuffer.wrap(gson.toJson(map).getBytes()));
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
            System.out.println(gson.fromJson(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))), ArrayList.class));
        }catch (LambdaFunctionException e){
        	e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
	}
	private void shuffleHouseSequences() {
		if (this.custRow.getHawkerCode().equals(editHawkerCodeLOV.getSelectionModel().getSelectedItem())) {
			if (this.custRow.getLineNum() == Long
					.parseLong(editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0])) {
				if (this.custRow.getHouseSeq() != Integer.parseInt(editHouseSeqTF.getText())) {
					shiftHouseSeqFromToForCustId(
							getCustomerDataToShift(this.custRow.getHawkerCode(), this.custRow.getLineNum().intValue()),
							this.custRow.getHouseSeq(), Integer.parseInt(editHouseSeqTF.getText()),
							this.custRow.getCustomerId());
				}
			} else {
				shiftHouseSeqForDelete(
						getCustomerDataToShift(this.custRow.getHawkerCode(), this.custRow.getLineNum().intValue()),
						this.custRow.getHouseSeq());
				shiftHouseSeqFrom(
						getCustomerDataToShift(this.custRow.getHawkerCode(),
								Integer.parseInt(editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0])),
						Integer.parseInt(editHouseSeqTF.getText()));
			}
		} else {
			shiftHouseSeqForDelete(
					getCustomerDataToShift(this.custRow.getHawkerCode(), this.custRow.getLineNum().intValue()),
					this.custRow.getHouseSeq());
			shiftHouseSeqFrom(
					getCustomerDataToShift(editHawkerCodeLOV.getSelectionModel().getSelectedItem(),
							Integer.parseInt(editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0])),
					Integer.parseInt(editHouseSeqTF.getText()));
		}

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

	public boolean validateCustomer() {
		if (mobileNumExists(editMobileNumTF.getText())) {
			Notifications.create().hideAfter(Duration.seconds(5)).title("Duplicate mobile number")
					.text("A customer with same mobile number already exists").showError();
			return false;
		}
		return true;
	}

	private boolean mobileNumExists(String mobileNum) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select customer_code,name from customer where mobile_num=? and customer_id <> ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, mobileNum);
			stmt.setLong(2, custRow.getCustomerId());
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

	private void populateProfileValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			profileValues=FXCollections.observableArrayList();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PROFILE_VALUES' order by seq");
			while (rs.next()) {
				if(profileValues!=null && !profileValues.contains(rs.getString(1)))
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

	public ArrayList<Customer> getCustomerDataToShift(String hawkerCode, int lineNum) {
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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

		return custData;
	}

	private void shiftHouseSeqFrom(ArrayList<Customer> custData, int seq) {

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
		// reloadData();
	}

	private void shiftHouseSeqForDelete(ArrayList<Customer> custData, int seq) {

		for (int i = 0; i < custData.size(); i++) {
			Customer cust = custData.get(i);
			if (cust != null && cust.getHouseSeq() >= seq) {
				cust.setHouseSeq(cust.getHouseSeq() - 1);
				cust.updateCustomerRecord();
			}
		}
		// reloadData();
	}

	private void shiftHouseSeqFromToForCustId(ArrayList<Customer> custData, int fromSeq, int toSeq, long custId) {
		if (fromSeq < toSeq) {
			for (int i = 0; i < custData.size(); i++) {
				Customer cust = custData.get(i);
				if (cust != null && cust.getHouseSeq() > fromSeq && cust.getHouseSeq() <= toSeq
						&& cust.getCustomerId() != custId) {
					cust.setHouseSeq(cust.getHouseSeq() - 1);
					cust.updateCustomerRecord();
				}
			}
		} else if (fromSeq > toSeq) {
			for (int i = 0; i < custData.size(); i++) {
				Customer cust = custData.get(i);
				if (cust != null && cust.getHouseSeq() < fromSeq && cust.getHouseSeq() >= toSeq
						&& cust.getCustomerId() != custId) {
					cust.setHouseSeq(cust.getHouseSeq() + 1);
					cust.updateCustomerRecord();
				}
			}
		}

		// reloadData();
	}

	public boolean houseSequenceExistsInLine(String hawkerCode, int seq, Long lineNum) {
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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return false;
	}

	private int maxSeq() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerLineNumData.clear();
			PreparedStatement stmt = con
					.prepareStatement("select max(house_seq) seq from customer where hawker_code=? and line_num=?");
			stmt.setString(1, editHawkerCodeLOV.getSelectionModel().getSelectedItem());
			stmt.setString(2, editLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0]);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return 1;
	}

	public void setPausedCust(long customerId) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			// populateHawkerCodes();
			String queryString;
			PreparedStatement stmt;
			queryString = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where customer_id=? ";
			stmt = con.prepareStatement(queryString);
			stmt.setLong(1, customerId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				custRow = new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getLong(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),
						rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
						rs.getString(21), rs.getDouble(22), rs.getLong(23), rs.getLong(24));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void releaseVariables() {

		hawkerCodeData = null;
		hawkerLineNumData = null;
		employmentData = null;
		profileValues = null;
	}
}
