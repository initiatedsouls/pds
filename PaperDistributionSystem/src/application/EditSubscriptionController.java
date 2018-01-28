package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class EditSubscriptionController implements Initializable {

	@FXML
	public GridPane gridPane;
	@FXML
	private ComboBox<Product> prodNameLOV;

	@FXML
	private Label prodType;
	@FXML
	private Label prodCode;

	@FXML
	private ComboBox<String> subscriptionTypeLOV;

	@FXML
	private ComboBox<String> durationLOV;

	@FXML
	private ComboBox<String> paymentTypeLOV;

	@FXML
	private TextField priceTF;

	@FXML
	private TextField serviceChargeTF;

	@FXML
	private ComboBox<String> frequencyLOV;

	@FXML
	private ComboBox<String> dowLOV;

	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;
	@FXML
	private DatePicker stopDate;
	@FXML
	private DatePicker resumeDate;
	@FXML
	private ComboBox<Integer> offerMonthsTF;
	@FXML
	private TextField subNumberTF;
	@FXML
	private TextField addToBillTF;
	@FXML
	private CheckBox chequeRcvd;
	@FXML
	private TextField addToDueTF;

	private boolean extendMode;
	private Subscription subsRow;
	private ObservableList<String> subscriptionTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> paymentTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> frequencyValues = FXCollections.observableArrayList();
	private ObservableList<String> durationValues = FXCollections.observableArrayList();
	private ObservableList<Product> productValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setupBindings() {
		prodNameLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				populateFrequencyValues();
				prodType.setText(newValue.getType());
				prodCode.setText(newValue.getCode());
			}
		});

		prodNameLOV.setConverter(new StringConverter<Product>() {

			@Override
			public String toString(Product object) {

				return object.getName();
			}

			@Override
			public Product fromString(String string) {
				while (productValues.iterator().hasNext()) {
					Product p = productValues.iterator().next();
					if (p.getName().equalsIgnoreCase(string)) {
						return p;
					}
				}
				return null;
			}
		});
		populateProducts();
		populatePaymentTypeValues();
		for (Object p : productValues.toArray()) {
			if (((Product) p).getProductId() == subsRow.getProductId()) {
				prodNameLOV.getSelectionModel().select(((Product) p));
			}
		}
		prodType.setText(subsRow.getProductType());
		priceTF.setText(subsRow.getCost() + "");
		serviceChargeTF.setText(subsRow.getServiceCharge() + "");
		startDate.setConverter(Main.dateConvertor);

		dowLOV.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
		dowLOV.getSelectionModel().select(subsRow.getDow());
		// dowLOV.setDisable(true);

		frequencyLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					if (newValue.equals("Weekly")) {
						dowLOV.setDisable(false);
					} else {
						dowLOV.setDisable(true);
						dowLOV.getSelectionModel().clearSelection();
					}
				}

			}
		});
		subscriptionTypeLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "Fixed Rate":
					priceTF.setDisable(false);
					serviceChargeTF.setDisable(false);
					chequeRcvd.setDisable(true);
					break;
				case "Actual Days Billing":
					priceTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					chequeRcvd.setDisable(true);
					break;
				case "Free Copy":
					priceTF.setText("0.0");
					serviceChargeTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					chequeRcvd.setDisable(true);
					break;
				case "Coupon Copy/Adv Payment":
					priceTF.setText("0.0");
					serviceChargeTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					chequeRcvd.setDisable(false);
					break;
				}

			}
		});

		populateSubscriptionTypeValues();
		startDate.setValue(subsRow.getStartDate());
		stopDate.setConverter(Main.dateConvertor);
		resumeDate.setConverter(Main.dateConvertor);
		endDate.setValue(subsRow.getStopDate());
		endDate.setConverter(Main.dateConvertor);
		offerMonthsTF.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
		startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (durationLOV.getSelectionModel().getSelectedItem() != null) {
					setEndDateValue();
				}

			}
		});
		durationLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					setEndDateValue();

				}

			}
		});
		populateDurationValues();
		endDate.setDisable(true);

		// offerMonthsTF.getSelectionModel().selectFirst();
		offerMonthsTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (durationLOV.getSelectionModel().getSelectedIndex() > 0) {
					setEndDateValue();
				}

			}
		});
		offerMonthsTF.getSelectionModel().select(subsRow.getOfferMonths());
		subNumberTF.setText(subsRow.getSubNumber());
		addToBillTF.setText(Double.toString(subsRow.getAddToBill()));
		stopDate.setValue(subsRow.getPausedDate());
		// stopDate.setDisable(subsRow.getStatus().equals("Active"));
		stopDate.setDisable(true);
		startDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					startDate.setValue(startDate.getConverter().fromString(startDate.getEditor().getText()));
				}
			}
		});
		endDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					endDate.setValue(endDate.getConverter().fromString(endDate.getEditor().getText()));
				}
			}
		});
		stopDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					stopDate.setValue(stopDate.getConverter().fromString(stopDate.getEditor().getText()));
				}
			}
		});
		resumeDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					resumeDate.setValue(resumeDate.getConverter().fromString(resumeDate.getEditor().getText()));
				}
			}
		});
		resumeDate.setValue(subsRow.getResumeDate());
		// resumeDate.setDisable(subsRow.getStatus().equals("Active"));
		resumeDate.setDisable(true);
		chequeRcvd.setSelected(subsRow.getChequeRcvd());
		prodNameLOV.requestFocus();
		if(extendMode){
			addToDueTF.setVisible(true);
			addToDueTF.setDisable(false);
			subNumberTF.setDisable(false);
			durationLOV.setDisable(false);
			offerMonthsTF.setDisable(false);
			endDate.setDisable(false);
			startDate.setDisable(false);
			chequeRcvd.setSelected(false);
			chequeRcvd.setDisable(true);
			
			prodNameLOV.setDisable(true);
			subscriptionTypeLOV.setDisable(true);
			paymentTypeLOV.setDisable(true);
			priceTF.setDisable(true);
			addToBillTF.setDisable(true);
			serviceChargeTF.setDisable(true);
			frequencyLOV.setDisable(true);
			dowLOV.setDisable(true);
			
		} else {

			addToDueTF.setVisible(false);
//			chequeRcvd.setVisible(false);
		}
	}

	public void setSubscriptionToEdit(Subscription subsRow, boolean extendMode) {
		this.subsRow = subsRow;
		this.extendMode = extendMode;
		
	}

	public void setEndDateValue() {
		int offerMonths = offerMonthsTF.getSelectionModel().getSelectedItem() != null
				? offerMonthsTF.getSelectionModel().getSelectedItem() : 0;

		switch (durationLOV.getSelectionModel().getSelectedItem()) {
		case "3 MONTHS":
			endDate.setValue(startDate.getValue().plusMonths(3 + offerMonths).minusDays(1));
			break;
		case "6 MONTHS":

			endDate.setValue(startDate.getValue().plusMonths(6 + offerMonths).minusDays(1));
			break;
		case "12 MONTHS":

			endDate.setValue(startDate.getValue().plusYears(1).plusMonths(offerMonths).minusDays(1));
			break;
		default:
			endDate.setValue(null);
			break;
		}
	}

	private void populateSubscriptionTypeValues() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			subscriptionTypeValues.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select value, code, seq from lov_lookup where code='SUBSCRIPTION_TYPE' order by seq");
			while (rs.next()) {
				subscriptionTypeValues.add(rs.getString(1));
			}

		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		subscriptionTypeLOV.getItems().clear();
		subscriptionTypeLOV.getItems().addAll(subscriptionTypeValues);
		subscriptionTypeLOV.getSelectionModel().select(subsRow.getSubscriptionType());

	}

	private void populatePaymentTypeValues() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			paymentTypeValues.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select value, code, seq from lov_lookup where code='SUB_PAYMENT_TYPE' order by seq");
			while (rs.next()) {
				paymentTypeValues.add(rs.getString(1));
			}

		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		paymentTypeLOV.getItems().clear();
		paymentTypeLOV.getItems().addAll(paymentTypeValues);
		paymentTypeLOV.getSelectionModel().select(subsRow.getPaymentType());

	}

	private void populateDurationValues() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			durationValues.clear();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select value, code, seq from lov_lookup where code='SUB_DURATION' order by seq");
			while (rs.next()) {
				durationValues.add(rs.getString(1));
			}

		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		durationLOV.getItems().clear();
		durationLOV.getItems().addAll(durationValues);
		durationLOV.getSelectionModel().select(subsRow.getDuration());

	}

	private void populateFrequencyValues() {
		frequencyValues.clear();
		frequencyValues.addAll(prodNameLOV.getSelectionModel().getSelectedItem().getSupportingFreq().split(","));
		frequencyLOV.getItems().clear();
		frequencyLOV.getItems().addAll(frequencyValues);
		frequencyLOV.getSelectionModel().select(subsRow.getFrequency());

	}

	public void populateProducts() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			productValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT prod.PRODUCT_ID, prod.NAME, prod.TYPE, prod.SUPPORTED_FREQ, prod.MONDAY, prod.TUESDAY, prod.WEDNESDAY, prod.THURSDAY, prod.FRIDAY, prod.SATURDAY, prod.SUNDAY, prod.PRICE, prod.CODE, prod.DOW, prod.FIRST_DELIVERY_DATE, prod.ISSUE_DATE, prod.bill_category FROM products prod, hawker_info hwk, point_name pn, customer cust where cust.hawker_code=hwk.hawker_code and hwk.point_name=pn.name and lower(pn.bill_category)=lower(prod.bill_category) and cust.customer_id=? ORDER BY prod.name");
			stmt.setLong(1, subsRow.getCustomerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				productValues.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
						rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13), rs.getString(14),
						rs.getDate(15).toLocalDate(), rs.getDate(16).toLocalDate(), rs.getString(17)));
			}

			prodNameLOV.getItems().clear();
			prodNameLOV.getItems().addAll(productValues);

		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public boolean isValid() {
		boolean valid = true;
		if (prodNameLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Product Selection")
					.text("Product selection cannot be left empty and must be selected").hideAfter(Duration.seconds(5))
					.showError();
		}
		if (paymentTypeLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Payment Type Selection")
					.text("Payment Type selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if ((subscriptionTypeLOV.getSelectionModel().getSelectedItem() == "Coupon Copy/Adv Payment"
				|| subscriptionTypeLOV.getSelectionModel().getSelectedItem() == "Free Copy")
				&& durationLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Duration Selection")
					.text("Duration selection cannot be left empty for Coupon Copy or Free Copy subscription")
					.hideAfter(Duration.seconds(5)).showError();
		}
		// if
		// ((subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Coupon
		// Copy/Adv Payment"))
		// && subNumberTF.getText() == null) {
		// valid = false;
		// Notifications.create().title("Empty Subscription Number")
		// .text("Subscription number cannot be empty for Coupon Copy/Adv
		// Payment")
		// .hideAfter(Duration.seconds(5)).showError();
		// }
		if (frequencyLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Frequency Selection")
					.text("Frequency selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (subscriptionTypeLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Subscription Type")
					.text("Subscription Type selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (dowLOV.getSelectionModel().getSelectedItem() == null
				&& frequencyLOV.getSelectionModel().getSelectedItem().equals("Weekly")) {
			valid = false;
			Notifications.create().title("Invalid Day of the week selection")
					.text("Day of the week selection cannot be left empty if Frequency field is set to : Weekly")
					.hideAfter(Duration.seconds(5)).showError();
		}

		return valid;
	}

	public void setPausedSubs(long subId) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select sub.SUBSCRIPTION_ID, sub.CUSTOMER_ID, sub.PRODUCT_ID, prod.name, prod.type, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.FREQUENCY, sub.TYPE, sub.DOW, sub.STATUS, sub.START_DATE, sub.PAUSED_DATE, prod.CODE, sub.STOP_DATE, sub.DURATION, sub.OFFER_MONTHS, sub.SUB_NUMBER, sub.resume_date, sub.ADD_TO_BILL, sub.cheque_rcvd from subscription sub, products prod where sub.PRODUCT_ID=prod.PRODUCT_ID and sub.SUBSCRIPTION_ID =?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setLong(1, subId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				subsRow = new Subscription(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getDouble(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12),
						rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(),
						rs.getDate(14) == null ? null : rs.getDate(14).toLocalDate(), rs.getString(15),
						rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17), rs.getInt(18),
						rs.getString(19), rs.getDate(20) == null ? null : rs.getDate(20).toLocalDate(),
						rs.getDouble(21),rs.getString(22).equalsIgnoreCase("Y"));
			}

		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void updateSubscriptionRecord() {
		subsRow.setProductId(prodNameLOV.getSelectionModel().getSelectedItem().getProductId());
		subsRow.setSubscriptionType(subscriptionTypeLOV.getSelectionModel().getSelectedItem());
		subsRow.setCost(Double
				.parseDouble(priceTF.getText() == null || priceTF.getText().isEmpty() ? "0.0" : priceTF.getText()));
		// if
		// (frequencyLOV.getSelectionModel().getSelectedItem().equals("Weekly"))
		subsRow.setDow(dowLOV.getSelectionModel().getSelectedItem());
		subsRow.setFrequency(frequencyLOV.getSelectionModel().getSelectedItem());
		subsRow.setPaymentType(paymentTypeLOV.getSelectionModel().getSelectedItem());
		subsRow.setServiceCharge(
				Double.parseDouble(serviceChargeTF.getText().isEmpty() || serviceChargeTF.getText() == null ? "0.0"
						: serviceChargeTF.getText()));
		subsRow.setStartDate(startDate.getValue());
		subsRow.setSubscriptionType(subscriptionTypeLOV.getSelectionModel().getSelectedItem());
		subsRow.setStopDate(endDate.getValue());
		subsRow.setDuration(durationLOV.getSelectionModel().getSelectedItem());
		subsRow.setOfferMonths((int) offerMonthsTF.getValue());
		subsRow.setSubNumber(subNumberTF.getText());
		subsRow.setAddToBill(Double.parseDouble(addToBillTF.getText()));
		subsRow.setChequeRcvd("Coupon Copy/Adv Payment".equalsIgnoreCase(subscriptionTypeLOV.getSelectionModel().getSelectedItem())?chequeRcvd.isSelected():false);
		// subsRow.setDow(dow);
		this.subsRow.updateSubscriptionRecord();
		
		if(this.extendMode){
			Customer custRow = BillingUtilityClass.custForCustId(subsRow.getCustomerId());
			custRow.setTotalDue(custRow.getTotalDue()+Double.parseDouble(addToDueTF.getText()));
			custRow.updateCustomerRecord();
			
		}
	}

	public void releaseVariables() {
		subscriptionTypeValues = null;
		paymentTypeValues = null;
		frequencyValues = null;
		productValues = null;
		durationValues = null;
		subscriptionTypeValues = FXCollections.observableArrayList();
		paymentTypeValues = FXCollections.observableArrayList();
		frequencyValues = FXCollections.observableArrayList();
		productValues = FXCollections.observableArrayList();
		durationValues = FXCollections.observableArrayList();
	}

}
