package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import com.amazonaws.util.NumberUtils;
 
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class ACustomerInfoTabController implements Initializable {

	@FXML
	private TableView<Customer> ACustInfoTable;

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private TextField addCustName;
	@FXML
	private TextField addCustInitials;
	@FXML
	private TextField addCustMobile;
	@FXML
	private ComboBox<String> addCustLineNum;
	@FXML
	private ComboBox<String> addCustHwkCode;
	@FXML
	private TextField addCustHouseSeq;
	@FXML
	private TextField addCustOldHouseNum;
	@FXML
	private TextField addCustNewHouseNum;
	@FXML
	private TextField addCustAddrLine1;
	@FXML
	private TextField addCustAddrLine2;
	@FXML
	private TextField addCustLocality;
	@FXML
	private TextField addCustCity;
	@FXML
	private ComboBox<String> addCustState;
	@FXML
	private ComboBox<String> addCustProf1;
	@FXML
	private ComboBox<String> addCustProf2;
	@FXML
	private TextField addCustProf3;
	@FXML
	private ComboBox<String> addCustEmployment;
	@FXML
	private TextField addCustComments;
	@FXML
	private TextField addCustBuildingStreet;
	@FXML
	private ComboBox<String> addPointName;

	// Columns
	@FXML
	private TableColumn<Customer, Long> customerCodeColumn;
	@FXML
	private TableColumn<Customer, String> customerNameColumn;
	@FXML
	private TableColumn<Customer, String> customerInitialsColumn;
	@FXML
	private TableColumn<Customer, String> mobileNumColumn;
	@FXML
	private TableColumn<Customer, String> hawkerCodeColumn;
	@FXML
	private TableColumn<Customer, String> lineNumColumn;
	@FXML
	private TableColumn<Customer, Long> houseSeqColumn;
	@FXML
	private TableColumn<Customer, String> oldHouseNumColumn;
	@FXML
	private TableColumn<Customer, String> newHouseNumColumn;
	@FXML
	private TableColumn<Customer, String> addrLine1Column;
	@FXML
	private TableColumn<Customer, String> addrLine2Column;
	@FXML
	private TableColumn<Customer, String> localityColumn;
	@FXML
	private TableColumn<Customer, String> cityColumn;
	@FXML
	private TableColumn<Customer, String> stateColumn;
	@FXML
	private TableColumn<Customer, String> profile1Column;
	@FXML
	private TableColumn<Customer, String> profile2Column;
	@FXML
	private TableColumn<Customer, String> profile3Column;
	@FXML
	private TableColumn<Customer, String> employmentColumn;
	@FXML
	private TableColumn<Customer, String> commentsColumn;
	@FXML
	private TableColumn<Customer, String> buildingStreetColumn;
	@FXML
	private TableColumn<Customer, Double> totalDueColumn;

	@FXML
	private TableView<Subscription> subscriptionsTable;

	@FXML
	private TableColumn<Subscription, Long> subsIdColumn;
	@FXML
	private TableColumn<Subscription, String> subsProdNameColumn;
	@FXML
	private TableColumn<Subscription, String> subsProdCodeColumn;

	@FXML
	private TableColumn<Subscription, String> subsProdTypeColumn;

	@FXML
	private TableColumn<Subscription, String> subsTypeColumn;

	@FXML
	private TableColumn<Subscription, String> subsPaymentTypeColumn;

	@FXML
	private TableColumn<Subscription, Double> subPriceColumn;
	@FXML
	private TableColumn<Subscription, Double> subAddToBillColumn;

	@FXML
	private TableColumn<Subscription, Double> subsServiceChargeColumn;

	@FXML
	private TableColumn<Subscription, String> subsFreqColumn;

	@FXML
	private TableColumn<Subscription, String> subsDOWColumn;

	@FXML
	private TableColumn<Subscription, String> subsStatusColumn;

	@FXML
	private TableColumn<Subscription, LocalDate> subsStartDateColumn;
	@FXML
	private TableColumn<Subscription, LocalDate> subsStopDateColumn;

	@FXML
	private TableColumn<Subscription, LocalDate> subsPausedDateColumn;
	@FXML
	private TableColumn<Subscription, LocalDate> subsResumeDateColumn;
	@FXML
	private TableColumn<Subscription, String> subsNumberColumn;

	@FXML
	private VBox billingVBOX;

	@FXML
	private Button addCustExtraButton;
	@FXML
	private Button saveCustomerButton;
	@FXML
	private Button resetButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button clearButton;

	@FXML
	private Label totalBillLabel;

	@FXML
	private Label totalDueLabel;

	@FXML
	private Label netBillLabel;

	@FXML
	private Label monthLabel;

	@FXML
	ComboBox<Billing> invoiceDateLOV;

	@FXML
	private TableView<BillingLine> billingTable;
	@FXML
	private TableColumn<BillingLine, String> prodCol;
	@FXML
	private TableColumn<BillingLine, Double> amountCol;
	@FXML
	private TableColumn<BillingLine, Double> teaExpensesCol;

	@FXML
	private TableView<StopHistory> stopHistoryTable;

	@FXML
	private TableColumn<StopHistory, Long> stpstopHistoryIdColumn;
	@FXML
	private TableColumn<StopHistory, Long> stpsubsIdColumn;

	@FXML
	private TableColumn<StopHistory, String> stpsubsProdCodeColumn;
	@FXML
	private TableColumn<StopHistory, String> stpsubsProdNameColumn;
	@FXML
	private TableColumn<StopHistory, String> stpsubsTypeColumn;
	@FXML
	private TableColumn<StopHistory, String> stpsubsFreqColumn;
	@FXML
	private TableColumn<StopHistory, String> stpsubsDOWColumn;
	@FXML
	private TableColumn<StopHistory, Double> stpstopHistAmountColumn;

	@FXML
	private TableColumn<StopHistory, LocalDate> stpsubsStopDateColumn;

	@FXML
	private TableColumn<StopHistory, LocalDate> stpsubsResumeDateColumn;

	@FXML
	private TableView<StopHistoryBackup> stopHistoryBkpTable;

	@FXML
	private TableColumn<StopHistoryBackup, Long> stphstopHistoryIdColumn;
	@FXML
	private TableColumn<StopHistoryBackup, Long> stphsubsIdColumn;
	@FXML
	private TableColumn<StopHistoryBackup, String> stphsubsProdCodeColumn;
	@FXML
	private TableColumn<StopHistoryBackup, String> stphsubsProdNameColumn;
	@FXML
	private TableColumn<StopHistoryBackup, String> stphsubsTypeColumn;
	@FXML
	private TableColumn<StopHistoryBackup, String> stphsubsFreqColumn;
	@FXML
	private TableColumn<StopHistoryBackup, String> stphsubsDOWColumn;
	@FXML
	private TableColumn<StopHistoryBackup, Double> stphstopHistAmountColumn;

	@FXML
	private TableColumn<StopHistoryBackup, LocalDate> stphsubsStopDateColumn;

	@FXML
	private TableColumn<StopHistoryBackup, LocalDate> stphsubsResumeDateColumn;

	private FilteredList<Customer> filteredData;
	private String searchText;
	private ObservableList<Customer> customerMasterData = FXCollections.observableArrayList();
	private ObservableList<Subscription> subscriptionMasterData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerLineNumData = FXCollections.observableArrayList();
	private ObservableList<String> employmentData = FXCollections.observableArrayList();
	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<Billing> invoiceDatesData = FXCollections.observableArrayList();
	private ObservableList<BillingLine> billingLinesData = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();
	private ObservableList<StopHistory> stopHistoryMasterData = FXCollections.observableArrayList();
	private ObservableList<StopHistoryBackup> stopHistoryBkpMasterData = FXCollections.observableArrayList();

	@FXML
	public RadioButton filterRadioButton;
	@FXML
	public RadioButton showAllRadioButton;

	@FXML
	private Label hawkerNameLabel;
	@FXML
	private Label hawkerMobLabel;

	private int seq = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main._logger.debug("Entered initialize method");
		if (HawkerLoginController.loggedInHawker != null) {
			filterRadioButton.setVisible(false);
			showAllRadioButton.setVisible(false);
		} else {
			ToggleGroup tg = new ToggleGroup();
			filterRadioButton.setToggleGroup(tg);
			showAllRadioButton.setToggleGroup(tg);
			filterRadioButton.setSelected(true);

		}
		System.out.println("Entered ACustomerInfoTabController");
		// Set cell value factories
		customerCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("customerCode"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		customerInitialsColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("initials"));
		hawkerCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("hawkerCode"));
		lineNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("lineNum"));
		houseSeqColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("houseSeq"));
		mobileNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("mobileNum"));
		newHouseNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("newHouseNum"));
		oldHouseNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("oldHouseNum"));
		addrLine1Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine1"));
		addrLine2Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine2"));
		localityColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("locality"));
		cityColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("state"));
		profile1Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("profile1"));
		profile2Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("profile2"));
		profile3Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("profile3"));
		employmentColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("employment"));
		commentsColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("comments"));
		buildingStreetColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("buildingStreet"));
		totalDueColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("totalDue"));

		subsIdColumn.setCellValueFactory(new PropertyValueFactory<Subscription, Long>("subscriptionId"));
		subsProdNameColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("productName"));
		subsProdCodeColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("productCode"));
		subsProdTypeColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("productType"));
		subsPaymentTypeColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("paymentType"));
		subsTypeColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("subscriptionType"));
		subPriceColumn.setCellValueFactory(new PropertyValueFactory<Subscription, Double>("cost"));
		subAddToBillColumn.setCellValueFactory(new PropertyValueFactory<Subscription, Double>("addToBill"));
		subsServiceChargeColumn.setCellValueFactory(new PropertyValueFactory<Subscription, Double>("serviceCharge"));
		subsFreqColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("frequency"));
		subsDOWColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("dow"));
		subsStatusColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("status"));
		subsStatusColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("status"));
		subsStartDateColumn.setCellValueFactory(new PropertyValueFactory<Subscription, LocalDate>("startDate"));
		subsPausedDateColumn.setCellValueFactory(new PropertyValueFactory<Subscription, LocalDate>("pausedDate"));
		subsResumeDateColumn.setCellValueFactory(new PropertyValueFactory<Subscription, LocalDate>("resumeDate"));
		subsNumberColumn.setCellValueFactory(new PropertyValueFactory<Subscription, String>("subNumber"));
		subsStopDateColumn.setCellValueFactory(new PropertyValueFactory<Subscription, LocalDate>("stopDate"));

		prodCol.setCellValueFactory(new PropertyValueFactory<BillingLine, String>("product"));
		amountCol.setCellValueFactory(new PropertyValueFactory<BillingLine, Double>("amount"));
		teaExpensesCol.setCellValueFactory(new PropertyValueFactory<BillingLine, Double>("teaExpenses"));

		stpstopHistoryIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Long>("stopHistoryId"));
		stpsubsIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Long>("subscriptionId"));
		stpsubsProdCodeColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("productCode"));
		stpsubsProdNameColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("productName"));
		stpsubsTypeColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionType"));
		stpsubsFreqColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionFreq"));
		stpsubsDOWColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionDOW"));
		stpsubsResumeDateColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, LocalDate>("resumeDate"));
		stpstopHistAmountColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Double>("amount"));
		stpsubsStopDateColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, LocalDate>("stopDate"));
		stpsubsResumeDateColumn
				.setCellFactory(new Callback<TableColumn<StopHistory, LocalDate>, TableCell<StopHistory, LocalDate>>() {

					@Override
					public TableCell<StopHistory, LocalDate> call(TableColumn<StopHistory, LocalDate> param) {
						TextFieldTableCell<StopHistory, LocalDate> cell = new TextFieldTableCell<StopHistory, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		stpsubsStopDateColumn
				.setCellFactory(new Callback<TableColumn<StopHistory, LocalDate>, TableCell<StopHistory, LocalDate>>() {

					@Override
					public TableCell<StopHistory, LocalDate> call(TableColumn<StopHistory, LocalDate> param) {
						TextFieldTableCell<StopHistory, LocalDate> cell = new TextFieldTableCell<StopHistory, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});

		subsPausedDateColumn.setCellFactory(
				new Callback<TableColumn<Subscription, LocalDate>, TableCell<Subscription, LocalDate>>() {

					@Override
					public TableCell<Subscription, LocalDate> call(TableColumn<Subscription, LocalDate> param) {
						TextFieldTableCell<Subscription, LocalDate> cell = new TextFieldTableCell<Subscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		subsResumeDateColumn.setCellFactory(
				new Callback<TableColumn<Subscription, LocalDate>, TableCell<Subscription, LocalDate>>() {

					@Override
					public TableCell<Subscription, LocalDate> call(TableColumn<Subscription, LocalDate> param) {
						TextFieldTableCell<Subscription, LocalDate> cell = new TextFieldTableCell<Subscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		subsStartDateColumn.setCellFactory(
				new Callback<TableColumn<Subscription, LocalDate>, TableCell<Subscription, LocalDate>>() {

					@Override
					public TableCell<Subscription, LocalDate> call(TableColumn<Subscription, LocalDate> param) {
						TextFieldTableCell<Subscription, LocalDate> cell = new TextFieldTableCell<Subscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		subsStopDateColumn.setCellFactory(
				new Callback<TableColumn<Subscription, LocalDate>, TableCell<Subscription, LocalDate>>() {

					@Override
					public TableCell<Subscription, LocalDate> call(TableColumn<Subscription, LocalDate> param) {
						TextFieldTableCell<Subscription, LocalDate> cell = new TextFieldTableCell<Subscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});

		stphstopHistoryIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, Long>("stopHistoryId"));
		stphsubsIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, Long>("subscriptionId"));
		stphsubsProdCodeColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, String>("productCode"));
		stphsubsProdNameColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, String>("productName"));
		stphsubsTypeColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, String>("subscriptionType"));
		stphsubsFreqColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, String>("subscriptionFreq"));
		stphsubsDOWColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, String>("subscriptionDOW"));
		stphsubsResumeDateColumn
				.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, LocalDate>("resumeDate"));
		stphstopHistAmountColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, Double>("amount"));
		stphsubsStopDateColumn.setCellValueFactory(new PropertyValueFactory<StopHistoryBackup, LocalDate>("stopDate"));
		stphsubsResumeDateColumn.setCellFactory(
				new Callback<TableColumn<StopHistoryBackup, LocalDate>, TableCell<StopHistoryBackup, LocalDate>>() {

					@Override
					public TableCell<StopHistoryBackup, LocalDate> call(
							TableColumn<StopHistoryBackup, LocalDate> param) {
						TextFieldTableCell<StopHistoryBackup, LocalDate> cell = new TextFieldTableCell<StopHistoryBackup, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		stphsubsStopDateColumn.setCellFactory(
				new Callback<TableColumn<StopHistoryBackup, LocalDate>, TableCell<StopHistoryBackup, LocalDate>>() {

					@Override
					public TableCell<StopHistoryBackup, LocalDate> call(
							TableColumn<StopHistoryBackup, LocalDate> param) {
						TextFieldTableCell<StopHistoryBackup, LocalDate> cell = new TextFieldTableCell<StopHistoryBackup, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});

		addCustLineNum.setDisable(true);
		addCustHouseSeq.setDisable(true);
		addCustLineNum.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				addCustHouseSeq.setDisable(false);
				seq = maxSeq();
				seq = seq == 0 ? 1 : seq;
				addCustHouseSeq.setText(seq + "");
			}
		});

		addCustHwkCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				customerMasterData.clear();
				ACustInfoTable.getItems().addAll(customerMasterData);
				subscriptionMasterData.clear();
				subscriptionsTable.getItems().addAll(subscriptionMasterData);
				subscriptionsTable.refresh();
				hawkerMobLabel.setText("");
				hawkerNameLabel.setText("");
				if (newValue != null) {
					hawkerNameMobCode(newValue);
					addCustLineNum.setDisable(false);
					addCustLineNum.getItems().clear();
					populateLineNumbersForHawkerCode(newValue);
					addCustLineNum.getItems().addAll(hawkerLineNumData);
					refreshCustomerTable();
				}
				invoiceDateLOV.getItems().clear();
				billingLinesData.clear();
				billingTable.setItems(billingLinesData);
				billingTable.refresh();
				stopHistoryTable.getItems().clear();
				stopHistoryTable.refresh();
			}

		});

		addCustState.getItems().addAll("Tamil Nadu", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
				"Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
				"Odisha", "Punjab", "Rajasthan", "Sikkim", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand",
				"West Bengal");

		ACustInfoTable.setRowFactory(new Callback<TableView<Customer>, TableRow<Customer>>() {

			@Override
			public TableRow<Customer> call(TableView<Customer> param) {

				final TableRow<Customer> row = new TableRow<>();
				MenuItem mnuDel = new MenuItem("Delete customer");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							deleteCustomer(custRow);
							ACustInfoTable.refresh();
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit customer");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							showEditCustomerDialog(custRow);
							ACustInfoTable.refresh();
						}
					}

				});
				MenuItem mnuView = new MenuItem("View customer");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							showViewCustomerDialog(custRow);
							// ACustInfoTable.refresh();
						}
					}

				});
				MenuItem mnuSubs = new MenuItem("Add subscription");
				mnuSubs.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							addCustSubscription(custRow);
							// refreshSubscriptions();
						}
					}

				});
				MenuItem mnuPause = new MenuItem("Stop Subscription");
				mnuPause.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();

						Dialog<ButtonType> pauseWarning = new Dialog<ButtonType>();
						pauseWarning.setTitle("Stop Subscription");
						pauseWarning.setHeaderText("Please select the subscription you want to Stop");
						ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
						pauseWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
						GridPane grid = new GridPane();
						grid.setHgap(10);
						grid.setVgap(10);
						grid.setPadding(new Insets(20, 150, 10, 10));
						ObservableList<Subscription> subsList = getActiveSubsListForCust(custRow);
						if (!subsList.isEmpty()) {
							CheckComboBox<Subscription> subsBox = new CheckComboBox<Subscription>();
							subsBox.setConverter(new StringConverter<Subscription>() {

								@Override
								public String toString(Subscription object) {
									return object.getSubscriptionId() + "-" + object.getProductCode() + "-"
											+ object.getProductName() + "-" + object.getFrequency() + "-"
											+ object.getDow() + "-" + object.getStartDate()
													.format(DateTimeFormatter.ofPattern("d/M/y")).toString();
								}

								@Override
								public Subscription fromString(String string) {
									for (int i = 0; i < subsList.size(); i++) {
										Subscription sub = subsList.get(i);
										if (sub.getSubscriptionId() == (Long.parseLong(string.split("-")[0])))
											return sub;
									}
									return null;
								}
							});
							subsBox.getItems().addAll(subsList);
							subsBox.setMaxWidth(250);
							// subsBox.getSelectionModel().selectFirst();
							grid.add(new Label("Subscription"), 0, 0);
							grid.add(new Label("Stop Date"), 1, 0);
							grid.add(new Label("Reminder Date"), 2, 0);
							DatePicker dp = new DatePicker(LocalDate.now());
							dp.setConverter(Main.dateConvertor);
							dp.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										dp.setValue(dp.getConverter().fromString(dp.getEditor().getText()));
									}
								}
							});
							grid.add(subsBox, 0, 1);
							grid.add(dp, 1, 1);
							DatePicker resumeDP = new DatePicker();
							resumeDP.setConverter(Main.dateConvertor);
							resumeDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										resumeDP.setValue(
												resumeDP.getConverter().fromString(resumeDP.getEditor().getText()));
									}
								}
							});
							grid.add(resumeDP, 2, 1);
							pauseWarning.getDialogPane().setContent(grid);
							Button yesButton = (Button) pauseWarning.getDialogPane().lookupButton(saveButtonType);
							yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
								if (subsBox.getCheckModel().getCheckedIndices().isEmpty()) {
									Notifications.create().title("No subscription selected")
											.text("No subscription selected to stop.").hideAfter(Duration.seconds(5))
											.showError();
									btnEvent.consume();
								}
								for (int i = 0; i < subsBox.getCheckModel().getCheckedItems().size(); i++) {
									Subscription sub = subsBox.getCheckModel().getCheckedItems().get(i);
									if (checkStopSubDate(dp.getValue(), sub)) {
										Notifications.create().title("Invalid stop date")
												.text("Stop date should not be before Start date for stopped subscription")
												.hideAfter(Duration.seconds(5)).showError();
										btnEvent.consume();
									}
								}
								for (int i = 0; i < subsBox.getCheckModel().getCheckedItems().size(); i++) {
									Subscription sub = subsBox.getCheckModel().getCheckedItems().get(i);
									if (stopEntryExistsForStartDate(sub, dp.getValue())) {
										Notifications.create().title("Stop Entry exists")
												.text("A stop entry for this subscription on selected StopDate already exists.")
												.hideAfter(Duration.seconds(5)).showError();
										btnEvent.consume();
									}
								}

							});
							Optional<ButtonType> result = pauseWarning.showAndWait();
							if (result.isPresent() && result.get() == saveButtonType) {
								for (Subscription subsRow : subsBox.getCheckModel().getCheckedItems()) {
									if (subsRow != null && subsRow.getStatus().equals("Active")) {
										if (resumeDP.getValue() != null) {
											if ((dp.getValue().isBefore(resumeDP.getValue()))) {
												subsRow.setStatus("Stopped");
												subsRow.setPausedDate(dp.getValue());
												subsRow.setResumeDate(resumeDP.getValue());
												subsRow.updateSubscriptionRecord();
												createStopHistoryForSub(subsRow, dp.getValue(), null);
												Notifications.create().title("Stop successful")
														.text("Stop subscription successful")
														.hideAfter(Duration.seconds(5)).showInformation();
											} else {

												Notifications.create().title("Invalid Resume Date")
														.text("Resume date must be after stop date")
														.hideAfter(Duration.seconds(5)).showError();
											}
										} else {
											subsRow.setStatus("Stopped");
											subsRow.setPausedDate(dp.getValue());
											subsRow.setResumeDate(null);
											subsRow.updateSubscriptionRecord();
											createStopHistoryForSub(subsRow, dp.getValue(), null);
											Notifications.create().title("Stop successful")
													.text("Stop subscription successful").hideAfter(Duration.seconds(5))
													.showInformation();
										}

									} else {
										Notifications.create().title("Invalid operation")
												.text("Subscription is already STOPPED").hideAfter(Duration.seconds(5))
												.showWarning();
									}

								}
								refreshSubscriptions();
								refreshStopHistory();
							}
						} else {

							Notifications.create().title("No Active subscriptions")
									.text("No active subscriptions present for this customer.")
									.hideAfter(Duration.seconds(5)).showError();
						}

					}

					private boolean checkStopSubDate(LocalDate value, Subscription checkedItems) {
						if (value.isBefore(checkedItems.getStartDate()))
							return true;

						return false;
					}

				});

				MenuItem mnuResume = new MenuItem("Resume Subscription");
				mnuResume.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();

						Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
						deleteWarning.setTitle("Start Subscriptions");
						deleteWarning.setHeaderText("Are you sure you want to START this subscription?");
						ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
						deleteWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

						ObservableList<Subscription> subsList = getPausedSubsListForCust(custRow);
						if (!subsList.isEmpty()) {
							ComboBox<Subscription> subsBox = new ComboBox<Subscription>();
							subsBox.setConverter(new StringConverter<Subscription>() {

								@Override
								public String toString(Subscription object) {
									return object.getSubscriptionId() + "-" + object.getProductCode() + "-"
											+ object.getProductName();
								}

								@Override
								public Subscription fromString(String string) {
									for (int i = 0; i < subsList.size(); i++) {
										Subscription sub = subsList.get(i);
										if (sub.getSubscriptionId() == (Long.parseLong(string.split("-")[0])))
											return sub;
									}
									return null;
								}
							});
							subsBox.getItems().addAll(subsList);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));
							DatePicker dp = new DatePicker();
							dp.setConverter(Main.dateConvertor);
							dp.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										dp.setValue(dp.getConverter().fromString(dp.getEditor().getText()));
									}
								}
							});
							dp.setDisable(true);
							DatePicker resumeDP = new DatePicker(LocalDate.now());
							resumeDP.setConverter(Main.dateConvertor);
							resumeDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										resumeDP.setValue(
												resumeDP.getConverter().fromString(resumeDP.getEditor().getText()));
									}
								}
							});
							// resumeDP.setDisable(true);
							subsBox.getSelectionModel().selectedItemProperty()
									.addListener(new ChangeListener<Subscription>() {

										@Override
										public void changed(ObservableValue<? extends Subscription> observable,
												Subscription oldValue, Subscription newValue) {
											if (newValue != null) {
												dp.setValue(newValue.getPausedDate());
												resumeDP.setValue(newValue.getResumeDate() == null ? LocalDate.now()
														: newValue.getResumeDate());
											}

										}
									});
							subsBox.getSelectionModel().selectFirst();
							grid.add(new Label("Subscription"), 0, 0);
							grid.add(new Label("Stop Date"), 0, 1);
							grid.add(new Label("Resume Date"), 0, 2);
							grid.add(dp, 1, 1);
							grid.add(subsBox, 1, 0);
							grid.add(resumeDP, 1, 2);
							deleteWarning.getDialogPane().setContent(grid);
							Optional<ButtonType> result = deleteWarning.showAndWait();
							if (result.isPresent() && result.get() == saveButtonType) {
								Subscription subsRow = subsBox.getSelectionModel().getSelectedItem();
								if (subsRow != null && subsRow.getStatus().equals("Stopped")) {
									LocalDate maxStopDate = ALineInfoTabController.findMaxStopDateForSub(subsRow);
									if (maxStopDate.isBefore(resumeDP.getValue())) {
										subsRow.resumeSubscription();

										int count = subsPostCount(subsRow, "Stopped");
										if (count > 0) {
											Notifications.create().title("Product has more stopped subscriptions")
													.text("This product has " + count
															+ " more stopped subscriptions for this customer")
													.hideAfter(Duration.seconds(5)).showWarning();
										}
										resumeStopHistoryForSub(subsRow, maxStopDate, resumeDP.getValue());
										refreshSubscriptions();
										refreshStopHistory();
										Notifications.create().title("Resume successful")
												.text("Resume subscription successful").hideAfter(Duration.seconds(5))
												.showInformation();
									} else {

										Notifications.create().title("Invalid Resume Date")
												.text("Resume date must be after the Stop History latest stop date.")
												.hideAfter(Duration.seconds(5)).showError();
									}
								} else {
									Notifications.create().title("Invalid operation")
											.text("Subscription is already ACTIVE").hideAfter(Duration.seconds(5))
											.showWarning();
								}
							}
						} else {

							Notifications.create().title("No Stopped subscriptions")
									.text("No Stopped subscriptions present for this customer.")
									.hideAfter(Duration.seconds(5)).showError();
						}

					}

				});
				MenuItem mnuBill = new MenuItem("Regenerate Invoice");
				mnuBill.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							Dialog<ButtonType> billWarning = new Dialog<ButtonType>();
							billWarning.setTitle("Generate invoice");
							billWarning.setHeaderText("Please select the Invoice Date");
							ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
							billWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Invoice Date"), 0, 0);
							DatePicker invoiceDP = new DatePicker(LocalDate.now());
							invoiceDP.setConverter(Main.dateConvertor);
							invoiceDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										invoiceDP.setValue(
												invoiceDP.getConverter().fromString(invoiceDP.getEditor().getText()));
									}
								}
							});
							// pauseDP.setDisable(true);
							grid.add(invoiceDP, 1, 0);
							billWarning.getDialogPane().setContent(grid);
							Optional<ButtonType> result = billWarning.showAndWait();
							if (result.isPresent() && result.get() == saveButtonType) {
								Task<Void> task = new Task<Void>() {
									Notifications n = Notifications.create().title("Please wait")
											.text("Generating custome bill. Please wait for the process to finish.");

									@Override
									protected Void call() throws Exception {
										Platform.runLater(new Runnable() {

											@Override
											public void run() {

												n.hideAfter(Duration.seconds(10)).position(Pos.CENTER)
														.showInformation();
											}
										});
										disableAll();
										BillingUtilityClass.createBillingInvoice(custRow.getCustomerId(),
												invoiceDP.getValue().withDayOfMonth(1),
												invoiceDP.getValue().withDayOfMonth(1).plusMonths(1).minusDays(1),
												true);
										refreshSubscriptions();
										refreshStopHistory();
										populateInvoiceDates(custRow);
										Platform.runLater(new Runnable() {

											@Override
											public void run() {
												Notifications.create().title("Bill generated")
														.text("Customer Bill generated").hideAfter(Duration.seconds(5))
														.position(Pos.CENTER).showInformation();
												enableAll();
											}
										});
										return null;
									}

								};

								new Thread(task).start();
							}

						}
					}

				});

				MenuItem mnuDue = new MenuItem("Change Due Amount");
				mnuDue.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							TextInputDialog dialog = new TextInputDialog();

							dialog.setTitle("Change total due value");
							dialog.setHeaderText("Change total due value below");

							Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
							dialog.getEditor().setText(Double.toString(custRow.getTotalDue()));
							saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

								try {
									Double d = Double.parseDouble(dialog.getEditor().getText());
									custRow.setTotalDue(d);
									custRow.updateCustomerRecord();
									refreshCustomerTable();
								} catch (NumberFormatException e) {
									Notifications.create().title("Invalid value")
											.text("Please enter only NUMERIC values in Total Due.")
											.hideAfter(Duration.seconds(5)).showError();

									Main._logger.debug("Error :", e);
									e.printStackTrace();
									btnevent.consume();
								} catch (Exception e) {
									Main._logger.debug("Error :", e);
									e.printStackTrace();
								}

							});
							dialog.showAndWait();
						}
					}

				});
				MenuItem mnuAddDue = new MenuItem("Add Due");
				mnuAddDue.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = ACustInfoTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							TextInputDialog dialog = new TextInputDialog();

							dialog.setTitle("Add to total due value");
							dialog.setHeaderText("Add to total due value below");

							Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
							// dialog.getEditor().setText(custRow.getTotalDue()+"");
							saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

								try {
									Double d = Double.parseDouble(dialog.getEditor().getText());
									custRow.setTotalDue(custRow.getTotalDue() + d);
									custRow.updateCustomerRecord();
									refreshCustomerTable();
								} catch (NumberFormatException e) {
									Notifications.create().title("Invalid value")
											.text("Please enter only NUMERIC values in Total Due.")
											.hideAfter(Duration.seconds(5)).showError();

									Main._logger.debug("Error :", e);
									e.printStackTrace();
									btnevent.consume();
								} catch (Exception e) {
									Main._logger.debug("Error :", e);
									e.printStackTrace();
								}

							});
							dialog.showAndWait();
						}
					}

				});

				ContextMenu menu = new ContextMenu();

				if (HawkerLoginController.loggedInHawker != null) {
					menu.getItems().addAll(mnuEdit, mnuView, mnuSubs, mnuPause, mnuResume, mnuBill, mnuAddDue, mnuDue);
				} else {
					menu.getItems().addAll(mnuEdit, mnuView, mnuDel, mnuSubs, mnuPause, mnuResume, mnuBill, mnuAddDue,
							mnuDue);
				}
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});
		addCustExtraButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addCustomerExtraScreenClicked(new ActionEvent());
				}
			}
		});

		addCustInitials.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.length() > 3)
					addCustInitials.setText(oldValue);
			}
		});

		saveCustomerButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addCustomerClicked(new ActionEvent());
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
		searchButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					filterCustomersClicked(new ActionEvent());
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

		subscriptionsTable.setRowFactory(new Callback<TableView<Subscription>, TableRow<Subscription>>() {

			@Override
			public TableRow<Subscription> call(TableView<Subscription> param) {
				final TableRow<Subscription> row = new TableRow<Subscription>();
				MenuItem mnuDel = new MenuItem("Delete Subscription");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
							if (HawkerLoginController.loggedInHawker != null && subscriptionMasterData.size() > 1) {
								Notifications.create().title("Delete not allowed")
										.text("Delete not allowed if customer has more than one subscription")
										.hideAfter(Duration.seconds(10)).showError();
							} else
								deleteSubscription(subsRow);

						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit Subscription");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
							showEditSubscriptionDialog(subsRow);
							// refreshSubscriptions();
						}
					}

				});

				MenuItem mnuPause = new MenuItem("Stop Subscription");
				mnuPause.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null && subsRow.getStatus().equals("Active")) {
							Dialog<ButtonType> pauseWarning = new Dialog<ButtonType>();
							pauseWarning.setTitle("Warning");
							pauseWarning.setHeaderText("Are you sure you want to STOP this subscription?");
							pauseWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Stop Date"), 0, 0);
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
							grid.add(pauseDP, 1, 0);
							grid.add(new Label("Reminder Date"), 0, 1);
							DatePicker resumeDP = new DatePicker();
							resumeDP.setConverter(Main.dateConvertor);
							resumeDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										resumeDP.setValue(
												resumeDP.getConverter().fromString(resumeDP.getEditor().getText()));
									}
								}
							});
							grid.add(resumeDP, 1, 1);
							pauseWarning.getDialogPane().setContent(grid);
							Button yesButton = (Button) pauseWarning.getDialogPane().lookupButton(ButtonType.YES);
							yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
								if (pauseDP.getValue().isBefore(subsRow.getStartDate())) {
									Notifications.create().title("Invalid stop date")
											.text("Stop date should not be before Start date for subscription")
											.hideAfter(Duration.seconds(5)).showError();
									btnEvent.consume();
								}
								if (resumeDP.getValue() != null && resumeDP.getValue().isBefore(pauseDP.getValue())) {
									Notifications.create().title("Invalid Resume date")
											.text("Resume date should not be before Stop date for subscription")
											.hideAfter(Duration.seconds(5)).showError();
									btnEvent.consume();
								}
								if (stopEntryExistsForStartDate(subsRow, pauseDP.getValue())) {
									Notifications.create().title("Stop Entry exists")
											.text("A stop entry for this subscription on selected StopDate already exists.")
											.hideAfter(Duration.seconds(5)).showError();
									btnEvent.consume();
								}
							});
							Optional<ButtonType> result = pauseWarning.showAndWait();
							if (result.isPresent() && result.get() == ButtonType.YES) {
								subsRow.setStatus("Stopped");
								subsRow.setPausedDate(pauseDP.getValue());
								subsRow.setResumeDate(resumeDP.getValue());
								subsRow.updateSubscriptionRecord();
								int count = subsPostCount(subsRow, "Active");
								if (count > 0) {
									Notifications.create().title("Product has more active subscriptions")
											.text("This product has " + count
													+ " more active subscriptions for this customer")
											.hideAfter(Duration.seconds(5)).showWarning();
								}
								createStopHistoryForSub(subsRow, pauseDP.getValue(), null);
								refreshSubscriptions();
								refreshStopHistory();
							}
						} else {
							Notifications.create().title("Invalid operation").text("Subscription is already STOPPED")
									.hideAfter(Duration.seconds(5)).showWarning();
						}

					}

				});

				MenuItem mnuResume = new MenuItem("Resume Subscription");
				mnuResume.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null && subsRow.getStatus().equals("Stopped")) {
							Dialog<ButtonType> resumeWarning = new Dialog<ButtonType>();
							resumeWarning.setTitle("Warning");
							resumeWarning.setHeaderText("Are you sure you want to RESUME this record?");
							resumeWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Stop Date"), 0, 0);
							DatePicker pauseDP = new DatePicker(subsRow.getPausedDate());
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
							pauseDP.setDisable(true);
							grid.add(pauseDP, 1, 0);
							grid.add(new Label("Resume Date"), 0, 1);
							DatePicker resumeDP = new DatePicker(LocalDate.now());
							resumeDP.setConverter(Main.dateConvertor);
							resumeDP.focusedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									if (!newValue) {
										resumeDP.setValue(
												resumeDP.getConverter().fromString(resumeDP.getEditor().getText()));
									}
								}
							});
							// resumeDP.setDisable(true);
							grid.add(resumeDP, 1, 1);
							resumeWarning.getDialogPane().setContent(grid);
							Optional<ButtonType> result = resumeWarning.showAndWait();
							if (result.isPresent() && result.get() == ButtonType.YES) {
								LocalDate maxStopDate = ALineInfoTabController.findMaxStopDateForSub(subsRow);
								if (maxStopDate.isBefore(resumeDP.getValue())) {
									if (resumeDP.getValue().isBefore(maxStopDate.plusMonths(1).withDayOfMonth(1))) {
										subsRow.resumeSubscription();

										int count = subsPostCount(subsRow, "Stopped");
										if (count > 0) {
											Notifications.create().title("Product has more stopped subscriptions")
													.text("This product has " + count
															+ " more stopped subscriptions for this customer")
													.hideAfter(Duration.seconds(5)).showWarning();
										}
										resumeStopHistoryForSub(subsRow, pauseDP.getValue(), resumeDP.getValue());
										refreshSubscriptions();
										refreshStopHistory();
										Notifications.create().title("Resume successful")
												.text("Resume subscription successful").hideAfter(Duration.seconds(5))
												.showInformation();
									} else {

										Notifications.create().title("Invalid Resume Date")
												.text("Resume date must be in the same month as Stop History latest stop date.")
												.hideAfter(Duration.seconds(5)).showError();
									}
								} else {

									Notifications.create().title("Invalid Resume Date")
											.text("Resume date must be after stop date").hideAfter(Duration.seconds(5))
											.showError();
								}
							}
						} else {
							Notifications.create().title("Invalid operation").text("Subscription is already ACTIVE")
									.hideAfter(Duration.seconds(5)).showWarning();
						}
					}

				});

				MenuItem mnuView = new MenuItem("View Subscription");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
							showViewSubscriptionDialog(subsRow);
							// refreshSubscriptions();
						}
					}

				});

				MenuItem mnuViewProd = new MenuItem("View Product");
				mnuViewProd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = BillingUtilityClass
								.productForSub(subscriptionsTable.getSelectionModel().getSelectedItem().getProductId());
						if (productRow != null) {
							showViewProductDialog(productRow);
						}
					}

				});
				ContextMenu menu = new ContextMenu();

				if (HawkerLoginController.loggedInHawker != null) {
					menu.getItems().addAll(mnuPause, mnuResume, mnuEdit, mnuView, mnuViewProd, mnuDel);
				} else {
					menu.getItems().addAll(mnuPause, mnuResume, mnuEdit, mnuView, mnuViewProd, mnuDel);
				}

				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		ACustInfoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {

			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
				if (newValue != null) {
					refreshSubscriptions();
					populateInvoiceDates(newValue);
					refreshStopHistory();
					refreshStopHistoryBkp();
				} else {
					if (!Platform.isFxApplicationThread()) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								subscriptionMasterData.clear();
								billingLinesData.clear();
								invoiceDatesData.clear();
								stopHistoryBkpMasterData.clear();
								stopHistoryMasterData.clear();
								stopHistoryBkpTable.setItems(stopHistoryBkpMasterData);
								stopHistoryTable.setItems(stopHistoryMasterData);
								subscriptionsTable.setItems(subscriptionMasterData);
								billingTable.setItems(billingLinesData);
								invoiceDateLOV.setItems(invoiceDatesData);
								totalDueLabel.setText(null);
								totalBillLabel.setText(null);
								netBillLabel.setText(null);
								monthLabel.setText(null);
							}
						});
					} else {

						subscriptionMasterData.clear();
						billingLinesData.clear();
						invoiceDatesData.clear();
						stopHistoryBkpMasterData.clear();
						stopHistoryMasterData.clear();
						stopHistoryBkpTable.setItems(stopHistoryBkpMasterData);
						stopHistoryTable.setItems(stopHistoryMasterData);
						subscriptionsTable.setItems(subscriptionMasterData);
						billingTable.setItems(billingLinesData);
						invoiceDateLOV.setItems(invoiceDatesData);
						totalDueLabel.setText(null);
						totalBillLabel.setText(null);
						netBillLabel.setText(null);
						monthLabel.setText(null);
					}
				}

			}
		});

		// billingVBOX.setVisible(false);

		addPointName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				customerMasterData.clear();
				ACustInfoTable.getItems().addAll(customerMasterData);
				subscriptionMasterData.clear();
				subscriptionsTable.getItems().addAll(subscriptionMasterData);
				subscriptionsTable.refresh();
				if (newValue != null) {
					populateHawkerCodes();
				} else {
					addCustHwkCode.getItems().clear();
				}
			}
		});

		invoiceDateLOV.setConverter(new StringConverter<Billing>() {

			@Override
			public String toString(Billing object) {

				return invoiceDatesData.indexOf(object) + 1 + ") "
						+ object.getInvoiceDate().format(DateTimeFormatter.ofPattern(Main.dateFormat));
			}

			@Override
			public Billing fromString(String string) {
				int i = Integer.parseInt(string.split(")")[0]);
				return invoiceDatesData.get(i - 1);
			}
		});

		invoiceDateLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Billing>() {

			@Override
			public void changed(ObservableValue<? extends Billing> observable, Billing oldValue, Billing newValue) {
				if (newValue != null) {
					populateBillingLines(newValue);
					double d = BillingUtilityClass.calculateTotalBillAmount(newValue);
					double net = d + newValue.getDue();
					String month = newValue.getMonth();

					totalDueLabel.setText(Double.toString(newValue.getDue()));
					totalBillLabel.setText(Double.toString(d));
					netBillLabel.setText(Double.toString(net));
					monthLabel.setText(month);
				} else {
					if (!Platform.isFxApplicationThread()) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								billingLinesData.clear();
								billingTable.setItems(billingLinesData);
								totalDueLabel.setText(null);
								totalBillLabel.setText(null);
								netBillLabel.setText(null);
								monthLabel.setText(null);
							}
						});

					} else {

						billingLinesData.clear();
						billingTable.setItems(billingLinesData);
						totalDueLabel.setText(null);
						totalBillLabel.setText(null);
						netBillLabel.setText(null);
						monthLabel.setText(null);
					}

				}
			}
		});

		stopHistoryTable.setRowFactory(new Callback<TableView<StopHistory>, TableRow<StopHistory>>() {

			@Override
			public TableRow<StopHistory> call(TableView<StopHistory> param) {
				final TableRow<StopHistory> row = new TableRow<StopHistory>();

				MenuItem mnuView = new MenuItem("View Subscription");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						StopHistory subsRow = stopHistoryTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
							showViewSubscriptionDialog(subsRow.getSubscriptionId());
							// refreshSubscriptions();
						}
					}

				});

				ContextMenu menu = new ContextMenu();

				menu.getItems().addAll(mnuView);

				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
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
	}

	private void deleteCustomer(Customer custRow) {
		Main._logger.debug("Entered deleteCustomer method");
		Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
		deleteWarning.setTitle("Warning");
		deleteWarning.setHeaderText("Are you sure you want to delete this record?");
		deleteWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = deleteWarning.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {

			try {
				ArrayList<Customer> custData = getCustomerDataToShift(custRow.getHawkerCode(),
						custRow.getLineNum().intValue());
				shiftHouseSeqForDelete(custData, custRow.getHouseSeq());
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String deleteString = "delete from customer where customer_id=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of customer was successful").showInformation();
				refreshCustomerTable();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of customer has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}

	}

	private void showEditCustomerDialog(Customer custRow) {
		Main._logger.debug("Entered showEditCustomerDialog method");
		int selectedIndex = ACustInfoTable.getSelectionModel().selectedIndexProperty().get();
		int prevHouseSeq = custRow.getHouseSeq();
		try {

			Dialog<Customer> editCustomerDialog = new Dialog<Customer>();
			editCustomerDialog.setTitle("Edit customer data");
			editCustomerDialog.setHeaderText("Update the customer data below");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			editCustomerDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			FXMLLoader editCustomerLoader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
			Parent editCustomerGrid = (Parent) editCustomerLoader.load();
			EditCustomerController editCustController = editCustomerLoader.<EditCustomerController> getController();

			editCustomerDialog.getDialogPane().setContent(editCustomerGrid);
			editCustController.setCustomerToEdit(custRow);
			editCustController.setupBindings();

			editCustomerDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Customer edittedCustomer = editCustController.returnUpdatedCustomer();
					if (houseSequenceExistsInLine(edittedCustomer.getHawkerCode(), edittedCustomer.getHouseSeq(),
							edittedCustomer.getLineNum())) {
						ArrayList<Customer> custData = getCustomerDataToShift(custRow.getHawkerCode(),
								custRow.getLineNum().intValue());
						shiftHouseSeqFromToForCustId(custData, prevHouseSeq, edittedCustomer.getHouseSeq(),
								custRow.getCustomerId());

					}
					return edittedCustomer;
				}
				return null;
			});

			Optional<Customer> updatedCustomer = editCustomerDialog.showAndWait();
			// refreshCustomerTable();

			updatedCustomer.ifPresent(new Consumer<Customer>() {

				@Override
				public void accept(Customer t) {

					customerMasterData.add(selectedIndex, t);
					customerMasterData.remove(custRow);
					ACustInfoTable.getSelectionModel().select(t);
					editCustController.releaseVariables();
					refreshCustomerTable();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void showViewCustomerDialog(Customer custRow) {
		Main._logger.debug("Entered showViewCustomerDialog method");
		try {

			Dialog<Customer> editCustomerDialog = new Dialog<Customer>();
			editCustomerDialog.setTitle("View customer data");
			editCustomerDialog.setHeaderText("View the customer data below");

			editCustomerDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editCustomerLoader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
			Parent editCustomerGrid = (Parent) editCustomerLoader.load();
			EditCustomerController editCustController = editCustomerLoader.<EditCustomerController> getController();

			editCustomerDialog.getDialogPane().setContent(editCustomerGrid);
			editCustController.setCustomerToEdit(custRow);
			editCustController.setupBindings();
			editCustController.gridPane.setDisable(true);
			// editCustController.gridPane.setStyle("-fx-opacity: 1.0;");

			Optional<Customer> updatedCustomer = editCustomerDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void deleteSubscription(Subscription subsRow) {
		Main._logger.debug("Entered deleteSubscription method");
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
				String deleteString = "delete from subscription where subscription_id=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, subsRow.getSubscriptionId());

				deleteStmt.executeUpdate();
				con.commit();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of subscription was successful").showInformation();
				refreshSubscriptions();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of subscription has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
	}

	private void showEditSubscriptionDialog(Subscription subsRow) {
		Main._logger.debug("Entered showEditSubscriptionDialog method");
		try {

			Dialog<Subscription> editSubscriptionDialog = new Dialog<Subscription>();
			editSubscriptionDialog.setTitle("Edit subscription data");
			editSubscriptionDialog.setHeaderText("Update the subscription data below");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			editSubscriptionDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			FXMLLoader editSubscriptionLoader = new FXMLLoader(getClass().getResource("EditSubscription.fxml"));
			Parent editSubscriptionGrid = (Parent) editSubscriptionLoader.load();
			EditSubscriptionController editSubsController = editSubscriptionLoader
					.<EditSubscriptionController> getController();

			editSubscriptionDialog.getDialogPane().setContent(editSubscriptionGrid);
			editSubsController.setSubscriptionToEdit(subsRow,false);
			editSubsController.setupBindings();
			Button saveBtn = (Button) editSubscriptionDialog.getDialogPane().lookupButton(saveButtonType);
			saveBtn.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (editSubsController.isValid()) {
					editSubsController.updateSubscriptionRecord();
					refreshSubscriptions();
					// btnEvent.consume();
				} else
					btnEvent.consume();
			});
			editSubscriptionDialog.setResultConverter(dialogButton -> {
				// if (dialogButton == saveButtonType) {
				// Subscription edittedSubscription =
				// editSubsController.returnUpdatedSubscription();
				// return null;
				// return edittedSubscription;
				// }
				return null;
			});

			Optional<Subscription> updatedSubscription = editSubscriptionDialog.showAndWait();
			// refreshCustomerTable();

			updatedSubscription.ifPresent(new Consumer<Subscription>() {

				@Override
				public void accept(Subscription t) {
					editSubsController.releaseVariables();
					refreshSubscriptions();
				}
			});

		} catch (IOException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void showViewSubscriptionDialog(Subscription subsRow) {
		Main._logger.debug("Entered showViewSubscriptionDialog method");
		try {

			Dialog<Subscription> editSubscriptionDialog = new Dialog<Subscription>();
			editSubscriptionDialog.setTitle("View subscription data");
			editSubscriptionDialog.setHeaderText("View the subscription data below");

			// Set the button types.

			editSubscriptionDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editSubscriptionLoader = new FXMLLoader(getClass().getResource("EditSubscription.fxml"));
			Parent editSubscriptionGrid = (Parent) editSubscriptionLoader.load();
			EditSubscriptionController editSubsController = editSubscriptionLoader
					.<EditSubscriptionController> getController();

			editSubscriptionDialog.getDialogPane().setContent(editSubscriptionGrid);
			editSubsController.setSubscriptionToEdit(subsRow,false);
			editSubsController.setupBindings();
			editSubsController.gridPane.setDisable(true);

			Optional<Subscription> updatedSubscription = editSubscriptionDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void addCustomerExtraScreenClicked(ActionEvent event) {
		Main._logger.debug("Entered addCustomerExtraScreenClicked method");
		try {

			Dialog<String> addCustomerDialog = new Dialog<String>();
			addCustomerDialog.setTitle("Add new customer");
			addCustomerDialog.setHeaderText("Add new Customer data below.");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save and add new", ButtonData.OK_DONE);
			addCustomerDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CLOSE);
			Button saveButton = (Button) addCustomerDialog.getDialogPane().lookupButton(saveButtonType);
			FXMLLoader addCustomerLoader = new FXMLLoader(getClass().getResource("AddCustomersExtraScreen.fxml"));
			Parent addCustomerGrid = (Parent) addCustomerLoader.load();
			AddCustomerExtraScreenController addCustController = addCustomerLoader
					.<AddCustomerExtraScreenController> getController();
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addCustController.isValid()) {
					addCustController.addCustomer();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Customer created")
							.text("Customer created successfully.").showInformation();
					refreshCustomerTable();
				} else {
					btnEvent.consume();
				}
			});
			addCustomerDialog.getDialogPane().setContent(addCustomerGrid);
			addCustController.setupBindings();

			addCustomerDialog.setResultConverter(dialogButton -> {
				if (dialogButton != saveButtonType) {
					return null;
				}
				return null;
			});

			Optional<String> updatedCustomer = addCustomerDialog.showAndWait();
			// refreshCustomerTable();

			updatedCustomer.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					addCustController.releaseVariables();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void populateLineNumbersForHawkerCode(String hawkerCode) {
		Main._logger.debug("Entered populateLineNumbersForHawkerCode method");

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

	public static long hawkerIdForCode(String hawkerCode) {
		Main._logger.debug("Entered hawkerIdForCode method");

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

	public static long lineIdForNumHwkCode(int lineNum, String hwkCode) {
		Main._logger.debug("Entered lineIdForNum method");

		long lineId = -1;
		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement lineIdStatement = null;
			String lineIdQuery = "select line_id from line_info where hawker_id = ? and line_num=?";
			lineIdStatement = con.prepareStatement(lineIdQuery);
			lineIdStatement.setLong(1, BillingUtilityClass.hawkerForHwkCode(hwkCode).getHawkerId());
			lineIdStatement.setInt(2, lineNum);

			ResultSet lineIdRs = lineIdStatement.executeQuery();

			if (lineIdRs.next()) {
				lineId = lineIdRs.getLong(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return lineId;
	}

	private void hawkerNameMobCode(String hawkerCode) {
		Main._logger.debug("Entered hawkerNameMobCode method");

		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement hawkerStatement = null;
			String hawkerQuery = "select name, mobile_num from hawker_info where hawker_code = ?";
			hawkerStatement = con.prepareStatement(hawkerQuery);
			hawkerStatement.setString(1, hawkerCode);
			ResultSet hawkerRs = hawkerStatement.executeQuery();

			if (hawkerRs.next()) {
				hawkerNameLabel.setText(hawkerRs.getString(1));
				hawkerMobLabel.setText(hawkerRs.getString(2));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void refreshCustomerTable() {
		Main._logger.debug("Entered refreshCustomerTable method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					// populateHawkerCodes();
					String queryString;
					PreparedStatement stmt;
					disableAll();
					if (HawkerLoginController.loggedInHawker == null) {
						if (showAllRadioButton.isSelected()) {
							queryString = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer order by hawker_code,line_num,house_seq";
							stmt = con.prepareStatement(queryString);
						} else {
							queryString = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_code=? order by hawker_code,line_num,house_seq";
							stmt = con.prepareStatement(queryString);
							stmt.setString(1, addCustHwkCode.getSelectionModel().getSelectedItem());
							// addPointName.setDisable(true);
							// addCustHwkCode.setDisable(true);
						}
					} else {
						queryString = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_code=? order by hawker_code,line_num,house_seq";
						stmt = con.prepareStatement(queryString);
						stmt.setString(1, HawkerLoginController.loggedInHawker.getHawkerCode());
					}

					customerMasterData.clear();
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						customerMasterData.add(new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3),
								rs.getString(4), rs.getString(5), rs.getLong(6), rs.getInt(7), rs.getString(8),
								rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13),
								rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),
								rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21),
								rs.getDouble(22), rs.getLong(23), rs.getLong(24)));
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
				if (!customerMasterData.isEmpty()) {
					filteredData = new FilteredList<>(customerMasterData, p -> true);
					SortedList<Customer> sortedData = new SortedList<>(filteredData);
					sortedData.comparatorProperty().bind(ACustInfoTable.comparatorProperty());

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							ACustInfoTable.setItems(sortedData);
							ACustInfoTable.refresh();
						}
					});
					enableAll();

				}

				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateHawkerCodes() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						if (HawkerLoginController.loggedInHawker != null) {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									hawkerCodeData = FXCollections.observableArrayList();
									hawkerCodeData.add(HawkerLoginController.loggedInHawker.getHawkerCode());
									addCustHwkCode.setItems(hawkerCodeData);
									addCustHwkCode.getSelectionModel().selectFirst();
									addCustHwkCode.setDisable(true);
								}
							});
						} else {
							hawkerCodeData = FXCollections.observableArrayList();
							PreparedStatement stmt = con.prepareStatement(
									"select distinct hawker_code from hawker_info where point_name=? order by hawker_code");
							stmt.setString(1, addPointName.getSelectionModel().getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if(hawkerCodeData!=null && !hawkerCodeData.contains(rs.getString(1)))
									hawkerCodeData.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {

									addCustHwkCode.getItems().clear();
									addCustHwkCode.setItems(hawkerCodeData);
									new AutoCompleteComboBoxListener<>(addCustHwkCode);
								}
							});
							rs.close();
							stmt.close();
						}
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

	private boolean checkExistingProfileValue(String profileValue) {
		Main._logger.debug("Entered checkExistingProfileValue method");
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

	private int maxSeq() {
		Main._logger.debug("Entered maxSeq method");
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			hawkerLineNumData.clear();
			PreparedStatement stmt = con
					.prepareStatement("select max(house_seq)+1 seq from customer where hawker_code=? and line_num=?");
			stmt.setString(1, addCustHwkCode.getSelectionModel().getSelectedItem());
			stmt.setString(2, addCustLineNum.getSelectionModel().getSelectedItem().split(" ")[0]);
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

	public boolean isValid() {
		Main._logger.debug("Entered isValid method");
		boolean validate = true;
		if (addCustName.getText() == null || addCustName.getText().isEmpty()) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Customer Name not provided")
					.text("Please provide a customer name before adding the the customer").showError();
		}
		if (addCustHwkCode.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
					.text("Please select a hawker before adding the the customer").showError();
		}
		if (addCustLineNum.getSelectionModel().getSelectedItem() == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Line number not selected")
					.text("Please select a line number before adding the the customer").showError();
		}
		if (NumberUtils.tryParseInt(addCustHouseSeq.getText()) == null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid house number")
					.text("House sequence should not be empty and must be NUMBERS only").showError();
		}
		if ((Integer.parseInt(addCustHouseSeq.getText()) > seq || Integer.parseInt(addCustHouseSeq.getText()) < 1)
				&& addCustLineNum.getSelectionModel().getSelectedItem() != null) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid House Sequence")
					.text("House Sequence must be between 1 and " + seq).showError();
		}
		if (addCustProf3.getText() != null && checkExistingProfileValue(addCustProf3.getText())) {
			validate = false;
			Notifications.create().hideAfter(Duration.seconds(5)).title("Profile 3 already exists")
					.text("Value for Profile 3 already exists, please select this in Profile 1 or Profile 2 field.")
					.showError();
		}
		// if (addCustMobile.getText().length()!=10) {
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// validate = false;
		// }
		// try {
		// Integer.parseInt(addCustMobile.getText());
		// } catch (NumberFormatException e) {
		// Notifications.create().title("Invalid mobile number")
		// .text("Mobile number should only contain 10 DIGITS")
		// .hideAfter(Duration.seconds(5)).showError();
		// validate = false;
		// Main._logger.debug("Error :",e); e.printStackTrace();
		// }
		return validate;
	}

	@FXML
	private void addCustomerClicked(ActionEvent event) {
		Main._logger.debug("Entered addCustomerClicked method");
		System.out.println("addCustomerClicked");

		if (isValid()) {
			if (houseSequenceExistsInLine(addCustHwkCode.getSelectionModel().getSelectedItem(),
					Integer.parseInt(addCustHouseSeq.getText()),
					Long.parseLong(addCustLineNum.getSelectionModel().getSelectedItem().split(" ")[0].trim()))) {
				ArrayList<Customer> custData = getCustomerDataToShift(
						addCustHwkCode.getSelectionModel().getSelectedItem(),
						Integer.parseInt(addCustLineNum.getSelectionModel().getSelectedItem().split(" ")[0].trim()));
				shiftHouseSeqFrom(custData, Integer.parseInt(addCustHouseSeq.getText()));
			}

			PreparedStatement insertCustomer = null;
			String insertStatement = "INSERT INTO CUSTOMER(name,mobile_num,hawker_code, line_num, house_seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street,HAWKER_ID, LINE_ID) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = Main.dbConnection;
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertCustomer = con.prepareStatement(insertStatement, new String[] { "CUSTOMER_ID" });
				insertCustomer.setString(1, addCustName.getText());
				insertCustomer.setString(2, addCustMobile.getText());
				insertCustomer.setString(3, addCustHwkCode.getSelectionModel().getSelectedItem());
				if (!addCustLineNum.isDisabled())
					insertCustomer.setLong(4,
							Long.parseLong(addCustLineNum.getSelectionModel().getSelectedItem().split(" ")[0].trim()));
				else
					insertCustomer.setString(4, null);
				if (!addCustHouseSeq.isDisabled())
					insertCustomer.setInt(5, Integer.parseInt(addCustHouseSeq.getText()));
				else
					insertCustomer.setString(5, null);
				insertCustomer.setString(6, addCustOldHouseNum.getText());
				insertCustomer.setString(7, addCustNewHouseNum.getText());
				insertCustomer.setString(8, addCustAddrLine1.getText());
				insertCustomer.setString(9, addCustAddrLine2.getText());
				insertCustomer.setString(10, addCustLocality.getText());
				insertCustomer.setString(11, addCustCity.getText());
				insertCustomer.setString(12, addCustState.getSelectionModel().getSelectedItem());
				insertCustomer.setString(13, addCustProf1.getSelectionModel().getSelectedItem());
				insertCustomer.setString(14, addCustProf2.getSelectionModel().getSelectedItem());
				insertCustomer.setString(15,
						addCustProf3.getText() == null ? null : addCustProf3.getText().toLowerCase());
				insertCustomer.setString(16, addCustInitials.getText());
				insertCustomer.setString(17, addCustEmployment.getSelectionModel().getSelectedItem());
				insertCustomer.setString(18, addCustComments.getText());
				insertCustomer.setString(19, addCustBuildingStreet.getText());
				insertCustomer.setLong(20, hawkerIdForCode(addCustHwkCode.getSelectionModel().getSelectedItem()));
				insertCustomer.setLong(21,
						lineIdForNumHwkCode(
								Integer.parseInt(
										addCustLineNum.getSelectionModel().getSelectedItem().split(" ")[0].trim()),
								addCustHwkCode.getSelectionModel().getSelectedItem()));
				insertCustomer.executeUpdate();
				refreshCustomerTable();
				con.commit();
				resetClicked(event);
				// con.close();

				if (HawkerLoginController.loggedInHawker != null) {
					addCustLineNum.requestFocus();
				} else {
					addCustName.requestFocus();
				}

			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Error!")
						.text("There has been some error during customer creation, please retry").showError();
				Main.reconnect();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}

		}

	}

	public ArrayList<Customer> getCustomerDataToShift(String hawkerCode, int lineNum) {
		Main._logger.debug("Entered getCustomerDataToShift method");
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
		Main._logger.debug("Entered shiftHouseSeqFrom method");

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
		Main._logger.debug("Entered shiftHouseSeqForDelete method");

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
		Main._logger.debug("Entered shiftHouseSeqFromToForCustId method");
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

	@FXML
	private void resetClicked(ActionEvent event) {
		Main._logger.debug("Entered resetClicked method");
		System.out.println("resetClicked");
		addCustName.clear();
		addCustMobile.clear();
		addCustLineNum.getSelectionModel().clearSelection();

		addCustHouseSeq.clear();
		addCustHouseSeq.setDisable(true);
		addCustOldHouseNum.clear();
		addCustNewHouseNum.clear();
		addCustAddrLine1.clear();
		addCustAddrLine2.clear();
		addCustLocality.clear();
		addCustCity.clear();
		// addCustState.setValue("State");
		addCustProf1.getSelectionModel().clearSelection();
		addCustProf2.getSelectionModel().clearSelection();
		addCustProf3.clear();
		addCustInitials.clear();
		addCustEmployment.getSelectionModel().clearSelection();
		addCustComments.clear();
		addCustBuildingStreet.clear();
	}

	@FXML
	private void filterCustomersClicked(ActionEvent event) {

		Main._logger.debug("Entered filterCustomersClicked method");
		TextInputDialog customersFilterDialog = new TextInputDialog(searchText);
		customersFilterDialog.setTitle("Filter Customers");
		customersFilterDialog.setHeaderText("Enter the filter text");
		Optional<String> returnValue = customersFilterDialog.showAndWait();
		if (returnValue.isPresent()) {
			try {
				searchText = returnValue.get();
				filteredData.setPredicate(new Predicate<Customer>() {

					@Override
					public boolean test(Customer customer) {

						if (searchText == null)
							return true;
						else if (customer.getAddrLine1() != null
								&& customer.getAddrLine1().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getAddrLine2() != null
								&& customer.getAddrLine2().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getCity() != null
								&& customer.getCity().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getHawkerCode().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getLocality() != null
								&& customer.getLocality().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getName() != null
								&& customer.getName().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getName() != null
								&& customer.getName().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getNewHouseNum() != null
								&& customer.getNewHouseNum().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getOldHouseNum() != null
								&& customer.getOldHouseNum().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getState() != null
								&& customer.getState().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getProfile1() != null
								&& customer.getProfile1().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getProfile2() != null
								&& customer.getProfile2().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getProfile3() != null
								&& customer.getProfile3().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (customer.getInitials() != null
								&& customer.getInitials().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if ((customer.getCustomerCode() + "").toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if ((customer.getEmployment() + "").toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if ((customer.getComments() + "").toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if ((customer.getBuildingStreet() + "").toUpperCase().contains(searchText.toUpperCase()))
							return true;
						return false;

					}
				});

				SortedList<Customer> sortedData = new SortedList<>(filteredData);
				sortedData.comparatorProperty().bind(ACustInfoTable.comparatorProperty());
				ACustInfoTable.setItems(sortedData);
				ACustInfoTable.refresh();

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
		Main._logger.debug("Entered clearClicked method");
		filteredData = new FilteredList<>(customerMasterData, p -> true);
		SortedList<Customer> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(ACustInfoTable.comparatorProperty());
		ACustInfoTable.setItems(sortedData);
		ACustInfoTable.getSelectionModel().clearSelection();
	}

	public void populatePointNames() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						if (HawkerLoginController.loggedInHawker != null) {
							pointNameValues = FXCollections.observableArrayList();
							pointNameValues.add(HawkerLoginController.loggedInHawker.getPointName());
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									addPointName.setItems(pointNameValues);
									addPointName.getSelectionModel()
											.select(HawkerLoginController.loggedInHawker.getPointName());
									addPointName.setDisable(true);
								}
							});
						} else {
							pointNameValues = FXCollections.observableArrayList();
							PreparedStatement stmt = con.prepareStatement(
									"select distinct name from point_name where city =? order by name");
							stmt.setString(1, cityTF.getSelectionModel().getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if(pointNameValues!=null && !pointNameValues.contains(rs.getString(1)))
									pointNameValues.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									addPointName.getItems().clear();
									addPointName.setItems(pointNameValues);
									new AutoCompleteComboBoxListener<>(addPointName);
								}
							});
							rs.close();
							stmt.close();
						}
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

	private void populateProfileValues() {
		Main._logger.debug("Entered populateProfileValues method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					profileValues=FXCollections.observableArrayList();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='PROFILE_VALUES' order by seq");
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
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {

						addCustProf1.setItems(profileValues);
						addCustProf2.setItems(profileValues);
						new AutoCompleteComboBoxListener<>(addCustProf1);
						new AutoCompleteComboBoxListener<>(addCustProf2);
						
					}
				});
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateEmploymentValues() {
		Main._logger.debug("Entered populateEmploymentValues method");
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
							"select value, code, seq from lov_lookup where code='EMPLOYMENT_STATUS' order by seq");
					while (rs.next()) {
						employmentData.add(rs.getString(1));
					}

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {

						addCustEmployment.setItems(employmentData);
						new AutoCompleteComboBoxListener<>(addCustEmployment);
						
					}
				});
				return null;
			}

		};

		new Thread(task).start();

	}

	public boolean houseSequenceExistsInLine(String hawkerCode, int seq, Long lineNum) {
		Main._logger.debug("Entered houseSequenceExistsInLine method");
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

	private void addCustSubscription(Customer custRow) {
		Main._logger.debug("Entered addCustomerSubscription method");

		try {

			Dialog<String> addSubscriptionDialog = new Dialog<String>();
			addSubscriptionDialog.setTitle("Add subscription data");
			addSubscriptionDialog.setHeaderText("Add the subscription data below");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			addSubscriptionDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			FXMLLoader addSubscriptionLoader = new FXMLLoader(getClass().getResource("AddCustSubscription.fxml"));
			Parent addSubscriptionGrid = (Parent) addSubscriptionLoader.load();
			AddSubscriptionController addSubsController = addSubscriptionLoader
					.<AddSubscriptionController> getController();

			addSubscriptionDialog.getDialogPane().setContent(addSubscriptionGrid);
			addSubsController.setCustomer(ACustInfoTable.getSelectionModel().getSelectedItem());
			addSubsController.setupBindings();
			Button saveBtn = (Button) addSubscriptionDialog.getDialogPane().lookupButton(saveButtonType);

			saveBtn.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addSubsController.isValid()) {
					addSubsController.addSubscription();
				} else
					btnEvent.consume();
			});

			addSubscriptionDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {

					return null;
				}
				return null;
			});

			Optional<String> updatedSubscription = addSubscriptionDialog.showAndWait();
			// refreshCustomerTable();

			updatedSubscription.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					refreshSubscriptions();
				}
			});

		} catch (IOException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void refreshSubscriptions() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						disableAll();
						subscriptionMasterData = FXCollections.observableArrayList();
						String query = "select sub.SUBSCRIPTION_ID, sub.CUSTOMER_ID, sub.PRODUCT_ID, prod.name, prod.type, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.FREQUENCY, sub.TYPE, sub.DOW, sub.STATUS, sub.START_DATE, sub.PAUSED_DATE, prod.CODE, sub.STOP_DATE, sub.DURATION, sub.OFFER_MONTHS, sub.SUB_NUMBER, sub.resume_date, sub.ADD_TO_BILL, sub.cheque_rcvd from subscription sub, products prod where sub.PRODUCT_ID=prod.PRODUCT_ID and sub.customer_id =? order by sub.STATUS,prod.name";
						PreparedStatement stmt = con.prepareStatement(query);
						Customer cust = ACustInfoTable.getSelectionModel().getSelectedItem();
						stmt.setLong(1, cust != null ? cust.getCustomerId() : null);
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							subscriptionMasterData.add(new Subscription(rs.getLong(1), rs.getLong(2), rs.getLong(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getDouble(8),
									rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
									rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(),
									rs.getDate(14) == null ? null : rs.getDate(14).toLocalDate(), rs.getString(15),
									rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17),
									rs.getInt(18), rs.getString(19),
									rs.getDate(20) == null ? null : rs.getDate(20).toLocalDate(), rs.getDouble(21), rs.getString(22).equalsIgnoreCase("Y")));
						}
						rs.close();
						stmt.close();
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								subscriptionsTable.setItems(subscriptionMasterData);
								subscriptionsTable.refresh();
								if (!subscriptionMasterData.isEmpty())
									subscriptionsTable.getSelectionModel().selectFirst();
							}
						});
						enableAll();
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

	private void resumeStopHistoryForSub(Subscription subsRow, LocalDate stopDate, LocalDate resumeDate) {
		Main._logger.debug("Entered resumeStopHistoryForSub method");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}

			if (!stopDate.isEqual(resumeDate)) {
				String insertStmt = "update stop_history set resume_date=? where sub_id=? and stop_date=?";
				PreparedStatement stmt = con.prepareStatement(insertStmt);
				stmt.setLong(2, subsRow.getSubscriptionId());
				stmt.setDate(3, Date.valueOf(stopDate));
				stmt.setDate(1, resumeDate == null ? null : Date.valueOf(resumeDate));
				if (stmt.executeUpdate() > 0) {

					Notifications.create().hideAfter(Duration.seconds(5)).title("Stop History Updated")
							.text("Stop History record successfully closed").show();
				}
			} else {
				String insertStmt = "delete from stop_history where sub_id=? and stop_date=?";
				PreparedStatement stmt = con.prepareStatement(insertStmt);
				stmt.setLong(1, subsRow.getSubscriptionId());
				stmt.setDate(2, Date.valueOf(stopDate));
				if (stmt.executeUpdate() > 0) {

					Notifications.create().hideAfter(Duration.seconds(5)).title("Stop History Deleted")
							.text("Stop Date is same as resume date. Stop History record successfully deleted.")
							.showInformation();
				}
				// ArrayList<StopHistory> stpHist = new
				// ArrayList<StopHistory>();
				String query = "SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE STP.sub_id=? and STP.stop_date=? AND STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC";
				stmt = con.prepareStatement(query);
				stmt.setLong(1, subsRow.getSubscriptionId());
				stmt.setDate(2, Date.valueOf(stopDate));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					StopHistory stp = new StopHistory(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getString(4),
							rs.getString(5), rs.getLong(6), rs.getLong(7), rs.getInt(8), rs.getString(9),
							rs.getString(10), rs.getString(11),
							rs.getDate(12) == null ? null : rs.getDate(12).toLocalDate(),
							rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(), rs.getString(14),
							rs.getString(15), rs.getString(16), rs.getDouble(17));
					stp.setAmount(BillingUtilityClass.calculateStopHistoryAmount(stp));
					stp.updateStopHistoryRecord();
				}
			}

		} catch (SQLException e) {

			Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
					.text("Error in creation of stop history record").showError();
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void createStopHistoryForSub(Subscription subsRow, LocalDate stopDate, LocalDate resumeDate) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						subscriptionMasterData = FXCollections.observableArrayList();
						String insertStmt = "insert into stop_history(SUB_ID,STOP_DATE,RESUME_DATE) values(?,?,?)";
						PreparedStatement stmt = con.prepareStatement(insertStmt);
						stmt.setLong(1, subsRow.getSubscriptionId());
						stmt.setDate(2, Date.valueOf(stopDate));
						stmt.setDate(3, resumeDate == null ? null : Date.valueOf(resumeDate));
						stmt.executeUpdate();
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
										.text("Stop History record created").showInformation();
							}
						});
						stmt.close();

					} catch (SQLException e) {

						Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
								.text("Error in creation of stop history record").showError();
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

	public boolean stopEntryExistsForStartDate(Subscription subsRow, LocalDate stopDate) {
		Main._logger.debug("Entered stopEntryExistsForStartDate method");

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String countStmt = "select count(*) from stop_history where sub_id=? and (resume_date is null or sysdate between stop_date and resume_date-1)";
			PreparedStatement stmt = con.prepareStatement(countStmt);
			stmt.setLong(1, subsRow.getSubscriptionId());
			stmt.setDate(2, Date.valueOf(stopDate));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {

			Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
					.text("Error in creation of stop history record").showError();
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

		return false;
	}

	private void populateBillingLines(Billing bill) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						billingLinesData = FXCollections.observableArrayList();
						String insertStmt = "SELECT BILL_LINE_ID, BILL_INVOICE_NUM, LINE_NUM, PRODUCT, AMOUNT, TEA_EXPENSES FROM BILLING_LINES WHERE BILL_INVOICE_NUM=?";
						PreparedStatement stmt = con.prepareStatement(insertStmt);
						stmt.setLong(1, bill.getBillInvoiceNum());
						ResultSet rs = stmt.executeQuery();

						while (rs.next()) {
							billingLinesData.add(new BillingLine(rs.getLong(1), rs.getLong(2), rs.getInt(3),
									rs.getString(4), rs.getDouble(5), rs.getDouble(6)));
						}

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								billingTable.setItems(billingLinesData);
								billingTable.refresh();
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
				// billingTable.getItems().clear();
				return null;
			}

		};

		new Thread(task).start();
	}

	private void populateInvoiceDates(Customer customer) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						invoiceDatesData = FXCollections.observableArrayList();
						String insertStmt = "select BILL_INVOICE_NUM, CUSTOMER_ID , INVOICE_DATE, PDF_URL, DUE, MONTH from BILLING where customer_id=? order by invoice_date desc";
						PreparedStatement stmt = con.prepareStatement(insertStmt);
						stmt.setLong(1, customer.getCustomerId());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							invoiceDatesData.add(new Billing(rs.getLong(1), rs.getLong(2), rs.getDate(3).toLocalDate(),
									rs.getString(4), rs.getDouble(5), rs.getString(6)));
						}
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								invoiceDateLOV.getItems().clear();
								invoiceDateLOV.setItems(invoiceDatesData);
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
							rs.close();
						} else {
							stmt = con.prepareStatement("select distinct city from point_name order by city");
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if(cityValues!=null && !cityValues.contains(rs.getString(1)))
										cityValues.add(rs.getString(1));
							}

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									cityTF.getItems().clear();
									cityTF.setItems(cityValues);
									new AutoCompleteComboBoxListener<>(cityTF);
								}
							});
							rs.close();
						}
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

	private void showViewSubscriptionDialog(long subsId) {
		try {

			Dialog<Subscription> editSubscriptionDialog = new Dialog<Subscription>();
			editSubscriptionDialog.setTitle("View subscription data");
			editSubscriptionDialog.setHeaderText("View the subscription data below");

			// Set the button types.

			editSubscriptionDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editSubscriptionLoader = new FXMLLoader(getClass().getResource("EditSubscription.fxml"));
			Parent editSubscriptionGrid = (Parent) editSubscriptionLoader.load();
			EditSubscriptionController editSubsController = editSubscriptionLoader
					.<EditSubscriptionController> getController();

			editSubscriptionDialog.getDialogPane().setContent(editSubscriptionGrid);
			editSubsController.setPausedSubs(subsId);
			editSubsController.setupBindings();
			editSubsController.gridPane.setDisable(true);

			Optional<Subscription> updatedSubscription = editSubscriptionDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void showViewProductDialog(Product productRow) {
		try {

			Dialog<Product> editProductDialog = new Dialog<Product>();
			editProductDialog.setTitle("View Product data");
			editProductDialog.setHeaderText("View the Product data below");

			editProductDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editProductLoader = new FXMLLoader(getClass().getResource("EditProducts.fxml"));
			Parent editProductsGrid = (Parent) editProductLoader.load();
			EditProductsController editProductsController = editProductLoader.<EditProductsController> getController();

			editProductDialog.getDialogPane().setContent(editProductsGrid);
			editProductsController.setProduct(productRow);
			editProductsController.setupBindings();
			editProductsController.gridPane.setDisable(true);
			Optional<Product> updatedProduct = editProductDialog.showAndWait();

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void refreshStopHistory() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						// stopHistoryTable.getItems().clear();
						stopHistoryMasterData = FXCollections.observableArrayList();
						String query = "SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE CUST.CUSTOMER_ID=? AND STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC";
						PreparedStatement stmt = con.prepareStatement(query);
						stmt.setLong(1, ACustInfoTable.getSelectionModel().getSelectedItem().getCustomerId());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							stopHistoryMasterData.add(new StopHistory(rs.getLong(1), rs.getString(2), rs.getLong(3),
									rs.getString(4), rs.getString(5), rs.getLong(6), rs.getLong(7), rs.getInt(8),
									rs.getString(9), rs.getString(10), rs.getString(11),
									rs.getDate(12) == null ? null : rs.getDate(12).toLocalDate(),
									rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(), rs.getString(14),
									rs.getString(15), rs.getString(16), rs.getDouble(17)));
						}

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								stopHistoryTable.setItems(stopHistoryMasterData);
								stopHistoryTable.refresh();
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

	private void refreshStopHistoryBkp() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						stopHistoryBkpMasterData = FXCollections.observableArrayList();
						String query = "SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY_BKP STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE CUST.CUSTOMER_ID=? AND STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC";
						PreparedStatement stmt = con.prepareStatement(query);
						stmt.setLong(1, ACustInfoTable.getSelectionModel().getSelectedItem().getCustomerId());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							stopHistoryBkpMasterData.add(new StopHistoryBackup(rs.getLong(1), rs.getString(2),
									rs.getLong(3), rs.getString(4), rs.getString(5), rs.getLong(6), rs.getLong(7),
									rs.getInt(8), rs.getString(9), rs.getString(10), rs.getString(11),
									rs.getDate(12) == null ? null : rs.getDate(12).toLocalDate(),
									rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(), rs.getString(14),
									rs.getString(15), rs.getString(16), rs.getDouble(17)));
						}

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								stopHistoryBkpTable.setItems(stopHistoryBkpMasterData);
								stopHistoryBkpTable.refresh();
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

	private ObservableList<Subscription> getActiveSubsListForCust(Customer custRow) {
		ObservableList<Subscription> subsList = FXCollections.observableArrayList();
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			subsList = FXCollections.observableArrayList();
			String query = "select sub.SUBSCRIPTION_ID, sub.CUSTOMER_ID, sub.PRODUCT_ID, prod.name, prod.type, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.FREQUENCY, sub.TYPE, sub.DOW, sub.STATUS, sub.START_DATE, sub.PAUSED_DATE, prod.code, sub.STOP_DATE, sub.DURATION, sub.OFFER_MONTHS, sub.SUB_NUMBER, sub.resume_date, sub.ADD_TO_BILL, sub.cheque_rcvd from subscription sub, products prod where sub.PRODUCT_ID=prod.PRODUCT_ID and sub.customer_id =? and sub.status='Active' order by sub.subscription_id";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setLong(1, custRow.getCustomerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				subsList.add(new Subscription(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getDouble(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12),
						rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(),
						rs.getDate(14) == null ? null : rs.getDate(14).toLocalDate(), rs.getString(15),
						rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17), rs.getInt(18),
						rs.getString(19), rs.getDate(20) == null ? null : rs.getDate(20).toLocalDate(),
						rs.getDouble(21), rs.getString(22).equalsIgnoreCase("Y")));
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
		return subsList;
	}

	private ObservableList<Subscription> getPausedSubsListForCust(Customer custRow) {
		ObservableList<Subscription> subsList = FXCollections.observableArrayList();
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			subsList = FXCollections.observableArrayList();
			String query = "select sub.SUBSCRIPTION_ID, sub.CUSTOMER_ID, sub.PRODUCT_ID, prod.name, prod.type, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.FREQUENCY, sub.TYPE, sub.DOW, sub.STATUS, sub.START_DATE, sub.PAUSED_DATE, prod.code, sub.STOP_DATE, sub.DURATION, sub.OFFER_MONTHS, sub.SUB_NUMBER, sub.resume_date, sub.ADD_TO_BILL, sub.cheque_rcvd from subscription sub, products prod where sub.PRODUCT_ID=prod.PRODUCT_ID and sub.customer_id =? and sub.status='Stopped' order by sub.subscription_id";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setLong(1, custRow.getCustomerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				subsList.add(new Subscription(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getDouble(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12),
						rs.getDate(13) == null ? null : rs.getDate(13).toLocalDate(),
						rs.getDate(14) == null ? null : rs.getDate(14).toLocalDate(), rs.getString(15),
						rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17), rs.getInt(18),
						rs.getString(19), rs.getDate(20) == null ? null : rs.getDate(20).toLocalDate(),
						rs.getDouble(21),rs.getString(22).equalsIgnoreCase("Y")));
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
		return subsList;
	}

	private int subsPostCount(Subscription subsRow, String status) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con.prepareStatement(
					"select count(1) from subscription where customer_id=? and subscription_id<>? and status=? and PRODUCT_ID=? ");
			stmt.setLong(1, ACustInfoTable.getSelectionModel().getSelectedItem().getCustomerId());
			stmt.setLong(2, subsRow.getSubscriptionId());
			stmt.setString(3, status);
			stmt.setLong(4, subsRow.getProductId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
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
		return 1;
	}

	private void disableAll() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					ACustInfoTable.setDisable(true);
					subscriptionsTable.setDisable(true);
					invoiceDateLOV.setDisable(true);
					billingTable.setDisable(true);
					stopHistoryTable.setDisable(true);
					stopHistoryBkpTable.setDisable(true);

				}
			});
		} else {
			ACustInfoTable.setDisable(true);
			subscriptionsTable.setDisable(true);
			invoiceDateLOV.setDisable(true);
			billingTable.setDisable(true);
			stopHistoryTable.setDisable(true);
			stopHistoryBkpTable.setDisable(true);
		}
	}

	private void enableAll() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// Notifications.create().title("Please wait").text("Please
					// wait till process
					// finishes").hideAfter(Duration.seconds(5)).showInformation();
					ACustInfoTable.setDisable(false);
					subscriptionsTable.setDisable(false);
					invoiceDateLOV.setDisable(false);
					billingTable.setDisable(false);
					stopHistoryTable.setDisable(false);
					stopHistoryBkpTable.setDisable(false);
				}
			});
		} else {
			ACustInfoTable.setDisable(false);
			subscriptionsTable.setDisable(false);
			invoiceDateLOV.setDisable(false);
			billingTable.setDisable(false);
			stopHistoryTable.setDisable(false);
			stopHistoryBkpTable.setDisable(false);
		}
	}

	// @Override
	public void reloadData() {
		Main._logger.debug("Entered reloadData method");

		// populatePointNames();
		if (HawkerLoginController.loggedInHawker == null) {
			if (showAllRadioButton.isSelected()) {
				cityTF.setDisable(true);
				addPointName.getSelectionModel().clearSelection();
				addPointName.setDisable(true);
				addCustHwkCode.getSelectionModel().clearSelection();
				addCustHwkCode.setDisable(true);
				refreshCustomerTable();
			} else if (filterRadioButton.isSelected()) {
				populateCityValues();
				cityTF.setDisable(false);
				addPointName.setDisable(false);
				addCustHwkCode.setDisable(false);
				if (addCustHwkCode.getSelectionModel().getSelectedItem() == null) {
					customerMasterData.clear();
					ACustInfoTable.setItems(customerMasterData);
				}
				// refreshCustomerTable();
			}
		} else {

			populateCityValues();
		}

		subscriptionMasterData.clear();
		subscriptionsTable.setItems(subscriptionMasterData);
		stopHistoryMasterData.clear();
		stopHistoryTable.setItems(stopHistoryMasterData);
		stopHistoryBkpMasterData.clear();
		stopHistoryBkpTable.setItems(stopHistoryBkpMasterData);
		addCustLineNum.getSelectionModel().clearSelection();
		invoiceDateLOV.getItems().clear();
		billingLinesData.clear();
		billingTable.setItems(billingLinesData);
		populateProfileValues();
		populateEmploymentValues();
		

	}

	// @Override
	public void releaseVariables() {
		Main._logger.debug("Entered releaseVariables method");
		customerMasterData = null;
		subscriptionMasterData = null;
		hawkerCodeData = null;
		hawkerLineNumData = null;
		employmentData = null;
		profileValues = null;
		pointNameValues = null;
		invoiceDatesData = null;
		billingLinesData = null;
		cityValues = null;
		stopHistoryBkpMasterData = null;
		stopHistoryMasterData = null;
		stopHistoryBkpMasterData = FXCollections.observableArrayList();
		stopHistoryMasterData = FXCollections.observableArrayList();
		customerMasterData = FXCollections.observableArrayList();
		subscriptionMasterData = FXCollections.observableArrayList();
		hawkerCodeData = FXCollections.observableArrayList();
		hawkerLineNumData = FXCollections.observableArrayList();
		employmentData = FXCollections.observableArrayList();
		profileValues = FXCollections.observableArrayList();
		pointNameValues = FXCollections.observableArrayList();
		invoiceDatesData = FXCollections.observableArrayList();
		billingLinesData = FXCollections.observableArrayList();
		cityValues = FXCollections.observableArrayList();
	}

}
