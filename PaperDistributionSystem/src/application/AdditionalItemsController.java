package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.Notifications;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Duration;

public class AdditionalItemsController implements Initializable {

	@FXML
	private Accordion accordianPane;
	@FXML
	private Button addPointButton;
	@FXML
	private Button deleteAllAdsButton;
	@FXML
	private TableView<PointName> pointNamesTable;
	@FXML
	private Button addEmploymentButton;
	@FXML
	private ListView<String> employmentValuesListView;
	@FXML
	private Button addProfileButton;
	@FXML
	private ListView<String> profileValuesListView;

	@FXML
	private TableColumn<PointName, String> pointNameColumn;
	@FXML
	private TableColumn<PointName, String> pointCityColumn;

	@FXML
	private TableColumn<PointName, String> pointCategoryColumn;
	@FXML
	private TableColumn<PointName, Double> pointFeeColumn;

	private ObservableList<String> profileValues = FXCollections.observableArrayList();
	private ObservableList<String> employmentValues = FXCollections.observableArrayList();
	private ObservableList<PointName> pointNameValues = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		pointNameColumn.setCellValueFactory(new PropertyValueFactory<PointName, String>("pointName"));
		pointCityColumn.setCellValueFactory(new PropertyValueFactory<PointName, String>("city"));
		pointCategoryColumn.setCellValueFactory(new PropertyValueFactory<PointName, String>("billCategory"));
		pointFeeColumn.setCellValueFactory(new PropertyValueFactory<PointName, Double>("fee"));
		profileValuesListView.setCellFactory(lv -> {

			ListCell<String> cell = new ListCell<>();

			ContextMenu contextMenu = new ContextMenu();

			MenuItem editItem = new MenuItem();
			editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
			editItem.setOnAction(event -> {
				showEditProfileValue();
			});
			MenuItem deleteItem = new MenuItem();
			deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
			deleteItem.setOnAction(event -> {
				showDeleteProfileValue(cell.getItem());
			});
			contextMenu.getItems().addAll(editItem, deleteItem);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(contextMenu);
				}
			});
			return cell;
		});

		employmentValuesListView.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<>();
			ContextMenu menu = new ContextMenu();
			MenuItem mnuDel = new MenuItem("Delete Employment Value");
			mnuDel.setOnAction(event -> {
				String employmentValue = employmentValuesListView.getSelectionModel().getSelectedItem();
				if (employmentValue != null) {
					deleteEmploymentValue(employmentValue);
				}
			});

			MenuItem mnuEdit = new MenuItem("Edit Employment Value");
			mnuEdit.setOnAction(event -> {
				String employmentValue = employmentValuesListView.getSelectionModel().getSelectedItem();
				editEmploymentValuesClicked(employmentValue);

			});
			menu.getItems().addAll(mnuEdit, mnuDel);
			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(menu);
				}
			});
			return cell;
		});

		pointNamesTable.setRowFactory(new Callback<TableView<PointName>, TableRow<PointName>>() {

			@Override
			public TableRow<PointName> call(TableView<PointName> param) {
				final TableRow<PointName> row = new TableRow<PointName>();
				MenuItem mnuDel = new MenuItem("Delete Point");
				mnuDel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PointName pointRow = pointNamesTable.getSelectionModel().getSelectedItem();
						if (pointRow != null) {
							deletePointName(pointRow);
							reloadData();
						}
					}

				});

				MenuItem mnuEdit = new MenuItem("Edit Point");
				mnuEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PointName pointRow = pointNamesTable.getSelectionModel().getSelectedItem();
						if (pointRow != null) {
							showEditPointNameDialog(pointRow);
							reloadData();
						}
					}

				});
				MenuItem mnuNew = new MenuItem("New Point in same category");
				mnuNew.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PointName pointRow = pointNamesTable.getSelectionModel().getSelectedItem();
						if (pointRow != null) {
							addPointNameInCategory(pointRow);
							reloadData();
						}
					}

				});
				MenuItem mnuRemoveAd = new MenuItem("Remove Advertisement");
				mnuRemoveAd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						PointName pointRow = pointNamesTable.getSelectionModel().getSelectedItem();
						if (pointRow != null) {
							Dialog<ButtonType> dialog = new Dialog<ButtonType>();
							dialog.setTitle("Confirm delete?");
							dialog.setHeaderText("Are you sure you want to remove advertisement banners?");
							dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES,
									ButtonType.NO);
							Optional<ButtonType> result = dialog.showAndWait();
							if (result.isPresent() && result.get() == ButtonType.YES) {
								AmazonS3 s3logoclient = Main.s3logoclient;
								if (s3logoclient.doesObjectExist("pdslogobucket",
										pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV1.jpg")
										&& s3logoclient.doesObjectExist("pdslogobucket",
												pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV2.jpg")) {
									try {
										s3logoclient.deleteObject("pdslogobucket",
												pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV1.jpg");
										s3logoclient.deleteObject("pdslogobucket",
												pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV2.jpg");
										Notifications.create().title("Ad banners deleted successfully.")
												.text("Ad banners of point is now deleted successfully.")
												.hideAfter(Duration.seconds(5)).showInformation();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}

				});
				ContextMenu menu = new ContextMenu();
				menu.getItems().addAll(mnuEdit, mnuDel, mnuNew,mnuRemoveAd);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
				return row;
			}
		});

		addPointButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addPointNameClicked(new ActionEvent());
				}

			}
		});
		addProfileButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addProfileClicked(new ActionEvent());
				}

			}
		});
	}

	private void showDeleteProfileValue(String item) {
		Dialog<ButtonType> deleteWarning = new Dialog<ButtonType>();

		deleteWarning.setTitle("Delete?");
		deleteWarning.setHeaderText("Are you sure you want to delete selected profile value");

		deleteWarning.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = deleteWarning.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {

			try {

				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}

				profileValues.clear();
				PreparedStatement stmt = con
						.prepareStatement("delete from lov_lookup where code = 'PROFILE_VALUES' AND value=?");
				stmt.setString(1, item);
				stmt.executeUpdate();
				Notifications.create().text("Profile deleted").text("Profile value successfully deleted")
						.hideAfter(Duration.seconds(5)).showInformation();
				reloadData();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of profile has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}

	}

	private void showEditProfileValue() {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Edit profile value");
		dialog.setHeaderText("Edit profile value below");

		Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		dialog.getEditor().setText(profileValuesListView.getSelectionModel().getSelectedItem());
		saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

			String profileValue = dialog.getEditor().getText().toLowerCase();

			if (!checkExistingProfileValue(profileValue)) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con.prepareStatement(
							"update lov_lookup set value=? where code = 'PROFILE_VALUES' AND value=?");
					stmt.setString(1, profileValue.toLowerCase());
					stmt.setString(2, profileValuesListView.getSelectionModel().getSelectedItem());
					stmt.executeUpdate();
					con.commit();

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}

				reloadData();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
						.text("Profile value successfully saved.").showInformation();
			} else {

				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid employment value")
						.text("This profile value already exists.").showError();
				btnevent.consume();
			}

		});
		dialog.showAndWait();

	}

	@FXML
	public void addProfileClicked(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Add profile value");
		dialog.setHeaderText("Add new profile value below");

		Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

		saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

			String profileValue = dialog.getEditor().getText();

			if (!checkExistingProfileValue(profileValue)) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con.prepareStatement(
							"insert into lov_lookup(value, code, seq) values(?,'PROFILE_VALUES',(select MAX(seq)+1 as mseq from lov_lookup where code='PROFILE_VALUES' group by code))");
					stmt.setString(1, dialog.getEditor().getText().toLowerCase());
					stmt.executeUpdate();
					con.commit();

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
				reloadData();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
						.text("Profile value successfully added.").showInformation();
			} else {
				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid profile value")
						.text("This profile value already exists.").showError();
			}

		});
		dialog.showAndWait();
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
	public void addEmploymentValuesClicked(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Add employment value");
		dialog.setHeaderText("Add new employment value below");

		Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

		saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

			String empValue = dialog.getEditor().getText().toLowerCase();

			if (!checkExistingEmploymentValue(empValue)) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con.prepareStatement(
							"insert into lov_lookup(value, code, seq) values(?,'EMPLOYMENT_STATUS',select MAX(seq)+1 as mseq from lov_lookup where code='EMPLOYMENT_STATUS' group by code)");
					stmt.setString(1, empValue);
					stmt.executeUpdate();
					con.commit();

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}

				reloadData();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
						.text("Employment value successfully added.").showInformation();
			} else {

				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid employment value")
						.text("This employment value already exists.").showError();
				btnevent.consume();
			}

		});
		dialog.showAndWait();
	}

	public void editEmploymentValuesClicked(String employmentValue) {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Edit employment value");
		dialog.setHeaderText("Edit employment value below");
		dialog.getEditor().setText(employmentValue);
		Button saveBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

		saveBtn.addEventFilter(ActionEvent.ACTION, btnevent -> {

			String empValue = dialog.getEditor().getText().toLowerCase();

			if (!checkExistingEmploymentValue(empValue)) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con.prepareStatement(
							"update lov_lookup set value=? where code = 'EMPLOYMENT_STATUS' AND value=?");
					stmt.setString(1, empValue);
					stmt.setString(2, employmentValue.toLowerCase());
					stmt.executeUpdate();
					con.commit();

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}

				reloadData();

				Notifications.create().hideAfter(Duration.seconds(5)).title("Successful")
						.text("Employment value successfully added.").showInformation();
			} else {

				Notifications.create().hideAfter(Duration.seconds(5)).title("Invalid employment value")
						.text("This employment value already exists.").showError();
				btnevent.consume();
			}

		});
		dialog.showAndWait();
	}

	private void deleteEmploymentValue(String employmentValue) {
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

				PreparedStatement stmt = con
						.prepareStatement("delete from lov_lookup where code='EMPLOYMENT_STATUS' AND value=?");
				stmt.setString(1, employmentValue.toLowerCase());
				stmt.executeUpdate();
				Notifications.create().text("Employment value deleted").text("Employment value successfully deleted")
						.hideAfter(Duration.seconds(5)).showInformation();
				reloadData();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of employment status has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}

	}

	private boolean checkExistingEmploymentValue(String emplValue) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt = con.prepareStatement(
					"select value from lov_lookup where code = 'EMPLOYMENT_STATUS' AND lower(value) =?");
			stmt.setString(1, emplValue.toLowerCase());
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

	private boolean checkExistingPointName(String pointName, Long pointId) {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			PreparedStatement stmt;
			if (pointId == null) {
				stmt = con.prepareStatement("select count(*) from point_name where lower(name) = ?");
			} else {
				stmt = con.prepareStatement("select count(*) from point_name where lower(name) = ? and point_id <> ?");
				stmt.setLong(2, pointId);
			}
			stmt.setString(1, pointName.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
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
	void deleteAllAdsClicked(ActionEvent event) {

		// PointName pointRow = pointNamesTable.getSelectionModel().getSelectedItem();

		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setTitle("Confirm delete?");
		dialog.setHeaderText("Are you sure you want to remove advertisement banners for ALL point names in the system?");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			for (PointName pointRow : pointNameValues) {
				AmazonS3 s3logoclient = Main.s3logoclient;
				boolean success = true;
				if (s3logoclient.doesObjectExist("pdslogobucket",
						pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV1.jpg")
						&& s3logoclient.doesObjectExist("pdslogobucket",
								pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV2.jpg")) {
					try {
						s3logoclient.deleteObject("pdslogobucket",
								pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV1.jpg");
						s3logoclient.deleteObject("pdslogobucket",
								pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV2.jpg");
						
					} catch (Exception e) {
						success = false;
						e.printStackTrace();
					}
					if(success) {
						Notifications.create().title("Ad banners deleted successfully.")
						.text("Ad banners of point is now deleted successfully.").hideAfter(Duration.seconds(5))
						.showInformation();
					} else {
						Notifications.create().title("Error")
						.text("There was some error while deleting some of the AD banners. Please send logs to administrator.")
						.hideAfter(Duration.seconds(10))
						.showError();
					}
				}
			}
		}
	}
	@FXML
	void addPointNameClicked(ActionEvent event) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle("Add a new Point");
		dialog.setHeaderText("Add the point details below");
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		// Iterator<Customer> custIter = customerData.iterator();

		grid.add(new Label("Point Name"), 0, 0);
		grid.add(new Label("Point City"), 0, 1);
		grid.add(new Label("Bill Category"), 0, 2);
		grid.add(new Label("Fee"), 0, 3);
		TextField nameField = new TextField();
		TextField cityField = new TextField();
		TextField billCategoryField = new TextField();
		TextField feeField = new TextField("0.0");
		grid.add(nameField, 1, 0);
		grid.add(cityField, 1, 1);
		grid.add(billCategoryField, 1, 2);
		grid.add(feeField, 1, 3);
		saveButton.addEventFilter(ActionEvent.ACTION, btnevent -> {
			if (nameField.getText().isEmpty() || nameField.getText() == null) {
				Notifications.create().title("Invalid Point Name").text("Please enter some value in Point Name")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (checkExistingPointName(nameField.getText().toLowerCase(), null)) {
				Notifications.create().title("Duplicate Point Name")
						.text("Point with same name already exists, please enter different name.")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (cityField.getText().isEmpty() || cityField.getText() == null) {
				Notifications.create().title("Invalid Point City").text("Please enter some value in Point City")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (billCategoryField.getText().isEmpty() || billCategoryField.getText() == null) {
				Notifications.create().title("Invalid Bill Category").text("Please enter some value in Bill Category")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}

			try {
				Double.parseDouble(feeField.getText());
			} catch (NumberFormatException e) {
				Notifications.create().title("Invalid Fee Value").text("Please enter only NUMBERS in Fee value")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}

		});
		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(nameField.getText().toLowerCase());
				list.add(cityField.getText().toLowerCase());
				list.add(billCategoryField.getText().toLowerCase());
				list.add(feeField.getText());

				return list;
			}
			return null;
		});

		Optional<ArrayList<String>> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result != null) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con
							.prepareStatement("insert into point_name(name,city,bill_category,fee) values(?,?,?,?)");
					stmt.setString(1, result.get().get(0).toLowerCase());
					stmt.setString(2, result.get().get(1).toLowerCase());
					stmt.setString(3, result.get().get(2).toLowerCase());
					stmt.setString(4, result.get().get(3));
					stmt.executeUpdate();
					con.commit();
					pointNamesTable.getItems().clear();
					populatePointNames();
					pointNamesTable.getItems().addAll(pointNameValues);
					pointNamesTable.refresh();
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
			}
		}
	}

	void addPointNameInCategory(PointName point) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle("Add a new Point");
		dialog.setHeaderText("Add the point details below");
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		// Iterator<Customer> custIter = customerData.iterator();

		grid.add(new Label("Point Name"), 0, 0);
		grid.add(new Label("Point City"), 0, 1);
		grid.add(new Label("Bill Category"), 0, 2);
		grid.add(new Label("Fee"), 0, 3);
		TextField nameField = new TextField();
		TextField cityField = new TextField(point.getCity());
		TextField billCategoryField = new TextField(point.getBillCategory());
		TextField feeField = new TextField(point.getFee() + "");
		grid.add(nameField, 1, 0);
		grid.add(cityField, 1, 1);
		grid.add(billCategoryField, 1, 2);
		grid.add(feeField, 1, 3);
		saveButton.addEventFilter(ActionEvent.ACTION, btnevent -> {
			if (nameField.getText().isEmpty() || nameField.getText() == null) {
				Notifications.create().title("Invalid Point Name").text("Please enter some value in Point Name")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (checkExistingPointName(nameField.getText().toLowerCase(), point.getPointId())) {
				Notifications.create().title("Duplicate Point Name")
						.text("Point with same name already exists, please enter different name.")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (cityField.getText().isEmpty() || cityField.getText() == null) {
				Notifications.create().title("Invalid Point City").text("Please enter some value in Point City")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (billCategoryField.getText().isEmpty() || billCategoryField.getText() == null) {
				Notifications.create().title("Invalid Bill Category").text("Please enter some value in Bill Category")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}

			try {
				Double.parseDouble(feeField.getText());
			} catch (NumberFormatException e) {
				Notifications.create().title("Invalid Fee Value").text("Please enter only NUMBERS in Fee value")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}

		});
		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(nameField.getText().toLowerCase());
				list.add(cityField.getText().toLowerCase());
				list.add(billCategoryField.getText().toLowerCase());
				list.add(feeField.getText());

				return list;
			}
			return null;
		});

		Optional<ArrayList<String>> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result != null) {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					PreparedStatement stmt = con
							.prepareStatement("insert into point_name(name,city,bill_category,fee) values(?,?,?,?)");
					stmt.setString(1, result.get().get(0).toLowerCase());
					stmt.setString(2, result.get().get(1).toLowerCase());
					stmt.setString(3, result.get().get(2).toLowerCase());
					stmt.setString(4, result.get().get(3));
					stmt.executeUpdate();
					con.commit();
					pointNamesTable.getItems().clear();
					populatePointNames();
					pointNamesTable.getItems().addAll(pointNameValues);
					pointNamesTable.refresh();
				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
			}
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
			profileValuesListView.getItems().clear();
			profileValuesListView.getItems().addAll(profileValues);
			profileValuesListView.refresh();
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
			employmentValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select value, code, seq, lov_lookup_id from lov_lookup where code='EMPLOYMENT_STATUS' order by seq");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				employmentValues.add(rs.getString(1));
			}
			employmentValuesListView.getItems().clear();
			employmentValuesListView.getItems().addAll(employmentValues);
			employmentValuesListView.refresh();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	public void populatePointNames() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			pointNameValues.clear();
			PreparedStatement stmt = con
					.prepareStatement("select point_id,name,city, bill_category, fee from point_name order by name");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				pointNameValues.add(new PointName(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getDouble(5)));
			}
			pointNamesTable.getItems().clear();
			pointNamesTable.getItems().addAll(pointNameValues);
			pointNamesTable.refresh();
		} catch (SQLException e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :", e);
			e.printStackTrace();
		}

	}

	private void deletePointName(PointName pointRow) {
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

				pointNameValues.clear();
				PreparedStatement stmt = con.prepareStatement("delete from point_name where point_id=?");
				stmt.setLong(1, pointRow.getPointId());
				stmt.executeUpdate();
				Notifications.create().text("Point deleted").text("Point successfully deleted")
						.hideAfter(Duration.seconds(5)).showInformation();
				reloadData();
			} catch (SQLException e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
				Notifications.create().hideAfter(Duration.seconds(5)).title("Delete failed")
						.text("Delete request of point has failed").showError();
			} catch (Exception e) {

				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		}

	}

	private void showEditPointNameDialog(PointName pointRow) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle("Edit Point");
		dialog.setHeaderText("Advertisement banner should be of Width 575px - Height 75px");
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		// Iterator<Customer> custIter = customerData.iterator();

		grid.add(new Label("Point Name"), 0, 0);
		grid.add(new Label("Point City"), 0, 1);
		grid.add(new Label("Bill Category"), 0, 2);
		grid.add(new Label("Fee"), 0, 3);
		grid.add(new Label("Right Advertisement"), 0, 4);
		grid.add(new Label("Width: 180 x Height: 270"), 0, 5);
		grid.add(new Label("Bottom Advertisement"), 0, 6);
		grid.add(new Label("Width: 270 x Height: 75"), 0, 7);
		TextField nameField = new TextField();
		TextField cityField = new TextField();
		TextField billCategoryField = new TextField();
		TextField feeField = new TextField();
		Button topAdvButton = new Button("Top Adv Banner");
		Button bottomAdvButton = new Button("Bottom Adv Banner");

		nameField.setText(pointRow.getPointName());
		cityField.setText(pointRow.getCity());
		billCategoryField.setText(pointRow.getBillCategory());
		feeField.setText(pointRow.getFee() + "");
		grid.add(nameField, 1, 0);
		grid.add(cityField, 1, 1);
		grid.add(billCategoryField, 1, 2);
		grid.add(feeField, 1, 3);
		grid.add(topAdvButton, 1, 4);
		grid.add(bottomAdvButton, 1, 6);

		topAdvButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
			try {
				AmazonS3 s3logoclient = Main.s3logoclient;
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Top Advertisement banner.");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
				File selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
				if (selectedFile != null) {
					Image img = new Image(new FileInputStream(selectedFile), 180, 270, true, false);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(SwingFXUtils.fromFXImage(img, null), "jpg", os);
					InputStream fis = new ByteArrayInputStream(os.toByteArray());
					String uploadFileName = pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV1.jpg";
					// File hwkLogo = new File(uploadFileName);
					s3logoclient.putObject(
							new PutObjectRequest("pdslogobucket", uploadFileName, fis, new ObjectMetadata()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		bottomAdvButton.addEventFilter(ActionEvent.ACTION, btnEvent -> {
			try {
				AmazonS3 s3logoclient = Main.s3logoclient;
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Top Advertisement banner.");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
				File selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
				if (selectedFile != null) {
					Image img = new Image(new FileInputStream(selectedFile), 270, 75, true, false);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(SwingFXUtils.fromFXImage(img, null), "jpg", os);
					InputStream fis = new ByteArrayInputStream(os.toByteArray());
					String uploadFileName = pointRow.getPointName().toUpperCase().replace(' ', '-') + "ADV2.jpg";
					// File hwkLogo = new File(uploadFileName);
					s3logoclient.putObject(
							new PutObjectRequest("pdslogobucket", uploadFileName, fis, new ObjectMetadata()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		saveButton.addEventFilter(ActionEvent.ACTION, btnevent -> {
			if (nameField.getText().isEmpty()) {
				Notifications.create().title("Invalid Point Name").text("Please enter some value in Point Name")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (checkExistingPointName(nameField.getText().toLowerCase(), pointRow.getPointId())) {
				Notifications.create().title("Duplicate Point Name")
						.text("Point with same name already exists, please enter different name.")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (cityField.getText().isEmpty()) {
				Notifications.create().title("Invalid Point City").text("Please enter some value in Point City")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}
			if (billCategoryField.getText().isEmpty()) {
				Notifications.create().title("Invalid Bill Category").text("Please enter some value in Bill Category")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
			}

			try {
				Double.parseDouble(feeField.getText());
			} catch (NumberFormatException e) {
				Notifications.create().title("Invalid Fee Value").text("Please enter only NUMBERS in Fee value")
						.hideAfter(Duration.seconds(5)).showError();
				btnevent.consume();
				Main._logger.debug("Error :", e);
				e.printStackTrace();
			}
		});
		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(nameField.getText());
				list.add(cityField.getText());
				list.add(billCategoryField.getText());
				list.add(feeField.getText());
				return list;
			}
			return null;
		});

		Optional<ArrayList<String>> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result != null) {
				pointRow.setPointName(result.get().get(0).toLowerCase());
				pointRow.setCity(result.get().get(1).toLowerCase());
				pointRow.setBillCategory(result.get().get(2).toLowerCase());
				pointRow.setFee(Double.parseDouble(result.get().get(3)));
				pointRow.updatePointName();
				pointNamesTable.getItems().clear();
				populatePointNames();
				pointNamesTable.getItems().addAll(pointNameValues);
				pointNamesTable.refresh();
			}
		}

	}

	// @Override
	public void reloadData() {
		// profileValuesListView.getItems().clear();
		// pointNamesTable.getItems().clear();
		// employmentValuesListView.getItems().clear();

		populateEmploymentValues();
		populatePointNames();
		populateProfileValues();

	}

	// @Override
	public void releaseVariables() {

		pointNameValues = null;
		employmentValues = null;
		profileValues = null;
		pointNameValues = FXCollections.observableArrayList();
		employmentValues = FXCollections.observableArrayList();
		profileValues = FXCollections.observableArrayList();
	}

}
