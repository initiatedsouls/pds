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

import org.controlsfx.control.Notifications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class AdminHomeController implements Initializable {


    @FXML
    private Label adminAgencyName;

    @FXML
    private Label adminMobileLabel;

    @FXML
    private Label adminAddrLabel;

	@FXML
	private Button logoutButton;
	// Stage stage;
	Parent root;
	@FXML
	private TabPane tabPane;
	@FXML
	private Button changePwdButton;

	@FXML
	private Tab customersTab;
	@FXML
	private Tab lineInfoTab;
	@FXML
	private Tab pausedCustTab;
	@FXML
	private Tab hawkerTab;
	@FXML
	private Tab lineDistTab;
	@FXML
	private Tab additionalItemsTab;
	@FXML
	private Tab productsTab;
	@FXML
	private Tab stopHistoryTab;
	@FXML
	private Tab reportsTab;

	private ACustomerInfoTabController customerTabController;
	private AHawkerInfoTabController hawkerTabController;
	private ALineInfoTabController lineInfoTabController;
	private APausedCustomerTabController pausedCustTabController;
	private ALineDistributorTabController lineDistTabController;
	private AdditionalItemsController additionalItemsTabController;
	private AProductsTabController productsTabController;
	private AStopHistoryTabController stopHistTabController;
	private AReportsTabController reportsTabController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateAdminHeaders();
		loadTabs();
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
					if (oldValue == hawkerTab) {
						hawkerTabController.releaseVariables();
					}
					if (oldValue == lineDistTab) {
						lineDistTabController.releaseVariables();
					}
					if (oldValue == additionalItemsTab) {
						additionalItemsTabController.releaseVariables();
					}
					if (oldValue == productsTab) {
						productsTabController.releaseVariables();
					}
					if (oldValue == stopHistoryTab) {
						stopHistTabController.releaseVariables();
					}
					if (oldValue == reportsTab) {
						reportsTabController.releaseVariables();
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
					if (newValue == hawkerTab) {
						hawkerTabController.reloadData();
					}
					if (newValue == lineDistTab) {
						lineDistTabController.reloadData();
					}
					if (newValue == additionalItemsTab) {
						additionalItemsTabController.reloadData();
					}
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

		/*logoutButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					try {
						logoutClicked(new ActionEvent());
					} catch (IOException e) {
						Main._logger.debug("Error :",e);
						e.printStackTrace();
					}
				}
			}
		});*/
	}

	private void populateAdminHeaders() {

		HashMap<String,String> adminMap = Main.getAdminDetails();
		adminAgencyName.setText(adminMap.get("name"));
		adminMobileLabel.setText(adminMap.get("mobile"));
		adminAddrLabel.setText(adminMap.get("addr"));
		
	}
	@FXML
	private void logoutClicked(ActionEvent event) throws IOException {
		System.out.println("Logout clicked");
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

			hawkerTab = new Tab();
			FXMLLoader hawkerTabLoader = new FXMLLoader(getClass().getResource("A-HawkerInfoTab.fxml"));
			Parent hawkerroot = (Parent) hawkerTabLoader.load();
			hawkerTabController = hawkerTabLoader.<AHawkerInfoTabController> getController();
			hawkerTab.setText("Hawkers");
			hawkerTab.setContent(hawkerroot);

			hawkerTab.setOnSelectionChanged(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {

					hawkerTabController.reloadData();
				}
			});

			lineInfoTab = new Tab();
			FXMLLoader lineInfoTabLoader = new FXMLLoader(getClass().getResource("A-LineInfoTab.fxml"));
			Parent lineinforoot = (Parent) lineInfoTabLoader.load();
			lineInfoTabController = lineInfoTabLoader.<ALineInfoTabController> getController();
			lineInfoTab.setText("Line Information");
			lineInfoTab.setContent(lineinforoot);

			lineDistTab = new Tab();
			FXMLLoader lineDistTabLoader = new FXMLLoader(getClass().getResource("A-LineDistributorTab.fxml"));
			Parent linedistroot = (Parent) lineDistTabLoader.load();
			lineDistTabController = lineDistTabLoader.<ALineDistributorTabController> getController();
			lineDistTab.setText("Line Distribution Boy");
			lineDistTab.setContent(linedistroot);

			additionalItemsTab = new Tab();
			FXMLLoader additionalItemsTabLoader = new FXMLLoader(getClass().getResource("AdditionalItems.fxml"));
			Parent additionalItemsRoot = (Parent) additionalItemsTabLoader.load();
			additionalItemsTabController = additionalItemsTabLoader.<AdditionalItemsController> getController();
			additionalItemsTab.setText("Additional Items");
			additionalItemsTab.setContent(additionalItemsRoot);

			productsTab = new Tab();
			FXMLLoader productsTabLoader = new FXMLLoader(getClass().getResource("AProductsTab.fxml"));
			Parent productsRoot = (Parent) productsTabLoader.load();
			productsTabController = productsTabLoader.<AProductsTabController> getController();
			productsTab.setText("Products");
			productsTab.setContent(productsRoot);

			pausedCustTab = new Tab();
			FXMLLoader pausedCustTabLoader = new FXMLLoader(getClass().getResource("A-PausedCustomersTab.fxml"));
			Parent pausedcustroot = (Parent) pausedCustTabLoader.load();
			pausedCustTabController = pausedCustTabLoader.<APausedCustomerTabController> getController();
			pausedCustTab.setText("Stopped Customers");
			pausedCustTab.setContent(pausedcustroot);

			reportsTab = new Tab();
			FXMLLoader reportsTabLoader = new FXMLLoader(getClass().getResource("AReportsTab.fxml"));
			Parent reportsRoot = (Parent) reportsTabLoader.load();
			reportsTabController = reportsTabLoader.<AReportsTabController> getController();
			reportsTab.setText("Reports");
			reportsTab.setContent(reportsRoot);

			stopHistoryTab = new Tab();
			FXMLLoader stopHistTabLoader = new FXMLLoader(getClass().getResource("AStopHistoryTab.fxml"));
			Parent stopHistRoot = (Parent) stopHistTabLoader.load();
			stopHistTabController = stopHistTabLoader.<AStopHistoryTabController> getController();
			stopHistoryTab.setText("Stop History");
			stopHistoryTab.setContent(stopHistRoot);

			tabPane.getTabs().addAll(hawkerTab, lineInfoTab,customersTab,  lineDistTab, productsTab, additionalItemsTab,
					pausedCustTab, stopHistoryTab, reportsTab);
		} catch (IOException e) {

			Main._logger.debug("Error :",e);
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
			if (t == hawkerTab) {
				hawkerTabController.reloadData();
			}
			if (t == lineDistTab) {
				lineDistTabController.reloadData();
			}
			if (t == additionalItemsTab) {
				additionalItemsTabController.reloadData();
			}
			if (t == productsTab) {
				productsTabController.reloadData();
			}
			if (t == stopHistoryTab) {
				stopHistTabController.reloadData();
			}
			if (t == reportsTab) {
				reportsTabController.reloadData();
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
			try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String deleteString = "update admin_login set password =? where username='admin'";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setString(1, changePwdDialog.getEditor().getText());

				deleteStmt.executeUpdate();
				con.commit();

				Notifications.create().title("Password updated")
						.text("Password was successfully updated. Please login again!").hideAfter(Duration.seconds(5))
						.showInformation();
				logoutClicked(new ActionEvent());
			} catch (SQLException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of hawker bill has failed").showError();
			} catch (IOException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}

		}
	}

    @FXML
    void updateAdminDetails(ActionEvent event) {
    	try {
		TextField mobileNum = new TextField();
		TextField agencyName = new TextField();
		TextField addr = new TextField();
    	

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select company_name,company_mobile,company_addr from admin_login where username ='admin' ";
			PreparedStatement stmt = con.prepareStatement(query);
//			stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				agencyName.setText(rs.getString(1));
				mobileNum.setText(rs.getString(2));
				addr.setText(rs.getString(3));
			}
			rs.close();
			stmt.close();
		
    	
    	Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
		deleteWarning.setTitle("Update Admin Details");
		deleteWarning.setHeaderText("Update admin details below.");
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		deleteWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			
			grid.add(new Label("Agency Name"), 0, 0);
			grid.add(new Label("Mobile"), 0, 1);
			grid.add(new Label("Address"), 0, 2);
			grid.add(agencyName, 1, 1);
			grid.add(mobileNum, 1, 0);
			grid.add(addr, 1, 2);
			deleteWarning.getDialogPane().setContent(grid);
			Optional<ButtonType> result = deleteWarning.showAndWait();
			if (result.isPresent() && result.get() == saveButtonType) {
				PreparedStatement updateStmt = con.prepareStatement("update admin_login set company_name=?, company_mobile=?, company_addr=?");
				updateStmt.setString(1, agencyName.getText());
				updateStmt.setString(2, mobileNum.getText());
				updateStmt.setString(3, addr.getText());
				updateStmt.executeUpdate();
			}
    	} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
    }
	

    @FXML
    void exportDataClicked(ActionEvent event) {
//    	ExcelToPDF.excelToPDF();
    	ExportToExcel.exportDataToExcel();
    }
	

	@FXML
	private void exitMenuOptionClicked(ActionEvent evt) {
		System.exit(0);
	}
}
