package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class AddProductsExtraScreenController implements Initializable {

	@FXML
	private TextField nameTF;

	@FXML
	private ComboBox<String> typeTF;

	@FXML
	private HBox freqHBox;

	@FXML
	private TextField priceTF;

	@FXML
	private TextField mondayTF;

	@FXML
	private TextField tuesdayTF;

	@FXML
	private TextField wednesdayTF;

	@FXML
	private TextField thursdayTF;

	@FXML
	private TextField fridayTF;

	@FXML
	private TextField saturdayTF;

	@FXML
	private TextField sundayTF;
	@FXML
	private TextField codeTF;
	@FXML
	private ComboBox<String> dowTF;
	@FXML
	private ComboBox<String> billCategoryTF;

	@FXML
	private DatePicker firstDeliveryDate;
	@FXML
	private DatePicker issueDate;

	private CheckComboBox<String> prodFreq;

	private ObservableList<String> freqValues = FXCollections.observableArrayList();
	private ObservableList<String> productTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> billCategoryValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prodFreq = new CheckComboBox<String>();
		freqHBox.getChildren().add(prodFreq);
		firstDeliveryDate.setConverter(Main.dateConvertor);
		firstDeliveryDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					firstDeliveryDate.setValue(
							firstDeliveryDate.getConverter().fromString(firstDeliveryDate.getEditor().getText()));
				}
			}
		});
		issueDate.setConverter(Main.dateConvertor);
		issueDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					issueDate.setValue(issueDate.getConverter().fromString(issueDate.getEditor().getText()));
				}
			}
		});
		typeTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "Newspaper":
					weekdaysDisable(false);
					break;
				case "Magazine":
					weekdaysDisable(true);
					break;

				}

			}
		});
	}

	public void setupBindings() {
		populateProdFreqValues();
		populateProdTypeValues();
		populateBillCategoryValues();
		dowTF.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
		codeTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 4)
					codeTF.setText(oldValue);
			}
		});
		typeTF.getSelectionModel().selectFirst();
		billCategoryTF.getSelectionModel().selectFirst();
	}

	public void addProduct() {
		if (isValid()) {
			PreparedStatement insertLineNum = null;
			String insertStatement = "INSERT INTO PRODUCTS(NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE,BILL_CATEGORY ) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = Main.dbConnection;
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertLineNum = con.prepareStatement(insertStatement);
				insertLineNum.setString(1, nameTF.getText());
				insertLineNum.setString(2, typeTF.getSelectionModel().getSelectedItem());
				insertLineNum.setString(3, supportedFreq(prodFreq.getCheckModel().getCheckedItems()));
				insertLineNum.setDouble(4,
						Double.parseDouble(mondayTF.getText().isEmpty() ? "0.0" : mondayTF.getText()));
				insertLineNum.setDouble(5,
						Double.parseDouble(tuesdayTF.getText().isEmpty() ? "0.0" : tuesdayTF.getText()));
				insertLineNum.setDouble(6,
						Double.parseDouble(wednesdayTF.getText().isEmpty() ? "0.0" : wednesdayTF.getText()));
				insertLineNum.setDouble(7,
						Double.parseDouble(thursdayTF.getText().isEmpty() ? "0.0" : thursdayTF.getText()));
				insertLineNum.setDouble(8,
						Double.parseDouble(fridayTF.getText().isEmpty() ? "0.0" : fridayTF.getText()));
				insertLineNum.setDouble(9,
						Double.parseDouble(saturdayTF.getText().isEmpty() ? "0.0" : saturdayTF.getText()));
				insertLineNum.setDouble(10,
						Double.parseDouble(sundayTF.getText().isEmpty() ? "0.0" : sundayTF.getText()));
				insertLineNum.setDouble(11,
						Double.parseDouble(priceTF.getText().isEmpty() ? "0.0" : priceTF.getText()));
				insertLineNum.setString(12, codeTF.getText());
				insertLineNum.setString(13, dowTF.getSelectionModel().getSelectedItem());
				insertLineNum.setDate(14, Date.valueOf(firstDeliveryDate.getValue()));
				insertLineNum.setDate(15, Date.valueOf(issueDate.getValue()));
				insertLineNum.setString(16, billCategoryTF.getSelectionModel().getSelectedItem());
				insertLineNum.execute();
			} catch (SQLException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			} catch (NumberFormatException e) {
				Notifications.create().title("Invalid value")
						.text("Please enter only numbers for Price and Monday - Sunday fields")
						.hideAfter(Duration.seconds(5)).showError();
				Main._logger.debug("Error :",e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}
		}
	}

	public boolean isValid() {
		boolean valid = true;
		if (nameTF.getText().isEmpty() || nameTF.getText() == null) {
			valid = false;
			Notifications.create().title("Invalid Product Name").text("Product name cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (codeTF.getText().isEmpty() || codeTF.getText() == null) {
			valid = false;
			Notifications.create().title("Invalid Product Code").text("Product code cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (typeTF.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Product Type Selection")
					.text("Type selection cannot be left empty and must be selected").hideAfter(Duration.seconds(5))
					.showError();
		}
		if (prodFreq.getCheckModel().isEmpty()) {
			valid = false;
			Notifications.create().title("Invalid Frequency")
					.text("Product Frequency selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (productExistsInCategory(nameTF.getText(), billCategoryTF.getSelectionModel().getSelectedItem())) {
			valid = false;
			Notifications.create().title("Duplicate Product in Bill Category")
					.text("Product Name already exists in this bill category.").hideAfter(Duration.seconds(5))
					.showError();
		}
		if (issueDate.getValue()==null) {
			valid = false;
			Notifications.create().title("Invalid Issue Date").text("Issue date is needed for product")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (firstDeliveryDate.getValue()==null) {
			valid = false;
			Notifications.create().title("Invalid First Delivery Date").text("First Delivery date is needed for product")
					.hideAfter(Duration.seconds(5)).showError();
		}
		return valid;
	}

	protected String supportedFreq(ObservableList<? extends String> list) {

		final StringBuilder sb = new StringBuilder();

		if (list != null) {

			for (int i = 0, max = list.size(); i < max; i++) {

				sb.append(list.get(i));

				if (i < max - 1) {

					sb.append(",");
				}
			}
		}
		final String str = sb.toString();

		return str;
	}

	public void populateProdFreqValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			freqValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PRODUCT_FREQ' order by seq");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				freqValues.add(rs.getString(1));
			}
			prodFreq.getItems().addAll(freqValues);
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	public void populateProdTypeValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			productTypeValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PRODUCT_TYPE' order by seq");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				productTypeValues.add(rs.getString(1));
			}
			typeTF.getItems().addAll(productTypeValues);
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	private boolean productExistsInCategory(String name, String category) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select count(*) from products where lower(name)=? and lower(bill_category)=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name.toLowerCase());
			stmt.setString(2, category.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0)
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

	public void populateBillCategoryValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			billCategoryValues=FXCollections.observableArrayList();
			PreparedStatement stmt = con
					.prepareStatement("select distinct bill_category from point_name order by bill_category");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				billCategoryValues.add(rs.getString(1).toLowerCase());
			}
			billCategoryTF.setItems(billCategoryValues);
			new AutoCompleteComboBoxListener<>(billCategoryTF);
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	private void weekdaysDisable(boolean val) {
		priceTF.setDisable(!val);
		mondayTF.setDisable(val);
		tuesdayTF.setDisable(val);
		wednesdayTF.setDisable(val);
		thursdayTF.setDisable(val);
		fridayTF.setDisable(val);
		saturdayTF.setDisable(val);
		sundayTF.setDisable(val);
	}

	public void setupDuplicateBindings(Product productRow) {

		nameTF.setText(productRow.getName());
		typeTF.getSelectionModel().select(productRow.getType());
		priceTF.setText("" + productRow.getPrice());
		mondayTF.setText("" + productRow.getMonday());
		tuesdayTF.setText("" + productRow.getTuesday());
		wednesdayTF.setText("" + productRow.getWednesday());
		thursdayTF.setText("" + productRow.getThursday());
		fridayTF.setText("" + productRow.getFriday());
		saturdayTF.setText("" + productRow.getSaturday());
		sundayTF.setText("" + productRow.getSunday());
		codeTF.setText(productRow.getCode());
		dowTF.getSelectionModel().select(productRow.getDow());
		firstDeliveryDate.setValue(productRow.getFirstDeliveryDate());
		issueDate.setValue(productRow.getIssueDate());
		billCategoryTF.getSelectionModel().select(productRow.getBillCategory());
		String freq[] = productRow.getSupportingFreq().split(",");
		for (int i = 0; i < freq.length; i++) {
			prodFreq.getCheckModel().check(freq[i]);
		}
	}

	public void releaseVariables() {
		freqValues = null;
		productTypeValues = null;
		billCategoryValues = null;
		freqValues = FXCollections.observableArrayList();
		productTypeValues = FXCollections.observableArrayList();
		billCategoryValues = FXCollections.observableArrayList();
	}
}
