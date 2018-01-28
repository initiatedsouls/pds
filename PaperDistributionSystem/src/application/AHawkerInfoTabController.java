package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class AHawkerInfoTabController implements Initializable {

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private TableView<Hawker> hawkerTable;
	@FXML
	private TableView<HawkerBilling> hawkerBillingTable;

	// Add Hawker Fields
	@FXML
	private TextField addHwkName;
	@FXML
	private TextField addHwkInitials;
	@FXML
	private TextField addHwkCode;
	@FXML
	private TextField addHwkMob;
	@FXML
	private TextField addHwkAgencyName;
	@FXML
	private TextField addHwkFee;
	@FXML
	private TextField addHwkOldHNum;
	@FXML
	private TextField addHwkNewHNum;
	@FXML
	private TextField addHwkAddrLine1;
	@FXML
	private TextField addHwkAddrLine2;
	@FXML
	private TextField addHwkLocality;
	@FXML
	private TextField addHwkCity;
	@FXML
	private CheckBox addHwkActive;
	@FXML
	private ComboBox<String> addHwkState;
	@FXML
	private ComboBox<String> addHwkProf1;
	@FXML
	private ComboBox<String> addHwkProf2;
	@FXML
	private TextField addHwkProf3;
	@FXML
	private ComboBox<String> addHwkEmployment;
	@FXML
	private TextField addHwkComments;
	@FXML
	private ComboBox<String> addPointName;
	@FXML
	private TextField addHwkBuildingStreet;
	@FXML
	private TextField addBankAcNo;
	@FXML
	private TextField addBankName;
	@FXML
	private TextField addIfscCode;

	// ScreenAccess Checkbox
	@FXML
	private CheckBox customerCheckBox;
	@FXML
	private CheckBox billingCheckBox;
	@FXML
	private CheckBox lineInfoCheckBox;
	@FXML
	private CheckBox lineDistCheckBox;
	@FXML
	private CheckBox pausedCustCheckBox;
	@FXML
	private CheckBox prodCatalogCheckBox;
	@FXML
	private CheckBox reportsCheckBox;
	@FXML
	private CheckBox stopHistoryCheckBox;

	// Hawker Columns
	@FXML
	private TableColumn<Hawker, Long> hwkIdColumn;
	@FXML
	private TableColumn<Hawker, String> hwkNameColumn;
	@FXML
	private TableColumn<Hawker, String> hwkInitialsColumn;
	@FXML
	private TableColumn<Hawker, String> hwkCodeColumn;
	@FXML
	private TableColumn<Hawker, String> hwkMobileColumn;
	@FXML
	private TableColumn<Hawker, String> hwkAgencyColumn;
	@FXML
	private TableColumn<Hawker, Double> hwkFeeColumn;
	@FXML
	private TableColumn<Hawker, Boolean> hwkActiveColumn;
	@FXML
	private TableColumn<Hawker, Double> hwkTotalDueColumn;
	@FXML
	private TableColumn<Hawker, String> hwkOldHouseNumColumn;
	@FXML
	private TableColumn<Hawker, String> hwkNewHouseNumColumn;
	@FXML
	private TableColumn<Hawker, String> hwkAddrLine1Column;
	@FXML
	private TableColumn<Hawker, String> hwkAddrLine2Column;
	@FXML
	private TableColumn<Hawker, String> hwkLocalityColumn;
	@FXML
	private TableColumn<Hawker, String> hwkCityColumn;
	@FXML
	private TableColumn<Hawker, String> hwkStateColumn;
	@FXML
	private TableColumn<Hawker, String> profile1Column;
	@FXML
	private TableColumn<Hawker, String> profile2Column;
	@FXML
	private TableColumn<Hawker, String> profile3Column;
	@FXML
	private TableColumn<Hawker, String> employmentColumn;
	@FXML
	private TableColumn<Hawker, String> commentsColumn;
	@FXML
	private TableColumn<Hawker, String> pointNameColumn;
	@FXML
	private TableColumn<Hawker, String> buildingStreetColumn;
	@FXML
	private TableColumn<Hawker, String> bankAcNumColumn;
	@FXML
	private TableColumn<Hawker, String> bankNameColumn;
	@FXML
	private TableColumn<Hawker, String> ifscCodeColumn;
	@FXML
	private TableColumn<Hawker, String> benNameColumn;

	// Billing columns
	@FXML
	private TableColumn<HawkerBilling, LocalDate> hwkBillDateColumn;
	@FXML
	private TableColumn<HawkerBilling, Double> hwkAmountColumn;
	@FXML
	private TableColumn<HawkerBilling, String> hwkBillTypeColumn;

	private String searchText;
	private FilteredList<Hawker> filteredData;

	@FXML
	private Button enableAllButton;
	@FXML
	private Button disableAllButton;
	@FXML
	private Button addHwkExtraButton;

	@FXML
	private Button saveButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button resetButton;
	@FXML
	public RadioButton filterRadioButton;
	@FXML
	public RadioButton showAllRadioButton;

	@FXML
	public Label billCategoryLabel;

	private ObservableList<Hawker> hawkerMasterData = FXCollections.observableArrayList();
	private ObservableList<HawkerBilling> hawkerBillingMasterData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> employmentValues = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();

	public AHawkerInfoTabController() {
		super();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (HawkerLoginController.loggedInHawker != null) {
			filterRadioButton.setVisible(false);
			showAllRadioButton.setVisible(false);
		} else {
			ToggleGroup tg = new ToggleGroup();
			filterRadioButton.setToggleGroup(tg);
			showAllRadioButton.setToggleGroup(tg);
			filterRadioButton.setSelected(true);

		}
		hwkIdColumn.setCellValueFactory(new PropertyValueFactory<Hawker, Long>("hawkerId"));
		hwkNameColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("name"));
		hwkInitialsColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("initials"));
		hwkCodeColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("hawkerCode"));

		hwkMobileColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("mobileNum"));
		hwkAgencyColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("agencyName"));
		hwkActiveColumn.setCellFactory(CheckBoxTableCell.forTableColumn(hwkActiveColumn));
		hwkActiveColumn.setCellValueFactory(param -> param.getValue().isActive());

		hwkFeeColumn.setCellValueFactory(new PropertyValueFactory<Hawker, Double>("fee"));
		hwkTotalDueColumn.setCellValueFactory(new PropertyValueFactory<Hawker, Double>("totalDue"));
		hwkNewHouseNumColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("newHouseNum"));
		hwkOldHouseNumColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("oldHouseNum"));
		hwkAddrLine1Column.setCellValueFactory(new PropertyValueFactory<Hawker, String>("addrLine1"));
		hwkAddrLine2Column.setCellValueFactory(new PropertyValueFactory<Hawker, String>("addrLine2"));
		hwkLocalityColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("locality"));
		hwkCityColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("city"));
		hwkStateColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("state"));
		profile1Column.setCellValueFactory(new PropertyValueFactory<Hawker, String>("profile1"));
		profile2Column.setCellValueFactory(new PropertyValueFactory<Hawker, String>("profile2"));
		profile3Column.setCellValueFactory(new PropertyValueFactory<Hawker, String>("profile3"));
		employmentColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("employment"));
		commentsColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("comments"));
		pointNameColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("pointName"));
		buildingStreetColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("buildingStreet"));
		bankAcNumColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("bankAcNo"));
		bankNameColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("bankName"));
		ifscCodeColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("ifscCode"));
		benNameColumn.setCellValueFactory(new PropertyValueFactory<Hawker, String>("benName"));

		hwkBillDateColumn.setCellValueFactory(new PropertyValueFactory<HawkerBilling, LocalDate>("entryDate"));
		hwkBillDateColumn
				.setCellFactory(new Callback<TableColumn<HawkerBilling, LocalDate>, TableCell<HawkerBilling, LocalDate>>() {
 
					@Override
					public TableCell<HawkerBilling, LocalDate> call(TableColumn<HawkerBilling, LocalDate> param) {
						TextFieldTableCell<HawkerBilling, LocalDate> cell = new TextFieldTableCell<HawkerBilling, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						
						return cell;
					}
				});
		hwkAmountColumn.setCellValueFactory(new PropertyValueFactory<HawkerBilling, Double>("amount"));
		hwkBillTypeColumn.setCellValueFactory(new PropertyValueFactory<HawkerBilling, String>("type"));

		addHwkState.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");
		if (hawkerTable.getSelectionModel().getSelectedItem() == null) {
			disableAllChild();
		}

		addPointName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					refreshHawkerTable();
					populateBillCategory();
				} else {
					billCategoryLabel.setText("");
				}
			}
		});

		// addHwkMob.textProperty().addListener(new ChangeListener<String>() {
		//
		// @Override
		// public void changed(ObservableValue<? extends String> observable,
		// String oldValue, String newValue) {
		//
		// if (newValue.length() > 10){
		// addHwkMob.setText(oldValue);
		//
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// }
		// try {
		// Integer.parseInt(newValue);
		// } catch (NumberFormatException e) {
		// addHwkMob.setText(oldValue);
		//
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// Main._logger.debug("Error :",e); e.printStackTrace();
		// }
		// }
		// });
		hawkerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Hawker>() {

			@Override
			public void changed(ObservableValue<? extends Hawker> observable, Hawker oldValue, Hawker newValue) {
				if (newValue != null) {
					if (oldValue != null) {

						// oldValue.updateHawkerRecord();
					}
					enableAllChild();
					refreshHawkerBilling(newValue.getHawkerId());
					customerCheckBox.setSelected(newValue.getCustomerAccess().equals("Y") ? true : false);
					billingCheckBox.setSelected(newValue.getBillingAccess().equals("Y") ? true : false);
					lineInfoCheckBox.setSelected(newValue.getLineInfoAccess().equals("Y") ? true : false);
					lineDistCheckBox.setSelected(newValue.getLineDistAccess().equals("Y") ? true : false);
					pausedCustCheckBox.setSelected(newValue.getPausedCustAccess().equals("Y") ? true : false);
					prodCatalogCheckBox.setSelected(newValue.getProductAccess().equals("Y") ? true : false);
					reportsCheckBox.setSelected(newValue.getReportsAccess().equals("Y") ? true : false);
					stopHistoryCheckBox.setSelected(newValue.getStopHistoryAccess().equals("Y") ? true : false);
				} else
					disableAllChild();
			}
		});

		hawkerBillingTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HawkerBilling>() {

			@Override
			public void changed(ObservableValue<? extends HawkerBilling> observable, HawkerBilling oldValue,
					HawkerBilling newValue) {

			}
		});
		customerCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setCustomerAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		billingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setBillingAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		lineInfoCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setLineInfoAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		lineDistCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setLineDistAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		pausedCustCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setPausedCustAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		prodCatalogCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setProductAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		reportsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setReportsAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});
		stopHistoryCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				hawkerTable.getSelectionModel().getSelectedItem().setStopHistoryAccess(newValue ? "Y" : "N");
				hawkerTable.getSelectionModel().getSelectedItem().updateHawkerRecord();
			}
		});

		hawkerTable.setRowFactory(new Callback<TableView<Hawker>, TableRow<Hawker>>() {

			@Override
			public TableRow<Hawker> call(TableView<Hawker> param) {

				final TableRow<Hawker> row = new TableRow<>();
				MenuItem mnuDel = new MenuItem("Delete hawker");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						if (hawkerRow != null) {
							deleteHawker(hawkerRow);
							hawkerTable.refresh();
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit hawker");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						if (hawkerRow != null) {
							showEditHawkerDialog(hawkerRow);
							refreshHawkerTable();
							// hawkerTable.refresh();
						}
					}

				});
				MenuItem mnuView = new MenuItem("View hawker");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						if (hawkerRow != null) {
							showViewHawkerDialog(hawkerRow);
							// hawkerTable.refresh();
						}
					}

				});

				MenuItem mnuNullDue = new MenuItem("Null Customer Due");
				mnuNullDue.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						if (hawkerRow != null) {
							nullAllCustomerDue(hawkerRow);
							// hawkerTable.refresh();
						}
					}

				});
				MenuItem mnuPwd = new MenuItem("Change Password");
				mnuPwd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						showPwdChangeDialog(hawkerRow);

					}

				});

				MenuItem mnuBill = new MenuItem("Regenerate Invoices");
				mnuBill.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Hawker hawkerRow = hawkerMasterData.get(hawkerTable.getSelectionModel().getSelectedIndex());
						if (hawkerRow != null) {
							Dialog<ButtonType> billWarning = new Dialog<ButtonType>();
							billWarning.setTitle("Generate invoice");
							billWarning.setHeaderText("Please select the Invoice Date");
							billWarning.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Invoice Date"), 0, 0);
							DatePicker pauseDP = new DatePicker(LocalDate.now());
							pauseDP.setConverter(Main.dateConvertor);
							pauseDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										pauseDP.setValue(
												pauseDP.getConverter().fromString(pauseDP.getEditor().getText()));
									}
								}
							});
							// pauseDP.setDisable(true);
							grid.add(pauseDP, 1, 0);
							billWarning.getDialogPane().setContent(grid);
							Optional<ButtonType> result = billWarning.showAndWait();
							if (result.isPresent() && result.get() == ButtonType.OK) {
								// BillingUtilityClass.createBillingInvoiceForHwk(hawkerRow.getHawkerCode(),
								// pauseDP.getValue().withDayOfMonth(1),
								// pauseDP.getValue().withDayOfMonth(1).plusMonths(1).minusDays(1),
								// true);
								generateBillingFunction(hawkerRow.getHawkerCode(),
										pauseDP.getValue().withDayOfMonth(1).plusMonths(1).minusDays(1).toString());
								refreshHawkerTable();
							}
						}
					}

				});

				MenuItem updatePaid = new MenuItem("Payment Recieved");
				updatePaid.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Hawker hawkerRow = hawkerTable.getSelectionModel().getSelectedItem();
						TextInputDialog payRcvdDialog = new TextInputDialog(
								"" + hawkerTable.getSelectionModel().getSelectedItem().getTotalDue());
						payRcvdDialog.setTitle("Payment recieved");
						payRcvdDialog.setHeaderText("Enter the payment recieved amount");
						Optional<String> returnValue = payRcvdDialog.showAndWait();
						if (returnValue.isPresent()) {
							try {
								Double rcvdValue = Double.parseDouble(returnValue.get().trim());
								Connection con = Main.dbConnection;
								if (con.isClosed()) {
									con = Main.reconnect();
								}
								PreparedStatement stmt = con.prepareStatement(
										"INSERT INTO HAWKER_BILLING(hawker_id, entry_date, amount, type) VALUES(?,?,?,?)");
								stmt.setLong(1, hawkerRow.getHawkerId());
								stmt.setDate(2, Date.valueOf(LocalDate.now()));
								stmt.setDouble(3, rcvdValue);
								stmt.setString(4, "Payment");
								int c = stmt.executeUpdate();
								stmt.close();
								refreshHawkerTable();
							} catch (NumberFormatException e) {
								Main._logger.debug("Error :", e);
								e.printStackTrace();
								Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid value entered")
										.text("Please enter numeric value only").showError();
							} catch (Exception e) {
								e.printStackTrace();
								Main._logger.debug("Error :", e);
							}
						}
					}
				});
				ContextMenu menu = new ContextMenu();
				if (HawkerLoginController.loggedInHawker != null) {
					menu.getItems().addAll(mnuEdit, mnuView, mnuPwd);
				} else {
					menu.getItems().addAll(mnuEdit, mnuView, mnuDel, mnuPwd, mnuNullDue, mnuBill,updatePaid);
				}
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		hawkerBillingTable.setRowFactory(new Callback<TableView<HawkerBilling>, TableRow<HawkerBilling>>() {

			@Override
			public TableRow<HawkerBilling> call(TableView<HawkerBilling> arg0) {

				final TableRow<HawkerBilling> row = new TableRow<HawkerBilling>();

				MenuItem mnuDel = new MenuItem("Delete Entry");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						HawkerBilling hwkBillRow = hawkerBillingTable.getSelectionModel().getSelectedItem();
						if (hwkBillRow != null) {
							deleteHawkerBill(hwkBillRow);
							refreshHawkerTable();
						}
					}

				});

				ContextMenu menu = new ContextMenu();
				if (HawkerLoginController.loggedInHawker != null) {
					// menu.getItems().addAll(updatePaid);
				} else {
					menu.getItems().addAll(mnuDel);
				}
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		addHwkExtraButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addHawkerExtraClicked(new ActionEvent());
				}

			}
		});

		saveButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addHawkerClicked(new ActionEvent());
					addHwkName.requestFocus();
				}

			}
		});

		resetButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					resetClicked(new ActionEvent());
				}

			}
		});

		clearButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					clearClicked(new ActionEvent());
				}

			}
		});

		searchButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					filterHawkersClicked(new ActionEvent());
				}

			}
		});

		addHwkInitials.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					addHwkInitials.setText(oldValue);
			}
		});
		cityTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					populatePointNames();
				} else {

				}
			}
		});
		addHwkFee.setText("1.0");
		// refreshHawkerTable();
		addHwkName.requestFocus();
	}

	private void populateBillCategory() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select bill_category from point_name where name =? order by bill_category";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, addPointName.getSelectionModel().getSelectedItem());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				billCategoryLabel.setText(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private void deleteHawkerBill(HawkerBilling hwkBillRow) {

		Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
		deleteWarning.setTitle("Warning");
		deleteWarning.setHeaderText("Are you sure you want to delete this record?");
		deleteWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = deleteWarning.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {

			try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String deleteString = "delete from hawker_billing where hwk_bill_id=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, hwkBillRow.getHwkBillId());

				deleteStmt.executeUpdate();
				con.commit();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of hawker bill was successful").showInformation();
				hawkerBillingMasterData.remove(hwkBillRow);
				hawkerBillingTable.refresh();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of hawker bill has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
	}

	private void deleteHawker(Hawker hawkerRow) {
		Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
		deleteWarning.setTitle("Warning");
		deleteWarning.setHeaderText("Are you sure you want to delete this record?");
		deleteWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = deleteWarning.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {

			try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String deleteString = "delete from hawker_info where hawker_id=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, hawkerRow.getHawkerId());

				deleteStmt.executeUpdate();
				con.commit();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of hawker was successful").showInformation();
				hawkerMasterData.remove(hawkerRow);
				hawkerTable.refresh();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of hawker has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}

	}

	private void showPwdChangeDialog(Hawker hawkerRow) {
		TextInputDialog changePwdDialog = new TextInputDialog();
		changePwdDialog.setTitle("Change password");
		changePwdDialog.setHeaderText("Please enter the new password. \nPassword must be atleast 5 characters long.");
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
			hawkerRow.setPassword(result.get());
			hawkerRow.updateHawkerRecord();
			Notifications.create().title("Password updated").text("Password was successfully updated")
					.hideAfter(Duration.seconds(5)).showInformation();
		}
	}

	public static void showEditHawkerDialog(Hawker hawkerRow) {
		// int selectedIndex =
		// hawkerTable.getSelectionModel().selectedIndexProperty().get();
		try {

			Dialog<Hawker> editHawkerDialog = new Dialog<Hawker>();
			editHawkerDialog.setTitle("Edit hawker data");
			editHawkerDialog.setHeaderText("Update the hawker data below");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			editHawkerDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
			Button saveBtn = (Button) editHawkerDialog.getDialogPane().lookupButton(saveButtonType);

			FXMLLoader editHawkerLoader = new FXMLLoader(AHawkerInfoTabController.class.getResource("EditHawker.fxml"));
			Parent editHawkerGrid = (Parent) editHawkerLoader.load();
			EditHawkerController editHawkerController = editHawkerLoader.<EditHawkerController> getController();

			editHawkerDialog.getDialogPane().setContent(editHawkerGrid);
			editHawkerController.setHawkerToEdit(hawkerRow);
			editHawkerController.setupBindings();
			saveBtn.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (!editHawkerController.isValid()) {

					btnEvent.consume();
				} else {
					editHawkerController.returnUpdatedHawker();
				}
			});
			editHawkerDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Hawker edittedHawker = editHawkerController.returnUpdatedHawker();
					return edittedHawker;
				}
				return null;
			});

			Optional<Hawker> updatedHawker = editHawkerDialog.showAndWait();
			// refreshCustomerTable();

			updatedHawker.ifPresent(new Consumer<Hawker>() {

				@Override
				public void accept(Hawker t) {

					t.updateHawkerRecord();
					editHawkerController.releaseVariables();

				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void showViewHawkerDialog(Hawker hawkerRow) {
		try {

			Dialog<Hawker> editHawkerDialog = new Dialog<Hawker>();
			editHawkerDialog.setTitle("View hawker data");
			editHawkerDialog.setHeaderText("View the hawker data below");

			editHawkerDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editHawkerLoader = new FXMLLoader(getClass().getResource("EditHawker.fxml"));
			Parent editHawkerGrid = (Parent) editHawkerLoader.load();
			EditHawkerController editHawkerController = editHawkerLoader.<EditHawkerController> getController();

			editHawkerDialog.getDialogPane().setContent(editHawkerGrid);
			editHawkerController.setHawkerToEdit(hawkerRow);
			editHawkerController.setupBindings();
			editHawkerController.gridPane.setDisable(true);
			Optional<Hawker> updatedHawker = editHawkerDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void refreshHawkerTable() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {
					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt;
					if (HawkerLoginController.loggedInHawker == null) {
						if (showAllRadioButton.isSelected()) {
							stmt = con.prepareStatement(
									"SELECT hwk.hawker_id, hwk.name, hwk.hawker_code, hwk.mobile_num, hwk.agency_name, hwk.active_flag, hwk.fee, hwk.old_house_num, hwk.new_house_num, hwk.addr_line1, hwk.addr_line2, hwk.locality, hwk.city, hwk.state, hwk.customer_access, hwk.billing_access, hwk.line_info_access, hwk.line_dist_access, hwk.paused_cust_access, hwk.product_access, hwk.reports_access, hwk.profile1, hwk.profile2, hwk.profile3, hwk.initials, hwk.password, hwk.employment, hwk.comments, hwk.point_name, hwk.building_street, hwk.bank_ac_no, hwk.bank_name, hwk.ifsc_code, hwk.stop_history_access, hwk.logo, hwk.ben_name, (select NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Bill'),0.0) - NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Payment'),0.0) from dual) total_due FROM hawker_info hwk ORDER BY name ");
						} else {
							stmt = con.prepareStatement(
									"SELECT hwk.hawker_id, hwk.name, hwk.hawker_code, hwk.mobile_num, hwk.agency_name, hwk.active_flag, hwk.fee, hwk.old_house_num, hwk.new_house_num, hwk.addr_line1, hwk.addr_line2, hwk.locality, hwk.city, hwk.state, hwk.customer_access, hwk.billing_access, hwk.line_info_access, hwk.line_dist_access, hwk.paused_cust_access, hwk.product_access, hwk.reports_access, hwk.profile1, hwk.profile2, hwk.profile3, hwk.initials, hwk.password, hwk.employment, hwk.comments, hwk.point_name, hwk.building_street, hwk.bank_ac_no, hwk.bank_name, hwk.ifsc_code, hwk.stop_history_access, hwk.logo, hwk.ben_name, (select NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Bill'),0.0) - NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Payment'),0.0) from dual) total_due FROM hawker_info hwk  where point_name=? ORDER BY name");
							stmt.setString(1, addPointName.getSelectionModel().getSelectedItem());
						}
					} else {
						stmt = con.prepareStatement(
								"SELECT hwk.hawker_id, hwk.name, hwk.hawker_code, hwk.mobile_num, hwk.agency_name, hwk.active_flag, hwk.fee, hwk.old_house_num, hwk.new_house_num, hwk.addr_line1, hwk.addr_line2, hwk.locality, hwk.city, hwk.state, hwk.customer_access, hwk.billing_access, hwk.line_info_access, hwk.line_dist_access, hwk.paused_cust_access, hwk.product_access, hwk.reports_access, hwk.profile1, hwk.profile2, hwk.profile3, hwk.initials, hwk.password, hwk.employment, hwk.comments, hwk.point_name, hwk.building_street, hwk.bank_ac_no, hwk.bank_name, hwk.ifsc_code, hwk.stop_history_access, hwk.logo, hwk.ben_name, (select NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Bill'),0.0) - NVL((select sum(amount) from hawker_billing b1 where b1.hawker_id=hwk.hawker_id and b1.type='Payment'),0.0) from dual) total_due FROM hawker_info hwk  where point_name=? ORDER BY name");
						stmt.setString(1, addPointName.getSelectionModel().getSelectedItem());
					}
					// hawkerTable.setDisable(true);
					ResultSet rs = stmt.executeQuery();
					hawkerMasterData = FXCollections.observableArrayList();
					while (rs.next()) {
						Hawker hwkRow = new Hawker(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6).equalsIgnoreCase("Y"), rs.getDouble(7),
								rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
								rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
								rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24),
								rs.getString(25), rs.getString(26), rs.getString(27), rs.getString(28),
								rs.getString(29), rs.getString(30), rs.getString(31), rs.getString(32),
								rs.getString(33), rs.getString(34), rs.getBlob(35), rs.getString(36), rs.getDouble(37));
						// hwkRow.calculateTotalDue();
						hawkerMasterData.add(hwkRow);

					}
					// hawkerTable.getItems().clear();
					if (!hawkerMasterData.isEmpty()) {

						filteredData = new FilteredList<>(hawkerMasterData, p -> true);
						SortedList<Hawker> sortedData = new SortedList<>(filteredData);
						sortedData.comparatorProperty().bind(hawkerTable.comparatorProperty());

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								hawkerTable.setItems(sortedData);
								// hawkerTable.setDisable(false);
								hawkerTable.refresh();
								hawkerTable.getSelectionModel().select(0);
							}
						});
					} else {

						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								hawkerTable.getItems().clear();
								hawkerTable.refresh();
							}
						});
					}
					// con.close();
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}

				hawkerTable.refresh();
				return null;
			}

		};

		new Thread(task).start();

	}

	private void disableAllChild() {

		customerCheckBox.setDisable(true);
		billingCheckBox.setDisable(true);
		lineInfoCheckBox.setDisable(true);
		lineDistCheckBox.setDisable(true);
		pausedCustCheckBox.setDisable(true);
		prodCatalogCheckBox.setDisable(true);
		reportsCheckBox.setDisable(true);
		stopHistoryCheckBox.setDisable(true);
		enableAllButton.setDisable(true);
		disableAllButton.setDisable(true);
		hawkerBillingTable.setDisable(true);

	}

	private void enableAllChild() {

		customerCheckBox.setDisable(false);
		billingCheckBox.setDisable(false);
		lineInfoCheckBox.setDisable(false);
		lineDistCheckBox.setDisable(false);
		pausedCustCheckBox.setDisable(false);
		prodCatalogCheckBox.setDisable(false);
		reportsCheckBox.setDisable(false);
		stopHistoryCheckBox.setDisable(false);
		enableAllButton.setDisable(false);
		disableAllButton.setDisable(false);
		hawkerBillingTable.setDisable(false);
	}

	protected void refreshHawkerBilling(Long hawkerId) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					String getHwkBillingInfo = "Select hwk_bill_id, hawker_id, entry_date, amount, type from hawker_billing where hawker_id = ? order by entry_date desc";
					PreparedStatement hwkBillInfo = null;
					hwkBillInfo = con.prepareStatement(getHwkBillingInfo);
					hwkBillInfo.setLong(1, hawkerId);
					ResultSet rs = hwkBillInfo.executeQuery();
					hawkerBillingMasterData.clear();
					while (rs.next()) {
						hawkerBillingMasterData.add(new HawkerBilling(rs.getLong(1), hawkerId, rs.getDate(3).toLocalDate(),
								rs.getDouble(4), rs.getString(5)));
					}

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							hawkerBillingTable.setItems(hawkerBillingMasterData);
							hawkerBillingTable.refresh();
						}
					});
					// con.close();

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
		boolean validate = true;
		if (validateHawkerCodeExists(addHwkCode.getText())) {
			Notifications.create().title("Hawker already exists")
					.text("Hawker with same Hawker Code alraedy exists. Please choose different hawker code.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (mobileNumExists(addHwkMob.getText())) {
			Notifications.create().title("Mobile already exists")
					.text("Hawker with same Mobile Number alraedy exists. Please enter a different value.")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addHwkName.getText() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
					.text("Please select a hawker before adding the the customer").showError();
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

		if (addHwkProf3.getText() != null && checkExistingProfileValue(addHwkProf3.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		if (addHwkMob.getText().length() != 10) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		if (addPointName.getSelectionModel().getSelectedItem() == null) {
			Notifications.create().title("Invalid point name").text("Point Name must be selected to create hawker")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
		}
		try {
			Long.parseLong(addHwkMob.getText());
		} catch (NumberFormatException e) {
			Notifications.create().title("Invalid mobile number").text("Mobile number should only contain 10 DIGITS")
					.hideAfter(Duration.seconds(5)).showError();
			validate = false;
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return validate;
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

	@FXML
	private void addHawkerClicked(ActionEvent event) {

		if (isValid()) {
			addHwkName.requestFocus();
			PreparedStatement insertHawker = null;
			String insertStatement = "INSERT INTO HAWKER_INFO(NAME,HAWKER_CODE, MOBILE_NUM, AGENCY_NAME,FEE,ACTIVE_FLAG, OLD_HOUSE_NUM, NEW_HOUSE_NUM,ADDR_LINE1,ADDR_LINE2,LOCALITY,CITY,STATE,customer_access, billing_access, line_info_access, line_dist_access, paused_cust_access, product_access, reports_access,profile1,profile2,profile3,initials, employment, comments, point_name, building_street,password,BANK_AC_NO, BANK_NAME, IFSC_CODE ) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = Main.dbConnection;
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertHawker = con.prepareStatement(insertStatement);
				insertHawker.setString(1, addHwkName.getText());
				insertHawker.setString(2, addHwkCode.getText().toLowerCase());
				insertHawker.setString(3, addHwkMob.getText());
				insertHawker.setString(4, addHwkAgencyName.getText());
				insertHawker.setDouble(5, Double.parseDouble(addHwkFee.getText() != null ? addHwkFee.getText() : "0"));
				insertHawker.setString(6, addHwkActive.isSelected() == true ? "Y" : "N");
				insertHawker.setString(7, addHwkOldHNum.getText());
				insertHawker.setString(8, addHwkNewHNum.getText());
				insertHawker.setString(9, addHwkAddrLine1.getText());
				insertHawker.setString(10, addHwkAddrLine2.getText());
				insertHawker.setString(11, addHwkLocality.getText());
				insertHawker.setString(12, addHwkCity.getText());
				insertHawker.setString(13, addHwkState.getSelectionModel().getSelectedItem());
				insertHawker.setString(14, "Y");
				insertHawker.setString(15, "Y");
				insertHawker.setString(16, "Y");
				insertHawker.setString(17, "Y");
				insertHawker.setString(18, "Y");
				insertHawker.setString(19, "Y");
				insertHawker.setString(20, "Y");
				insertHawker.setString(21, addHwkProf1.getSelectionModel().getSelectedItem());
				insertHawker.setString(22, addHwkProf2.getSelectionModel().getSelectedItem());
				insertHawker.setString(23, addHwkProf3.getText());
				insertHawker.setString(24, addHwkInitials.getText());
				insertHawker.setString(25, addHwkEmployment.getSelectionModel().getSelectedItem());
				insertHawker.setString(26, addHwkComments.getText());
				insertHawker.setString(27, addPointName.getSelectionModel().getSelectedItem());
				insertHawker.setString(28, addHwkBuildingStreet.getText());
				insertHawker.setString(29, "password");
				insertHawker.setString(30, addBankAcNo.getText());
				insertHawker.setString(31, addBankName.getText());
				insertHawker.setString(32, addIfscCode.getText());

				insertHawker.execute();

				refreshHawkerTable();
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
			resetClicked(event);
		}

	}

	private void insertLineZero(String hwkCode) {
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

	private boolean validateHawkerCodeExists(String text) {

		for (Hawker hwk : hawkerMasterData) {
			if (hwk.getHawkerCode().equalsIgnoreCase(text.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	@FXML
	private void resetClicked(ActionEvent event) {
		System.out.println("resetClicked");
		addHwkName.clear();
		addHwkCode.clear();
		addHwkMob.clear();
		addHwkAgencyName.clear();
		addHwkFee.setText("1.0");
		addHwkOldHNum.clear();
		addHwkNewHNum.clear();
		addHwkAddrLine1.clear();
		addHwkAddrLine2.clear();
		addHwkLocality.clear();
		addHwkCity.clear();
		addHwkActive.setSelected(true);
		// addHwkState.setValue("State");
		addHwkProf1.getSelectionModel().clearSelection();
		addHwkProf2.getSelectionModel().clearSelection();
		addHwkProf3.clear();
		addHwkInitials.clear();
		addHwkEmployment.getSelectionModel().clearSelection();
		addHwkComments.clear();
		// addPointName.getSelectionModel().clearSelection();
		addHwkBuildingStreet.clear();
	}

	@FXML
	private void enableAllClicked(ActionEvent event) {
		customerCheckBox.setSelected(true);
		billingCheckBox.setSelected(true);
		lineInfoCheckBox.setSelected(true);
		lineDistCheckBox.setSelected(true);
		pausedCustCheckBox.setSelected(true);
		prodCatalogCheckBox.setSelected(true);
		reportsCheckBox.setSelected(true);
		stopHistoryCheckBox.setSelected(true);
	}

	@FXML
	private void disableAllClicked(ActionEvent event) {
		customerCheckBox.setSelected(false);
		billingCheckBox.setSelected(false);
		lineInfoCheckBox.setSelected(false);
		lineDistCheckBox.setSelected(false);
		pausedCustCheckBox.setSelected(false);
		prodCatalogCheckBox.setSelected(false);
		reportsCheckBox.setSelected(false);
		stopHistoryCheckBox.setSelected(false);
	}

	@FXML
	private void filterHawkersClicked(ActionEvent event) {

		TextInputDialog hawkerFilterDialog = new TextInputDialog(searchText);
		hawkerFilterDialog.setTitle("Filter Hawkers");
		hawkerFilterDialog.setHeaderText("Enter the filter text");
		Optional<String> returnValue = hawkerFilterDialog.showAndWait();
		if (returnValue.isPresent()) {
			try {
				searchText = returnValue.get();
				filteredData.setPredicate(new Predicate<Hawker>() {

					@Override
					public boolean test(Hawker hawker) {

						if (searchText == null || searchText.isEmpty())
							return true;
						else if (hawker.getAddrLine1().contains(searchText))
							return true;
						else if (hawker.getAddrLine2().contains(searchText))
							return true;
						else if (hawker.getAgencyName().contains(searchText))
							return true;
						else if (hawker.getCity().contains(searchText))
							return true;
						else if (hawker.getHawkerCode().contains(searchText))
							return true;
						else if (hawker.getLocality().contains(searchText))
							return true;
						else if (hawker.getMobileNum().contains(searchText))
							return true;
						else if (hawker.getName().contains(searchText))
							return true;
						else if (hawker.getNewHouseNum().contains(searchText))
							return true;
						else if (hawker.getOldHouseNum().contains(searchText))
							return true;
						else if (hawker.getState().contains(searchText))
							return true;
						else if (hawker.getProfile1() != null && hawker.getProfile1().contains(searchText))
							return true;
						else if (hawker.getProfile2() != null && hawker.getProfile2().contains(searchText))
							return true;
						else if (hawker.getProfile3() != null && hawker.getProfile3().contains(searchText))
							return true;
						else if (hawker.getInitials() != null && hawker.getInitials().contains(searchText))
							return true;
						else if (hawker.getEmployment() != null && hawker.getEmployment().contains(searchText))
							return true;
						else if (hawker.getComments() != null && hawker.getComments().contains(searchText))
							return true;
						else if (hawker.getPointName() != null && hawker.getPointName().contains(searchText))
							return true;
						else if (hawker.getBuildingStreet() != null && hawker.getBuildingStreet().contains(searchText))
							return true;
						return false;
					}
				});

				SortedList<Hawker> sortedData = new SortedList<>(filteredData);
				sortedData.comparatorProperty().bind(hawkerTable.comparatorProperty());
				hawkerTable.setItems(sortedData);
				disableAllChild();

			} catch (NumberFormatException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid value entered")
						.text("Please enter numeric value only").showError();
			}
		}

	}

	@FXML
	private void clearClicked(ActionEvent event) {
		filteredData = new FilteredList<>(hawkerMasterData, p -> true);
		SortedList<Hawker> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(hawkerTable.comparatorProperty());
		hawkerTable.setItems(sortedData);
		hawkerTable.getSelectionModel().clearSelection();
		disableAllChild();
	}

	// @Override

	private boolean mobileNumExists(String mobileNum) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select hawker_code,name from hawker_info where mobile_num=? and lower(hawker_code) <> ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, mobileNum);
			stmt.setString(2, addHwkCode.getText().toLowerCase());
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
	private void addHawkerExtraClicked(ActionEvent event) {
		try {

			Dialog<String> addHawkerDialog = new Dialog<String>();
			addHawkerDialog.setTitle("Add new hawker");
			addHawkerDialog.setHeaderText("Add new Hawker data below.");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save and add new", ButtonData.OK_DONE);
			addHawkerDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CLOSE);
			Button saveButton = (Button) addHawkerDialog.getDialogPane().lookupButton(saveButtonType);
			FXMLLoader addHawkerLoader = new FXMLLoader(getClass().getResource("AddHawkersExtraScreen.fxml"));
			Parent addHawkerGrid = (Parent) addHawkerLoader.load();
			AddHawkerExtraScreenController addHwkController = addHawkerLoader
					.<AddHawkerExtraScreenController> getController();
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addHwkController.isValid()) {
					addHwkController.addHawker();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker created")
							.text("Hawker created successfully.").showInformation();
					refreshHawkerTable();
				} else {
					btnEvent.consume();
				}

			});

			addHawkerDialog.getDialogPane().setContent(addHawkerGrid);
			addHwkController.setupBindings();

			addHawkerDialog.setResultConverter(dialogButton -> {
				if (dialogButton != saveButtonType) {
					return null;
				}
				return null;
			});

			Optional<String> updatedHawker = addHawkerDialog.showAndWait();
			// refreshHawkerTable();

			updatedHawker.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					addHwkController.releaseVariables();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void populateProfileValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			profileValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='PROFILE_VALUES' order by seq");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				profileValues.add(rs.getString(1));
			}
			addHwkProf1.getItems().clear();
			addHwkProf2.getItems().clear();
			addHwkProf1.setItems(profileValues);
			addHwkProf2.setItems(profileValues);
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public void populateEmploymentValues() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			employmentValues = FXCollections.observableArrayList();
			PreparedStatement stmt = con.prepareStatement(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='EMPLOYMENT_STATUS' order by seq");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (employmentValues != null && !employmentValues.contains(rs.getString(1)))
					employmentValues.add(rs.getString(1));
			}
			addHwkEmployment.setItems(employmentValues);
			new AutoCompleteComboBoxListener<>(addHwkEmployment);
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public void populatePointNames() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					pointNameValues = FXCollections.observableArrayList();
					PreparedStatement stmt = con
							.prepareStatement("select distinct name from point_name where city =? order by name");
					stmt.setString(1, cityTF.getSelectionModel().getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						if (pointNameValues != null && !pointNameValues.contains(rs.getString(1)))
							pointNameValues.add(rs.getString(1));
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							addPointName.setItems(pointNameValues);
							new AutoCompleteComboBoxListener<>(addPointName);
						}
					});
					if (HawkerLoginController.loggedInHawker != null) {

						addPointName.setDisable(true);
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

	private void nullAllCustomerDue(Hawker hawkerRow) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con.prepareStatement("UPDATE CUSTOMER SET TOTAL_DUE=0.0 WHERE HAWKER_CODE=?");
			stmt.setString(1, hawkerRow.getHawkerCode());
			int r = stmt.executeUpdate();
			if (r > 0) {
				Notifications.create().title("Successful").text("Total Due set NULL for all customers of the hawker")
						.hideAfter(Duration.seconds(5)).showInformation();
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public void populateCityValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					cityValues = FXCollections.observableArrayList();
					PreparedStatement stmt = null;
					if (HawkerLoginController.loggedInHawker != null) {
						stmt = con.prepareStatement("select distinct city from point_name where name=?");
						stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
						ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							cityValues.add(rs.getString(1));
						}
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								cityTF.getItems().clear();
								cityTF.setItems(cityValues);
								cityTF.getSelectionModel().selectFirst();
								cityTF.setDisable(true);
							}
						});
					} else {
						stmt = con.prepareStatement("select distinct city from point_name order by city");
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							if (cityValues != null && !cityValues.contains(rs.getString(1)))
								cityValues.add(rs.getString(1));
						}

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								cityTF.setItems(cityValues);
								new AutoCompleteComboBoxListener<>(cityTF);
							}
						});
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

	@SuppressWarnings("deprecation")
	private void generateBillingFunction(String hawkerCode, String invoiceDate) {

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			String ACCESS_KEY = prop.getProperty("ACCESS_KEY");
			String SECRET = prop.getProperty("SECRET");
			Gson gson = new Gson();
			AWSCredentials credentials = null;
			credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
			AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("HawkerCode", hawkerCode);
			// map.put("LinueNum", lineNum);
			map.put("InvoiceDate", invoiceDate);
			lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("GenerateBilling");
			invokeRequest.setPayload(ByteBuffer.wrap(gson.toJson(map).getBytes(StringUtils.UTF8)));
			InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);

			if (invokeResult.getLogResult() != null) {
				System.out.println(" log: " + new String(Base64.decode(invokeResult.getLogResult())));
			}

			if (invokeResult.getFunctionError() != null) {
				throw new LambdaFunctionException(invokeResult.getFunctionError(), false,
						new String(invokeResult.getPayload().array()));
			}

			if (invokeResult.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
				return;
			}
			String output = gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					String.class);
		} catch (LambdaFunctionException e) {

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}


    @FXML
    void generateHwkBillingReport(ActionEvent event) {
    	try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String reportSrcFile = "HwkPendingPayment.jrxml";
				InputStream input = AHawkerInfoTabController.class.getResourceAsStream(reportSrcFile);
				JasperReport jasperReport = JasperCompileManager.compileReport(input);
				
				JasperPrint print = JasperFillManager.fillReport(jasperReport, null, Main.dbConnection);
				// Make sure the output directory exists.
				File outDir = new File("C:/pds");
				outDir.mkdirs();
				// PDF Exportor.
				JRPdfExporter exporter = new JRPdfExporter();
				ExporterInput exporterInput = new SimpleExporterInput(print);
				// ExporterInput
				exporter.setExporterInput(exporterInput);
				// ExporterOutput
				String filename = "C:/pds/" + "HwkPendingBillPayment-"
						+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
				OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
				// Output
				exporter.setExporterOutput(exporterOutput);
				//
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
				exporter.exportReport();
//				File outFile = new File(filename);
				Notifications.create().title("Report PDF Created").text("Report PDF created at : " + filename)
						.hideAfter(Duration.seconds(15)).showInformation();
							

		} catch (JRException e) {
			Main._logger.debug("Error during report PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
    }
	
	public void reloadData() {
		if (HawkerLoginController.loggedInHawker == null) {
			if (showAllRadioButton.isSelected()) {
				refreshHawkerTable();
				cityTF.getSelectionModel().clearSelection();
				cityTF.setDisable(true);
				addPointName.getSelectionModel().clearSelection();
				addPointName.setDisable(true);
			} else if (filterRadioButton.isSelected()) {
				// hawkerTable.getItems().clear();
				// hawkerTable.refresh();
				populateCityValues();
				cityTF.setDisable(false);
				addPointName.setDisable(false);
				refreshHawkerTable();
			}
		} else {
			populateCityValues();
		}
		populateEmploymentValues();
		populateProfileValues();
	}

	// @Override
	public void releaseVariables() {
		filteredData = null;
		searchText = null;
		hawkerMasterData = null;
		hawkerBillingMasterData = null;
		profileValues = null;
		employmentValues = null;
		pointNameValues = null;
		cityValues = null;
		hawkerMasterData = FXCollections.observableArrayList();
		hawkerBillingMasterData = FXCollections.observableArrayList();
		profileValues = FXCollections.observableArrayList();
		employmentValues = FXCollections.observableArrayList();
		pointNameValues = FXCollections.observableArrayList();
		cityValues = FXCollections.observableArrayList();
	}

}
