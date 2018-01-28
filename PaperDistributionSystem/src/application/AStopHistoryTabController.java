package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.Duration;

public class AStopHistoryTabController implements Initializable {

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private TableView<LineInfo> lineNumTable;
	@FXML
	private TableView<Customer> lineNumCustomersTable;
	@FXML
	private ComboBox<String> hawkerComboBox;
	@FXML
	private ComboBox<String> addPointName;
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
	private TableColumn<Customer, String> line2Column;

	@FXML
	private TableView<StopHistory> stopHistoryTable;

	@FXML
	private TableColumn<StopHistory, Long> stopHistoryIdColumn;
	@FXML
	private TableColumn<StopHistory, Long> subsIdColumn;
	@FXML
	private TableColumn<StopHistory, String> subsProdNameColumn;
	@FXML
	private TableColumn<StopHistory, String> subsTypeColumn;
	@FXML
	private TableColumn<StopHistory, String> subsFreqColumn;
	@FXML
	private TableColumn<StopHistory, String> subsDOWColumn;
	@FXML
	private TableColumn<StopHistory, Double> stopHistAmountColumn;

	@FXML
	private TableColumn<StopHistory, LocalDate> subsStopDateColumn;

	@FXML
	private TableColumn<StopHistory, LocalDate> subsResumeDateColumn;
	@FXML
	private Label hawkerNameLabel;
	@FXML
	private Label hawkerMobLabel;

	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<LineInfo> lineNumData = FXCollections.observableArrayList();
	private ObservableList<Customer> customerData = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	private ObservableList<StopHistory> stopHistoryMasterData = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lineNumColumn.setCellValueFactory(new PropertyValueFactory<LineInfo, String>("lineNumDist"));
		lineNumTable.setDisable(true);

		customerIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("customerCode"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		mobileNumColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("mobileNum"));
		flatNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("buildingStreet"));
		line1Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine1"));
		line2Column.setCellValueFactory(new PropertyValueFactory<Customer, String>("addrLine2"));
		houseSeqColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("houseSeq"));

		stopHistoryIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Long>("stopHistoryId"));
		subsIdColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Long>("subscriptionId"));
		subsProdNameColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("productName"));
		subsTypeColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionType"));
		subsFreqColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionFreq"));
		subsDOWColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, String>("subscriptionDOW"));
		subsResumeDateColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, LocalDate>("resumeDate"));
		stopHistAmountColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, Double>("amount"));
		subsStopDateColumn.setCellValueFactory(new PropertyValueFactory<StopHistory, LocalDate>("stopDate"));
		subsResumeDateColumn
				.setCellFactory(new Callback<TableColumn<StopHistory, LocalDate>, TableCell<StopHistory, LocalDate>>() {

					@Override
					public TableCell<StopHistory, LocalDate> call(TableColumn<StopHistory, LocalDate> param) {
						TextFieldTableCell<StopHistory, LocalDate> cell = new TextFieldTableCell<StopHistory, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		subsStopDateColumn
				.setCellFactory(new Callback<TableColumn<StopHistory, LocalDate>, TableCell<StopHistory, LocalDate>>() {

					@Override
					public TableCell<StopHistory, LocalDate> call(TableColumn<StopHistory, LocalDate> param) {
						TextFieldTableCell<StopHistory, LocalDate> cell = new TextFieldTableCell<StopHistory, LocalDate>();
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
				}
				stopHistoryMasterData.clear();
				stopHistoryTable.getItems().clear();
				stopHistoryTable.refresh();

			}

		});

		lineNumTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LineInfo>() {

			@Override
			public void changed(ObservableValue<? extends LineInfo> observable, LineInfo oldValue, LineInfo newValue) {

				populateCustomersForLine();
			}

		});

		lineNumCustomersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {

			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {

				refreshStopHistory();
			}

		});

		lineNumCustomersTable.setRowFactory(new Callback<TableView<Customer>, TableRow<Customer>>() {

			@Override
			public TableRow<Customer> call(TableView<Customer> param) {

				final TableRow<Customer> row = new TableRow<>();

				MenuItem mnuView = new MenuItem("View customer");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Customer custRow = lineNumCustomersTable.getSelectionModel().getSelectedItem();
						if (custRow != null) {
							showViewCustomerDialog(custRow);
							// ACustInfoTable.refresh();
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
			hawkerRs.close();
			hawkerStatement.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private void refreshStopHistory() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {
					synchronized (this) {
						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						stopHistoryTable.getItems().clear();
						stopHistoryMasterData.clear();
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

								stopHistoryTable.getItems().addAll(stopHistoryMasterData);
								stopHistoryTable.refresh();
							}
						});
						rs.close();
						stmt.close();
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

	private void populateCustomersForLine() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {
					synchronized (this) {
						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						customerData.clear();
						// lineNumCustomersTable.getItems().clear();
						PreparedStatement stmt = con.prepareStatement(
								"select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due, hawker_id, line_id from customer where hawker_code = ? and line_num = ? ORDER BY HOUSE_SEQ");
						stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
						stmt.setInt(2, lineNumTable.getSelectionModel().getSelectedItem().getLineNum());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							customerData.add(new Customer(rs.getLong(1), rs.getLong(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getLong(6), rs.getInt(7), rs.getString(8),
									rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
									rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
									rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
									rs.getString(21), rs.getDouble(22), rs.getLong(23), rs.getLong(24)));
						}
						rs.close();
						stmt.close();

						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								if (!customerData.isEmpty())
									lineNumCustomersTable.setItems(customerData);
								lineNumCustomersTable.refresh();
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
								if(hawkerCodeData!=null && !hawkerCodeData.contains(rs.getString(1)))
								hawkerCodeData.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {

									hawkerComboBox.getItems().clear();
									hawkerComboBox.setItems(hawkerCodeData);
									new AutoCompleteComboBoxListener<>(hawkerComboBox);
								}
							});
							rs.close();
							stmt.close();
						}
					} catch (

					SQLException e) {

						Main._logger.debug("Error :",e);
						e.printStackTrace();
					} catch (Exception e) {

						Main._logger.debug("Error :",e);
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
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					synchronized (this) {
						try {

							lineNumData.clear();
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
							lineNumTable.getItems().clear();
							lineNumTable.getItems().addAll(lineNumData);
							lineNumTable.refresh();
							rs.close();
							lineNumStatement.close();
						} catch (SQLException e) {

							Main._logger.debug("Error :",e);
							e.printStackTrace();
						} catch (Exception e) {

							Main._logger.debug("Error :",e);
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
			hawkerIdRs.close();
			hawkerIdStatement.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return hawkerId;
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
					} catch (

					SQLException e) {

						Main._logger.debug("Error :",e);
						e.printStackTrace();
					} catch (Exception e) {

						Main._logger.debug("Error :",e);
						e.printStackTrace();

					}
				}
				return null;
			}

		};

		new Thread(task).start();

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
			// editCustController.gridPane.setStyle("-fx-opacity: 1.0;");

			Optional<Customer> updatedCustomer = editCustomerDialog.showAndWait();
			// refreshCustomerTable();

		} catch (IOException e) {

			Main._logger.debug("Error :",e);
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
			Main._logger.debug("Error :",e);
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
						cityValues=FXCollections.observableArrayList();
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
								if(cityValues!=null && !cityValues.contains(rs.getString(1)))
								cityValues.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									cityTF.setItems(cityValues);
									new AutoCompleteComboBoxListener<>(cityTF);
								}
							});
							rs.close();
							stmt.close();
						}
					} catch (SQLException e) {
						Main._logger.debug("Error :",e);
						e.printStackTrace();
					} catch (Exception e) {

						Main._logger.debug("Error :",e);
						e.printStackTrace();
					}
				}
				return null;
			}

		};

		new Thread(task).start();
	}

	public void reloadData() {
		lineNumData.clear();
		lineNumTable.getItems().clear();
		lineNumTable.getItems().addAll(lineNumData);
		lineNumCustomersTable.getItems().clear();
		lineNumCustomersTable.refresh();
		populateCityValues();

	}

	public void releaseVariables() {

		hawkerCodeData = null;
		lineNumData = null;
		customerData = null;
		pointNameValues = null;
		stopHistoryMasterData = null;
		cityValues = null;

		hawkerCodeData = FXCollections.observableArrayList();
		lineNumData = FXCollections.observableArrayList();
		customerData = FXCollections.observableArrayList();
		pointNameValues = FXCollections.observableArrayList();
		stopHistoryMasterData = FXCollections.observableArrayList();
		cityValues = FXCollections.observableArrayList();
	}

}
