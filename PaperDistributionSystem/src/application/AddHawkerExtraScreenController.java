package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AddHawkerExtraScreenController implements Initializable {
	// Edit Customer Fields FXML
	@FXML
	private TextField addHwkName;
	@FXML
	private TextField addHwkCode;
	@FXML
	private TextField addHwkMobileNum;
	@FXML
	private TextField addHwkAgencyName;
	@FXML
	private TextField addHwkOldHouseNum;
	@FXML
	private TextField addHwkNewHouseNum;
	@FXML
	private TextField addHwkAddrLine1;
	@FXML
	private TextField addHwkAddrLine2;
	@FXML
	private TextField addHwkLocality;
	@FXML
	private ComboBox<String> addHwkCity;
	@FXML
	private TextField addHwkFee;
	@FXML
	private CheckBox addHwkActiveCheck;
	@FXML
	private ComboBox<String> addHwkStateLOV;
	@FXML
	private ComboBox<String> addHwkProfile1;
	@FXML
	private ComboBox<String> addHwkProfile2;
	@FXML
	private TextField addHwkProfile3;
	@FXML
	private TextField initials;
	@FXML
	private ComboBox<String> addEmploymentLOV;
	@FXML
	private ComboBox<String> addPointNameLOV;
	@FXML
	private TextField addComments;
	@FXML
	private TextField addBldgStreet;
	@FXML
	private TextField addPwd;
	@FXML
	private TextField addBankAcNo;
	@FXML
	private TextField addBankName;
	@FXML
	private TextField addIfscCode;
	@FXML
	private TextField addBenName;

	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main._logger.debug("Entered  initialize  method");

	}

	public void setupBindings() {
		Main._logger.debug("Entered  setupBindings  method");
		addHwkStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		populateEmploymentValues();
		populateProfileValues();
		populateCityValues();
		// populatePointNames();
		addHwkActiveCheck.setSelected(true);

		addHwkCity.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					addPointNameLOV.setDisable(true);
					populatePointNames();
				} else {

				}
			}
		});
		initials.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					initials.setText(oldValue);
			}
		});

		addPointNameLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				addHwkFee.setText(Double.toString(feeFromPoint()));

			}
		});
		addHwkName.requestFocus();
	}

	public static boolean mobileNumExists(String mobileNum, String hwkCode) {
		Main._logger.debug("Entered  mobileNumExists  method");
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select hawker_code,name from hawker_info where mobile_num=? and lower(hawker_code) <> ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, mobileNum);
			stmt.setString(2, hwkCode.toLowerCase());
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

	@FXML
	private void resetClicked(ActionEvent event) {
		Main._logger.debug("Entered  resetClicked  method");
		addHwkName.clear();
		addHwkCode.clear();
		addHwkMobileNum.clear();
		addHwkAgencyName.clear();
		addHwkFee.setText("1.0");
		addHwkOldHouseNum.clear();
		addHwkNewHouseNum.clear();
		addHwkAddrLine1.clear();
		addHwkAddrLine2.clear();
		addHwkLocality.clear();
		addHwkCity.getSelectionModel().clearSelection();
		addHwkActiveCheck.setSelected(true);
		// addHwkState.setValue("State");
		addHwkProfile1.getSelectionModel().clearSelection();
		addHwkProfile2.getSelectionModel().clearSelection();
		addHwkProfile3.clear();
		initials.clear();
		addEmploymentLOV.getSelectionModel().clearSelection();
		addComments.clear();
		addPointNameLOV.getSelectionModel().clearSelection();
		addBldgStreet.clear();
	}

	private void populateProfileValues() {
		Main._logger.debug("Entered  populateProfileValues  method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							addHwkProfile1.setItems(profileValues);
							new AutoCompleteComboBoxListener<>(addHwkProfile1);
							addHwkProfile2.setItems(profileValues);
							new AutoCompleteComboBoxListener<>(addHwkProfile2);
						}
					});

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
				return null;
			}

		};
		new Thread(task).start();
	}

	private void populateEmploymentValues() {
		Main._logger.debug("Entered  populateEmploymentValues  method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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
						if (employmentData != null && !employmentData.contains(rs.getString(1)))
							employmentData.add(rs.getString(1));
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							addEmploymentLOV.setItems(employmentData);
							new AutoCompleteComboBoxListener<>(addEmploymentLOV);
						}
					});

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
				return null;
			}

		};
		new Thread(task).start();
	}

	public boolean isValid() {
		Main._logger.debug("Entered  isValid  method");
		boolean validate = true;
		if (hawkerCodeExists(addHwkCode.getText())) {
			Notifications.create().title("Hawker already exists")
					.text("Hawker with same Hawker Code alraedy exists. Please choose different hawker code.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (mobileNumExists(addHwkMobileNum.getText(), addHwkCode.getText())) {
			Notifications.create().title("Mobile already exists")
					.text("Hawker with same Mobile Number alraedy exists. Please enter a different value.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addHwkName.getText() == null || addHwkName.getText().isEmpty()) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker Name empty")
					.text("Please provide hawker name before saving.").showError();
			try {
				Double.parseDouble(addHwkFee.getText());
			} catch (NumberFormatException e) {
				validate = false;
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid fee")
						.text("Fee per subscription should not be empty and must be numeric only").showError();
				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
		if (addPwd.getText().length() < 5) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid password")
					.text("Password cannot be left empty and must be more than 5 characters long").showError();
		}
		if (addHwkProfile3.getText() != null && checkExistingProfileValue(addHwkProfile3.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		if (addHwkMobileNum.getText().length() != 10) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addPointNameLOV.getSelectionModel().getSelectedItem() == null) {
			Notifications.create().title("Invalid point name").text("Point Name must be selected to create hawker")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		try {
			Long.parseLong(addHwkMobileNum.getText());
		} catch (NumberFormatException e) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return validate;
	}

	public void addHawker() {
		Main._logger.debug("Entered  addHawker  method");

		if (isValid()) {
			PreparedStatement insertHawker = null;
			String insertStatement = "INSERT INTO HAWKER_INFO(NAME,HAWKER_CODE, MOBILE_NUM, AGENCY_NAME,FEE,ACTIVE_FLAG, OLD_HOUSE_NUM, NEW_HOUSE_NUM,ADDR_LINE1,ADDR_LINE2,LOCALITY,CITY,STATE,customer_access, billing_access, line_info_access, line_dist_access, paused_cust_access, product_access, reports_access,profile1,profile2,profile3,initials, employment, comments, point_name, building_street,password,BANK_AC_NO,BANK_NAME,IFSC_CODE,BEN_NAME ) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = Main.dbConnection;
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertHawker = con.prepareStatement(insertStatement);
				insertHawker.setString(1, addHwkName.getText());
				insertHawker.setString(2, addHwkCode.getText().toLowerCase());
				insertHawker.setString(3, addHwkMobileNum.getText());
				insertHawker.setString(4, addHwkAgencyName.getText());
				insertHawker.setDouble(5, Double.parseDouble(addHwkFee.getText() != null ? addHwkFee.getText() : "0"));
				insertHawker.setString(6, addHwkActiveCheck.isSelected() == true ? "Y" : "N");
				insertHawker.setString(7, addHwkOldHouseNum.getText());
				insertHawker.setString(8, addHwkNewHouseNum.getText());
				insertHawker.setString(9, addHwkAddrLine1.getText());
				insertHawker.setString(10, addHwkAddrLine2.getText());
				insertHawker.setString(11, addHwkLocality.getText());
				insertHawker.setString(12, addHwkCity.getSelectionModel().getSelectedItem());
				insertHawker.setString(13, addHwkStateLOV.getSelectionModel().getSelectedItem());
				insertHawker.setString(14, "Y");
				insertHawker.setString(15, "Y");
				insertHawker.setString(16, "Y");
				insertHawker.setString(17, "Y");
				insertHawker.setString(18, "Y");
				insertHawker.setString(19, "Y");
				insertHawker.setString(20, "Y");
				insertHawker.setString(21, addHwkProfile1.getSelectionModel().getSelectedItem());
				insertHawker.setString(22, addHwkProfile2.getSelectionModel().getSelectedItem());
				insertHawker.setString(23,
						addHwkProfile3.getText() == null ? null : addHwkProfile3.getText().toLowerCase());
				insertHawker.setString(24, initials.getText());
				insertHawker.setString(25, addEmploymentLOV.getSelectionModel().getSelectedItem());
				insertHawker.setString(26, addComments.getText());
				insertHawker.setString(27, addPointNameLOV.getSelectionModel().getSelectedItem());
				insertHawker.setString(28, addBldgStreet.getText());
				insertHawker.setString(29, addPwd.getText());
				insertHawker.setString(30, addBankAcNo.getText());
				insertHawker.setString(31, addBankName.getText());
				insertHawker.setString(32, addIfscCode.getText());
				insertHawker.setString(33, addBenName.getText());

				insertHawker.executeUpdate();

				con.commit();
				insertLineZero(addHwkCode.getText().toLowerCase());

			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
						.text("There has been some error during hawker creation, please retry").showError();
				Main.reconnect();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return false;
	}

	public void reset() {
		Main._logger.debug("Entered  reset  method");
		addHwkName.clear();
		addHwkCode.clear();
		addHwkMobileNum.clear();
		addHwkAgencyName.clear();
		addHwkOldHouseNum.clear();
		addHwkNewHouseNum.clear();
		addHwkAddrLine1.clear();
		addHwkAddrLine2.clear();
		addHwkLocality.clear();
		addHwkCity.getSelectionModel().clearSelection();
		addHwkFee.clear();
		addHwkActiveCheck.setSelected(true);
		// addHwkStateLOV
		addHwkProfile1.getSelectionModel().clearSelection();
		addHwkProfile2.getSelectionModel().clearSelection();
		addHwkProfile3.clear();
		initials.clear();
		addEmploymentLOV.getSelectionModel().clearSelection();
		addPointNameLOV.getSelectionModel().clearSelection();
		addComments.clear();
		addBldgStreet.clear();
		addBenName.clear();

	}

	private boolean hawkerCodeExists(String hawkerCode) {
		Main._logger.debug("Entered  hawkerCodeExists  method");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select count(*) from hawker_info where lower(hawker_code) = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, hawkerCode.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0)
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

	public void populatePointNames() {
		Main._logger.debug("Entered  populatePointNames  method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					pointNameValues.clear();
					PreparedStatement stmt = con
							.prepareStatement("select distinct name from point_name where city=? order by name");
					stmt.setString(1, addHwkCity.getSelectionModel().getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						if (pointNameValues != null && !pointNameValues.contains(rs.getString(1)))
							pointNameValues.add(rs.getString(1));
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							addPointNameLOV.setItems(pointNameValues);
							new AutoCompleteComboBoxListener<>(addPointNameLOV);

							addPointNameLOV.setDisable(false);
						}
					});
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}

				return null;
			}

		};
		new Thread(task).start();
	}

	public double feeFromPoint() {
		Main._logger.debug("Entered  feeFromPoint  method");
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select fee from point_name where name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, addPointNameLOV.getSelectionModel().getSelectedItem());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {

				return rs.getDouble(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return 0.0;
	}

	private void insertLineZero(String hwkCode) {
		Main._logger.debug("Entered  insertLineZero  method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				PreparedStatement insertLineNum = null;
				String insertStatement = "INSERT INTO LINE_INFO(LINE_NUM,HAWKER_ID) " + "VALUES (?,?)";
				Connection con = Main.dbConnection;
				try {
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					insertLineNum = con.prepareStatement(insertStatement);
					long hawkerId = hawkerIdForCode(hwkCode);
					if (hawkerId >= 1) {
						insertLineNum.setInt(1, 0);
						insertLineNum.setLong(2, hawkerId);
						insertLineNum.execute();
					}
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
				return null;
			}

		};
		new Thread(task).start();

	}

	public void populateCityValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						cityValues = FXCollections.observableArrayList();
						PreparedStatement stmt = null;
						stmt = con.prepareStatement("select distinct city from point_name order by city");
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							if (cityValues != null && !cityValues.contains(rs.getString(1)))
								cityValues.add(rs.getString(1));
						}

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								addHwkCity.getItems().clear();
								addHwkCity.setItems(cityValues);
								new AutoCompleteComboBoxListener<>(addHwkCity);
							}
						});
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
				return null;
			}

		};

		new Thread(task).start();

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

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	public void releaseVariables() {
		Main._logger.debug("Entered  void releaseVari  method");

		pointNameValues = null;
		employmentData = null;
		profileValues = null;
	}
}
