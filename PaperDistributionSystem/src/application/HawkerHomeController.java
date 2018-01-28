package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import org.controlsfx.control.Notifications;

import com.lowagie.toolbox.arguments.filters.ImageFilter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class HawkerHomeController implements Initializable {
	JFileChooser fc = new JFileChooser();
	@FXML
	private Button logoutButton;
	@FXML
	private Button refreshButton;
	@FXML
	private Button changePwdButton;
	@FXML
	private Label hawkerName;
	@FXML
	private Label code;
	@FXML
	private Label agencyName;
	@FXML
	private Label mobileNum;
	@FXML
	private Label billCategory;
	@FXML
	private Label pointName;

	@FXML
	private Label adminAgencyName;

	@FXML
	private Label adminMobileLabel;

	@FXML
	private Label adminAddrLabel;
	// Stage stage;
	Parent root;
	@FXML
	private Tab customersTab;
	@FXML
	private Tab lineInfoTab;
	@FXML
	private Tab pausedCustTab;
	@FXML
	private Tab lineDistTab;
	@FXML
	private Tab productsTab;
	@FXML
	private Tab stopHistoryTab;
	@FXML
	private Tab reportsTab;
	@FXML
	private Tab hawkerProfileTab;
	@FXML
	private TabPane tabPane;

	private ACustomerInfoTabController customerTabController;
	private ALineDistributorTabController lineDistTabController;
	private ALineInfoTabController lineInfoTabController;
	private APausedCustomerTabController pausedCustTabController;
	private AProductsTabController productsTabController;
	private AStopHistoryTabController stopHistTabController;
	private AReportsTabController reportsTabController;
	private HawkerProfileController hawkerProfileTabController;
	private ObservableList<Product> productValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

//		populateBillCategory();
		populateAdminHeaders();
		loadTabs();
		if (!HawkerLoginController.loggedInHawker.getCustomerAccess().equals("Y"))
			tabPane.getTabs().remove(customersTab);
		if (!HawkerLoginController.loggedInHawker.getLineInfoAccess().equals("Y"))
			tabPane.getTabs().remove(lineInfoTab);
		if (!HawkerLoginController.loggedInHawker.getPausedCustAccess().equals("Y"))
			tabPane.getTabs().remove(pausedCustTab);
		if (!HawkerLoginController.loggedInHawker.getLineDistAccess().equals("Y"))
			tabPane.getTabs().remove(lineDistTab);
		if (!HawkerLoginController.loggedInHawker.getProductAccess().equals("Y"))
			tabPane.getTabs().remove(productsTab);
		if (!HawkerLoginController.loggedInHawker.getStopHistoryAccess().equals("Y"))
			tabPane.getTabs().remove(stopHistoryTab);
		if (!HawkerLoginController.loggedInHawker.getReportsAccess().equals("Y"))
			tabPane.getTabs().remove(reportsTab);

		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				if (oldValue != null) {
					if (oldValue == customersTab) {
						customerTabController.releaseVariables();
					}
					if (oldValue == lineInfoTab) {
						lineInfoTabController.releaseVariables();
					}
					if (oldValue == pausedCustTab) {
						pausedCustTabController.releaseVariables();
					}
					// if (oldValue == hawkerTab) {
					// hawkerTabController.releaseVariables();
					// }
					if (oldValue == lineDistTab) {
						lineDistTabController.releaseVariables();
					}
					// if (oldValue == additionalItemsTab) {
					// additionalItemsTabController.releaseVariables();
					// }
					if (oldValue == productsTab) {
						productsTabController.releaseVariables();
					}
					if (oldValue == stopHistoryTab) {
						stopHistTabController.releaseVariables();
					}
					if (oldValue == reportsTab) {
						reportsTabController.releaseVariables();
					}
					if (oldValue == hawkerProfileTab) {
//						hawkerProfileTabController.releaseVariables();
					}
					System.gc();
				}

				if (newValue != null) {
					if (newValue == customersTab) {
						customerTabController.reloadData();
					}
					if (newValue == lineInfoTab) {
						lineInfoTabController.reloadData();
					}
					if (newValue == pausedCustTab) {
						pausedCustTabController.reloadData();
					}
					// if (newValue == hawkerTab) {
					// hawkerTabController.reloadData();
					// }
					if (newValue == lineDistTab) {
						lineDistTabController.reloadData();
					}
					// if (newValue == additionalItemsTab) {
					// additionalItemsTabController.reloadData();
					// }
					if (newValue == productsTab) {
						productsTabController.reloadData();
					}
					if (newValue == stopHistoryTab) {
						stopHistTabController.reloadData();
					}
					if (newValue == reportsTab) {
						reportsTabController.reloadData();
					}

				}

			}
		});
		tabPane.getSelectionModel().selectFirst();
		hawkerProfileTabController.reloadData();
		fc.setFileFilter(new ImageFilter());

	}

	private void populateAdminHeaders() {

//		hawkerName.setText(HawkerLoginController.loggedInHawker.getName());
//		code.setText(HawkerLoginController.loggedInHawker.getHawkerCode());
//		agencyName.setText(HawkerLoginController.loggedInHawker.getAgencyName());
//		mobileNum.setText(HawkerLoginController.loggedInHawker.getMobileNum());
//		pointName.setText(HawkerLoginController.loggedInHawker.getPointName());
		HashMap<String, String> adminMap = Main.getAdminDetails();
		adminAgencyName.setText(adminMap.get("name"));
		adminMobileLabel.setText(adminMap.get("mobile"));
		adminAddrLabel.setText(adminMap.get("addr"));

	}

	private void populateBillCategory() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select bill_category from point_name where name =? order by bill_category";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				billCategory.setText(rs.getString(1));
			}
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

	@FXML
	private void logoutClicked(ActionEvent event) throws IOException {
		System.out.println("Logout clicked");
		HawkerLoginController.loggedInHawker = null;
		// stage=(Stage) logoutButton.getScene().getWindow();
		// load up OTHER FXML document
		root = FXMLLoader.load(getClass().getResource("HawkerLogin.fxml"));

		Scene scene = new Scene(root);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.setMaximized(true);
		Main.primaryStage.show();
	}

	private void loadTabs() {
		tabPane.getTabs().clear();
		try {
			FXMLLoader customerTabLoader = new FXMLLoader(getClass().getResource("A-CustomersTab.fxml"));
			Parent custroot = (Parent) customerTabLoader.load();
			customerTabController = customerTabLoader.<ACustomerInfoTabController> getController();
			customersTab = new Tab();
			customersTab.setText("Customers");
			customersTab.setContent(custroot);

			lineDistTab = new Tab();
			FXMLLoader lineDistTabLoader = new FXMLLoader(getClass().getResource("A-LineDistributorTab.fxml"));
			Parent linedistroot = (Parent) lineDistTabLoader.load();
			lineDistTabController = lineDistTabLoader.<ALineDistributorTabController> getController();
			lineDistTab.setText("Line Distribution Boy");
			lineDistTab.setContent(linedistroot);

			lineInfoTab = new Tab();
			FXMLLoader lineInfoTabLoader = new FXMLLoader(getClass().getResource("A-LineInfoTab.fxml"));
			Parent lineinforoot = (Parent) lineInfoTabLoader.load();
			lineInfoTabController = lineInfoTabLoader.<ALineInfoTabController> getController();
			lineInfoTab.setText("Line Information");
			lineInfoTab.setContent(lineinforoot);

			pausedCustTab = new Tab();
			FXMLLoader pausedCustTabLoader = new FXMLLoader(getClass().getResource("A-PausedCustomersTab.fxml"));
			Parent pausedcustroot = (Parent) pausedCustTabLoader.load();
			pausedCustTabController = pausedCustTabLoader.<APausedCustomerTabController> getController();
			pausedCustTab.setText("Stopped Customers");
			pausedCustTab.setContent(pausedcustroot);
			productsTab = new Tab();
			FXMLLoader productsTabLoader = new FXMLLoader(getClass().getResource("AProductsTab.fxml"));
			Parent productsRoot = (Parent) productsTabLoader.load();
			productsTabController = productsTabLoader.<AProductsTabController> getController();
			productsTab.setText("Products");
			productsTab.setContent(productsRoot);

			stopHistoryTab = new Tab();
			FXMLLoader stopHistTabLoader = new FXMLLoader(getClass().getResource("AStopHistoryTab.fxml"));
			Parent stopHistRoot = (Parent) stopHistTabLoader.load();
			stopHistTabController = stopHistTabLoader.<AStopHistoryTabController> getController();
			stopHistoryTab.setText("Stop History");
			stopHistoryTab.setContent(stopHistRoot);

			reportsTab = new Tab();
			FXMLLoader reportsTabLoader = new FXMLLoader(getClass().getResource("AReportsTab.fxml"));
			Parent reportsRoot = (Parent) reportsTabLoader.load();
			reportsTabController = reportsTabLoader.<AReportsTabController> getController();
			reportsTab.setText("Reports");
			reportsTab.setContent(reportsRoot);

			hawkerProfileTab = new Tab();
			FXMLLoader hawkerProfileTabLoader = new FXMLLoader(getClass().getResource("HawkerProfile.fxml"));
			Parent hawkerProfileRoot = (Parent) hawkerProfileTabLoader.load();
			hawkerProfileTabController = hawkerProfileTabLoader.<HawkerProfileController> getController();
			hawkerProfileTab.setText("Hawker Profile");
			hawkerProfileTab.setContent(hawkerProfileRoot);

			tabPane.getTabs().addAll(hawkerProfileTab, lineInfoTab, customersTab, lineDistTab, pausedCustTab, stopHistoryTab, productsTab,
					reportsTab);
		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	@FXML
	private void refreshClicked(ActionEvent event) {
		populateAdminHeaders();
		Tab t = tabPane.getSelectionModel().getSelectedItem();
		if (t != null) {
			if (t == customersTab) {
				customerTabController.reloadData();
			}
			if (t == lineInfoTab) {
				lineInfoTabController.reloadData();
			}
			if (t == pausedCustTab) {
				pausedCustTabController.reloadData();
			}
			if (t == lineDistTab) {
				lineDistTabController.reloadData();
			}
			if (t == productsTab) {
				productsTabController.reloadData();
			}
			if (t == reportsTab) {
				reportsTabController.reloadData();
			}
			if (t == hawkerProfileTab) {
				hawkerProfileTabController.reloadData();
			}

		}
	}

	@FXML
	private void changePasswordClicked(ActionEvent evt) {
		TextInputDialog changePwdDialog = new TextInputDialog();
		changePwdDialog.setTitle("Change password");
		changePwdDialog.setHeaderText("Please enter the new password. \nPassword must be atleast 5 characters long.");
		// changePwdDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
		// ButtonType.CANCEL);
		final Button btOk = (Button) changePwdDialog.getDialogPane().lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> {
			if (changePwdDialog.getEditor().getText().isEmpty() || changePwdDialog.getEditor().getText().length() < 5) {
				Notifications.create().title("Empty password")
						.text("Password cannot be left empty and must be more than 5 characters. Try again.")
						.hideAfter(Duration.seconds(5)).showError();
				event.consume();
			}
		});
		Optional<String> result = changePwdDialog.showAndWait();
		if (result.isPresent()) {
			// changeHawkerPwd(hawkerRow,result.get());
			HawkerLoginController.loggedInHawker.setPassword(result.get());
			HawkerLoginController.loggedInHawker.updateHawkerRecord();
			Notifications.create().title("Password updated").text("Password was successfully updated")
					.hideAfter(Duration.seconds(5)).showInformation();
		}
	}

	@FXML
	private void changeMobileClicked(ActionEvent evt) {
		AHawkerInfoTabController.showEditHawkerDialog(HawkerLoginController.loggedInHawker);

		Notifications.create().title("Please login again")
				.text("Hawker profile updated, please logout and login again to see changes.")
				.hideAfter(Duration.seconds(5)).showInformation();
		populateAdminHeaders();
		/*
		 * TextInputDialog changeMobDialog = new TextInputDialog();
		 * changeMobDialog.setTitle("Change Mobile");
		 * changeMobDialog.setHeaderText(
		 * "Please enter the new mobile. \nMobile number should only contain 10 digits."
		 * ); //
		 * changePwdDialog.getDialogPane().getButtonTypes().addAll(ButtonType.
		 * OK, // ButtonType.CANCEL); final Button btOk = (Button)
		 * changeMobDialog.getDialogPane().lookupButton(ButtonType.OK);
		 * changeMobDialog.getEditor().setText(HawkerLoginController.
		 * loggedInHawker.getMobileNum());
		 * btOk.addEventFilter(ActionEvent.ACTION, event -> { if
		 * (changeMobDialog.getEditor().getText().isEmpty() ||
		 * changeMobDialog.getEditor().getText().length() != 10) {
		 * Notifications.create().title("Invalid mobile number").text(
		 * "Mobile number should only contain 10 DIGITS")
		 * .hideAfter(Duration.seconds(5)).showError(); event.consume(); } else
		 * if (AddHawkerExtraScreenController.mobileNumExists(changeMobDialog.
		 * getEditor().getText(),
		 * HawkerLoginController.loggedInHawker.getHawkerCode())){
		 * 
		 * Notifications.create().title("Mobile already exists") .text(
		 * "Hawker with same Mobile Number alraedy exists. Please enter a different value."
		 * ) .hideAfter(Duration.seconds(5)).showError(); event.consume(); } });
		 * Optional<String> result = changeMobDialog.showAndWait(); if
		 * (result.isPresent()) { // changeHawkerPwd(hawkerRow,result.get());
		 * HawkerLoginController.loggedInHawker.setMobileNum(result.get());
		 * HawkerLoginController.loggedInHawker.updateHawkerRecord();
		 * Notifications.create().title("Mobile Number updated").text(
		 * "Mobile Number was successfully updated")
		 * .hideAfter(Duration.seconds(5)).showInformation(); }
		 */
	}

	@FXML
	private void exitMenuOptionClicked(ActionEvent evt) {
		System.exit(0);
	}
	
	@FXML
	private void updateProductFixedRateClicked(ActionEvent evt) {
		populateDailyProducts();
		Dialog<ButtonType> prodDialog = new Dialog<ButtonType>();
		prodDialog.setTitle("Update product fixed rate.");
		prodDialog.setHeaderText("Update customer subscription fixed rate for the product selection below.");
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		prodDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		Label prodLabel = new Label("Product: ");
		ComboBox<Product> prodBox = new ComboBox<Product>(productValues);
		prodBox.setConverter(new StringConverter<Product>() {

			@Override
			public String toString(Product object) {
				if (object != null)
					return object.getName();
				else
					return null;
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

//		new AutoCompleteComboBoxListener<Product>(prodBox);
		Label fixedRateLabel = new Label("Fixed Rate: ");
		TextField fixedRateTF = new TextField("0.0");
		grid.add(prodLabel, 0, 0);
		grid.add(prodBox, 1, 0);
		grid.add(fixedRateLabel, 0, 1);
		grid.add(fixedRateTF, 1, 1);
		prodDialog.getDialogPane().setContent(grid);
		prodBox.getSelectionModel().selectFirst();
		Button yesButton = (Button) prodDialog.getDialogPane().lookupButton(saveButtonType);
		yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
			if (prodBox.getSelectionModel().getSelectedItem() == null) {

				Notifications.create().title("Invalid product selection")
						.text("Please select a product before updating.").hideAfter(Duration.seconds(10))
						.showError();
				btnEvent.consume();
			}
			if (fixedRateTF.getText() == null) {
				Notifications.create().title("Invalid Fixed Rate").text("Please provide appropriate fixed rate.")
						.hideAfter(Duration.seconds(10)).showError();
				btnEvent.consume();
			}
			if (fixedRateTF.getText() != null) {
				try {
					Double.parseDouble(fixedRateTF.getText().trim());
				} catch (NumberFormatException e) {
					Notifications.create().title("Invalid Fixed Rate").text("Please provide appropriate fixed rate.")
							.hideAfter(Duration.seconds(10)).showError();
					btnEvent.consume();
				}
			}
		});
		Optional<ButtonType> result = prodDialog.showAndWait();
		if (result.isPresent() && result.get() == saveButtonType) {
			Notifications.create().title("Updating fixed rate.")
					.text("Updating fixed rate of all subscriptions for selected product. Please refresh when finished.")
					.hideAfter(Duration.seconds(10)).showInformation();
			try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
//				productValues.clear();
				PreparedStatement stmt;
				stmt = con.prepareStatement(
						"update subscription sub set sub.subscription_cost=? where sub.product_id=? and sub.type='Fixed Rate' and sub.subscription_cost>0 and exists (select 1 from customer cust where cust.customer_id=sub.customer_id and cust.hawker_id=?)");
				stmt.setDouble(1, Double.parseDouble(fixedRateTF.getText().trim()));
				stmt.setLong(2, prodBox.getSelectionModel().getSelectedItem().getProductId());
				stmt.setLong(3, HawkerLoginController.loggedInHawker.getHawkerId());

				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().title("Error").text("Something failed while updating subscriptions fixed rate.")
				.hideAfter(Duration.seconds(10)).showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().title("Error").text("Something failed while updating subscriptions fixed rate.")
				.hideAfter(Duration.seconds(10)).showError();
			}
		}
	}

	public void populateDailyProducts() {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			productValues.clear();
			PreparedStatement stmt;
			stmt = con.prepareStatement(
					"SELECT prod.PRODUCT_ID, prod.NAME, prod.TYPE, prod.SUPPORTED_FREQ, prod.MONDAY, prod.TUESDAY, prod.WEDNESDAY, prod.THURSDAY, prod.FRIDAY, prod.SATURDAY, prod.SUNDAY, prod.PRICE, prod.CODE, prod.DOW, prod.FIRST_DELIVERY_DATE, prod.ISSUE_DATE, prod.bill_Category FROM products prod, point_name pn, hawker_info hwk where hwk.HAWKER_CODE = ? and hwk.POINT_NAME = pn.name and pn.BILL_CATEGORY = prod.BILL_CATEGORY ORDER BY name");
			stmt.setString(1, HawkerLoginController.loggedInHawker.getHawkerCode());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				productValues.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
						rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13), rs.getString(14),
						rs.getDate(15) == null ? null : rs.getDate(15).toLocalDate(),
						rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17)));
			}
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

}
