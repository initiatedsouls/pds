package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;
import org.jpedal.examples.viewer.OpenViewerFX;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.util.Base64;
import com.amazonaws.util.NumberUtils;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdfViewerFX.PDFViewer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;

public class ALineInfoTabController implements Initializable {

	@FXML
	private TableView<LineInfo> lineNumTable;
	@FXML
	private TextField addLineNumField;
	@FXML
	private TableView<Customer> lineNumCustomersTable;
	@FXML
	private ComboBox<String> hawkerComboBox;
	@FXML
	private ComboBox<String> addPointName;

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private Button addCustExtraButton;
	@FXML
	private Button showCounts;
	// Columns
	@FXML
	private TableColumn<LineInfo, String> lineNumColumn;
	@FXML
	private TableColumn<Customer, Long> customerIDColumn;
	@FXML
	private TableColumn<Customer, String> customerNameColumn;
	@FXML
	private TableColumn<Customer, String> mobileNumColumn;
	@FXML
	private TableColumn<Customer, Integer> houseSeqColumn;
	@FXML
	private TableColumn<Customer, String> flatNameColumn;

	@FXML
	private TableColumn<Customer, String> line1Column;
	@FXML
	private TableColumn<Customer, Double> totalDueColumn;

	@FXML
	private TableColumn<Customer, String> line2Column;

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
	private TableColumn<Subscription, Boolean> subsChequeReceivedColumn;
	@FXML
	private CheckBox extendSubscription;
	@FXML
	private Button addLineButton;
	@FXML
	private Button shuffleButton;
	@FXML
	private Label hawkerNameLabel;
	@FXML
	private Label hawkerMobLabel;

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

	private ObservableList<Billing> invoiceDatesData = FXCollections.observableArrayList();
	private ObservableList<BillingLine> billingLinesData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<LineInfo> lineNumData = FXCollections.observableArrayList();
	private ObservableList<Customer> customerData = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<Subscription> subscriptionMasterData = FXCollections.observableArrayList();
	private ObservableList<StopHistory> stopHistoryMasterData = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();
	private ObservableList<StopHistoryBackup> stopHistoryBkpMasterData = FXCollections.observableArrayList();

	private ArrayList<TextField> newHouseSeqTFArray;

	@FXML
	private TitledPane customerTitledPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("Entered HLineInfoTabController");

		lineNumColumn.setCellValueFactory(new PropertyValueFactory<LineInfo, String>("lineNumDist"));
		lineNumTable.setDisable(true);
		addLineNumField.setDisable(true);

		customerIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("customerCode"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		mobileNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("mobileNum"));
		flatNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("buildingStreet"));
		line1Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine1"));
		line2Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine2"));
		houseSeqColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("houseSeq"));
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
		subsChequeReceivedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(subsChequeReceivedColumn));
		subsChequeReceivedColumn.setCellValueFactory(param -> param.getValue().isChequeReceived());

		prodCol.setCellValueFactory(new PropertyValueFactory<BillingLine, String>("product"));
		amountCol.setCellValueFactory(new PropertyValueFactory<BillingLine, Double>("amount"));
		teaExpensesCol.setCellValueFactory(new PropertyValueFactory<BillingLine, Double>("teaExpenses"));
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

		addPointName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateHawkerCodes();
				// hawkerComboBox.getItems().clear();
				// hawkerComboBox.getItems().addAll(hawkerCodeData);
			}
		});

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

		hawkerComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				hawkerMobLabel.setText("");
				hawkerNameLabel.setText("");
				if (newValue != null) {
					hawkerNameMobCode(newValue);
					lineNumTable.setDisable(false);
					addLineNumField.setDisable(false);
					refreshLineNumTableForHawker(newValue);
				} else {
					lineNumTable.getItems().clear();
					lineNumTable.refresh();
					lineNumData.clear();
				}
				subscriptionMasterData.clear();
				subscriptionsTable.getItems().clear();
				subscriptionsTable.refresh();
				customerData.clear();
				lineNumCustomersTable.getItems().clear();
				lineNumCustomersTable.refresh();
				invoiceDateLOV.getItems().clear();
				billingLinesData.clear();
				billingTable.setItems(billingLinesData);
				billingTable.refresh();
				stopHistoryTable.getItems().clear();
				stopHistoryTable.refresh();

			}

		});

		lineNumTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LineInfo>() {

			@Override
			public void changed(ObservableValue<? extends LineInfo> observable, LineInfo oldValue, LineInfo newValue) {
				if (newValue != null) {
					// customerListForLineFunction();
					customerData.clear();
					lineNumCustomersTable.setItems(customerData);
					stopHistoryMasterData.clear();
					stopHistoryTable.setItems(stopHistoryMasterData);
					invoiceDatesData.clear();
					invoiceDateLOV.setItems(invoiceDatesData);
					subscriptionMasterData.clear();
					subscriptionsTable.setItems(subscriptionMasterData);
					populateCustomersForLine();
					// refreshStopHistory();
				}
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

		addLineButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addLineButtonClicked(new ActionEvent());
				}
			}
		});

		shuffleButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					shuffleHouseSeqClicked(new ActionEvent());
				}
			}
		});

		lineNumTable.setRowFactory(new Callback<TableView<LineInfo>, TableRow<LineInfo>>() {

			@Override
			public TableRow<LineInfo> call(TableView<LineInfo> param) {
				final TableRow<LineInfo> row = new TableRow<>();

				MenuItem mnuDel = new MenuItem("Delete line");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							deleteLine(lineRow);
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit line number");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							if (lineRow.getLineNum() > 0) {
								showEditLineDialog(lineRow);
								lineNumTable.refresh();
							} else {
								Notifications.create().title("Cannot edit line number 0")
										.text("Cannot edit line number 0").hideAfter(Duration.seconds(5)).showError();
							}
						}
					}

				});

				MenuItem mnuPDF = new MenuItem("Download Invoice PDF");
				mnuPDF.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							if (lineRow.getLineNum() > 0) {
								Dialog<ButtonType> dateListDialog = new Dialog<ButtonType>();
								dateListDialog.setTitle("Download PDF");
								dateListDialog.setHeaderText(
										"Please select the invoice date for which PDF should be created.");
								ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
								dateListDialog.getDialogPane().getButtonTypes().addAll(saveButtonType,
										ButtonType.CANCEL);
								GridPane grid = new GridPane();
								grid.setHgap(10);
								grid.setVgap(10);
								grid.setPadding(new Insets(20, 150, 10, 10));
								ObservableList<LocalDate> dateList = getInvoiceDateListForLine(lineRow);
								Label dateLabel = new Label("Invoice Date: ");
								ComboBox<LocalDate> dateBox = new ComboBox<>(dateList);
								dateBox.setConverter(Main.dateConvertor);
								dateBox.getSelectionModel().selectFirst();
								grid.add(dateLabel, 0, 0);
								grid.add(dateBox, 1, 0);
								dateListDialog.getDialogPane().setContent(grid);
								Button yesButton = (Button) dateListDialog.getDialogPane().lookupButton(saveButtonType);
								yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {

								});
								Optional<ButtonType> result = dateListDialog.showAndWait();
								if (result.isPresent() && result.get() == saveButtonType) {
									try {
										Notifications.create().title("Generating Invoice PDF")
												.text("Generating Invoice PDF for selected line. Please wait...")
												.hideAfter(Duration.seconds(10)).showInformation();
										File pdfFile = BillingUtilityClass.generateInvoiceADVPDF(
												hawkerComboBox.getSelectionModel().getSelectedItem(),
												lineRow.getLineNum(), dateBox.getSelectionModel().getSelectedItem()
														.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
										// openPDFWindow(pdfFile);
									} catch (JRException e) {
										Main._logger.debug(e.getMessage());
									}
								}

							} else {
								Notifications.create().title("Cannot generate PDF for line number 0")
										.text("Cannot generate PDF for line number 0").hideAfter(Duration.seconds(5))
										.showError();
							}
						}
					}

				});

				MenuItem mnuSummaryPDF = new MenuItem("Download Invoice Summary PDF");
				mnuSummaryPDF.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							if (lineRow.getLineNum() > 0) {
								Dialog<ButtonType> dateListDialog = new Dialog<ButtonType>();
								dateListDialog.setTitle("Download Invoice Summary PDF");
								dateListDialog.setHeaderText(
										"Please select the invoice date for which Summary PDF should be created.");
								ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
								dateListDialog.getDialogPane().getButtonTypes().addAll(saveButtonType,
										ButtonType.CANCEL);
								GridPane grid = new GridPane();
								grid.setHgap(10);
								grid.setVgap(10);
								grid.setPadding(new Insets(20, 150, 10, 10));
								ObservableList<LocalDate> dateList = getInvoiceDateListForLine(lineRow);
								Label dateLabel = new Label("Invoice Date: ");
								ComboBox<LocalDate> dateBox = new ComboBox<>(dateList);
								dateBox.setConverter(Main.dateConvertor);
								dateBox.getSelectionModel().selectFirst();
								grid.add(dateLabel, 0, 0);
								grid.add(dateBox, 1, 0);
								dateListDialog.getDialogPane().setContent(grid);
								Button yesButton = (Button) dateListDialog.getDialogPane().lookupButton(saveButtonType);
								yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {

								});
								Optional<ButtonType> result = dateListDialog.showAndWait();
								if (result.isPresent() && result.get() == saveButtonType) {
									try {
										Notifications.create().title("Generating Invoice PDF")
												.text("Generating Invoice PDF for selected line. Please wait...")
												.hideAfter(Duration.seconds(10)).showInformation();
										File pdfFile = BillingUtilityClass.generateInvoiceSummaryPDF(
												hawkerComboBox.getSelectionModel().getSelectedItem(),
												lineRow.getLineNum(), dateBox.getSelectionModel().getSelectedItem()
														.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
										// openPDFWindow(pdfFile);
									} catch (JRException e) {
										Main._logger.debug(e.getMessage());
									}
								}

							} else {
								Notifications.create().title("Cannot generate PDF for line number 0")
										.text("Cannot generate PDF for line number 0").hideAfter(Duration.seconds(5))
										.showError();
							}
						}
					}

				});
				MenuItem mnuBill = new MenuItem("Regenerate Invoices");
				mnuBill.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumData.get(lineNumTable.getSelectionModel().getSelectedIndex());
						if (lineRow != null) {
							if (lineRow.getLineNum() > 0) {
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
								if (result.isPresent() && result.get() == saveButtonType) {
									Task<Void> task = new Task<Void>() {
										Notifications n = null;

										@Override
										protected Void call() throws Exception {
											Platform.runLater(new Runnable() {

												@Override
												public void run() {
													n = Notifications.create().title("Please wait").text(
															"Generating customer bills. Please wait for the process to finish.");
													n.hideAfter(Duration.seconds(10)).position(Pos.CENTER)
															.showInformation();
												}
											});
											disableAll();
											generateBillingFunction(
													hawkerComboBox.getSelectionModel().getSelectedItem(),
													((Integer) lineRow.getLineNum()).toString(), pauseDP.getValue()
															.withDayOfMonth(1).plusMonths(1).minusDays(1).toString());
											// BillingUtilityClass.createBillingInvoiceForHwkLine(
											// hawkerComboBox.getSelectionModel().getSelectedItem(),
											// lineRow.getLineNum(),
											// pauseDP.getValue().withDayOfMonth(1),
											// pauseDP.getValue().withDayOfMonth(1).plusMonths(1).minusDays(1),
											// true);

											populateCustomersForLine();
											Platform.runLater(new Runnable() {

												@Override
												public void run() {

													Notifications.create().title("Bill generated")
															.text("Customer Bills for selected Line are generated")
															.hideAfter(Duration.seconds(5)).position(Pos.CENTER)
															.showInformation();
													enableAll();
												}
											});

											return null;
										}

									};

									new Thread(task).start();
								}
							} else {
								Notifications.create().title("Line Number 0")
										.text("Cannot generate invoices for line number 0")
										.hideAfter(Duration.seconds(5)).showError();
							}
						}
					}

				});

				MenuItem mnuTransfer = new MenuItem("Transfer Line");
				mnuTransfer.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							if (lineRow.getLineNum() > 0) {
								Dialog<ButtonType> billWarning = new Dialog<ButtonType>();
								billWarning.setTitle("Select Hawker");
								billWarning.setHeaderText("Please select the new Hawker for this line.");
								ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
								billWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
								Button saveButton = (Button) billWarning.getDialogPane().lookupButton(saveButtonType);
								GridPane grid = new GridPane();
								grid.setHgap(10);
								grid.setVgap(10);
								grid.setPadding(new Insets(20, 150, 10, 10));
								ComboBox<String> cityCombo = new ComboBox<>(cityValues);
								cityCombo.getSelectionModel().select(cityTF.getSelectionModel().getSelectedItem());
								cityCombo.setDisable(true);
								ComboBox<String> pointCombo = new ComboBox<>(pointNameValues);
								pointCombo.getSelectionModel()
										.select(addPointName.getSelectionModel().getSelectedItem());
								pointCombo.setDisable(true);
								ComboBox<String> hawkerCombo = new ComboBox<>(hawkerCodeData);
								hawkerCombo.getSelectionModel()
										.select(hawkerComboBox.getSelectionModel().getSelectedItem());
								grid.add(new Label("City"), 0, 0);
								grid.add(cityCombo, 1, 0);
								grid.add(new Label("Point"), 0, 1);
								grid.add(pointCombo, 1, 1);
								grid.add(new Label("Hawker"), 0, 2);
								grid.add(hawkerCombo, 1, 2);

								billWarning.getDialogPane().setContent(grid);

								saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
									if (hawkerCombo.getSelectionModel().getSelectedItem()
											.equals(hawkerComboBox.getSelectionModel().getSelectedItem())) {
										Notifications.create().title("Invalid Hawker selection")
												.text("Please select a different hawker").hideAfter(Duration.seconds(5))
												.showError();
										btnEvent.consume();
									}
								});
								Optional<ButtonType> result = billWarning.showAndWait();
								if (result.isPresent() && result.get() == saveButtonType) {
									Task<Void> task = new Task<Void>() {

										@Override
										protected Void call() throws Exception {
											transferLine(lineRow, hawkerCombo.getSelectionModel().getSelectedItem());

											Platform.runLater(new Runnable() {

												@Override
												public void run() {

													Notifications.create().title("Line Transferred")
															.text("Line is transferred to selected hawker along with Customer and Line Distributor")
															.hideAfter(Duration.seconds(5)).position(Pos.CENTER)
															.showInformation();
												}
											});
											return null;
										}

									};

									new Thread(task).start();
								}
								lineNumTable.refresh();
							} else {
								Notifications.create().title("Cannot transfer line number 0")
										.text("Cannot transfer line number 0").hideAfter(Duration.seconds(5))
										.showError();
							}
						}
					}

				});

				MenuItem mnuAdd = new MenuItem("Add Customer");
				mnuAdd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							addCustomerExtraScreenClicked(t);
							lineNumCustomersTable.refresh();

						}
					}

				});

				MenuItem mnuShuffle = new MenuItem("Shuffle House Sequence");
				mnuShuffle.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						LineInfo lineRow = lineNumTable.getSelectionModel().getSelectedItem();
						if (lineRow != null) {
							shuffleHouseSeqClicked(t);

						}
					}

				});
				ContextMenu menu = new ContextMenu();
				if (HawkerLoginController.loggedInHawker != null) {
					menu.getItems().addAll(mnuEdit, mnuBill, mnuPDF, mnuSummaryPDF, mnuAdd, mnuShuffle);
				} else {
					menu.getItems().addAll(mnuEdit, mnuDel, mnuBill, mnuPDF, mnuSummaryPDF, mnuTransfer, mnuAdd,
							mnuShuffle);
				}
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		addLineNumField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addLineButtonClicked(new ActionEvent());
				}

			}
		});

		lineNumCustomersTable.setRowFactory(new Callback<TableView<Customer>, TableRow<Customer>>() {

			@Override
			public TableRow<Customer> call(TableView<Customer> param) {
				final TableRow<Customer> row = new TableRow<Customer>();
				MenuItem mnuDel = new MenuItem("Delete customer");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							deleteCustomer(custRow);
							lineNumCustomersTable.refresh();
						}
					}

				});
				MenuItem mnuEdit = new MenuItem("Edit customer");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							showEditCustomerDialog(custRow);
							// populateCustomersForLine();
						}
					}

				});

				MenuItem mnuView = new MenuItem("View customer");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							showViewCustomerDialog(custRow);
							// populateCustomersForLine();
						}
					}

				});
				MenuItem mnuSubs = new MenuItem("Add subscription");
				mnuSubs.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
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
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();

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
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();

						Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
						deleteWarning.setTitle("Resume Subscriptions");
						deleteWarning.setHeaderText("Are you sure you want to RESUME this subscription?");
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
									LocalDate maxStopDate = findMaxStopDateForSub(subsRow);
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

				MenuItem mnuPDF = new MenuItem("Download Invoice PDF");
				mnuPDF.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							if (custRow.getLineNum() > 0) {
								Dialog<ButtonType> dateListDialog = new Dialog<ButtonType>();
								dateListDialog.setTitle("Download PDF");
								dateListDialog.setHeaderText(
										"Please select the invoice date for which PDF should be created.");
								ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
								dateListDialog.getDialogPane().getButtonTypes().addAll(saveButtonType,
										ButtonType.CANCEL);
								GridPane grid = new GridPane();
								grid.setHgap(10);
								grid.setVgap(10);
								grid.setPadding(new Insets(20, 150, 10, 10));
								ObservableList<LocalDate> dateList = getInvoiceDateListForCust(custRow);
								Label dateLabel = new Label("Invoice Date: ");
								ComboBox<LocalDate> dateBox = new ComboBox<>(dateList);
								dateBox.setConverter(Main.dateConvertor);
								dateBox.getSelectionModel().selectFirst();
								grid.add(dateLabel, 0, 0);
								grid.add(dateBox, 1, 0);
								dateListDialog.getDialogPane().setContent(grid);
								Button yesButton = (Button) dateListDialog.getDialogPane().lookupButton(saveButtonType);
								yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {

								});
								Optional<ButtonType> result = dateListDialog.showAndWait();
								if (result.isPresent() && result.get() == saveButtonType) {
									try {
										Notifications.create().title("Generating Invoice PDF")
												.text("Generating Invoice PDF for selected line. Please wait...")
												.hideAfter(Duration.seconds(10)).showInformation();
										File pdfFile = BillingUtilityClass.generateInvoiceADVPDFCust(
												hawkerComboBox.getSelectionModel().getSelectedItem(),
												custRow.getLineNum().intValue(), custRow,
												dateBox.getSelectionModel().getSelectedItem()
														.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
										// openPDFWindow(pdfFile);
									} catch (JRException e) {
										Main._logger.debug(e.getMessage());
									}
								}

							} else {
								Notifications.create().title("Cannot generate PDF for line number 0")
										.text("Cannot generate PDF for line number 0").hideAfter(Duration.seconds(5))
										.showError();
							}
						}
					}

				});

				MenuItem mnuBill = new MenuItem("Regenerate Invoice");
				mnuBill.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
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
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							TextInputDialog dialog = new TextInputDialog();

							dialog.setTitle("Change total due value");
							dialog.setHeaderText("Change total due value below");

							Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
							dialog.getEditor().setText(custRow.getTotalDue() + "");
							saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

								try {
									Double d = Double.parseDouble(dialog.getEditor().getText());
									custRow.setTotalDue(d);
									custRow.updateCustomerRecord();
									populateCustomersForLine();
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
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
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
									populateCustomersForLine();
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
					menu.getItems().addAll(mnuEdit, mnuView, mnuDel, mnuSubs, mnuPause, mnuResume, mnuBill, mnuPDF,
							mnuAddDue, mnuDue);
				} else {
					menu.getItems().addAll(mnuEdit, mnuView, mnuDel, mnuSubs, mnuPause, mnuResume, mnuBill, mnuPDF,
							mnuAddDue, mnuDue);
				}
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		lineNumCustomersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {

			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
				if (newValue != null) {
					// subsListForCustFunction();
					// stpHistoryistForCustFunction();
					// billingListForCustFunction();
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
							// if (HawkerLoginController.loggedInHawker != null
							// && subscriptionMasterData.size() > 1) {
							// Notifications.create().title("Delete not
							// allowed")
							// .text("Delete not allowed if customer has more
							// than one subscription")
							// .hideAfter(Duration.seconds(10)).showError();
							// } else
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
							showEditSubscriptionDialog(subsRow, false);
							// refreshSubscriptions();
						}
					}

				});

				MenuItem mnuChequeRcvd = new MenuItem("Mark Checque Received");
				mnuChequeRcvd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
//							showEditSubscriptionDialog(subsRow, false);
							subsRow.setChequeRcvd(true);
							subsRow.updateSubscriptionRecord();
							Notifications.create().title("Successful").text("Subscription is marked Cheque Received.")
							.hideAfter(Duration.seconds(5)).show(); 
							refreshSubscriptions();
							 
						}
					}

				});

				MenuItem mnuExtend = new MenuItem("Extend Coupon Copy Subscription");
				mnuExtend.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Subscription subsRow = subscriptionsTable.getSelectionModel().getSelectedItem();
						if (subsRow != null) {
							if ("Coupon Copy/Adv Payment".equalsIgnoreCase(subsRow.getSubscriptionType())) {
								showEditSubscriptionDialog(subsRow, true);
								if (subsRow.getStatus().equalsIgnoreCase("Stopped")) {
									resumeStopHistoryForSub(subsRow, subsRow.getPausedDate(), subsRow.getStartDate());
									subsRow.resumeSubscription();
									populateCustomersForLine();
								}
								// refreshSubscriptions();
							} else {
								Notifications.create().title("Please select valid subscription")
										.text("Please select a subscription of type Coupon Copy/Adv Payment only")
										.hideAfter(Duration.seconds(5)).showError();
							}
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
								LocalDate maxStopDate = findMaxStopDateForSub(subsRow);
								if (maxStopDate.isBefore(resumeDP.getValue()) || maxStopDate.isEqual(resumeDP.getValue())) {
									if (resumeDP.getValue().isBefore(maxStopDate.plusMonths(1).withDayOfMonth(1))) {
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
												.text("Resume date must be in the same month as Stop History latest stop date.")
												.hideAfter(Duration.seconds(5)).showError();
									}
								} else {

									Notifications.create().title("Invalid Resume Date")
											.text("Resume date must be same or after stop date").hideAfter(Duration.seconds(5))
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
					menu.getItems().addAll(mnuPause, mnuResume, mnuEdit, mnuView, mnuViewProd, mnuDel, mnuExtend, mnuChequeRcvd);
				} else {
					menu.getItems().addAll(mnuPause, mnuResume, mnuEdit, mnuView, mnuViewProd, mnuDel, mnuExtend, mnuChequeRcvd);
				}

				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
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

					totalDueLabel.setText(newValue.getDue() + "");
					totalBillLabel.setText(Double.toString(d));
					netBillLabel.setText(Double.toString(net));
					monthLabel.setText(month);
					// billingLineListForBillFunction();
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

		// customerTitledPane.setExpanded(true);

		// TextFields.bindAutoCompletion(cityTF.getEditor(), cityTF.getItems());
		// TextFields.bindAutoCompletion(addPointName.getEditor(),
		// pointNameValues);
		// TextFields.bindAutoCompletion(hawkerComboBox.getEditor(),
		// hawkerCodeData);
	}

	private void hawkerNameMobCode(String hawkerCode) {

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

	private void deleteSubscription(Subscription subsRow) {
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

	private void showEditSubscriptionDialog(Subscription subsRow, boolean extendMode) {
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
			editSubsController.setSubscriptionToEdit(subsRow, extendMode);
			editSubsController.setupBindings();
			Button saveBtn = (Button) editSubscriptionDialog.getDialogPane().lookupButton(saveButtonType);
			saveBtn.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (editSubsController.isValid()) {
					editSubsController.updateSubscriptionRecord();
					refreshSubscriptions();

				} else
					btnEvent.consume();
			});
			editSubscriptionDialog.setResultConverter(dialogButton -> {

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
			editSubsController.setSubscriptionToEdit(subsRow, false);
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
						Customer cust = lineNumCustomersTable.getSelectionModel().getSelectedItem();
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
									rs.getDate(20) == null ? null : rs.getDate(20).toLocalDate(), rs.getDouble(21),
									rs.getString(22).equalsIgnoreCase("Y")));
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

	private void showEditLineDialog(LineInfo lineRow) {

		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Edit line number");
		dialog.setHeaderText("Enter new line number below");

		// dialog.getDialogPane().getButtonTypes().addAll(saveButton,
		// ButtonType.CANCEL);
		Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

		saveBtn.addEventFilter(ActionEvent.ACTION, event -> {
			try {
				Integer newLineNum = Integer.parseInt(dialog.getEditor().getText());

				if (checkExistingLineNum(newLineNum)) {
					lineRow.setLineNum(newLineNum);
					lineRow.updateLineNumRecord();
					updateLineNumForCust(newLineNum, customerData);
					updateLineNumForDist(lineRow);
					refreshLineNumTableForHawker(hawkerComboBox.getSelectionModel().getSelectedItem());
					Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
							.text("Line number update successful.").showInformation();
				} else {
					Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid line num")
							.text("This line number already exists.").showError();
				}

			} catch (NumberFormatException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid line num")
						.text("Please enter numeric line number only").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		});
		dialog.showAndWait();
	}

	private void updateLineNumForDist(LineInfo lineRow) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update line_distributor set line_num=? where hawker_id=?, line_num=?";
			PreparedStatement stmt = con.prepareStatement(updateString);
			stmt.setLong(1, lineRow.getHawkerId());
			stmt.setInt(2, lineRow.getLineNum());
			stmt.executeUpdate();
			con.commit();
			stmt.close();
			Notifications.create().hideAfter(Duration.seconds(5)).title("Update successful")
					.text("Line number updation in line distribution boy successful").showInformation();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
			Notifications.create().hideAfter(Duration.seconds(5)).title("Update failed")
					.text("Line number updation in line distribution boy failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private void updateLineNumForCust(Integer newLineNum, ObservableList<Customer> custData) {

		for (int i = 0; i < custData.size(); i++) {
			custData.get(i).setLineNum(newLineNum);
			custData.get(i).setLineId(
					ACustomerInfoTabController.lineIdForNumHwkCode(newLineNum, custData.get(i).getHawkerCode()));
			custData.get(i).updateCustomerRecord();
		}
		lineNumCustomersTable.refresh();
	}

	private boolean deleteLine(LineInfo lineRow) {
		if (lineRow.getLineNum() > 0) {
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

					String findString = "select count(*) from customer where hawker_code=? and line_id=?";
					PreparedStatement findStmt = con.prepareStatement(findString);
					findStmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
					findStmt.setLong(2, lineNumTable.getSelectionModel().getSelectedItem().getLineId());
					ResultSet rs = findStmt.executeQuery();
					if (rs.next() && rs.getInt(1) == 0) {
						String deleteString = "delete from line_info where line_id=?";
						PreparedStatement deleteStmt = con.prepareStatement(deleteString);
						deleteStmt.setLong(1, lineRow.getLineId());
						deleteStmt.executeUpdate();
						con.commit();
						deleteStmt.close();
						Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
								.text("Deletion of line was successful").showInformation();
						refreshLineNumTableForHawker(hawkerComboBox.getSelectionModel().getSelectedItem());
					} else {
						Notifications.create().hideAfter(Duration.seconds(5)).title("Delete not allowed")
								.text("This line has customers associated to it, hence cannot be deleted").showError();
					}
					rs.close();
					findStmt.close();
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
							.text("Delete request of line has failed").showError();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
			}
		} else {
			Notifications.create().hideAfter(Duration.seconds(5)).title("Line 0 cannot be deleted")
					.text("You cannot delete line 0.").showError();
		}
		return false;
	}

	private void populateCustomersForLine() {
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
						customerData = FXCollections.observableArrayList();
						// lineNumCustomersTable.getItems().clear();
						PreparedStatement stmt = con.prepareStatement(
								"select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_id = ? and line_id = ? ORDER BY HOUSE_SEQ");
						stmt.setLong(1, hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem()));
						stmt.setLong(2, lineNumTable.getSelectionModel().getSelectedItem().getLineId());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							customerData.add(new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getLong(6), rs.getInt(7), rs.getString(8),
									rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
									rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
									rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
									rs.getString(21), rs.getDouble(22), rs.getLong(23), rs.getLong(24)));
						}
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								if (!customerData.isEmpty())
									lineNumCustomersTable.setItems(customerData);
								lineNumCustomersTable.refresh();
							}
						});
						rs.close();
						stmt.close();
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
									hawkerComboBox.setItems(hawkerCodeData);
									hawkerComboBox.getSelectionModel().selectFirst();
									hawkerComboBox.setDisable(true);
								}
							});
						} else {
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									hawkerComboBox.setDisable(true);
								}
							});
							hawkerCodeData = FXCollections.observableArrayList();
							PreparedStatement stmt = con.prepareStatement(
									"select distinct hawker_code from hawker_info where point_name=? order by hawker_code");
							stmt.setString(1, addPointName.getSelectionModel().getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if (hawkerCodeData != null && !hawkerCodeData.contains(rs.getString(1)))
									hawkerCodeData.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {

									hawkerComboBox.getItems().clear();
									hawkerComboBox.setItems(hawkerCodeData);
									hawkerComboBox.setDisable(false);
									new AutoCompleteComboBoxListener<>(hawkerComboBox);
									// if (HawkerLoginController.loggedInHawker
									// != null) {
									// hawkerComboBox.getSelectionModel()
									// .select(HawkerLoginController.loggedInHawker.getHawkerCode());
									// hawkerComboBox.setDisable(true);
									// }
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

	private void refreshLineNumTableForHawker(String hawkerCode) {

		// lineNumTable.refresh();
		System.out.println("refreshLineNumTableForHawker : " + hawkerCode);

		long hawkerId = hawkerIdForCode(hawkerCode);

		if (hawkerId >= 1) {
			/*
			 * Task<Void> task = new Task<Void>() {
			 * 
			 * @Override protected Void call() throws Exception {
			 * 
			 * synchronized (this) {
			 */
			try {

				lineNumData = FXCollections.observableArrayList();
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				disableAll();
				PreparedStatement lineNumStatement = null;
				String lineNumQuery = "select li.line_id, li.line_num, li.hawker_id,li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_id=ld.line_id(+) and li.hawker_id = ? order by li.line_num";
				lineNumStatement = con.prepareStatement(lineNumQuery);
				lineNumStatement.setLong(1, hawkerId);
				ResultSet rs = lineNumStatement.executeQuery();
				while (rs.next()) {
					LineInfo l = new LineInfo(rs.getLong(1), rs.getInt(2), rs.getLong(3), rs.getString(4));
					if (!lineNumData.contains(l))
						lineNumData.add(l);
				}
				// System.out.println("LineNumData = " +
				// lineNumData.toString());
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						lineNumTable.setItems(lineNumData);
						lineNumTable.refresh();
					}
				});
				enableAll();
				rs.close();
				lineNumStatement.close();
				// lineNumTable.getSelectionModel().selectFirst();
				// populateSubscriptionCount();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
			/*
			 * } return null; }
			 * 
			 * };
			 * 
			 * new Thread(task).start();
			 */
		} else {
			lineNumData.clear();
			lineNumTable.getItems().clear();
			lineNumTable.refresh();
			Notifications.create().hideAfter(Duration.seconds(5)).title("No lines found")
					.text("No lines found under the hawker").show();
		}

	}

	@FXML
	private void addLineButtonClicked(ActionEvent event) {
		try {
			if (hawkerComboBox.getSelectionModel().selectedIndexProperty().get() != -1) {
				Integer addLineNumValue = Integer.parseInt(addLineNumField.getText().trim());

				if (checkExistingLineNum(addLineNumValue)) {
					if (addLineNumValue == 0) {
						Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid line number value")
								.text("Cannot add line number 0.").showError();
					}
					Task<Void> task = new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							PreparedStatement insertLineNum = null;
							String insertStatement = "INSERT INTO LINE_INFO(LINE_NUM,HAWKER_ID) " + "VALUES (?,?)";
							Connection con = Main.dbConnection;
							synchronized (this) {
								try {
									if (con.isClosed()) {
										con = Main.reconnect();
									}
									insertLineNum = con.prepareStatement(insertStatement);
									long hawkerId = hawkerIdForCode(
											hawkerComboBox.getSelectionModel().getSelectedItem());
									if (hawkerId >= 1) {
										insertLineNum.setInt(1, Integer.parseInt(addLineNumField.getText()));
										insertLineNum.setLong(2, hawkerId);
										insertLineNum.execute();
										refreshLineNumTableForHawker(
												hawkerComboBox.getSelectionModel().getSelectedItem());
										addLineNumField.clear();
										Notifications.create().hideAfter(Duration.seconds(5))
												.title("Line Number added.").text("Line number added successfully")
												.showInformation();
									}
								} catch (SQLException e) {

									Main._logger.debug("Error :", e);
									e.printStackTrace();
								} catch (Exception e) {

									Main._logger.debug("Error :", e);
									e.printStackTrace();
								}
							}
							insertLineNum.close();
							return null;
						}

					};
					new Thread(task).start();
				}
			} else
				Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker not selected")
						.text("Please select hawker before adding line number").showError();

		} catch (NumberFormatException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
			Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
					.text("Please enter proper numeric value in Line Number field").showError();
		}

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
			hawkerIdStatement.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	private boolean checkExistingLineNum(Integer lineNum) {

		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement lineNumExists = null;
			String lineNumExistsQuery = "select line_num from line_info where line_num = ? and hawker_id = ?";
			lineNumExists = con.prepareStatement(lineNumExistsQuery);
			lineNumExists.setInt(1, lineNum);
			lineNumExists.setLong(2, hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem()));
			ResultSet lineNumExistsRs = lineNumExists.executeQuery();
			if (lineNumExistsRs.next()) {
				Notifications.create().hideAfter(Duration.seconds(5)).title("Line number exists")
						.text("This line number already exists in the hawker selected").showError();
				return false;
			}
			lineNumExists.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return true;
	}

	@FXML
	public void shuffleHouseSeqClicked(ActionEvent event) {
		// Create the custom dialog.
		if (!customerData.isEmpty()) {
			// Dialog<ArrayList<Pair<Long, Integer>>> dialog = new Dialog<>();
			Dialog<ArrayList<Integer>> dialog = new Dialog<>();
			dialog.setTitle("Shuffle house sequences!");
			dialog.setHeaderText("Update house numbers below");

			newHouseSeqTFArray = new ArrayList<TextField>();
			ArrayList<Integer> newHouseSeq = new ArrayList<Integer>(customerData.size());

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
			ScrollPane scrollPane = new ScrollPane();
			Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
			saveButton.addEventFilter(ActionEvent.ACTION, btnevent -> {
				for (int i = 0; i < customerData.size(); i++) {
					if (NumberUtils.tryParseInt(newHouseSeqTFArray.get(i).getText()) != null) {
						if (!newHouseSeq.contains(NumberUtils.tryParseInt(newHouseSeqTFArray.get(i).getText()))) {
							newHouseSeq.add(i, NumberUtils.tryParseInt(newHouseSeqTFArray.get(i).getText()));
						} else {
							Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid sequence")
									.text("Duplicate house sequence found").showError();
							newHouseSeq.clear();
							btnevent.consume();
						}
					} else {
						Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid sequence")
								.text("House sequence should not be empty and must be NUMBERS only").showError();
						newHouseSeq.clear();
						btnevent.consume();
					}
				}
			});

			// Create the username and password labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			// Iterator<Customer> custIter = customerData.iterator();

			grid.add(new Label("Customer Code"), 0, 0);
			grid.add(new Label("Name"), 1, 0);
			grid.add(new Label("Mobile Number"), 2, 0);
			grid.add(new Label("Flat/Street Name"), 3, 0);
			grid.add(new Label("Addr Line1"), 4, 0);
			grid.add(new Label("Addr Line2"), 5, 0);
			grid.add(new Label("Old House Seq"), 6, 0);
			grid.add(new Label("New House Seq"), 7, 0);
			for (int i = 0; i < customerData.size(); i++) {
				Customer cust = customerData.get(i);
				grid.add(new Label("" + cust.getCustomerCode()), 0, i + 1);
				grid.add(new Label(cust.getName()), 1, i + 1);
				grid.add(new Label(cust.getMobileNum()), 2, i + 1);
				grid.add(new Label(cust.getBuildingStreet()), 3, i + 1);
				grid.add(new Label(cust.getAddrLine1()), 4, i + 1);
				grid.add(new Label(cust.getAddrLine2()), 5, i + 1);
				grid.add(new Label("" + cust.getHouseSeq()), 6, i + 1);
				TextField newHNumTF = new TextField();

				newHouseSeqTFArray.add(i, newHNumTF);
				grid.add(newHNumTF, 7, i + 1);
			}
			scrollPane.setContent(grid);

			dialog.getDialogPane().setContent(scrollPane);

			// Convert the result to a username-password-pair when the login
			// button is clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					return newHouseSeq;
				}
				return null;
			});

			// Optional<ArrayList<Pair<Long, Integer>>> result =
			// dialog.showAndWait();
			Optional<ArrayList<Integer>> result = dialog.showAndWait();
			if (result.isPresent()) {
				if (result != null) {
					updateHouseSequences(result.get());
				}
			}
		} else {
			Notifications.create().hideAfter(Duration.seconds(5)).title("No customers")
					.text("There are no customers in this line").showError();
		}
	}

	private void updateHouseSequences(ArrayList<Integer> houseSeqList) {

		for (int i = 0; i < houseSeqList.size(); i++) {
			customerData.get(i).setHouseSeq(houseSeqList.get(i));
			customerData.get(i).updateCustomerRecord();
		}
		lineNumCustomersTable.refresh();
	}

	private void showEditCustomerDialog(Customer custRow) {
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

					return edittedCustomer;
				}
				return null;
			});

			Optional<Customer> updatedCustomer = editCustomerDialog.showAndWait();
			// refreshCustomerTable();

			updatedCustomer.ifPresent(new Consumer<Customer>() {

				@Override
				public void accept(Customer t) {

					populateCustomersForLine();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	private void showViewCustomerDialog(Customer custRow) {
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

			Optional<Customer> updatedCustomer = editCustomerDialog.showAndWait();

		} catch (IOException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
	}

	public void addCustomerExtraScreenClicked(ActionEvent event) {
		if (hawkerComboBox.getSelectionModel().getSelectedItem() == null
				|| lineNumTable.getSelectionModel().getSelectedItem() == null) {
			Notifications.create().title("Hawker and Line not selected").text("Please select hawker and line first")
					.hideAfter(Duration.seconds(5)).showError();
		} else {
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
						populateCustomersForLine();
					} else {
						btnEvent.consume();
					}
				});
				addCustomerDialog.getDialogPane().setContent(addCustomerGrid);
				addCustController.setupBindings();
				addCustController.setupHawkerAndLine(hawkerComboBox.getSelectionModel().getSelectedItem(),
						lineNumTable.getSelectionModel().getSelectedItem().getLineNum() + "");
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

					}
				});

			} catch (IOException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
	}

	private void deleteCustomer(Customer custRow) {
		Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();
		deleteWarning.setTitle("Warning");
		deleteWarning.setHeaderText("Are you sure you want to delete this record?");
		deleteWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = deleteWarning.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {

			try {
				ArrayList<Customer> custData = getCustomerDataToShift(custRow.getHawkerCode(), custRow.getLineId());
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
				deleteString = "delete from subscription where customer_id=?";
				deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();

				deleteString = "delete from stop_history where sub_id in (select distinct subscription_id from subscription where customer_id=?)";
				deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();

				deleteString = "delete from STOP_HISTORY_BKP where sub_id in (select distinct subscription_id from subscription where customer_id=?)";
				deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();
				deleteString = "delete from billing_lines where bill_invoice_num in (select distinct bill_invoice_num from billing where customer_id=?)";
				deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();

				deleteString = "delete from billing where customer_id=?";
				deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, custRow.getCustomerId());

				deleteStmt.executeUpdate();
				con.commit();
				deleteStmt.close();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of customer was successful").showInformation();
				populateCustomersForLine();
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

	public ArrayList<Customer> getCustomerDataToShift(String hawkerCode, Long lineId) {
		ArrayList<Customer> custData = new ArrayList<Customer>();

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_id=? and line_id=? order by house_seq";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setLong(1, hawkerIdForCode(hawkerCode));
			stmt.setLong(2, lineId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				custData.add(new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getLong(6), rs.getInt(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
						rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19),
						rs.getString(20), rs.getString(21), rs.getDouble(22), rs.getLong(23), rs.getLong(24)));
			}
			stmt.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

		return custData;
	}

	private void shiftHouseSeqForDelete(ArrayList<Customer> custData, int seq) {

		for (int i = 0; i < custData.size(); i++) {
			Customer cust = custData.get(i);
			if (cust != null && cust.getHouseSeq() >= seq) {
				cust.setHouseSeq(cust.getHouseSeq() - 1);
				cust.updateCustomerRecord();
			}
		}
		// reloadData();
	}

	private void addCustSubscription(Customer custRow) {

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
			addSubsController.setCustomer(lineNumCustomersTable.getSelectionModel().getSelectedItem());
			addSubsController.setupBindings();
			Button saveBtn = (Button) addSubscriptionDialog.getDialogPane().lookupButton(saveButtonType);

			saveBtn.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addSubsController.isValid()) {
					long subId = addSubsController.addSubscription();
					if (subId > 0) {
						Notifications.create().title("New Subscription created")
								.text("New subscription created successfully").hideAfter(Duration.seconds(5))
								.showInformation();
						if (subId > 0) {
							Subscription subRow = BillingUtilityClass.subForSubId(subId);
							if (subRow.getPaymentType().equalsIgnoreCase("Current Month")) {
								Notifications.create().title("Refreshing Customers")
										.text("Customer due updated, refreshing customers")
										.hideAfter(Duration.seconds(5)).showInformation();
							}
							populateCustomersForLine();

						}
					}
				} else
					btnEvent.consume();
			});

			addSubscriptionDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {

					// populateCustomersForLine();
					return null;
				}
				return null;
			});

			Optional<String> updatedSubscription = addSubscriptionDialog.showAndWait();
			// refreshCustomerTable();

			updatedSubscription.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {
					// refreshSubscriptions();
				}
			});

		} catch (IOException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
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
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									addPointName.getItems().clear();
									addPointName.setDisable(true);
								}
							});
							pointNameValues = FXCollections.observableArrayList();
							PreparedStatement stmt = con.prepareStatement(
									"select distinct name from point_name where city =? order by name");
							stmt.setString(1, cityTF.getSelectionModel().getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if (pointNameValues != null && !pointNameValues.contains(rs.getString(1)))
									pointNameValues.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									addPointName.getItems().clear();
									addPointName.setItems(pointNameValues);
									new AutoCompleteComboBoxListener<>(addPointName);
									addPointName.setDisable(false);
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

	public void showSubscriptionCounts() {
		if (hawkerComboBox.getSelectionModel().getSelectedItem() == null
				|| lineNumTable.getSelectionModel().getSelectedItem() == null) {
			Notifications.create().hideAfter(Duration.seconds(5)).title("Hawker Code and Line Num values required.")
					.text("Please select hawker code and line number.").showError();
		} else {
			try {

				Dialog<String> showCountsDialog = new Dialog<String>();
				showCountsDialog.setTitle("View Subscriptions count");
				// editCustomerDialog.setHeaderText("View the customer data
				// below");

				showCountsDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

				FXMLLoader showCountsLoader = new FXMLLoader(getClass().getResource("LineSubscriptionCount.fxml"));
				Parent showCountsGrid = (Parent) showCountsLoader.load();
				LineSubscriptionCount showCountsController = showCountsLoader.<LineSubscriptionCount> getController();

				showCountsDialog.getDialogPane().setContent(showCountsGrid);
				showCountsController.setupBindings(hawkerComboBox.getSelectionModel().getSelectedItem(),
						lineNumTable.getSelectionModel().getSelectedItem().getLineNum());

				Optional<String> updatedCustomer = showCountsDialog.showAndWait();

			} catch (IOException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
	}

	private void resumeStopHistoryForSub(Subscription subsRow, LocalDate stopDate, LocalDate resumeDate) {

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
							.text("Stop History record successfully closed").showInformation();
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
				rs.close();
				stmt.close();
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
				stmt.close();
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

	public boolean stopEntryExistsForStartDate(Subscription subList, LocalDate stopDate) {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			// subscriptionsTable.getItems().clear();
			subscriptionMasterData = FXCollections.observableArrayList();
			String countStmt = "select count(*) from stop_history where sub_id=? and (resume_date is null or ? between stop_date and resume_date-1)";
			PreparedStatement stmt = con.prepareStatement(countStmt);
			stmt.setLong(1, subList.getSubscriptionId());
			stmt.setDate(2, Date.valueOf(stopDate));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			rs.close();
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

		return false;
	}

	private int subsPostCount(Subscription subsRow, String status) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con.prepareStatement(
					"select count(1) from subscription where customer_id=? and subscription_id<>? and status=? and PRODUCT_ID=? ");
			stmt.setLong(1, lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId());
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

	private void refreshStopHistory() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					// stopHistoryTable.getItems().clear();
					stopHistoryMasterData = FXCollections.observableArrayList();
					String query = "SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE CUST.CUSTOMER_ID=? AND STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC";
					PreparedStatement stmt = con.prepareStatement(query);
					stmt.setLong(1, lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId());
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
						stmt.setLong(1, lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId());
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
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									cityTF.setDisable(true);
								}
							});
							stmt = con.prepareStatement("select distinct city from point_name order by city");
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								if (cityValues != null && !cityValues.contains(rs.getString(1)))
									cityValues.add(rs.getString(1));
							}

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									cityTF.getItems().clear();
									cityTF.setItems(cityValues);
									cityTF.setDisable(false);
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

	private ObservableList<LocalDate> getInvoiceDateListForLine(LineInfo lineRow) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			ObservableList<LocalDate> dateList = FXCollections.observableArrayList();
			PreparedStatement stmt = null;
			stmt = con.prepareStatement(
					"select distinct invoice_date from billing where customer_id in (select distinct customer_id from customer where hawker_id=? and line_id=?) order by invoice_date desc");
			stmt.setLong(1, hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem()));
			stmt.setLong(2, lineRow.getLineId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				dateList.add(rs.getDate(1).toLocalDate());
			}

			rs.close();
			stmt.close();
			return dateList;
		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return null;
	}

	private ObservableList<LocalDate> getInvoiceDateListForCust(Customer custRow) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			ObservableList<LocalDate> dateList = FXCollections.observableArrayList();
			PreparedStatement stmt = null;
			stmt = con.prepareStatement(
					"select distinct invoice_date from billing where customer_id =? order by invoice_date desc");
			stmt.setLong(1, custRow.getCustomerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				dateList.add(rs.getDate(1).toLocalDate());
			}

			rs.close();
			stmt.close();
			return dateList;
		} catch (SQLException e) {
			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return null;
	}

	public static LocalDate findMaxStopDateForSub(Subscription newValue) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			ObservableList<LocalDate> dateList = FXCollections.observableArrayList();
			PreparedStatement stmt = null;
			stmt = con.prepareStatement("select max(stop_date) from stop_history where sub_id =? group by sub_id");
			stmt.setLong(1, newValue.getSubscriptionId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getDate(1).toLocalDate();
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
		return null;
	}

	private void transferLine(LineInfo lineRow, String hawkerCode) {

		PreparedStatement insertLineNum = null;
		String insertStatement = "INSERT INTO LINE_INFO(LINE_NUM,HAWKER_ID) " + "VALUES (?,?)";
		Connection con = Main.dbConnection;
		synchronized (this) {
			try {
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				insertLineNum = con.prepareStatement(insertStatement, new String[] { "LINE_ID" });
				long hawkerId = hawkerIdForCode(hawkerCode);
				if (hawkerId >= 1) {
					int maxLineNum = maxLineNumForHawker(hawkerId) + 1;
					insertLineNum.setInt(1, maxLineNum);
					insertLineNum.setLong(2, hawkerId);
					insertLineNum.executeUpdate();
					ResultSet rs = insertLineNum.getGeneratedKeys();
					long newLineId = 0;
					if (rs.next()) {
						newLineId = rs.getLong(1);
					}
					long existHawkerId = hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem());
					// Transfer Customers to new line
					PreparedStatement updateCustStmt = con.prepareStatement(
							"UPDATE CUSTOMER SET LINE_NUM=?, HAWKER_CODE=?, LINE_ID=?, HAWKER_ID=? WHERE HAWKER_ID=? AND LINE_ID=?");
					updateCustStmt.setInt(1, maxLineNum);
					updateCustStmt.setString(2, hawkerCode);
					updateCustStmt.setLong(3, newLineId);
					updateCustStmt.setLong(4, hawkerId);
					updateCustStmt.setLong(5, existHawkerId);
					updateCustStmt.setLong(6, lineRow.getLineId());
					updateCustStmt.executeUpdate();

					// Transfer LineDistributor to new Hawker
					PreparedStatement updateDistStmt = con.prepareStatement(
							"UPDATE LINE_DISTRIBUTOR SET LINE_NUM=?, LINE_ID=?, HAWKER_ID=? WHERE HAWKER_ID=? AND LINE_ID=?");
					updateDistStmt.setInt(1, maxLineNum);
					updateDistStmt.setLong(2, newLineId);
					updateDistStmt.setLong(3, hawkerId);
					updateDistStmt.setLong(4, existHawkerId);
					updateDistStmt.setLong(5, lineRow.getLineId());
					updateDistStmt.executeUpdate();

					refreshLineNumTableForHawker(hawkerComboBox.getSelectionModel().getSelectedItem());
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create().hideAfter(Duration.seconds(5)).title("Line Number added.")
									.text("Line number added successfully").showInformation();

						}
					});

				}
				insertLineNum.close();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}
	}

	private int maxLineNumForHawker(long hawkerId) {
		PreparedStatement maxLineNum = null;
		String query = "SELECT MAX(LINE_NUM) FROM LINE_INFO WHERE HAWKER_ID=?";
		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			maxLineNum = con.prepareStatement(query);
			if (hawkerId >= 1) {
				maxLineNum.setLong(1, hawkerId);
				ResultSet rs = maxLineNum.executeQuery();
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return 0;
	}

	private void disableAll() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// Notifications.create().title("Please wait").text("Please
					// wait till process
					// finishes").hideAfter(Duration.seconds(5)).showInformation();
					lineNumTable.setDisable(true);
					lineNumCustomersTable.setDisable(true);
					subscriptionsTable.setDisable(true);
					invoiceDateLOV.setDisable(true);
					billingTable.setDisable(true);
					stopHistoryTable.setDisable(true);

				}
			});
		} else {
			lineNumTable.setDisable(true);
			lineNumCustomersTable.setDisable(true);
			subscriptionsTable.setDisable(true);
			invoiceDateLOV.setDisable(true);
			billingTable.setDisable(true);
			stopHistoryTable.setDisable(true);
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
					lineNumTable.setDisable(false);
					lineNumCustomersTable.setDisable(false);
					subscriptionsTable.setDisable(false);
					invoiceDateLOV.setDisable(false);
					billingTable.setDisable(false);
					stopHistoryTable.setDisable(false);
				}
			});
		} else {
			lineNumTable.setDisable(false);
			lineNumCustomersTable.setDisable(false);
			subscriptionsTable.setDisable(false);
			invoiceDateLOV.setDisable(false);
			billingTable.setDisable(false);
			stopHistoryTable.setDisable(false);
		}
	}

	private void openPDFWindow(File pdfFile) {

		BorderPane root;
		root = new BorderPane();
		Stage stage = new Stage();
		stage.setTitle("Invoice PDF");

		OpenViewerFX fx = new OpenViewerFX(stage, null);
		fx.setupViewer();
		fx.openDefaultFile(pdfFile.getAbsolutePath());

		// stage.setScene(new Scene(root, 1024, 800));
		// stage.showAndWait();

	}

	public BorderPane createAndLoad(File pdfFile) {
		long before = System.currentTimeMillis();
		PDFViewer notesBean = new PDFViewer();
		System.out.println("After: " + (System.currentTimeMillis() - before));
		new Thread(() -> {
			try {
				notesBean.loadPDF(new FileInputStream(pdfFile));
			} catch (PDFException | FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}).run();
		BorderPane borderPane = new BorderPane(notesBean);
		return borderPane;
	}

	@SuppressWarnings("deprecation")
	private void customerListForLineFunction() {

		String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
		String SECRET = "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";
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

		lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		try {
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("CustomerListForHawkerAndLine");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("hawkerId", HawkerLoginController.loggedInHawker.getHawkerId().toString());
			map.put("lineId", lineNumTable.getSelectionModel().getSelectedItem().getLineId().toString());
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
			System.out.println(gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					ArrayList.class));
		} catch (LambdaFunctionException e) {

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("deprecation")
	private void subsListForCustFunction() {

		String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
		String SECRET = "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";
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);
		lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		try {
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("SubsListForCustomer");
			invokeRequest.setPayload(ByteBuffer.wrap(
					gson.toJson(lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId().toString())
							.getBytes(StringUtils.UTF8)));
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
			System.out.println(gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					ArrayList.class));
		} catch (LambdaFunctionException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("deprecation")
	private void stpHistoryistForCustFunction() {

		String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
		String SECRET = "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";
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId().toString());
		lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		try {
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("StopHistoryListForCust");
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
			System.out.println(gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					ArrayList.class));
		} catch (LambdaFunctionException e) {

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("deprecation")
	private void billingListForCustFunction() {

		String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
		String SECRET = "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";
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId().toString());
		lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		try {
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("BillingListForCust");
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
			System.out.println(gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					ArrayList.class));
		} catch (LambdaFunctionException e) {

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("deprecation")
	private void billingLineListForBillFunction() {

		String ACCESS_KEY = "AKIAJHK6Z2KAU4WJSTGQ";
		String SECRET = "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";
		Gson gson = new Gson();
		AWSCredentials credentials = null;
		credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET);
		AWSLambdaClient lambdaClient = new AWSLambdaClient(credentials);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", lineNumCustomersTable.getSelectionModel().getSelectedItem().getCustomerId().toString());
		lambdaClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		try {
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName("BillingLineListForBill");
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
			System.out.println(gson.fromJson(
					new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(invokeResult.getPayload().array()))),
					ArrayList.class));
		} catch (LambdaFunctionException e) {

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("deprecation")
	private void generateBillingFunction(String hawkerCode, String lineNum, String invoiceDate) {

		try {
			Properties prop = new Properties();
			String propFileName = "application/config.properties";

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
			map.put("LinueNum", lineNum);
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

	// @Override
	public void reloadData() {
		lineNumData.clear();
		lineNumTable.getItems().clear();
		lineNumTable.setItems(lineNumData);
		lineNumCustomersTable.getItems().clear();
		lineNumCustomersTable.refresh();
		invoiceDateLOV.getItems().clear();
		billingLinesData.clear();
		billingTable.setItems(billingLinesData);
		populateCityValues();
		customerTitledPane.setExpanded(true);
	}

	// @Override
	public void releaseVariables() {
		invoiceDatesData = null;
		billingLinesData = null;
		hawkerCodeData = null;
		lineNumData = null;
		customerData = null;
		pointNameValues = null;
		subscriptionMasterData = null;
		stopHistoryMasterData = null;
		cityValues = null;
		stopHistoryBkpMasterData = null;
		invoiceDatesData = FXCollections.observableArrayList();
		billingLinesData = FXCollections.observableArrayList();
		hawkerCodeData = FXCollections.observableArrayList();
		lineNumData = FXCollections.observableArrayList();
		customerData = FXCollections.observableArrayList();
		pointNameValues = FXCollections.observableArrayList();
		subscriptionMasterData = FXCollections.observableArrayList();
		stopHistoryMasterData = FXCollections.observableArrayList();
		cityValues = FXCollections.observableArrayList();
		stopHistoryBkpMasterData = FXCollections.observableArrayList();
		newHouseSeqTFArray = new ArrayList<TextField>();
	}
}
