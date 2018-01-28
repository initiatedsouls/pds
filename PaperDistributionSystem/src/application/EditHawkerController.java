package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.Notifications;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import javafx.scene.control.ChoiceDialog;

public class EditHawkerController implements Initializable {

	private Hawker hawkerRow;
	@FXML
	public GridPane gridPane;

	// Edit Customer Fields FXML
	@FXML
	private TextField editNameTF;
	@FXML
	private TextField editHawkerCodeTF;
	@FXML
	private TextField editMobileNumTF;
	@FXML
	private TextField editAgencyNameTF;
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
	private TextField editFeeTF;
	@FXML
	private CheckBox editActiveCheck;
	@FXML
	private ComboBox<String> editStateLOV;
	@FXML
	private ComboBox<String> editProfile1TF;
	@FXML
	private ComboBox<String> editProfile2TF;
	@FXML
	private TextField editProfile3TF;
	@FXML
	private TextField initialsTF;
	@FXML
	private ComboBox<String> editEmploymentLOV;
	@FXML
	private ComboBox<String> editPointNameLOV;
	@FXML
	private TextField editCommentsTF;
	@FXML
	private TextField editBankAcNo;
	@FXML
	private TextField editBankName;
	@FXML
	private TextField editIfscCode;
	@FXML
	private TextField editBenName;
	@FXML
	private ImageView logoImage;
	@FXML
	private TextField editTotalDue;

	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<String> stateValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// setupBindings();

	}

	public void setHawkerToEdit(Hawker hawker) {
		this.hawkerRow = hawker;
	}

	public void setupBindings() {
		editStateLOV.setItems(stateValues);
		editStateLOV.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		new AutoCompleteComboBoxListener<>(editStateLOV);
		editStateLOV.getSelectionModel().select(stateValues.indexOf(hawkerRow.getState()));

		populateEmploymentValues();
		populateProfileValues();
		populatePointNames();
		editNameTF.setText(hawkerRow.getName());
		editHawkerCodeTF.setText(hawkerRow.getHawkerCode());
		editMobileNumTF.setText(hawkerRow.getMobileNum());
		editOldHouseNumTF.setText(hawkerRow.getOldHouseNum());
		editNewHouseNumTF.setText(hawkerRow.getNewHouseNum());
		editBldgStreetTF.setText(hawkerRow.getBuildingStreet());
		editAddrLine1.setText(hawkerRow.getAddrLine1());
		editAddrLine2.setText(hawkerRow.getAddrLine2());
		editLocalityTF.setText(hawkerRow.getLocality());
		editCityTF.setText(hawkerRow.getCity());
		editAgencyNameTF.setText(hawkerRow.getAgencyName());
		editFeeTF.setText("" + hawkerRow.getFee());
		if (HawkerLoginController.loggedInHawker != null)
			editFeeTF.setDisable(true);
		editActiveCheck.setSelected(hawkerRow.getActiveFlag());
		editProfile3TF.setText(hawkerRow.getProfile3());
		initialsTF.setText(hawkerRow.getInitials());
		initialsTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					initialsTF.setText(oldValue);
			}
		});

		editProfile1TF.setItems(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile1TF);
		editProfile2TF.setItems(profileValues);
		new AutoCompleteComboBoxListener<>(editProfile2TF);
		editProfile1TF.getSelectionModel().select(hawkerRow.getProfile1());
		editProfile2TF.getSelectionModel().select(hawkerRow.getProfile2());
		editProfile3TF.setText(hawkerRow.getProfile3());
		editEmploymentLOV.setItems(employmentData);
		new AutoCompleteComboBoxListener<>(editEmploymentLOV);
		editEmploymentLOV.getSelectionModel().select(hawkerRow.getEmployment());
		editCommentsTF.setText(hawkerRow.getComments());
		editPointNameLOV.setItems(pointNameValues);
		new AutoCompleteComboBoxListener<>(editPointNameLOV);
		editPointNameLOV.getSelectionModel().select(hawkerRow.getPointName());
		editNameTF.requestFocus();
		editBankAcNo.setText(hawkerRow.getBankAcNo());
		editBankName.setText(hawkerRow.getBankName());
		editIfscCode.setText(hawkerRow.getIfscCode());
		editBenName.setText(hawkerRow.getBenName());
		editTotalDue.setText("" + hawkerRow.getTotalDue());
		if(HawkerLoginController.loggedInHawker!=null)
			editTotalDue.setDisable(true);
		
		AmazonS3 s3logoclient = Main.s3logoclient;
		if (s3logoclient.doesObjectExist("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg")) {
			S3Object s3o = s3logoclient.getObject("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg");
			logoImage.setImage(new Image(s3o.getObjectContent()));
		}
	}

	public boolean isValid() {
		boolean validate = true;
		if (hawkerCodeExists(editHawkerCodeTF.getText())) {
			Notifications.create().title("Hawker already exists")
					.text("Hawker with same Hawker Code alraedy exists. Please choose different hawker code.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (mobileNumExists(editMobileNumTF.getText())) {
			Notifications.create().title("Mobile already exists")
					.text("Hawker with same Mobile Number alraedy exists. Please enter a different value.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (editNameTF.getText() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
					.text("Please select a hawker before adding the the customer").showError();
			try {
				Double.parseDouble(editFeeTF.getText());
			} catch (NumberFormatException e) {
				validate = false;
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid fee")
						.text("Fee per subscription should not be empty and must be numeric only").showError();
				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
		if (editProfile3TF.getText() != null && checkExistingProfileValue(editProfile3TF.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}

		if (editMobileNumTF.getText().length() != 10) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (editPointNameLOV.getSelectionModel().getSelectedItem() == null) {
			Notifications.create().title("Invalid point name").text("Point Name must be selected to create hawker")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		try {
			Long.parseLong(editMobileNumTF.getText().trim());
		} catch (NumberFormatException e) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		try {
			Double.parseDouble(editTotalDue.getText().trim());
		} catch (NumberFormatException e) {
			Notifications.create().title("Invalid Due amount").text("Total Due amount should be a valid number")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return validate;
	}

	private boolean hawkerCodeExists(String hawkerCode) {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select count(*) from hawker_info where lower(hawker_code) = ? and hawker_id<>?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, hawkerCode.toLowerCase());
			stmt.setLong(2, hawkerRow.getHawkerId());
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

	public Hawker returnUpdatedHawker() {
		if (isValid()) {
			Hawker edittedHawker = new Hawker(hawkerRow);
			edittedHawker.setName(editNameTF.getText());
			edittedHawker.setMobileNum(editMobileNumTF.getText());
			edittedHawker.setHawkerCode(editHawkerCodeTF.getText());
			edittedHawker.setOldHouseNum(editOldHouseNumTF.getText());
			edittedHawker.setNewHouseNum(editNewHouseNumTF.getText());
			edittedHawker.setAddrLine1(editAddrLine1.getText());
			edittedHawker.setAddrLine2(editAddrLine2.getText());
			edittedHawker.setLocality(editLocalityTF.getText());
			edittedHawker.setCity(editCityTF.getText());
			edittedHawker.setState(editStateLOV.getSelectionModel().getSelectedItem());
			edittedHawker.setFee(Double.parseDouble(editFeeTF.getText()));
			edittedHawker.setActiveFlag(editActiveCheck.isSelected());
			edittedHawker.setAgencyName(editAgencyNameTF.getText());
			edittedHawker.setProfile1(editProfile1TF.getSelectionModel().getSelectedItem());
			edittedHawker.setProfile2(editProfile2TF.getSelectionModel().getSelectedItem());
			edittedHawker.setProfile3(editProfile3TF.getText());
			edittedHawker.setInitials(initialsTF.getText());
			edittedHawker.setPointName(editPointNameLOV.getSelectionModel().getSelectedItem());
			edittedHawker.setEmployment(editEmploymentLOV.getSelectionModel().getSelectedItem());
			edittedHawker.setComments(editCommentsTF.getText());
			edittedHawker.setBankAcNo(editBankAcNo.getText());
			edittedHawker.setBankName(editBankName.getText());
			edittedHawker.setIfscCode(editIfscCode.getText());
			edittedHawker.setBenName(editBenName.getText());
			edittedHawker.setTotalDue(Double.parseDouble(editTotalDue.getText().trim()));
			edittedHawker.updateHawkerRecord();
			return edittedHawker;
		} else
			return null;
	}

	private void populateProfileValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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
				return null;
			}

		};
		new Thread(task).start();
	}

	private void populateEmploymentValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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
				return null;
			}

		};
		new Thread(task).start();
	}

	public void populatePointNames() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			pointNameValues = FXCollections.observableArrayList();
			PreparedStatement stmt = con.prepareStatement("select distinct name from point_name order by name");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (pointNameValues != null && !pointNameValues.contains(rs.getString(1)))
					pointNameValues.add(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private boolean mobileNumExists(String mobileNum) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select hawker_code,name from hawker_info where mobile_num=? and hawker_code <> ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, mobileNum);
			stmt.setString(2, editHawkerCodeTF.getText());
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
	void changeLogoClicked(ActionEvent event) {

		try {
			AmazonS3 s3logoclient = Main.s3logoclient;
			if (s3logoclient.doesObjectExist("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg")) {
				S3Object s3o = s3logoclient.getObject("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg");
				logoImage.setImage(new Image(s3o.getObjectContent()));
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Logo File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			File selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
			if (selectedFile != null) {
				Image img = new Image(new FileInputStream(selectedFile), 200, 200, true, false);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(SwingFXUtils.fromFXImage(img, null), "jpg", os);
				InputStream fis = new ByteArrayInputStream(os.toByteArray());
				String uploadFileName = this.hawkerRow.getHawkerCode() + "logo.jpg";
				// File hwkLogo = new File(uploadFileName);
				s3logoclient
						.putObject(new PutObjectRequest("pdslogobucket", uploadFileName, fis, new ObjectMetadata()));
				logoImage.setImage(img);

			}
		} catch (Exception e) {
			Main._logger.debug(e);
		}

	}

	@FXML
	void removeLogoClicked(ActionEvent event) {

		try {
			Dialog<ButtonType> dialog = new Dialog<ButtonType>();
			
			dialog.setTitle("Confirm delete?");
			dialog.setHeaderText("Are you sure you want to remove logo?");
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES,
					ButtonType.NO);
			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.YES) {
				AmazonS3 s3logoclient = Main.s3logoclient;
				if (s3logoclient.doesObjectExist("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg")) {
					try {
						s3logoclient.deleteObject("pdslogobucket", this.hawkerRow.getHawkerCode() + "logo.jpg");
						Notifications.create().title("Logo deleted successfully.")
								.text("Hawker Logo is now deleted successfully.").hideAfter(Duration.seconds(5))
								.showInformation();
						this.logoImage.setImage(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			Main._logger.debug(e);
		}

	}

	public void releaseVariables() {

		pointNameValues = null;
		employmentData = null;
		profileValues = null;
	}
}
