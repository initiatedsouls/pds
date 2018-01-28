package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class AddSubscriptionController implements Initializable {

	@FXML
	private ComboBox<Product> prodNameLOV;

	@FXML
	private HBox freqHBox;
	@FXML
	private Label prodType;
	@FXML
	private Label prodCode;

	@FXML
	private ComboBox<String> subscriptionTypeLOV;

	@FXML
	private ComboBox<String> paymentTypeLOV;

	@FXML
	private TextField priceTF;

	@FXML
	private TextField serviceChargeTF;
	@FXML
	private TextField addToBillTF;

	@FXML
	private ComboBox<String> frequencyLOV;

	private CheckComboBox<String> dowLOV;
	@FXML
	private ComboBox<String> durationLOV;

	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker stopDate;
	@FXML
	private ComboBox<Integer> offerMonthsTF;
	@FXML
	private TextField subNumberTF;

	private ObservableList<String> subscriptionTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> paymentTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> frequencyValues = FXCollections.observableArrayList();
	private ObservableList<Product> productValues = FXCollections.observableArrayList();
	private ObservableList<String> durationValues = FXCollections.observableArrayList();

	private Customer custRow;

	// ShowProductDetails:
	@FXML
	public GridPane gridPane;
	@FXML
	private TextField nameTF;

	@FXML
	private Label typeTF;

	@FXML
	private Label freqValues;

	@FXML
	private TextField priceTF1;

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
	private Label billCategoryTF;
	@FXML
	private Label dowTF;

	@FXML
	private DatePicker firstDeliveryDate;
	@FXML
	private DatePicker issueDate;

	private CheckComboBox<String> prodFreq;

	private Product productRow;
	// @FXML private TitledPane prodTitledPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setCustomer(Customer custRow) {
		this.custRow = custRow;
	}

	public void setupBindings() {

		prodNameLOV.setConverter(new StringConverter<Product>() {

			@Override
			public String toString(Product object) {
				if(object!=null)
				return object.getName();
				else return null;
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
		populateSubscriptionTypeValues();
		populateDurationValues();
		dowLOV = new CheckComboBox<>();
		startDate.setConverter(Main.dateConvertor);
		startDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					startDate.setValue(startDate.getConverter().fromString(startDate.getEditor().getText()));
				}
			}
		});
		prodNameLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				if (newValue != null) {
					setProduct(newValue);
					setupProdBindings();
					populateFrequencyValues();
					if (newValue.getDow() != null) {
						// dowLOV.getSelectionModel().select(newValue.getDow());
						dowLOV.setDisable(true);
						populateDOWValues(newValue);
					} else {
						dowLOV.getCheckModel().clearChecks();
						dowLOV.setDisable(false);
					}
					prodType.setText(newValue.getType());
					prodCode.setText(newValue.getCode());
				}

			}

		});
		offerMonthsTF.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
		dowLOV.setDisable(true);
		freqHBox.getChildren().add(dowLOV);
		frequencyLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue!=null) {
					if (newValue.equals("Weekly")) {
						dowLOV.setDisable(false);
					} else {
						dowLOV.setDisable(true);
						dowLOV.getCheckModel().clearChecks();
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
					break;
				case "Actual Days Billing":
					priceTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					break;
				case "Free Copy":
					priceTF.setText("0.0");
					serviceChargeTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					break;
				case "Coupon Copy/Adv Payment":
					priceTF.setText("0.0");
					serviceChargeTF.setText("0.0");
					priceTF.setDisable(true);
					serviceChargeTF.setDisable(false);
					break;
				}

			}
		});
		startDate.setValue(LocalDate.now());
		startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (durationLOV.getSelectionModel().getSelectedItem() != null) {
					switch (durationLOV.getSelectionModel().getSelectedItem()) {
					case "3 MONTHS":
						stopDate.setValue(startDate.getValue()
								.plusMonths(3 + offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
						break;
					case "6 MONTHS":

						stopDate.setValue(startDate.getValue()
								.plusMonths(6 + offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
						break;
					case "12 MONTHS":

						stopDate.setValue(startDate.getValue().plusYears(1)
								.plusMonths(offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
						break;
					default:
						stopDate.setValue(null);
						break;
					}
				}

			}
		});

		durationLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "3 MONTHS":
					stopDate.setValue(startDate.getValue()
							.plusMonths(3 + offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
					break;
				case "6 MONTHS":

					stopDate.setValue(startDate.getValue()
							.plusMonths(6 + offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
					break;
				case "12 MONTHS":

					stopDate.setValue(startDate.getValue().plusYears(1)
							.plusMonths(offerMonthsTF.getSelectionModel().getSelectedItem()).minusDays(1));
					break;
				default:
					stopDate.setValue(null);
					break;
				}

			}
		});
		offerMonthsTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (durationLOV.getSelectionModel().getSelectedItem() != null) {
					switch (durationLOV.getSelectionModel().getSelectedItem()) {
					case "3 MONTHS":
						stopDate.setValue(startDate.getValue().plusMonths(3 + newValue).minusDays(1));
						break;
					case "6 MONTHS":

						stopDate.setValue(startDate.getValue().plusMonths(6 + newValue).minusDays(1));
						break;
					case "12 MONTHS":

						stopDate.setValue(startDate.getValue().plusYears(1).plusMonths(newValue).minusDays(1));
						break;
					default:
						stopDate.setValue(null);
						break;
					}
				}

			}
		});

		offerMonthsTF.getSelectionModel().selectFirst();
		stopDate.setConverter(Main.dateConvertor);

		stopDate.setDisable(true);

		firstDeliveryDate.setConverter(Main.dateConvertor);
		issueDate.setConverter(Main.dateConvertor);

	}

	private void populateDOWValues(Product prod) {
		dowLOV.getItems().clear();
		if (prod.getType().equals("Newspaper")) {

			dowLOV.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
		} else {
			String[] dow = prod.getDow().split(",");
			dowLOV.getItems().addAll(dow);
		}
	}

	private void populateSubscriptionTypeValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				subscriptionTypeLOV.getItems().clear();
				subscriptionTypeLOV.getItems().addAll(subscriptionTypeValues);
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populatePaymentTypeValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					paymentTypeValues.clear();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='SUB_PAYMENT_TYPE' order by seq");
					while (rs.next()) {
						paymentTypeValues.add(rs.getString(1));
					}

				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				paymentTypeLOV.getItems().clear();
				paymentTypeLOV.getItems().addAll(paymentTypeValues);

				try {
					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement pstmt = con
							.prepareStatement("Select distinct payment_type from subscription where customer_id=?");
					pstmt.setLong(1, custRow.getCustomerId());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								try {
									paymentTypeLOV.getSelectionModel().select(rs.getString(1));

									paymentTypeLOV.setDisable(true);
								} catch (SQLException e) {
									Main._logger.debug("Error :",e);
									e.printStackTrace();
								}
							}
						});

					}

				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateDurationValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					durationValues.clear();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='SUB_DURATION' order by seq");
					while (rs.next()) {
						durationValues.add(rs.getString(1));
					}

				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				durationLOV.getItems().clear();
				durationLOV.getItems().addAll(durationValues);
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateFrequencyValues() {
		frequencyValues.clear();
		frequencyValues.addAll(prodNameLOV.getSelectionModel().getSelectedItem().getSupportingFreq().split(","));
		frequencyLOV.getItems().clear();
		frequencyLOV.getItems().addAll(frequencyValues);
	}

	public void populateProducts() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			productValues=FXCollections.observableArrayList();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT prod.PRODUCT_ID, prod.NAME, prod.TYPE, prod.SUPPORTED_FREQ, prod.MONDAY, prod.TUESDAY, prod.WEDNESDAY, prod.THURSDAY, prod.FRIDAY, prod.SATURDAY, prod.SUNDAY, prod.PRICE, prod.CODE, prod.DOW, prod.FIRST_DELIVERY_DATE, prod.ISSUE_DATE, prod.bill_category FROM products prod, hawker_info hwk, point_name pn, customer cust where cust.hawker_code=hwk.hawker_code and hwk.point_name=pn.name and lower(pn.bill_category)=lower(prod.bill_category) and cust.customer_id=? ORDER BY prod.name");
			stmt.setLong(1, custRow.getCustomerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				productValues.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
						rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13), rs.getString(14),
						rs.getDate(15).toLocalDate(), rs.getDate(16).toLocalDate(), rs.getString(17)));
			}

			prodNameLOV.setItems(productValues);
//			new AutoCompleteComboBoxListener<>(prodNameLOV);

		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
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
		if ((subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Coupon Copy/Adv Payment")
				|| subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Free Copy"))
				&& durationLOV.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().title("Invalid Duration Selection")
					.text("Duration selection cannot be left empty for Coupon Copy or Free Copy subscription")
					.hideAfter(Duration.seconds(5)).showError();
		}
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
		if (dowLOV.getCheckModel().getCheckedIndices().isEmpty() && dowLOV.isDisable() == false) {
			valid = false;
			Notifications.create().title("Invalid Day of the week selection")
					.text("Day of the week selection cannot be left empty if Frequency field is set to : Weekly")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Fixed Rate")
				&& (priceTF.getText() == null || priceTF.getText().trim().isEmpty())) {
			valid = false;
			Notifications.create().title("Invalid Amount")
					.text("Amount should not be null when Subscription Type is Fixed Rate")
					.hideAfter(Duration.seconds(5)).showError();
		}

		return valid;
	}

	public long addSubscription() {

		if (isValid()) {
			if (frequencyLOV.getSelectionModel().getSelectedItem().equalsIgnoreCase("Weekly")) {
				return createSubscriptions();
			} else {
				PreparedStatement insertSubscription = null;
				String insertStatement = "insert into subscription(CUSTOMER_ID, PRODUCT_ID, PAYMENT_TYPE, SUBSCRIPTION_COST, SERVICE_CHARGE, FREQUENCY, TYPE, DOW, STATUS, START_DATE, PAUSED_DATE, STOP_DATE, DURATION, OFFER_MONTHS,SUB_NUMBER,ADD_TO_BILL ) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Connection con = Main.dbConnection;
				try {
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					insertSubscription = con.prepareStatement(insertStatement, new String[] { "SUBSCRIPTION_ID" });
					insertSubscription.setLong(1, custRow.getCustomerId());
					insertSubscription.setLong(2, prodNameLOV.getSelectionModel().getSelectedItem().getProductId());
					insertSubscription.setString(3, paymentTypeLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setDouble(4, Double.parseDouble(
							(priceTF.getText() == null || priceTF.getText().isEmpty()) ? "0.0" : priceTF.getText()));
					insertSubscription.setDouble(5,
							Double.parseDouble(serviceChargeTF.getText() == null || serviceChargeTF.getText().isEmpty()
									? "0.0" : serviceChargeTF.getText()));
					insertSubscription.setString(6, frequencyLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setString(7, subscriptionTypeLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setString(8, null);
					insertSubscription.setString(9, "Active");
					insertSubscription.setDate(10, Date.valueOf(startDate.getValue()));
					insertSubscription.setDate(11, null);
					insertSubscription.setDate(12,
							stopDate.getValue() != null ? Date.valueOf(stopDate.getValue()) : null);
					insertSubscription.setString(13, durationLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setInt(14, (int) offerMonthsTF.getValue());
					insertSubscription.setString(15, subNumberTF.getText());
					insertSubscription.setString(16, addToBillTF.getText());
					insertSubscription.executeUpdate();
					ResultSet rs = insertSubscription.getGeneratedKeys();
					long subId = 0;
					if (rs.next()) {
						subId = rs.getLong(1);
					}
					Subscription subRow = BillingUtilityClass.subForSubId(subId);
					if ((subRow.getPaymentType().equalsIgnoreCase("Current Month")) && (subscriptionTypeLOV
							.getSelectionModel().getSelectedItem().equals("Actual Days Billing")
							|| subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Fixed Rate"))) {
						BillingUtilityClass.createCurrentMonthStartingDue(subRow, subRow.getStartDate(),
								subRow.getStartDate().withDayOfMonth(1).plusMonths(1).minusDays(1));
					}

					return subId;

				} catch (NumberFormatException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
							.text("Only numeric values should be entered in Price and Tea Expenses fields").showError();
					Main.reconnect();
				} catch (SQLException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
							.text("There has been some error during subscription creation, please check all the values and retry")
							.showError();
					Main.reconnect();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	private long createSubscriptions() {
		long subId = 0;
		if (isValid()) {
			Object[] dowList = dowLOV.getCheckModel().getCheckedItems().toArray();
			for (int i = 0; i < dowList.length; i++) {
				String s = (String) dowList[i];
				PreparedStatement insertSubscription = null;
				String insertStatement = "insert into subscription(CUSTOMER_ID, PRODUCT_ID, PAYMENT_TYPE, SUBSCRIPTION_COST, SERVICE_CHARGE, FREQUENCY, TYPE, DOW, STATUS, START_DATE, PAUSED_DATE, STOP_DATE, DURATION, OFFER_MONTHS,SUB_NUMBER,ADD_TO_BILL ) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Connection con = Main.dbConnection;
				try {
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					insertSubscription = con.prepareStatement(insertStatement, new String[] { "SUBSCRIPTION_ID" });
					insertSubscription.setLong(1, custRow.getCustomerId());
					insertSubscription.setLong(2, prodNameLOV.getSelectionModel().getSelectedItem().getProductId());
					insertSubscription.setString(3, paymentTypeLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setDouble(4, Double.parseDouble(
							priceTF.getText() == null || priceTF.getText().isEmpty() || i > 0 ? "0.0" : priceTF.getText()));
					insertSubscription.setDouble(5,
							Double.parseDouble(
									(serviceChargeTF.getText() == null || serviceChargeTF.getText().isEmpty() || i > 0)
											? "0.0" : serviceChargeTF.getText()));
					insertSubscription.setString(6, frequencyLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setString(7, subscriptionTypeLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setString(8, s);
					insertSubscription.setString(9, "Active");
					insertSubscription.setDate(10, Date.valueOf(startDate.getValue()));
					insertSubscription.setDate(11, null);
					insertSubscription.setDate(12,
							stopDate.getValue() == null ? null : Date.valueOf(stopDate.getValue()));
					insertSubscription.setString(13, durationLOV.getSelectionModel().getSelectedItem());
					insertSubscription.setInt(14, (int) offerMonthsTF.getValue());
					insertSubscription.setString(15, subNumberTF.getText());
					insertSubscription.setString(16, (addToBillTF.getText() == null || addToBillTF.getText().isEmpty() || i > 0)
							? "0.0" : addToBillTF.getText());
					insertSubscription.executeUpdate();
					ResultSet rs = insertSubscription.getGeneratedKeys();
					subId = 0;
					if (rs.next()) {
						subId = rs.getLong(1);
					}
					Subscription subRow = BillingUtilityClass.subForSubId(subId);

					if ((subRow.getPaymentType().equalsIgnoreCase("Current Month")) && (subscriptionTypeLOV
							.getSelectionModel().getSelectedItem().equals("Actual Days Billing")
							|| subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("Fixed Rate"))) {
						BillingUtilityClass.createCurrentMonthStartingDue(subRow, subRow.getStartDate(),
								subRow.getStartDate().withDayOfMonth(1).plusMonths(1).minusDays(1));
					}

				} catch (NumberFormatException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
							.text("Only numeric values should be entered in Price and Tea Expenses fields").showError();
					Main.reconnect();
				} catch (SQLException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
							.text("There has been some error during subscription creation, please check all the values and retry")
							.showError();
					Main.reconnect();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
			}
		}
		return subId;
	}

	public void setProduct(Product product) {
		productRow = product;
	}

	public void setupProdBindings() {
		// prodTitledPane.setExpanded(true);
		freqValues.setText(productRow.getSupportingFreq());
		typeTF.setText(productRow.getType());
		billCategoryTF.setText(productRow.getBillCategory());
		dowTF.setText(productRow.getDow());
		nameTF.setText(productRow.getName());
		priceTF1.setText("" + productRow.getPrice());
		mondayTF.setText("" + productRow.getMonday());
		tuesdayTF.setText("" + productRow.getTuesday());
		wednesdayTF.setText("" + productRow.getWednesday());
		thursdayTF.setText("" + productRow.getThursday());
		fridayTF.setText("" + productRow.getFriday());
		saturdayTF.setText("" + productRow.getSaturday());
		sundayTF.setText("" + productRow.getSunday());
		codeTF.setText(productRow.getCode());
		firstDeliveryDate.setValue(productRow.getFirstDeliveryDate());
		issueDate.setValue(productRow.getIssueDate());

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
