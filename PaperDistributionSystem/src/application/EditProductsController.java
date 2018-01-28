package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

public class EditProductsController implements Initializable {

	@FXML
	public GridPane gridPane;
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
	private TextField dueTF;
	@FXML
	private ComboBox<String> billCategoryTF;
	@FXML
	private ComboBox<String> dowTF;

	@FXML
	private DatePicker firstDeliveryDate;
	@FXML
	private DatePicker issueDate;

	private CheckComboBox<String> prodFreq;

	private Product productRow;
	

	@FXML
	private TableView<ProductSpecialPrice> spclPriceTable;

	@FXML
	private TableColumn<ProductSpecialPrice, LocalDate> spclPriceDateCol;

	@FXML
	private TableColumn<ProductSpecialPrice, String> spclPriceDayCol;

	@FXML
	private TableColumn<ProductSpecialPrice, Double> spclPriceCol;

	private ObservableList<String> freqValues = FXCollections.observableArrayList();
	private ObservableList<String> productTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> billCategoryValues = FXCollections.observableArrayList();
	private ObservableList<ProductSpecialPrice> prodSpclPriceValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		spclPriceDateCol.setCellValueFactory(new PropertyValueFactory<ProductSpecialPrice, LocalDate>("fullDate"));
		spclPriceDayCol.setCellValueFactory(new PropertyValueFactory<ProductSpecialPrice, String>("day"));
		spclPriceCol.setCellValueFactory(new PropertyValueFactory<ProductSpecialPrice, Double>("price"));
		spclPriceDateCol.setCellFactory(
				new Callback<TableColumn<ProductSpecialPrice, LocalDate>, TableCell<ProductSpecialPrice, LocalDate>>() {

					@Override
					public TableCell<ProductSpecialPrice, LocalDate> call(
							TableColumn<ProductSpecialPrice, LocalDate> param) {
						TextFieldTableCell<ProductSpecialPrice, LocalDate> cell = new TextFieldTableCell<ProductSpecialPrice, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		prodFreq = new CheckComboBox<String>();
		freqHBox.getChildren().add(prodFreq);
		codeTF.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 4)
					codeTF.setText(oldValue);
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

		firstDeliveryDate.setConverter(Main.dateConvertor);
		issueDate.setConverter(Main.dateConvertor);
		firstDeliveryDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					firstDeliveryDate.setValue(
							firstDeliveryDate.getConverter().fromString(firstDeliveryDate.getEditor().getText()));
				}
			}
		});
		issueDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					issueDate.setValue(issueDate.getConverter().fromString(issueDate.getEditor().getText()));
				}
			}
		});
	}

	public void setProduct(Product product) {
		productRow = product;
	}

	public void setupBindings() {
		populateProdFreqValues();
		populateProdTypeValues();
		populateBillCategoryValues();
		refreshProdSpecialPriceTable();
		checkItems();
		dowTF.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
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

	private void checkItems() {
		String[] freqItems = productRow.getSupportingFreq().split(",");
		for (int i = 0; i < freqItems.length; i++) {
			String freq = freqItems[i].trim();
			prodFreq.getCheckModel().check(freq);
		}
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

	public boolean isValid() {
		boolean valid = true;
		if (nameTF.getText().isEmpty() || nameTF.getText() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Name").text("Product name cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (codeTF.getText().isEmpty() || codeTF.getText() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Code").text("Product code cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (typeTF.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Type Selection")
					.text("Type selection cannot be left empty and must be selected").hideAfter(Duration.seconds(5))
					.showError();
		}
		if (prodFreq.getCheckModel().isEmpty()) {
			valid = false;
			Notifications.create().text("Invalid Frequency")
					.text("Product Frequency selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (productExistsInCategory(nameTF.getText(), billCategoryTF.getSelectionModel().getSelectedItem())) {
			valid = false;
			Notifications.create().text("Duplicate Product in Bill Category")
					.text("Product Name already exists in this bill category.").hideAfter(Duration.seconds(5))
					.showError();
		}
		return valid;
	}

	public Product returnUpdatedProduct() {
		if (isValid()) {
			try {
				productRow.setName(nameTF.getText());
				productRow.setType(typeTF.getSelectionModel().getSelectedItem());
				productRow.setSupportingFreq(supportedFreq(prodFreq.getCheckModel().getCheckedItems()));
				productRow.setPrice(Double.parseDouble(priceTF.getText().isEmpty() ? "0.0" : priceTF.getText()));
				productRow.setMonday(Double.parseDouble(mondayTF.getText().isEmpty() ? "0.0" : mondayTF.getText()));
				productRow.setTuesday(Double.parseDouble(tuesdayTF.getText().isEmpty() ? "0.0" : tuesdayTF.getText()));
				productRow.setWednesday(
						Double.parseDouble(wednesdayTF.getText().isEmpty() ? "0.0" : wednesdayTF.getText()));
				productRow
						.setThursday(Double.parseDouble(thursdayTF.getText().isEmpty() ? "0.0" : thursdayTF.getText()));
				productRow.setFriday(Double.parseDouble(fridayTF.getText().isEmpty() ? "0.0" : fridayTF.getText()));
				productRow
						.setSaturday(Double.parseDouble(saturdayTF.getText().isEmpty() ? "0.0" : saturdayTF.getText()));
				productRow.setSunday(Double.parseDouble(sundayTF.getText().isEmpty() ? "0.0" : sundayTF.getText()));
				productRow.setCode(codeTF.getText());
				productRow.setDow(dowTF.getSelectionModel().getSelectedItem());
				productRow.setFirstDeliveryDate(firstDeliveryDate.getValue());
				productRow.setIssueDate(issueDate.getValue());
				productRow.setBillCategory(billCategoryTF.getSelectionModel().getSelectedItem());
				productRow.updateProductRecord();
				
				if(dueTF.getText()!=null){
					try {
						Double val = Double.parseDouble(dueTF.getText().trim());
						PreparedStatement stmt = 
								Main.dbConnection.prepareStatement("update customer cust set cust.total_due=(cust.total_due+?) where cust.customer_id in (select distinct customer_id from subscription where product_id=? and payment_type = 'Current Month' and TYPE = 'Actual Days Billing')");
						stmt.setDouble(1, val);
						stmt.setLong(2, productRow.getProductId());
						int count = stmt.executeUpdate();

						Notifications.create().title("Due added to customers")
								.text("Due amount Rs."+val+" has been added to "+count+" matching customers.")
								.hideAfter(Duration.seconds(5)).show();
					} catch (NumberFormatException e) {
				Notifications.create().title("Invalid value")
						.text("Please enter numbers for Due field.")
						.hideAfter(Duration.seconds(5)).showError();
						e.printStackTrace();
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e) {
				Notifications.create().title("Invalid value")
						.text("Please enter numbers for Price and Monday - Sunday fields")
						.hideAfter(Duration.seconds(5)).showError();
				Main._logger.debug("Error :",e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}
		}
		return productRow;
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
				if(billCategoryValues!=null && !billCategoryValues.contains(rs.getString(1)))
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

	private boolean productExistsInCategory(String name, String category) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select count(*) from products where lower(name)=? and lower(bill_category)=? and product_id<>?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name.toLowerCase());
			stmt.setString(2, category.toLowerCase());
			stmt.setLong(3, productRow.getProductId());
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
	

	public void refreshProdSpecialPriceTable() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					prodSpclPriceValues.clear();
					PreparedStatement stmt = con.prepareStatement(
							"select SPCL_PRICE_ID, PRODUCT_ID, FULL_DATE, PRICE from prod_spcl_price where product_id=? order by full_date desc");
					stmt.setLong(1, productRow.getProductId());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						prodSpclPriceValues.add(new ProductSpecialPrice(rs.getLong(1), rs.getLong(2),
								rs.getDate(3).toLocalDate(), rs.getDouble(4)));
					}
					spclPriceTable.getItems().clear();
					spclPriceTable.getItems().addAll(prodSpclPriceValues);
					spclPriceTable.refresh();
					rs.close();
					stmt.close();
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


	public void releaseVariables() {
		freqValues = null;
		productTypeValues = null;
		billCategoryValues = null;
		prodSpclPriceValues=null;
		freqValues = FXCollections.observableArrayList();
		productTypeValues = FXCollections.observableArrayList();
		billCategoryValues = FXCollections.observableArrayList();
		prodSpclPriceValues=FXCollections.observableArrayList();
	}

}
