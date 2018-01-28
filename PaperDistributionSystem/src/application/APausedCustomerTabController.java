package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

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
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;

public class APausedCustomerTabController implements Initializable {

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private ComboBox<String> hawkerComboBox;
	@FXML
	private ComboBox<String> addPointName;

	@FXML
	private TableView<PausedSubscription> pausedCustTable;

	@FXML
	private TableColumn<PausedSubscription, String> custNameCol;

	@FXML
	private TableColumn<PausedSubscription, Long> custCodeCol;

	@FXML
	private TableColumn<PausedSubscription, String> custMobCol;

	@FXML
	private TableColumn<PausedSubscription, String> custHwkCodeCol;

	@FXML
	private TableColumn<PausedSubscription, String> custLineNumCol;

	@FXML
	private TableColumn<PausedSubscription, Integer> custHouseSeqCol;

	@FXML
	private TableColumn<PausedSubscription, String> prodNameCol;

	@FXML
	private TableColumn<PausedSubscription, String> prodTypeCol;

	@FXML
	private TableColumn<PausedSubscription, String> subTypeCol;

	@FXML
	private TableColumn<PausedSubscription, String> freqCol;

	@FXML
	private TableColumn<PausedSubscription, String> paymentTypeCol;

	@FXML
	private TableColumn<PausedSubscription, Double> subCostCol;

	@FXML
	private TableColumn<PausedSubscription, Double> srvChargeCol;

	@FXML
	private TableColumn<PausedSubscription, LocalDate> pausedDateCol;
	@FXML
	private TableColumn<PausedSubscription, LocalDate> resumeDateCol;
	@FXML
	private TableColumn<PausedSubscription, String> custAddr1Col;
	@FXML
	private TableColumn<PausedSubscription, String> custAddr2Col;
	@FXML
	private Label hawkerNameLabel;
	@FXML
	private Label hawkerMobLabel;

	private ObservableList<PausedSubscription> pausedSubsValues = FXCollections.observableArrayList();
	private ObservableList<LineInfo> lineNumData = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();

	@FXML
	private TableColumn<LineInfo, String> lineNumColumn;
	@FXML
	private TableView<LineInfo> lineNumTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		custNameCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("customerName"));
		custCodeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, Long>("customerCode"));
		custMobCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("mobileNum"));
//		custHwkCodeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("hawkerCode"));
//		custLineNumCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("lineNum"));
		custHouseSeqCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, Integer>("houseSeq"));
		prodNameCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("productName"));
		prodTypeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("productType"));
		subTypeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("subscriptionType"));
		freqCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("frequency"));
		paymentTypeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("paymentType"));
		subCostCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, Double>("subscriptionCost"));
		srvChargeCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, Double>("serviceCharge"));
		pausedDateCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, LocalDate>("pausedDate"));
		resumeDateCol.setCellValueFactory(new PropertyValueFactory<PausedSubscription, LocalDate>("resumeDate"));
		custAddr1Col.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("addr1"));
		custAddr2Col.setCellValueFactory(new PropertyValueFactory<PausedSubscription, String>("addr2"));
		pausedDateCol.setCellFactory(
				new Callback<TableColumn<PausedSubscription, LocalDate>, TableCell<PausedSubscription, LocalDate>>() {

					@Override
					public TableCell<PausedSubscription, LocalDate> call(
							TableColumn<PausedSubscription, LocalDate> param) {
						TextFieldTableCell<PausedSubscription, LocalDate> cell = new TextFieldTableCell<PausedSubscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		resumeDateCol.setCellFactory(
				new Callback<TableColumn<PausedSubscription, LocalDate>, TableCell<PausedSubscription, LocalDate>>() {

					@Override
					public TableCell<PausedSubscription, LocalDate> call(
							TableColumn<PausedSubscription, LocalDate> param) {
						TextFieldTableCell<PausedSubscription, LocalDate> cell = new TextFieldTableCell<PausedSubscription, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		pausedCustTable.setRowFactory(new Callback<TableView<PausedSubscription>, TableRow<PausedSubscription>>() {

			@Override
			public TableRow<PausedSubscription> call(TableView<PausedSubscription> param) {
				final TableRow<PausedSubscription> row = new TableRow<>();
				MenuItem mnuRes = new MenuItem("Resume subscription");
				mnuRes.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PausedSubscription pausedSubsRow = pausedCustTable.getSelectionModel().getSelectedItem();
						if (pausedSubsRow != null) {
							Dialog<ButtonType> resumeWarning = new Dialog<ButtonType>();
							resumeWarning.setTitle("Warning");
							resumeWarning.setHeaderText("Are you sure you want to RESUME this record?");
							resumeWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Stop Date"), 0, 0);
							DatePicker pauseDP = new DatePicker(pausedSubsRow.getPausedDate());
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
								pausedSubsRow.resumeSubscription();
								resumeStopHistoryForSub(pausedSubsRow.getSubscriptionId(), pauseDP.getValue(),
										resumeDP.getValue());
								refreshPausedCustTable();
							}

						}
					}

				});
				MenuItem mnuView = new MenuItem("View customer");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PausedSubscription pausedSubsRow = pausedCustTable.getSelectionModel().getSelectedItem();
						if (pausedSubsRow != null) {
							showViewCustomerDialog(pausedSubsRow.getCustomerId());
						}
					}

				});
				MenuItem mnuSubView = new MenuItem("View Subscription");
				mnuSubView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PausedSubscription pausedSubsRow = pausedCustTable.getSelectionModel().getSelectedItem();
						if (pausedSubsRow != null) {
							showViewSubscriptionDialog(pausedSubsRow.getSubscriptionId());
						}
					}

				});
				ContextMenu menu = new ContextMenu();
				menu.getItems().addAll(mnuRes, mnuView, mnuSubView);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		lineNumColumn.setCellValueFactory(new PropertyValueFactory<LineInfo, String>("lineNumDist"));
		lineNumTable.setDisable(true);
		addPointName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateHawkerCodes();
				hawkerComboBox.getItems().clear();
				hawkerComboBox.getItems().addAll(hawkerCodeData);
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
					refreshLineNumTableForHawker(newValue);
				} else {
					lineNumTable.getItems().clear();
					lineNumTable.refresh();
					lineNumData.clear();
					pausedCustTable.getItems().clear();
					pausedCustTable.refresh();
					pausedSubsValues.clear();
				}

			}

		});

		lineNumTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LineInfo>() {

			@Override
			public void changed(ObservableValue<? extends LineInfo> observable, LineInfo oldValue, LineInfo newValue) {

				refreshPausedCustTable();
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

	private void refreshLineNumTableForHawker(String hawkerCode) {
		lineNumData = FXCollections.observableArrayList();
		lineNumTable.setItems(lineNumData);
		lineNumTable.refresh();
		System.out.println("refreshLineNumTableForHawker : " + hawkerCode);

		lineNumData.clear();

		long hawkerId = hawkerIdForCode(hawkerCode);

		if (hawkerId >= 1) {
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					synchronized (this) {
						try {

							Connection con = Main.dbConnection;
							if (con.isClosed()) {
								con = Main.reconnect();
							}
							PreparedStatement lineNumStatement = null;
							String lineNumQuery = "select li.line_id, li.line_num, li.hawker_id,li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_num=ld.line_num(+) and li.hawker_id = ? and li.line_num<>0 order by li.line_num";
							lineNumStatement = con.prepareStatement(lineNumQuery);
							lineNumStatement.setLong(1, hawkerId);
							// Statement stmt = con.createStatement();
							ResultSet rs = lineNumStatement.executeQuery();
							while (rs.next()) {
								lineNumData
										.add(new LineInfo(rs.getLong(1), rs.getInt(2), rs.getLong(3), rs.getString(4)));
							}
							System.out.println("LineNumData = " + lineNumData.toString());
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									lineNumTable.setItems(lineNumData);
									lineNumTable.refresh();
								}
							});
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

		} else {
			lineNumData.clear();
			lineNumTable.getItems().clear();
			lineNumTable.refresh();
			Notifications.create().hideAfter(Duration.seconds(5)).title("No lines found")
					.text("No lines found under the hawker").show();
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
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}
		return hawkerId;
	}

	public void refreshPausedCustTable() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				synchronized (this) {
					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						pausedSubsValues.clear();
						PreparedStatement stmt;
						String queryString = "select cust.CUSTOMER_ID, sub.SUBSCRIPTION_ID, prod.PRODUCT_ID, cust.NAME Customer_Name, cust.CUSTOMER_CODE, cust.MOBILE_NUM,cust.HAWKER_CODE, cust.LINE_NUM, cust.HOUSE_SEQ, prod.NAME Product_Name, prod.TYPE product_type, sub.TYPE subscription_type, sub.FREQUENCY, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.PAUSED_DATE, sub.resume_date,cust.address_line1, cust.address_line2 from customer cust, subscription sub, products prod where cust.customer_id=sub.customer_id and sub.product_id=prod.product_id and sub.STATUS='Stopped' and hawker_code=? and cust.LINE_NUM=? order by sub.resume_date desc, cust.HAWKER_CODE, cust.LINE_NUM, cust.HOUSE_SEQ, prod.name";

						if (HawkerLoginController.loggedInHawker != null) {
							stmt = con.prepareStatement(queryString);
							stmt.setString(1, HawkerLoginController.loggedInHawker.getHawkerCode());
						} else {
							stmt = con.prepareStatement(queryString);
							stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
						}

						stmt.setInt(2, lineNumTable.getSelectionModel().getSelectedItem().getLineNum());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							pausedSubsValues.add(new PausedSubscription(rs.getLong(1), rs.getLong(2), rs.getLong(3),
									rs.getString(4), rs.getLong(5), rs.getString(6), rs.getString(7), rs.getString(8),
									rs.getInt(9), rs.getString(10), rs.getString(11), rs.getString(12),
									rs.getString(13), rs.getString(14), rs.getDouble(15), rs.getDouble(16),
									rs.getDate(17) == null ? null : rs.getDate(17).toLocalDate(),
									rs.getDate(18) == null ? null : rs.getDate(18).toLocalDate(), rs.getString(19), rs.getString(20)));
						}
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								pausedCustTable.getItems().clear();
								pausedCustTable.setItems(pausedSubsValues);
								pausedCustTable.refresh();
							}
						});

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

									hawkerComboBox.setItems(hawkerCodeData);
									new AutoCompleteComboBoxListener<>(hawkerComboBox);
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

	private void resumeStopHistoryForSub(long subsId, LocalDate stopDate, LocalDate resumeDate) {

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}

			if (!stopDate.isEqual(resumeDate)) {
				String insertStmt = "update stop_history set resume_date=? where sub_id=? and stop_date=?";
				PreparedStatement stmt = con.prepareStatement(insertStmt);
				stmt.setLong(2, subsId);
				stmt.setDate(3, Date.valueOf(stopDate));
				stmt.setDate(1, resumeDate == null ? null : Date.valueOf(resumeDate));
				if (stmt.executeUpdate() > 0) {

					Notifications.create().hideAfter(Duration.seconds(5)).title("Stop History Updated")
							.text("Stop History record successfully closed").show();
				}
				// ArrayList<StopHistory> stpHist = new
				// ArrayList<StopHistory>();
				String query = "SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE STP.sub_id=? and STP.stop_date=? AND STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC";
				stmt = con.prepareStatement(query);
				stmt.setLong(1, subsId);
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
				}
			} else {
				String insertStmt = "delete from stop_history where sub_id=? and stop_date=?";
				PreparedStatement stmt = con.prepareStatement(insertStmt);
				stmt.setLong(1, subsId);
				stmt.setDate(2, Date.valueOf(stopDate));
				if (stmt.executeUpdate() > 0) {
					Notifications.create().hideAfter(Duration.seconds(5)).title("Stop History Deleted")
							.text("Stop Date is same as Resume Date. Stop History record successfully deleted.")
							.showInformation();
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

	private void showViewCustomerDialog(long customerId) {
		try {

			Dialog<Customer> editCustomerDialog = new Dialog<Customer>();
			editCustomerDialog.setTitle("View customer data");
			editCustomerDialog.setHeaderText("View the customer data below");

			editCustomerDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

			FXMLLoader editCustomerLoader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
			Parent editCustomerGrid = (Parent) editCustomerLoader.load();
			EditCustomerController editCustController = editCustomerLoader.<EditCustomerController> getController();

			editCustomerDialog.getDialogPane().setContent(editCustomerGrid);
			editCustController.setPausedCust(customerId);
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
						cityValues.clear();
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
									cityTF.getItems().addAll(cityValues);
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
				}
				return null;
			}

		};

		new Thread(task).start();
	}

	// @Override
	public void reloadData() {
		populateCityValues();
		if (HawkerLoginController.loggedInHawker != null) {
			addPointName.getSelectionModel().select(HawkerLoginController.loggedInHawker.getPointName());
			addPointName.setDisable(true);
			hawkerComboBox.getSelectionModel().select(HawkerLoginController.loggedInHawker.getHawkerCode());
			hawkerComboBox.setDisable(true);

		} else {
			cityTF.setDisable(false);
			addPointName.setDisable(false);
			hawkerComboBox.setDisable(false);
			lineNumTable.getItems().clear();
			lineNumTable.refresh();
		}
	}

	// @Override
	public void releaseVariables() {
		pausedSubsValues = null;
		lineNumData = null;
		pointNameValues = null;
		hawkerCodeData = null;
		cityValues = null;
		pausedSubsValues = FXCollections.observableArrayList();
		lineNumData = FXCollections.observableArrayList();
		pointNameValues = FXCollections.observableArrayList();
		hawkerCodeData = FXCollections.observableArrayList();
		cityValues = FXCollections.observableArrayList();
	}

}
