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
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

public class AProductsTabController implements Initializable {
	@FXML
	private HBox freqHBOX;
	@FXML
	VBox inputVBOX;
	@FXML
	private TextField addProdName;

	@FXML
	private TitledPane prodAccordian;
	@FXML
	private ComboBox<String> addProdType;
	@FXML
	private ComboBox<String> addCity;
	@FXML
	private CheckComboBox<String> addProdFreq;

	@FXML
	private TextField addProdPrice;

	@FXML
	private TextField addProdMonday;

	@FXML
	private TextField addProdTuesday;

	@FXML
	private TextField addProdWednesday;

	@FXML
	private TextField addProdThursday;

	@FXML
	private TextField addProdFriday;

	@FXML
	private TextField addProdSaturday;

	@FXML
	private TextField addProdSunday;
	@FXML
	private ComboBox<String> dowTF;
	@FXML
	private ComboBox<String> billCategoryTF;

	@FXML
	private DatePicker firstDeliveryDate;
	@FXML
	private DatePicker issueDate;
	@FXML
	private TextField codeTF;

	@FXML
	private Button saveButton;

	@FXML
	private Button resetButton;

	@FXML
	private Button filterButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button addProdExtraButton;
	@FXML
	private Button duplicateProdCatButton;

	@FXML
	private TableView<Product> productsTable;

	@FXML
	private TableColumn<Product, String> prodNameColumn;

	@FXML
	private TableColumn<Product, String> prodTypeColumn;

	@FXML
	private TableColumn<Product, String> supportingFreqColumn;

	@FXML
	private TableColumn<Product, Double> priceColumn;

	@FXML
	private TableColumn<Product, Double> mondayColumn;

	@FXML
	private TableColumn<Product, Double> tuesdayColumn;

	@FXML
	private TableColumn<Product, Double> wednesdayColumn;

	@FXML
	private TableColumn<Product, Double> thursdayColumn;

	@FXML
	private TableColumn<Product, Double> fridayColumn;

	@FXML
	private TableColumn<Product, Double> saturdayColumn;

	@FXML
	private TableColumn<Product, Double> sundayColumn;
	@FXML
	private TableColumn<Product, String> prodCodeColumn;
	@FXML
	private TableColumn<Product, String> prodDowColumn;
	@FXML
	private TableColumn<Product, LocalDate> prodFirstDeliveryDateColumn;
	@FXML
	private TableColumn<Product, LocalDate> prodIssueDateColumn;
	@FXML
	private TableColumn<Product, String> prodBillCategoryColumn;

	@FXML
	private TableView<ProductSpecialPrice> spclPriceTable;

	@FXML
	private TableColumn<ProductSpecialPrice, LocalDate> spclPriceDateCol;

	@FXML
	private TableColumn<ProductSpecialPrice, Double> spclPriceCol;

	private ObservableList<String> freqValues = FXCollections.observableArrayList();
	private ObservableList<Product> productValues = FXCollections.observableArrayList();
	private ObservableList<ProductSpecialPrice> prodSpclPriceValues = FXCollections.observableArrayList();
	private ObservableList<String> productTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> billCategoryValues = FXCollections.observableArrayList();

	private FilteredList<Product> filteredData;
	private String searchText;
	@FXML
	public RadioButton filterRadioButton;
	@FXML
	public RadioButton showAllRadioButton;
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
		prodNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		prodTypeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("type"));
		supportingFreqColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("supportingFreq"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		mondayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("monday"));
		tuesdayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("tuesday"));
		wednesdayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("wednesday"));
		thursdayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("thursday"));
		fridayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("friday"));
		saturdayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("saturday"));
		sundayColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("sunday"));
		prodCodeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		prodDowColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("dow"));
		prodFirstDeliveryDateColumn
				.setCellValueFactory(new PropertyValueFactory<Product, LocalDate>("firstDeliveryDate"));
		prodIssueDateColumn.setCellValueFactory(new PropertyValueFactory<Product, LocalDate>("issueDate"));
		prodBillCategoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("billCategory"));
		addProdFreq = new CheckComboBox<>();
		addProdFreq.setMaxWidth(250);
		freqHBOX.setSpacing(10);
		freqHBOX.getChildren().addAll(new Label("Supporting Frequency :"), addProdFreq);
		spclPriceDateCol.setCellValueFactory(new PropertyValueFactory<ProductSpecialPrice, LocalDate>("fullDate"));
		spclPriceCol.setCellValueFactory(new PropertyValueFactory<ProductSpecialPrice, Double>("price"));

		dowTF.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

		prodFirstDeliveryDateColumn
				.setCellFactory(new Callback<TableColumn<Product, LocalDate>, TableCell<Product, LocalDate>>() {

					@Override
					public TableCell<Product, LocalDate> call(TableColumn<Product, LocalDate> param) {
						TextFieldTableCell<Product, LocalDate> cell = new TextFieldTableCell<Product, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		prodIssueDateColumn
				.setCellFactory(new Callback<TableColumn<Product, LocalDate>, TableCell<Product, LocalDate>>() {

					@Override
					public TableCell<Product, LocalDate> call(TableColumn<Product, LocalDate> param) {
						TextFieldTableCell<Product, LocalDate> cell = new TextFieldTableCell<Product, LocalDate>();
						cell.setConverter(Main.dateConvertor);
						return cell;
					}
				});
		// clearButton.setDisable(HawkerLoginController.loggedInHawker!=null);
		clearButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					clearClicked(new ActionEvent());
				}

			}
		});
		// filterButton.setDisable(HawkerLoginController.loggedInHawker!=null);
		filterButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					filterClicked(new ActionEvent());
				}

			}
		});
		saveButton.setDisable(HawkerLoginController.loggedInHawker != null);
		saveButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					saveButtonClicked(new ActionEvent());
					addProdName.requestFocus();
				}

			}
		});
		resetButton.setDisable(HawkerLoginController.loggedInHawker != null);
		resetButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					resetButtonClicked(new ActionEvent());
					addProdName.requestFocus();
				}

			}
		});
		addProdExtraButton.setDisable(HawkerLoginController.loggedInHawker != null);
		addProdExtraButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addProdExtraClicked(new ActionEvent());
				}

			}
		});
		// productsTable.setDisable(HawkerLoginController.loggedInHawker !=
		// null);
		productsTable.setRowFactory(new Callback<TableView<Product>, TableRow<Product>>() {

			@Override
			public TableRow<Product> call(TableView<Product> param) {
				final TableRow<Product> row = new TableRow<>();
				MenuItem mnuDel = new MenuItem("Delete Product");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							deleteProduct(productRow);
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit Product");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							showEditProductDialog(productRow);
							productsTable.refresh();
						}
					}

				});
				MenuItem mnuView = new MenuItem("View Product");
				mnuView.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							showViewProductDialog(productRow);
						}
					}

				});

				MenuItem mnuDuplicate = new MenuItem("Duplicate Product");
				mnuDuplicate.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							dupProdClicked(productRow);
							refreshProductsTable();
						}
					}

				});
				MenuItem mnuAddSpcl = new MenuItem("Add Special Price");
				mnuAddSpcl.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							addProductSpecialPrice(productRow);

						}
					}

				});
				MenuItem mnuChangeDay = new MenuItem("Change Day");
				mnuChangeDay.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						if (productRow != null) {
							Dialog<ButtonType> billWarning = new Dialog<ButtonType>();
							billWarning.setTitle("Change Product Day");
							billWarning.setHeaderText("Please select the new Product Day");
							ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
							billWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Existing DayOfWeek"), 0, 0);
							Label existingDOW = new Label(productRow.getDow());
							ComboBox<String> dowTF = new ComboBox<String>();
							dowTF.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
							dowTF.getItems().remove(productRow.getDow());
							dowTF.getSelectionModel().selectFirst();
							// pauseDP.setDisable(true);
							grid.add(existingDOW, 1, 0);
							grid.add(new Label("New DayOfWeek"), 0, 1);
							grid.add(dowTF, 1, 1);
							
							billWarning.getDialogPane().setContent(grid);
							Button saveBtn = (Button) billWarning.getDialogPane().lookupButton(saveButtonType);

							saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

								try {
									if(productRow.getType().equals("Magazine") && productRow.getSupportingFreq().equals("Weekly")){
										
									}else{

										Notifications.create().title("Invalid product")
												.text("You can change DayOfWeek only for Weekly Magazine.")
												.hideAfter(Duration.seconds(5)).showError();
										btnevent.consume();
									}
										
								} catch (NumberFormatException e) {
									Notifications.create().title("Invalid value")
											.text("Please enter only NUMERIC values in Total Due.")
											.hideAfter(Duration.seconds(5)).showError();

									Main._logger.debug("Error :",e);
									e.printStackTrace();
									btnevent.consume();
								} catch (Exception e) {
									Main._logger.debug("Error :",e);
									e.printStackTrace();
								}

							});
							Optional<ButtonType> result = billWarning.showAndWait();
							if (result.isPresent() && result.get() == saveButtonType) {
								productRow.setDow(dowTF.getSelectionModel().getSelectedItem());
								productRow.updateProductRecord();
								Task<Void> task = new Task<Void>() {
									Notifications n = Notifications.create().title("Please wait")
											.text("Updating DayOfWeek in all subscriptions for this product.");
									

									@Override
									protected Void call() throws Exception {
										updateSubscriptionDOW(productRow,dowTF.getSelectionModel().getSelectedItem());
										Platform.runLater(new Runnable() {

											@Override
											public void run() {

												n.hideAfter(Duration.seconds(10)).position(Pos.CENTER)
														.showInformation();
												refreshProductsTable();
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
				

				MenuItem mnuChangeFreq = new MenuItem("Change Frequency");
				mnuChangeFreq.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Product productRow = productsTable.getSelectionModel().getSelectedItem();
						Boolean valid = true;
						if(productRow == null){
							valid=false;
							Notifications.create().title("Product not selected.").text("Please select a product first.").hideAfter(Duration.seconds(5)).showError();
						}
						if(valid  && !productRow.getType().equalsIgnoreCase("Magazine")){

							valid=false;
							Notifications.create().title("Select a magazine.").text("Please select a Magazine only.").hideAfter(Duration.seconds(5)).showError();
						}
						if(valid  && !(productRow.getSupportingFreq().split(",").length==1)){

							valid=false;
							Notifications.create().title("Multiple frequencies not supported.").text("Please select a Magazine having single frequency only.").hideAfter(Duration.seconds(5)).showError();
						}
						if (valid) {
							Dialog<ButtonType> freqWarning = new Dialog<ButtonType>();
							freqWarning.setTitle("Change Product Frequency");
							freqWarning.setHeaderText("Please select the new Product Frequency");
							ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
							freqWarning.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
							GridPane grid = new GridPane();
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(20, 150, 10, 10));

							grid.add(new Label("Current Frequency"), 0, 0);
							Label existingFreq = new Label(productRow.getSupportingFreq());
							ComboBox<String> freqTF = new ComboBox<String>();
							freqTF.getItems().addAll("Weekly","14 Days", "15 Days", "Monthly", "Quarterly", "Half Yearly", "Yearly");
							freqTF.getItems().remove(productRow.getSupportingFreq());
							ComboBox<String> dowTF = new ComboBox<String>();
							dowTF.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
//							dowTF.getItems().remove(productRow.getDow());
							grid.add(new Label("New DayOfWeek"), 0, 2);
							grid.add(dowTF, 1, 2);
							freqTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue,
										String newValue) {
									if(newValue!=null && newValue.equalsIgnoreCase("Weekly")){
										dowTF.setVisible(true);

										dowTF.getSelectionModel().selectFirst();
									} else {
										dowTF.getSelectionModel().clearSelection();
										dowTF.setVisible(false);
									}
									
								}
							});
							freqTF.getSelectionModel().selectFirst();
							// pauseDP.setDisable(true);
							grid.add(existingFreq, 1, 0);
							grid.add(new Label("New Frequency"), 0, 1);
							grid.add(freqTF, 1, 1);
							
							freqWarning.getDialogPane().setContent(grid);
							Button saveBtn = (Button) freqWarning.getDialogPane().lookupButton(saveButtonType);

							/*saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

								try {
									if(productRow.getType().equals("Magazine") && productRow.getSupportingFreq().equals("Weekly")){
										
									}else{

										Notifications.create().title("Invalid product")
												.text("You can change DayOfWeek only for Weekly Magazine.")
												.hideAfter(Duration.seconds(5)).showError();
										btnevent.consume();
									}
										
								} catch (NumberFormatException e) {
									Notifications.create().title("Invalid value")
											.text("Please enter only NUMERIC values in Total Due.")
											.hideAfter(Duration.seconds(5)).showError();

									Main._logger.debug("Error :",e);
									e.printStackTrace();
									btnevent.consume();
								} catch (Exception e) {
									Main._logger.debug("Error :",e);
									e.printStackTrace();
								}

							});*/
							Optional<ButtonType> result = freqWarning.showAndWait();
							if (result.isPresent() && result.get() == saveButtonType) {
								productRow.setSupportingFreq(freqTF.getSelectionModel().getSelectedItem());
								if(!freqTF.getSelectionModel().getSelectedItem().equalsIgnoreCase("Weekly")){
									productRow.setDow(null);
								}else {
									productRow.setDow(dowTF.getSelectionModel().getSelectedItem());
								}
								productRow.updateProductRecord();
								Task<Void> task = new Task<Void>() {
									Notifications n = Notifications.create().title("Please wait")
											.text("Updating Frequency in all subscriptions for this product.");
									

									@Override
									protected Void call() throws Exception {
										updateSubscriptionFreq(productRow,freqTF.getSelectionModel().getSelectedItem(), dowTF.getSelectionModel().getSelectedItem());
										Platform.runLater(new Runnable() {

											@Override
											public void run() {

												n.hideAfter(Duration.seconds(5)).showInformation();
												refreshProductsTable();
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

				if (HawkerLoginController.loggedInHawker == null) {
					ContextMenu menu = new ContextMenu();
					if (HawkerLoginController.loggedInHawker != null) {
						menu.getItems().addAll(mnuEdit, mnuView, mnuDuplicate, mnuAddSpcl);
					} else {
						menu.getItems().addAll(mnuEdit, mnuView, mnuDuplicate, mnuDel, mnuAddSpcl,mnuChangeDay, mnuChangeFreq);
					}

					row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu)
							.otherwise((ContextMenu) null));
				}
				if (HawkerLoginController.loggedInHawker != null) {
					ContextMenu menu = new ContextMenu();
					menu.getItems().addAll(mnuView);
					row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu)
							.otherwise((ContextMenu) null));
				} 
				return row;
			}
		});

		productsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				if (newValue != null) {
					refreshProdSpecialPriceTable();
				}

			}
		});

		addProdType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				switch (newValue) {
				case "Newspaper":
					addProdPrice.setDisable(true);
					addProdMonday.setDisable(false);
					addProdTuesday.setDisable(false);
					addProdWednesday.setDisable(false);
					addProdThursday.setDisable(false);
					addProdFriday.setDisable(false);
					addProdSaturday.setDisable(false);
					addProdSunday.setDisable(false);
					addProdFreq.setDisable(false);
					break;
				case "Magazine":
					addProdPrice.setDisable(false);
					addProdMonday.setDisable(true);
					addProdTuesday.setDisable(true);
					addProdWednesday.setDisable(true);
					addProdThursday.setDisable(true);
					addProdFriday.setDisable(true);
					addProdSaturday.setDisable(true);
					addProdSunday.setDisable(true);
					addProdFreq.setDisable(false);
					break;
				}

			}
		});
		// spclPriceTable.setDisable(HawkerLoginController.loggedInHawker !=
		// null);
		spclPriceTable.setRowFactory(new Callback<TableView<ProductSpecialPrice>, TableRow<ProductSpecialPrice>>() {

			@Override
			public TableRow<ProductSpecialPrice> call(TableView<ProductSpecialPrice> param) {
				TableRow<ProductSpecialPrice> row = new TableRow<ProductSpecialPrice>();
				MenuItem mnuDel = new MenuItem("Delete Product Special Price");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						ProductSpecialPrice prodSpclRow = spclPriceTable.getSelectionModel().getSelectedItem();
						if (prodSpclRow != null) {
							deleteProductSpclPrice(prodSpclRow);
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit Product Special Price");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						ProductSpecialPrice prodSpclRow = spclPriceTable.getSelectionModel().getSelectedItem();
						if (prodSpclRow != null) {
							showEditProductSpclDialog(prodSpclRow);
							refreshProdSpecialPriceTable();
						}
					}

				});

				if (HawkerLoginController.loggedInHawker == null) {
					ContextMenu menu = new ContextMenu();
					menu.getItems().addAll(mnuEdit,mnuDel);
					row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu)
							.otherwise((ContextMenu) null));
				}
				return row;
			}
		});
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
		if (HawkerLoginController.loggedInHawker != null)
			duplicateProdCatButton.setVisible(false);
		duplicateProdCatButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					showDuplicateProductsFromToCategory();
				}

			}
		});
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
		billCategoryTF.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue!=null)
					refreshProductsTable();
					refreshProdSpecialPriceTable();
			}
		});
	}

	@FXML
	void addProdExtraClicked(ActionEvent event) {

		try {

			Dialog<String> addProductDialog = new Dialog<String>();
			addProductDialog.setTitle("Add new product");
			addProductDialog.setHeaderText("Add new products data below.");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			addProductDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CLOSE);
			Button saveButton = (Button) addProductDialog.getDialogPane().lookupButton(saveButtonType);
			FXMLLoader addProductsLoader = new FXMLLoader(getClass().getResource("AddProductsExtraScreen.fxml"));
			Parent addProductstGrid = (Parent) addProductsLoader.load();
			AddProductsExtraScreenController addProductsController = addProductsLoader
					.<AddProductsExtraScreenController> getController();
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addProductsController.isValid()) {
					addProductsController.addProduct();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Product created")
							.text("Product created successfully.").showInformation();
					reloadData();
				} else {
					btnEvent.consume();
				}
			});
			addProductDialog.getDialogPane().setContent(addProductstGrid);
			addProductsController.setupBindings();

			addProductDialog.setResultConverter(dialogButton -> {
				if (dialogButton != saveButtonType) {
					return null;
				}
				return null;
			});

			Optional<String> updatedProduct = addProductDialog.showAndWait();
			// refreshCustomerTable();

			updatedProduct.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					addProductsController.releaseVariables();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	void dupProdClicked(Product productRow) {

		try {

			Dialog<String> addProductDialog = new Dialog<String>();
			addProductDialog.setTitle("Add new product");
			addProductDialog.setHeaderText("Add new products data below.");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			addProductDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CLOSE);
			Button saveButton = (Button) addProductDialog.getDialogPane().lookupButton(saveButtonType);
			FXMLLoader addProductsLoader = new FXMLLoader(getClass().getResource("AddProductsExtraScreen.fxml"));
			Parent addProductstGrid = (Parent) addProductsLoader.load();
			AddProductsExtraScreenController addProductsController = addProductsLoader
					.<AddProductsExtraScreenController> getController();
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				if (addProductsController.isValid()) {
					addProductsController.addProduct();
					Notifications.create().hideAfter(Duration.seconds(5)).title("Product created")
							.text("Product created successfully.").showInformation();
					reloadData();
				} else {
					btnEvent.consume();
				}
			});
			addProductDialog.getDialogPane().setContent(addProductstGrid);
			addProductsController.setupBindings();
			addProductsController.setupDuplicateBindings(productRow);

			addProductDialog.setResultConverter(dialogButton -> {
				if (dialogButton != saveButtonType) {
					return null;
				}
				return null;
			});

			Optional<String> updatedProduct = addProductDialog.showAndWait();
			// refreshCustomerTable();

			updatedProduct.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {

					// addProductDialog.showAndWait();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	@FXML
	void clearClicked(ActionEvent event) {
		filteredData = new FilteredList<>(productValues, p -> true);
		SortedList<Product> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(productsTable.comparatorProperty());
		productsTable.setItems(sortedData);
		productsTable.getSelectionModel().clearSelection();
	}

	@FXML
	void filterClicked(ActionEvent event) {
		TextInputDialog productFilterDialog = new TextInputDialog(searchText);
		productFilterDialog.setTitle("Filter Products");
		productFilterDialog.setHeaderText("Enter the filter text");
		Optional<String> returnValue = productFilterDialog.showAndWait();
		if (returnValue.isPresent()) {
			try {
				searchText = returnValue.get();
				filteredData.setPredicate(new Predicate<Product>() {

					@Override
					public boolean test(Product product) {
						if (searchText == null || searchText.isEmpty())
							return true;
						else if (product.getName() != null
								&& product.getName().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (product.getType() != null
								&& product.getType().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (product.getSupportingFreq() != null
								&& product.getSupportingFreq().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						else if (product.getBillCategory() != null
								&& product.getBillCategory().toUpperCase().contains(searchText.toUpperCase()))
							return true;
						return false;

					}
				});

				SortedList<Product> sortedData = new SortedList<>(filteredData);
				sortedData.comparatorProperty().bind(productsTable.comparatorProperty());
				productsTable.setItems(sortedData);
				productsTable.refresh();

			} catch (NumberFormatException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid value entered")
						.text("Please enter numeric value only").showError();
			}
		}
	}

	@FXML
	void resetButtonClicked(ActionEvent event) {
		addProdName.clear();
		addProdType.getSelectionModel().clearSelection();
		addProdFreq.getCheckModel().clearChecks();
		addProdPrice.clear();
		addProdMonday.clear();
		addProdTuesday.clear();
		addProdWednesday.clear();
		addProdThursday.clear();
		addProdFriday.clear();
		addProdSaturday.clear();
		addProdSunday.clear();
	}

	public boolean isValid() {
		boolean valid = true;
		if (addProdName.getText().isEmpty() || addProdName.getText() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Name").text("Product name cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (codeTF.getText().isEmpty() || codeTF.getText() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Code").text("Product code cannot be left empty.")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (addProdType.getSelectionModel().getSelectedItem() == null) {
			valid = false;
			Notifications.create().text("Invalid Product Type Selection")
					.text("Type selection cannot be left empty and must be selected").hideAfter(Duration.seconds(5))
					.showError();
		}
		if (addProdFreq.getCheckModel().isEmpty()) {
			valid = false;
			Notifications.create().text("Invalid Frequency")
					.text("Product Frequency selection cannot be left empty and must be selected")
					.hideAfter(Duration.seconds(5)).showError();
		}
		if (productExistsInCategory(addProdName.getText(), billCategoryTF.getSelectionModel().getSelectedItem())) {
			valid = false;
			Notifications.create().text("Duplicate Product in Bill Category")
					.text("Product Name already exists in this bill category.").hideAfter(Duration.seconds(5))
					.showError();
		}
		return valid;
	}

	private boolean productExistsInCategory(String name, String category) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String query = "select count(*) from products where lower(name)=? and lower(bill_category)=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name.toLowerCase());
			stmt.setString(2, category.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0)
					return true;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return false;
	}

	@FXML
	void saveButtonClicked(ActionEvent event) {
		PreparedStatement insertLineNum = null;
		String insertStatement = "INSERT INTO PRODUCTS(NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE,BILL_CATEGORY) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = Main.dbConnection;
		try {
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			insertLineNum = con.prepareStatement(insertStatement);
			insertLineNum.setString(1, addProdName.getText());
			insertLineNum.setString(2, addProdType.getSelectionModel().getSelectedItem());
			insertLineNum.setString(3, supportedFreq(addProdFreq.getCheckModel().getCheckedItems()));
			insertLineNum.setDouble(4,
					Double.parseDouble(addProdMonday.getText().isEmpty() ? "0" : addProdMonday.getText()));
			insertLineNum.setDouble(5,
					Double.parseDouble(addProdTuesday.getText().isEmpty() ? "0" : addProdTuesday.getText()));
			insertLineNum.setDouble(6,
					Double.parseDouble(addProdWednesday.getText().isEmpty() ? "0" : addProdWednesday.getText()));
			insertLineNum.setDouble(7,
					Double.parseDouble(addProdThursday.getText().isEmpty() ? "0" : addProdThursday.getText()));
			insertLineNum.setDouble(8,
					Double.parseDouble(addProdFriday.getText().isEmpty() ? "0" : addProdFriday.getText()));
			insertLineNum.setDouble(9,
					Double.parseDouble(addProdSaturday.getText().isEmpty() ? "0" : addProdSaturday.getText()));
			insertLineNum.setDouble(10,
					Double.parseDouble(addProdSunday.getText().isEmpty() ? "0" : addProdSunday.getText()));
			insertLineNum.setDouble(11,
					Double.parseDouble(addProdPrice.getText().isEmpty() ? "0" : addProdPrice.getText()));
			insertLineNum.setString(12, codeTF.getText());
			insertLineNum.setString(13, dowTF.getSelectionModel().getSelectedItem());
			insertLineNum.setDate(14, Date.valueOf(firstDeliveryDate.getValue()));
			insertLineNum.setDate(15, Date.valueOf(issueDate.getValue()));
			insertLineNum.setString(16, billCategoryTF.getSelectionModel().getSelectedItem());
			insertLineNum.execute();
			resetButtonClicked(event);
			refreshProductsTable();
			clearClicked(event);
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			Notifications.create().title("Invalid value")
					.text("Please enter only numbers for Price and Monday - Sunday fields")
					.hideAfter(Duration.seconds(5)).showError();
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
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

	public void refreshProductsTable() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					productValues.clear();
					PreparedStatement stmt;
					if (HawkerLoginController.loggedInHawker == null) {
						if (showAllRadioButton.isSelected()) {
							stmt = con.prepareStatement(
									"select PRODUCT_ID, NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE, bill_Category from products order by name");
							
						} else {
							stmt = con.prepareStatement(
									"select PRODUCT_ID, NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE, bill_Category from products where bill_category=? order by name");
							stmt.setString(1, billCategoryTF.getSelectionModel().getSelectedItem());
						}
						
					} else {
						stmt = con.prepareStatement(
								"SELECT prod.PRODUCT_ID, prod.NAME, prod.TYPE, prod.SUPPORTED_FREQ, prod.MONDAY, prod.TUESDAY, prod.WEDNESDAY, prod.THURSDAY, prod.FRIDAY, prod.SATURDAY, prod.SUNDAY, prod.PRICE, prod.CODE, prod.DOW, prod.FIRST_DELIVERY_DATE, prod.ISSUE_DATE, prod.bill_Category FROM products prod, point_name pn, hawker_info hwk where hwk.HAWKER_CODE = ? and hwk.POINT_NAME = pn.name and pn.BILL_CATEGORY = prod.BILL_CATEGORY ORDER BY name");
						stmt.setString(1, HawkerLoginController.loggedInHawker.getHawkerCode());
					}
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						productValues.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
								rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13),
								rs.getString(14), rs.getDate(15) == null ? null : rs.getDate(15).toLocalDate(),
								rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17)));
					}
					rs.close();
					stmt.close();
					if (!productValues.isEmpty()) {
						filteredData = new FilteredList<>(productValues, p -> true);
						SortedList<Product> sortedData = new SortedList<>(filteredData);
						sortedData.comparatorProperty().bind(productsTable.comparatorProperty());
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								productsTable.setItems(sortedData);
								productsTable.refresh();
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

	public void populateProdFreqValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				synchronized (this) {
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
						addProdFreq.getItems().clear();
						addProdFreq.getItems().addAll(freqValues);
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
			}

		};
		new Thread(task).start();
	}

	public void populateProdTypeValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				synchronized (this) {
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
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								addProdType.getItems().clear();
								addProdType.getItems().addAll(productTypeValues);
							}
						});
						rs.close();
						stmt.close();
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

	private void deleteProduct(Product productRow) {
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

				String deleteString = "delete from products where product_id=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, productRow.getProductId());
				deleteStmt.executeUpdate();
				con.commit();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of product was successful").showInformation();
				productValues.remove(productRow);
				productsTable.refresh();

			} catch (SQLException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of line distributor has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}
		}
	}

	private void showEditProductDialog(Product productRow) {
		int selectedIndex = productsTable.getSelectionModel().selectedIndexProperty().get();
		try {

			Dialog<Product> editProductDialog = new Dialog<Product>();
			editProductDialog.setTitle("Edit Product data");
			editProductDialog.setHeaderText("Update the Product data below");

			// Set the button types.
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			editProductDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
			Button saveButton = (Button) editProductDialog.getDialogPane().lookupButton(saveButtonType);

			FXMLLoader editProductLoader = new FXMLLoader(getClass().getResource("EditProducts.fxml"));
			Parent editProductsGrid = (Parent) editProductLoader.load();
			EditProductsController editProductsController = editProductLoader.<EditProductsController> getController();

			editProductDialog.getDialogPane().setContent(editProductsGrid);
			editProductsController.setProduct(productRow);
			editProductsController.setupBindings();
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				boolean valid = editProductsController.isValid();
				if (!valid) {
					Notifications.create().title("Invalid Product")
							.text("Please provide appropriate values for product").hideAfter(Duration.seconds(5))
							.showError();
					btnEvent.consume();
				} 
//				else {
//					editProductsController.returnUpdatedProduct();
//				}
			});

			editProductDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Product edittedProduct = editProductsController.returnUpdatedProduct();
					return edittedProduct;
				}
				return null;
			});

			Optional<Product> updatedProduct = editProductDialog.showAndWait();
			// refreshCustomerTable();

			updatedProduct.ifPresent(new Consumer<Product>() {

				@Override
				public void accept(Product t) {

					productValues.add(selectedIndex, t);
					productValues.remove(productRow);
					productsTable.getSelectionModel().select(t);
					productsTable.getSelectionModel().getSelectedItem().updateProductRecord();
					editProductsController.releaseVariables();
				}
			});

		} catch (IOException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private void showViewProductDialog(Product productRow) {
		int selectedIndex = productsTable.getSelectionModel().selectedIndexProperty().get();
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

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	public void refreshProdSpecialPriceTable() {
		/*Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {*/
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					spclPriceTable.getItems().clear();
					prodSpclPriceValues=FXCollections.observableArrayList();
					if(productsTable.getSelectionModel().getSelectedItem()!=null){
						PreparedStatement stmt = con.prepareStatement(
								"select SPCL_PRICE_ID, PRODUCT_ID, FULL_DATE, PRICE from prod_spcl_price where product_id=? order by full_date desc");
						
						stmt.setLong(1, productsTable.getSelectionModel().getSelectedItem().getProductId());
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							prodSpclPriceValues.add(new ProductSpecialPrice(rs.getLong(1), rs.getLong(2),
									rs.getDate(3).toLocalDate(), rs.getDouble(4)));
						}
						rs.close();
						stmt.close();
					}
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							spclPriceTable.setItems(prodSpclPriceValues);
							spclPriceTable.refresh();							
						}
					});
					
				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				/*return null;
			}

		};
		new Thread(task).start();*/

	}

	private void addProductSpecialPrice(Product productRow) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Add Product Special Price");
		dialog.setHeaderText("Add product special price details below");

		// Set the button types.
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		ScrollPane scrollPane = new ScrollPane();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		DatePicker dp = new DatePicker(LocalDate.now());
		dp.setConverter(Main.dateConvertor);
		dp.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					dp.setValue(dp.getConverter().fromString(dp.getEditor().getText()));
				}
			}
		});
		TextField priceTF = new TextField();
		grid.add(new Label("Date :"), 0, 0);
		grid.add(new Label("Name"), 0, 1);
		grid.add(dp, 1, 0);
		grid.add(priceTF, 1, 1);

		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.addEventFilter(ActionEvent.ACTION, btnevent -> {
			if (!prodSpclPriceExistsForDate(dp.getValue())) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con
							.prepareStatement("insert into prod_spcl_price(product_id, full_date,price) values(?,?,?)");
					stmt.setLong(1, productsTable.getSelectionModel().getSelectedItem().getProductId());
					stmt.setDate(2, Date.valueOf(dp.getValue()));
					stmt.setDouble(3, Double.parseDouble(priceTF.getText()));
					stmt.executeUpdate();
					refreshProdSpecialPriceTable();
				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
			} else {
				Notifications.create().title("Duplicate!").text("Duplicate Product special price exists on this date")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
		});

		scrollPane.setContent(grid);

		dialog.getDialogPane().setContent(scrollPane);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				return "Success";
			}
			return null;
		});

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result != null) {
				// updateHouseSequences(result.get());
			}
		}

	}

	private boolean prodSpclPriceExistsForDate(LocalDate value) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con.prepareStatement(
					"select full_date, price, product_id, SPCL_PRICE_ID from prod_spcl_price where product_id=? and full_date=?");
			stmt.setLong(1, productsTable.getSelectionModel().getSelectedItem().getProductId());
			stmt.setDate(2, Date.valueOf(value));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
		return false;
	}

	private void deleteProductSpclPrice(ProductSpecialPrice prodSpclRow) {

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

				String deleteString = "delete from prod_spcl_price where SPCL_PRICE_ID=?";
				PreparedStatement deleteStmt = con.prepareStatement(deleteString);
				deleteStmt.setLong(1, prodSpclRow.getSpclPriceId());
				deleteStmt.executeUpdate();
				con.commit();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete Successful")
						.text("Deletion of product was successful").showInformation();
				refreshProdSpecialPriceTable();

			} catch (SQLException e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of product special price has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :",e);
				e.printStackTrace();
			}
		}
	}

	public void populateBillCategoryValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				synchronized (this) {

					try {

						Connection con = Main.dbConnection;
						if (con.isClosed()) {
							con = Main.reconnect();
						}
						billCategoryValues=FXCollections.observableArrayList();
						PreparedStatement stmt = null;
						ResultSet rs = null;
						if(HawkerLoginController.loggedInHawker!=null){
							stmt = con.prepareStatement("select distinct bill_Category from point_name where name =?");
							stmt.setString(1, HawkerLoginController.loggedInHawker.getPointName());
							rs = stmt.executeQuery();
							if (rs.next()) {
								billCategoryValues.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									billCategoryTF.getItems().clear();
									billCategoryTF.setItems(billCategoryValues);
									billCategoryTF.getSelectionModel().selectFirst();
									billCategoryTF.setDisable(true);
									
								}
							});
						}
						else {
							stmt = con.prepareStatement(
									"select distinct bill_Category from point_name order by bill_category");
							rs = stmt.executeQuery();
							while (rs.next()) {
								billCategoryValues.add(rs.getString(1));
							}
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									billCategoryTF.getItems().clear();
									billCategoryTF.setItems(billCategoryValues);
									new AutoCompleteComboBoxListener<>(billCategoryTF);
								}
							});
						}
						
						rs.close();
						stmt.close();
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

	private void showEditProductSpclDialog(ProductSpecialPrice prodSpclRow) {
		try {
			
			TextInputDialog editProductSpclDialog = new TextInputDialog();
			editProductSpclDialog.setTitle("Edit Product Special Price");
			editProductSpclDialog.setHeaderText("Update the Product Special Price below");

			// Set the button types.
//			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
//			editProductSpclDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
			Button saveButton = (Button) editProductSpclDialog.getDialogPane().lookupButton(ButtonType.OK);

			editProductSpclDialog.getEditor().setText((new Double(prodSpclRow.getPrice())).toString());
			saveButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
				boolean valid=true;
				if(editProductSpclDialog.getEditor().getText()!=null){
						try {
							double d = Double.parseDouble(editProductSpclDialog.getEditor().getText().trim());
						} catch (NumberFormatException e) {
							Main._logger.debug("Error :",e);
							Notifications.create().title("Invalid Number").text("Please enter valid number only").hideAfter(Duration.seconds(5)).showError();
							e.printStackTrace();
							valid=false;
						} catch(Exception e){
							Main._logger.debug("Error :",e);
							Notifications.create().title("Invalid Number").text("Please enter valid number only").hideAfter(Duration.seconds(5)).showError();
							e.printStackTrace();
							valid=false;
						}
				} else {
					valid=false;
				}
				if(valid){
					prodSpclRow.setPrice(Double.parseDouble(editProductSpclDialog.getEditor().getText().trim()));
					prodSpclRow.updateProductSpecialPriceRecord();
					Notifications.create().title("Successful").text("Product Special Price updated successfully.").hideAfter(Duration.seconds(5)).showInformation();
				}
			});

			/*editProductSpclDialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Product edittedProduct = editProductsController.returnUpdatedProduct();
					return edittedProduct;
				}
				return null;
			});*/

			Optional<String> updatedProduct = editProductSpclDialog.showAndWait();
			// refreshCustomerTable();

			updatedProduct.ifPresent(new Consumer<String>() {

				@Override
				public void accept(String t) {
					refreshProdSpecialPriceTable();
				}
			});

		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	@FXML
	public void showDuplicateProductsFromToCategory() {

		ObservableList<String> fullBillCategoryValues = FXCollections.observableArrayList();
		ObservableList<String> emptyBillCategoryValues = FXCollections.observableArrayList();

		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			fullBillCategoryValues.clear();
			PreparedStatement stmt = con
					.prepareStatement("select distinct bill_Category from products order by bill_Category");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				fullBillCategoryValues.add(rs.getString(1));
			}
			billCategoryTF.getItems().clear();
			billCategoryTF.getItems().addAll(fullBillCategoryValues);

			stmt = con.prepareStatement("select distinct bill_Category from point_name order by bill_Category");
			rs = stmt.executeQuery();
			while (rs.next()) {
				emptyBillCategoryValues.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			emptyBillCategoryValues.removeAll(fullBillCategoryValues);

			Dialog<ButtonType> duplicateWarning = new Dialog<ButtonType>();
			duplicateWarning.setTitle("Duplicate Bill Category Products");
			duplicateWarning.setHeaderText("Please select source and destination bill category to duplicate products");
			duplicateWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			ComboBox<String> sourceBox = new ComboBox<String>();
			sourceBox.getItems().addAll(fullBillCategoryValues);
			ComboBox<String> destBox = new ComboBox<String>();
			destBox.getItems().addAll(emptyBillCategoryValues);
			sourceBox.getSelectionModel().selectFirst();
			destBox.getSelectionModel().selectFirst();
			grid.add(new Label("From Bill Category :"), 0, 0);
			grid.add(new Label("To Bill Category :"), 0, 1);

			grid.add(destBox, 1, 1);
			grid.add(sourceBox, 1, 0);
			duplicateWarning.getDialogPane().setContent(grid);
			// Button yesButton = (Button)
			// duplicateWarning.getDialogPane().lookupButton(ButtonType.YES);
			// yesButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
			// if
			// (dp.getValue().isBefore(sourceBox.getSelectionModel().getSelectedItem().getStartDate()))
			// {
			// Notifications.create().title("Invalid stop date")
			// .text("Stop date should not be before Start date for
			// subscription")
			// .hideAfter(Duration.seconds(5)).showError();
			// btnEvent.consume();
			// }
			// });
			Optional<ButtonType> result = duplicateWarning.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.YES) {
				duplicateProductFromToCategory(sourceBox.getSelectionModel().getSelectedItem(),
						destBox.getSelectionModel().getSelectedItem());
				productsTable.setDisable(true);
				duplicateProdCatButton.setDisable(true);
				Notifications.create().title("Duplicating Products")
						.text("Duplicating products from category : " + sourceBox.getSelectionModel().getSelectedItem()
								+ " to category :" + destBox.getSelectionModel().getSelectedItem()
								+ ". Please wait while this finishes.")
						.hideAfter(Duration.seconds(5)).showInformation();

			}

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}

	}

	private void duplicateProductFromToCategory(String source, String dest) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					ObservableList<Product> sourceProductValues = FXCollections.observableArrayList();
					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					sourceProductValues.clear();
					PreparedStatement stmt;

					stmt = con.prepareStatement(
							"select PRODUCT_ID, NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE, bill_Category from products where bill_Category=? order by name");
					stmt.setString(1, source);

					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						sourceProductValues.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3),
								rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8),
								rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13),
								rs.getString(14), rs.getDate(15) == null ? null : rs.getDate(15).toLocalDate(),
								rs.getDate(16) == null ? null : rs.getDate(16).toLocalDate(), rs.getString(17)));

					}
					rs.close();
					stmt.close();
					Product p = null;
					for (int i = 0; i < sourceProductValues.size(); i++) {
						p = sourceProductValues.get(i);
						PreparedStatement insertLineNum = null;
						String insertStatement = "INSERT INTO PRODUCTS(NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE,BILL_CATEGORY) "
								+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

						try {
							if (con.isClosed()) {
								con = Main.reconnect();
							}
							insertLineNum = con.prepareStatement(insertStatement);
							insertLineNum.setString(1, p.getName());
							insertLineNum.setString(2, p.getType());
							insertLineNum.setString(3, p.getSupportingFreq());
							insertLineNum.setDouble(4, p.getMonday());
							insertLineNum.setDouble(5, p.getTuesday());
							insertLineNum.setDouble(6, p.getWednesday());
							insertLineNum.setDouble(7, p.getThursday());
							insertLineNum.setDouble(8, p.getFriday());
							insertLineNum.setDouble(9, p.getSaturday());
							insertLineNum.setDouble(10, p.getSunday());
							insertLineNum.setDouble(11, p.getPrice());
							insertLineNum.setString(12, p.getCode());
							insertLineNum.setString(13, p.getDow());
							insertLineNum.setDate(14, Date.valueOf(p.getFirstDeliveryDate()));
							insertLineNum.setDate(15, Date.valueOf(p.getIssueDate()));
							insertLineNum.setString(16, dest);
							insertLineNum.execute();
						} catch (SQLException e) {

							Main._logger.debug("Error :",e);
							e.printStackTrace();
						} catch (Exception e) {

							Main._logger.debug("Error :",e);
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}
				refreshProductsTable();
				productsTable.setDisable(false);
				duplicateProdCatButton.setDisable(false);
				Notifications.create()
						.title("Duplicating Products Successful").text("Duplicating products from category : " + source
								+ " to category :" + dest + " was successful.")
						.hideAfter(Duration.seconds(5)).showInformation();
				return null;
			}

		};
		new Thread(task).start();

	}
	

	public void updateSubscriptionDOW(Product prod, String dow) {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update subscription set dow=? where product_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, dow);
			updateStmt.setLong(2, prod.getProductId());
			updateStmt.executeUpdate();
			con.commit();
			updateStmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Product record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private void updateSubscriptionFreq(Product productRow, String freq,
			String dow) {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString;
			PreparedStatement updateStmt ;
			if(!freq.equalsIgnoreCase("Weekly")){
				updateString = "update subscription set FREQUENCY=?, dow=null where product_id=?";
				updateStmt = con.prepareStatement(updateString);
				updateStmt.setString(1, freq);
				updateStmt.setLong(2, productRow.getProductId());
			}else {
				updateString = "update subscription set dow=?, FREQUENCY=? where product_id=?";
				updateStmt = con.prepareStatement(updateString);
				updateStmt.setString(1, dow);
				updateStmt.setString(2, freq);
				updateStmt.setLong(3, productRow.getProductId());
			}
			
			updateStmt.executeUpdate();
			con.commit();
			updateStmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Product record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}
	
	// @Override
	public void reloadData() {
		if (HawkerLoginController.loggedInHawker == null) {
			if (showAllRadioButton.isSelected()) {
				billCategoryTF.getSelectionModel().clearSelection();
				billCategoryTF.setDisable(true);
				refreshProductsTable();
			} else if (filterRadioButton.isSelected()) {
				populateBillCategoryValues();
				billCategoryTF.setDisable(false);
				if (billCategoryTF.getSelectionModel().getSelectedItem() == null) {
					productValues = FXCollections.observableArrayList();
					productsTable.setItems(productValues);
				}
				// refreshCustomerTable();
			}
		} else {

			populateBillCategoryValues();
		}

		freqValues.clear();
//		billCategoryValues.clear();
		populateProdFreqValues();
		populateProdTypeValues();
//		refreshProductsTable();
		if (HawkerLoginController.loggedInHawker != null) {
			inputVBOX.setDisable(true);
		}

		prodAccordian.setExpanded(true);
	}

	// @Override
	public void releaseVariables() {
		filteredData = null;
		searchText = null;

		freqValues = null;
		productValues = null;
		prodSpclPriceValues = null;
		productTypeValues = null;
		billCategoryValues = null;
		freqValues = FXCollections.observableArrayList();
		productValues = FXCollections.observableArrayList();
		prodSpclPriceValues = FXCollections.observableArrayList();
		productTypeValues = FXCollections.observableArrayList();
		billCategoryValues = FXCollections.observableArrayList();
	}

}
