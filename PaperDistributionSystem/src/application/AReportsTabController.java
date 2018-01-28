package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfViewerFX.PDFViewer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

public class AReportsTabController implements Initializable {

	@FXML
	private ComboBox<String> hawkerComboBox;
	@FXML
	private ComboBox<String> addPointName;

	@FXML
	ComboBox<String> cityTF;
	@FXML
	private ComboBox<String> addLineNumLOV;
	@FXML
	private ComboBox<String> addLineNumLOV2;
	@FXML
	private ComboBox<String> addLineNumLOV3;
	@FXML
	private ComboBox<String> addLineNumLOV4;

	@FXML
	private AnchorPane pdfPane;

	@FXML
	private Button generateFilteredReportButton;

	@FXML
	private Button hwkAllLineSubButton;
	@FXML
	private Button hwkAllLineSubProdCodeButton;
	@FXML
	private Button hwkAllLineSubProdNameButton;

	@FXML
	private Button lineAllSubButton;
	@FXML
	private Button lineAllSubSmallButton;

	@FXML
	private Label datesList;

	@FXML
	private DatePicker forDate;

	@FXML
	private DatePicker firstForDate;
	private ObservableList<String> hawkerCodeData = FXCollections.observableArrayList();
	private ObservableList<String> hawkerLineNumData = FXCollections.observableArrayList();

	private ObservableList<String> subscriptionTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> paymentTypeValues = FXCollections.observableArrayList();
	private ObservableList<String> frequencyValues = FXCollections.observableArrayList();
	private ObservableList<String> productValues = FXCollections.observableArrayList();
	private ObservableList<Product> productValues2 = FXCollections.observableArrayList();
	private ObservableList<String> durationValues = FXCollections.observableArrayList();
	private ObservableList<String> dowValues = FXCollections.observableArrayList();
	private ObservableList<String> cityValues = FXCollections.observableArrayList();
	private ObservableList<String> pointNameValues = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> dowLOV;
	@FXML
	private ComboBox<String> subscriptionTypeLOV;
	@FXML
	private ComboBox<String> paymentTypeLOV;
	@FXML
	private ComboBox<String> durationLOV;
	@FXML
	private ComboBox<String> frequencyLOV;
	@FXML
	private ComboBox<String> prodNameLOV;
	@FXML
	private ComboBox<String> prodNameLOV1;
	@FXML
	private ComboBox<String> prodNameLOV2;

	@FXML
	private ComboBox<String> statusLOV;


    @FXML
    private ComboBox<String> invoiceDateLOV;

    @FXML
    private Button allLineInvDateSummaryButton1;

	private ObservableList<String> invoiceDatesData = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		hawkerComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					addLineNumLOV.getItems().clear();
					populateLineNumbersForHawkerCode(newValue);
					addLineNumLOV.getSelectionModel().clearSelection();
					populateProducts();
				} else {
					hawkerLineNumData = FXCollections.observableArrayList();
					addLineNumLOV.setItems(hawkerLineNumData);
				}
			}
		});

		firstForDate.setValue(LocalDate.now().plusDays(1));
		firstForDate.setConverter(Main.dateConvertor);
		forDate.setValue(LocalDate.now().plusDays(1));
		forDate.setConverter(Main.dateConvertor);
		prodNameLOV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					if (prodNameLOV.getSelectionModel().getSelectedIndex() > 1) {
						Product prod = productValues2.get(prodNameLOV.getSelectionModel().getSelectedIndex() - 1);
						populateDOWValues(prod);
						datesList.setText("");
						datesList.setText(BillingUtilityClass.findDeliveryDatesForMonth(prod));
					} else {
						datesList.setText("");
						dowValues.clear();
						dowValues.addAll("All", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
								"Sunday");
						dowLOV.setItems(dowValues);
						new AutoCompleteComboBoxListener<String>(dowLOV);
						dowLOV.getSelectionModel().selectFirst();
					}
				}

			}
		});
		/*
		 * prodNameLOV.setConverter(new StringConverter<Product>() {
		 * 
		 * @Override public String toString(Product object) { if (object !=
		 * null) return object.getName(); else return null; }
		 * 
		 * @Override public Product fromString(String string) { while
		 * (productValues.iterator().hasNext()) { Product p =
		 * productValues.iterator().next(); if
		 * (p.getName().equalsIgnoreCase(string)) { return p; } } return null; }
		 * });
		 */


		addPointName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateHawkerCodes();
				// hawkerComboBox.getItems().clear();
				// hawkerComboBox.getItems().addAll(hawkerCodeData);
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

	private void populateLineNumbersForHawkerCode(String hawkerCode) {
		Main._logger.debug("Entered  populateLineNumbersForHawkerCode  method");
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					ObservableList<String> hawkerLineNumData2 = FXCollections.observableArrayList();
					hawkerLineNumData.clear();
					hawkerLineNumData2.add("All");
					PreparedStatement stmt = con.prepareStatement(
							"select li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_num=ld.line_num(+) and li.hawker_id = ? and li.line_num<>0 order by li.line_num");
					stmt.setLong(1, hawkerIdForCode(hawkerCode));
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						hawkerLineNumData.add(rs.getString(1));
						hawkerLineNumData2.add(rs.getString(1));
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							addLineNumLOV.setItems(hawkerLineNumData);
							addLineNumLOV2.setItems(hawkerLineNumData2);
							addLineNumLOV3.setItems(hawkerLineNumData2);
							addLineNumLOV4.setItems(hawkerLineNumData2);

							new AutoCompleteComboBoxListener<>(addLineNumLOV);
							new AutoCompleteComboBoxListener<>(addLineNumLOV2);
							new AutoCompleteComboBoxListener<>(addLineNumLOV3);
							new AutoCompleteComboBoxListener<>(addLineNumLOV4);
							addLineNumLOV.getSelectionModel().selectFirst();
							addLineNumLOV2.getSelectionModel().selectFirst();
							addLineNumLOV3.getSelectionModel().selectFirst();
							addLineNumLOV4.getSelectionModel().selectFirst();
						}
					});
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
		Main._logger.debug("Entered  hawkerIdForCode  method");

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

	@FXML
	void lineAllSubButtonClicked(ActionEvent event) {
		try {
			
			if (hawkerComboBox.getSelectionModel().getSelectedItem() != null && firstForDate.getValue() != null) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				PreparedStatement stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
				stmt = con.prepareStatement("INSERT INTO REPORT_PARAM(HAWKER_CODE,FORDATE) VALUES(?,?)");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.setDate(2, Date.valueOf(firstForDate.getValue()));
				stmt.executeUpdate();
				stmt.close();

			String reportSrcFile = "HwkLineAllSubsList.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			String lineNum = addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0];
			String prodName = prodNameLOV2.getSelectionModel().getSelectedItem().equals("All") ? null
					: prodNameLOV2.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			parameters.put("LINE_NUM", lineNum);
			parameters.put("PROD_NAME", prodName);
			parameters.put("TESTDATE", Date.valueOf(firstForDate.getValue()));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-" + lineNum + "-" + "SubscriptionList"  + "-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) +  ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();
			stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
			stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
			stmt.executeUpdate();
			} else {
				Notifications.create().title("Required values missing")
						.text("Please select Hawker and Date before generating report.").hideAfter(Duration.seconds(5))
						.showError();
			}
		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@FXML
	void lineAllSubSmallButtonClicked(ActionEvent event) {
		try {
			
			if (hawkerComboBox.getSelectionModel().getSelectedItem() != null && firstForDate.getValue() != null) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				PreparedStatement stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
				stmt = con.prepareStatement("INSERT INTO REPORT_PARAM(HAWKER_CODE,FORDATE) VALUES(?,?)");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.setDate(2, Date.valueOf(firstForDate.getValue()));
				stmt.executeUpdate();
				stmt.close();

			String reportSrcFile = "HwkLineAllSubsListSmall.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			String lineNum = addLineNumLOV.getSelectionModel().getSelectedItem().split(" ")[0];
			String prodName = prodNameLOV2.getSelectionModel().getSelectedItem().equals("All") ? null
					: prodNameLOV2.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			parameters.put("LINE_NUM", lineNum);
			parameters.put("PROD_NAME", prodName);
			parameters.put("TESTDATE", Date.valueOf(firstForDate.getValue()));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-" + lineNum + "-" + "SubsListSmall"  + "-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) +  ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();
			stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
			stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
			stmt.executeUpdate();
			} else {
				Notifications.create().title("Required values missing")
						.text("Please select Hawker and Date before generating report.").hideAfter(Duration.seconds(5))
						.showError();
			}
		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
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

	@FXML
	void closeButtonClicked(ActionEvent event) {
		if (pdfPane.getChildren() != null && pdfPane.getChildren().size() > 0)
			pdfPane.getChildren().removeAll(pdfPane.getChildren());
	}

	@FXML
	void hwkAllLineSubButtonClicked(ActionEvent event) {
		try {

			if (hawkerComboBox.getSelectionModel().getSelectedItem() != null && forDate.getValue() != null) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				PreparedStatement stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
				stmt = con.prepareStatement("INSERT INTO REPORT_PARAM(HAWKER_CODE,FORDATE) VALUES(?,?)");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.setDate(2, Date.valueOf(forDate.getValue()));
				stmt.executeUpdate();
				stmt.close();

				ExportToExcel.exportHwkAllLineSubCountToExcel(hawkerComboBox.getSelectionModel().getSelectedItem(),
						forDate.getValue());
				String reportSrcFile = "HawkerAllLinesProdList.jrxml";
				InputStream input = AReportsTabController.class.getResourceAsStream(reportSrcFile);
				JasperReport jasperReport = JasperCompileManager.compileReport(input);
				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();
				String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
				// String lineNum =
				// addLineNumLOV.getSelectionModel().getSelectedItem().split("
				// ")[0];
				parameters.put("HAWKER_CODE", hawkerCode);
				parameters.put("TESTDATE", Date.valueOf(forDate.getValue()));
				parameters.put("DayOfWeek", forDate.getValue().getDayOfWeek().name());
				JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);
				// Make sure the output directory exists.
				File outDir = new File("C:/pds");
				outDir.mkdirs();
				// PDF Exportor.
				JRPdfExporter exporter = new JRPdfExporter();
				ExporterInput exporterInput = new SimpleExporterInput(print);
				// ExporterInput
				exporter.setExporterInput(exporterInput);
				// ExporterOutput
				String filename = "C:/pds/" + hawkerCode + "-AllLine-" + "SubsCount-For-"
						+ forDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"))  + "-At-"
								+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) +  ".pdf";
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
				stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
			} else {
				Notifications.create().title("Required values missing")
						.text("Please select Hawker and Date before generating report.").hideAfter(Duration.seconds(5))
						.showError();
			}

		} catch (JRException e) {
			Main._logger.debug("Error during Bill PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void hwkAllLineSubProdCodeButtonClicked(ActionEvent event) {
		try {

			if (hawkerComboBox.getSelectionModel().getSelectedItem() != null && forDate.getValue() != null) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				PreparedStatement stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
				stmt = con.prepareStatement("INSERT INTO REPORT_PARAM(HAWKER_CODE,FORDATE) VALUES(?,?)");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.setDate(2, Date.valueOf(forDate.getValue()));
				stmt.executeUpdate();
				stmt.close();

//				ExportToExcel.exportHwkAllLineSubCountToExcel(hawkerComboBox.getSelectionModel().getSelectedItem(),
//						forDate.getValue());
				String reportSrcFile = "HawkerAllLineProdCodeWiseList.jrxml";
				InputStream input = AReportsTabController.class.getResourceAsStream(reportSrcFile);
				JasperReport jasperReport = JasperCompileManager.compileReport(input);
				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();
				String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
				// String lineNum =
				// addLineNumLOV.getSelectionModel().getSelectedItem().split("
				// ")[0];
				parameters.put("HAWKER_CODE", hawkerCode);
				parameters.put("TESTDATE", Date.valueOf(forDate.getValue()));
				parameters.put("DayOfWeek", forDate.getValue().getDayOfWeek().name());
				JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);
				// Make sure the output directory exists.
				File outDir = new File("C:/pds");
				outDir.mkdirs();
				// PDF Exportor.
				JRPdfExporter exporter = new JRPdfExporter();
				ExporterInput exporterInput = new SimpleExporterInput(print);
				// ExporterInput
				exporter.setExporterInput(exporterInput);
				// ExporterOutput
				String filename = "C:/pds/" + hawkerCode + "-AllLine-" + "ProdCodeWiseSubsCount-For-"
						+ forDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YYYY")) + "-At-"
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
				stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
			} else {
				Notifications.create().title("Required values missing")
						.text("Please select Hawker and Date before generating report.").hideAfter(Duration.seconds(5))
						.showError();
			}

		} catch (JRException e) {
			Main._logger.debug("Error during Bill PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@FXML
	void hwkAllLineSubProdNameButtonClicked(ActionEvent event) {
		try {

			if (hawkerComboBox.getSelectionModel().getSelectedItem() != null && forDate.getValue() != null) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				PreparedStatement stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
				stmt = con.prepareStatement("INSERT INTO REPORT_PARAM(HAWKER_CODE,FORDATE) VALUES(?,?)");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.setDate(2, Date.valueOf(forDate.getValue()));
				stmt.executeUpdate();
				stmt.close();

//				ExportToExcel.exportHwkAllLineSubCountToExcel(hawkerComboBox.getSelectionModel().getSelectedItem(),
//						forDate.getValue());
				String reportSrcFile = "HawkerAllLineProdNameWiseList.jrxml";
				InputStream input = AReportsTabController.class.getResourceAsStream(reportSrcFile);
				JasperReport jasperReport = JasperCompileManager.compileReport(input);
				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();
				String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
				// String lineNum =
				// addLineNumLOV.getSelectionModel().getSelectedItem().split("
				// ")[0];
				parameters.put("HAWKER_CODE", hawkerCode);
				parameters.put("TESTDATE", Date.valueOf(forDate.getValue()));
				parameters.put("DayOfWeek", forDate.getValue().getDayOfWeek().name());
				JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);
				// Make sure the output directory exists.
				File outDir = new File("C:/pds");
				outDir.mkdirs();
				// PDF Exportor.
				JRPdfExporter exporter = new JRPdfExporter();
				ExporterInput exporterInput = new SimpleExporterInput(print);
				// ExporterInput
				exporter.setExporterInput(exporterInput);
				// ExporterOutput
				String filename = "C:/pds/" + hawkerCode + "-AllLine-" + "ProdNameWiseSubsCount-For-"
						+ forDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YYYY")) + "-At-"
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
				stmt = con.prepareStatement("DELETE FROM REPORT_PARAM WHERE HAWKER_CODE=?");
				stmt.setString(1, hawkerComboBox.getSelectionModel().getSelectedItem());
				stmt.executeUpdate();
			} else {
				Notifications.create().title("Required values missing")
						.text("Please select Hawker and Date before generating report.").hideAfter(Duration.seconds(5))
						.showError();
			}

		} catch (JRException e) {
			Main._logger.debug("Error during Bill PDF Generation: ", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void generateFilteredReportButtonClicked(ActionEvent event) {
		try {
			String reportSrcFile = "HwkLinesFilteredSubslist.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			String lineNum2 = addLineNumLOV2.getSelectionModel().getSelectedItem().equals("All") ? null
					: addLineNumLOV2.getSelectionModel().getSelectedItem().split(" ")[0];
			String paymentType = paymentTypeLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: paymentTypeLOV.getSelectionModel().getSelectedItem();
			String subType = subscriptionTypeLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: subscriptionTypeLOV.getSelectionModel().getSelectedItem();
			String freq = frequencyLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: frequencyLOV.getSelectionModel().getSelectedItem();
			String dow = dowLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: dowLOV.getSelectionModel().getSelectedItem();
			String prodName = prodNameLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: prodNameLOV.getSelectionModel().getSelectedItem();
			String status = statusLOV.getSelectionModel().getSelectedItem().equals("All") ? null
					: statusLOV.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			parameters.put("LINE_NUM", lineNum2);
			parameters.put("PAYMENT_TYPE", paymentType);
			parameters.put("SUB_TYPE", subType);
			parameters.put("FREQUENCY", freq);
			parameters.put("DOW", dow);
			parameters.put("PROD_NAME", prodName);
			parameters.put("STATUS", status);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-Filtered" + "-" + "SubList-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss"))  +".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

			// BorderPane root;
			// root = new BorderPane();
			// Stage stage = new Stage();
			// stage.setTitle("Invoice PDF");
			// stage.setScene(new Scene(root, 1024, 800));
			//
			/*
			 * if (pdfPane.getChildren() != null && pdfPane.getChildren().size()
			 * > 0) pdfPane.getChildren().removeAll(pdfPane.getChildren());
			 * OpenViewerFX fx = new OpenViewerFX(pdfPane, null);
			 * fx.setupViewer(); fx.openDefaultFile(outFile.getAbsolutePath());
			 */

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
	}

	@FXML
	void upcomingEndDateListClicked(ActionEvent event) {
		try {
			String reportSrcFile = "HawkerSubListEndDate.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-SubEndDateList-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
	}

	@FXML
	void upcomingResumeDateListClicked(ActionEvent event) {
		try {
			String reportSrcFile = "HawkerSubListResumeDate.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-SubResumeDateList-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
	}

	@FXML
	void upcomingEndDateListFilteredClicked(ActionEvent event) {
		try {
			String reportSrcFile = "HawkerSubListEndDate.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);
			String lineNum = addLineNumLOV3.getSelectionModel().getSelectedItem().equals("All") ? null
					: addLineNumLOV3.getSelectionModel().getSelectedItem().split(" ")[0];
			String prodName = prodNameLOV1.getSelectionModel().getSelectedItem().equals("All") ? null
					: prodNameLOV1.getSelectionModel().getSelectedItem();

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			parameters.put("LINE_NUM", lineNum);
			parameters.put("PROD_NAME", prodName);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-SubEndDateListFilter-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
	}

	@FXML
	void upcomingResumeDateListFilteredClicked(ActionEvent event) {
		try {
			String reportSrcFile = "HawkerSubListResumeDate.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);
			String lineNum = addLineNumLOV3.getSelectionModel().getSelectedItem().equals("All") ? null
					: addLineNumLOV3.getSelectionModel().getSelectedItem().split(" ")[0];
			String prodName = prodNameLOV1.getSelectionModel().getSelectedItem().equals("All") ? null
					: prodNameLOV1.getSelectionModel().getSelectedItem();

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			parameters.put("HAWKER_CODE", hawkerCode);
			parameters.put("LINE_NUM", lineNum);
			parameters.put("PROD_NAME", prodName);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-SubResumeDateListFilter-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
	}
	

    @FXML
    void allLineInvSummaryButtonClicked(ActionEvent event) {
    	try {
			String reportSrcFile = "HwkAllLineInvoiceSummary.jrxml";

			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);
//			String lineNum = addLineNumLOV3.getSelectionModel().getSelectedItem().equals("All") ? null
//					: addLineNumLOV3.getSelectionModel().getSelectedItem().split(" ")[0];
//			String prodName = prodNameLOV1.getSelectionModel().getSelectedItem().equals("All") ? null
//					: prodNameLOV1.getSelectionModel().getSelectedItem();

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = HawkerLoginController.loggedInHawker!=null?HawkerLoginController.loggedInHawker.getHawkerCode():
				hawkerComboBox.getSelectionModel().getSelectedItem();
			String invoiceDate = invoiceDateLOV.getSelectionModel().getSelectedItem();
			parameters.put("HWK_CODE", hawkerCode);
			parameters.put("INVOICE_DATE", invoiceDate);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-HwkAllLineInvoiceSummary-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) + ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
		}
    }

    private void populateInvoiceDates(){
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
						String insertStmt = "select distinct TO_CHAR(INVOICE_DATE,'DD/MM/YYYY') INVOICE_DATE from BILLING where customer_id in (select distinct customer_id from customer where hawker_id = ?) order by invoice_date desc";
						PreparedStatement stmt = con.prepareStatement(insertStmt);
						Long hawkerId = HawkerLoginController.loggedInHawker!=null?HawkerLoginController.loggedInHawker.getHawkerId():hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem());
						stmt.setLong(1, hawkerId);
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							invoiceDatesData.add(rs.getString(1));
						}
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								invoiceDateLOV.getItems().clear();
								invoiceDateLOV.setItems(invoiceDatesData);
								invoiceDateLOV.getSelectionModel().selectFirst();
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
	private void populateDOWValues(Product prod) {
		// dowLOV.getItems().clear();
		// dowLOV.getItems().addAll("All");
		dowValues.clear();
		if (prod.getType().equals("Newspaper")) {

			dowValues.addAll("All", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
		} else if(prod.getDow()!=null) {
			String[] dow = prod.getDow().split(",");
			dowValues.addAll(dow);
		}
		dowLOV.setItems(dowValues);
		new AutoCompleteComboBoxListener<String>(dowLOV);
		dowLOV.getSelectionModel().selectFirst();
	}

	private void populateSubscriptionTypeValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					subscriptionTypeValues.clear();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='SUBSCRIPTION_TYPE' order by seq");
					subscriptionTypeValues.addAll("All");
					while (rs.next()) {
						subscriptionTypeValues.add(rs.getString(1));
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
						subscriptionTypeLOV.setItems(subscriptionTypeValues);
						new AutoCompleteComboBoxListener<String>(subscriptionTypeLOV);
						subscriptionTypeLOV.getSelectionModel().selectFirst();

					}
				});
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populatePaymentTypeValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					paymentTypeValues.clear();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='SUB_PAYMENT_TYPE' order by seq");
					paymentTypeValues.addAll("All");
					while (rs.next()) {
						paymentTypeValues.add(rs.getString(1));
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
						paymentTypeLOV.setItems(paymentTypeValues);
						new AutoCompleteComboBoxListener<String>(paymentTypeLOV);
						paymentTypeLOV.getSelectionModel().selectFirst();

					}
				});

				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateDurationValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					durationValues.clear();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select value, code, seq from lov_lookup where code='SUB_DURATION' order by seq");
					durationValues.addAll("All");
					while (rs.next()) {
						durationValues.add(rs.getString(1));
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
						durationLOV.setItems(durationValues);
						new AutoCompleteComboBoxListener<String>(durationLOV);
						durationLOV.getSelectionModel().selectFirst();

					}
				});
				return null;
			}

		};

		new Thread(task).start();

	}

	private void populateFrequencyValues() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					frequencyValues.clear();
					PreparedStatement stmt = con.prepareStatement(
							"select value, code, seq, lov_lookup_id from lov_lookup where code='PRODUCT_FREQ' order by seq");
					ResultSet rs = stmt.executeQuery();
					frequencyValues.addAll("All");
					while (rs.next()) {
						frequencyValues.add(rs.getString(1));
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							frequencyLOV.setItems(frequencyValues);
							new AutoCompleteComboBoxListener<String>(frequencyLOV);
							frequencyLOV.getSelectionModel().selectFirst();
						}
					});
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

	public void populateProducts() {
//		Task<Void> task = new Task<Void>() {
//
//			@Override
//			protected Void call() throws Exception {
				try {

					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					productValues = FXCollections.observableArrayList();
					productValues2 = FXCollections.observableArrayList();
					PreparedStatement stmt = con.prepareStatement(
							"SELECT prod.PRODUCT_ID, prod.NAME, prod.TYPE, prod.SUPPORTED_FREQ, prod.MONDAY, prod.TUESDAY, prod.WEDNESDAY, prod.THURSDAY, prod.FRIDAY, prod.SATURDAY, prod.SUNDAY, prod.PRICE, prod.CODE, prod.DOW, prod.FIRST_DELIVERY_DATE, prod.ISSUE_DATE, prod.bill_category, upper(prod.NAME) FROM products prod, hawker_info hwk, point_name pn where hwk.point_name=pn.name and lower(pn.bill_category)=lower(prod.bill_category) and hwk.hawker_id=? ORDER BY upper(prod.name)");
					Long hawkerId = HawkerLoginController.loggedInHawker!=null?HawkerLoginController.loggedInHawker.getHawkerId():hawkerIdForCode(hawkerComboBox.getSelectionModel().getSelectedItem());
					stmt.setLong(1, hawkerId);
					ResultSet rs = stmt.executeQuery();
					productValues.add("All");
					while (rs.next()) {
						productValues2.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
								rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
								rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getString(13),
								rs.getString(14), rs.getDate(15).toLocalDate(), rs.getDate(16).toLocalDate(),
								rs.getString(17)));
						productValues.add(rs.getString(2));
					}

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							prodNameLOV.setItems(productValues);
							new AutoCompleteComboBoxListener<String>(prodNameLOV);
							prodNameLOV.getSelectionModel().selectFirst();
							prodNameLOV1.setItems(productValues);
							new AutoCompleteComboBoxListener<String>(prodNameLOV1);
							prodNameLOV1.getSelectionModel().selectFirst();
							prodNameLOV2.setItems(productValues);
							new AutoCompleteComboBoxListener<String>(prodNameLOV2);
							prodNameLOV2.getSelectionModel().selectFirst();

						}
					});
					// new AutoCompleteComboBoxListener<>(prodNameLOV);

				} catch (SQLException e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :", e);
					e.printStackTrace();
				}
//				return null;
//			}
//
//		};
//
//		new Thread(task).start();
	}

    @FXML
    void fixedRateSubCostBtnClicked(ActionEvent event) {
    	try {
			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			String hawkerCode = hawkerComboBox.getSelectionModel().getSelectedItem();
			String hawkerId = Long.toString(hawkerIdForCode(hawkerCode));
			String lineNum = addLineNumLOV4.getSelectionModel().getSelectedIndex()==1?"All":addLineNumLOV4.getSelectionModel().getSelectedItem().split(" ")[0];
			
			String reportSrcFile = lineNum.equalsIgnoreCase("All")?"HwkAllLineFixedRateSubsCost.jrxml":"HwkLineFixedRateSubsCost.jrxml";
			//HwkAllLineFixedRateSubsCost.jrxml
			//HwkLineFixedRateSubsCost.jrxml
			InputStream input = BillingUtilityClass.class.getResourceAsStream(reportSrcFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(input);
			if(addLineNumLOV4.getSelectionModel().getSelectedIndex()>1){
				String lineId = Long.toString((!lineNum.equalsIgnoreCase("All"))?ACustomerInfoTabController.lineIdForNumHwkCode(Integer.parseInt(lineNum), hawkerCode):null);
			
				parameters.put("LINE_NUM", lineNum);
				parameters.put("LINE_ID", lineId);
			}
			parameters.put("HAWKER_ID", hawkerId);
			parameters.put("HAWKER_CODE", hawkerCode);
			
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, Main.dbConnection);

			// Make sure the output directory exists.
			File outDir = new File("C:/pds");
			outDir.mkdirs();

			// PDF Exportor.
			JRPdfExporter exporter = new JRPdfExporter();

			ExporterInput exporterInput = new SimpleExporterInput(print);
			// ExporterInput
			exporter.setExporterInput(exporterInput);

			// ExporterOutput
			String filename = "C:/pds/" + hawkerCode + "-" + ((!lineNum.equalsIgnoreCase("All"))?lineNum:"" )+ "-" + "FixedRateCost"  + "-At-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh-mm-ss")) +  ".pdf";
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(filename);
			// Output
			exporter.setExporterOutput(exporterOutput);

			//
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			File outFile = new File(filename);

			Notifications.create().title("Report created").text("Report PDF created at : " + filename)
					.hideAfter(Duration.seconds(15)).showInformation();

		} catch (JRException e) {
			Main._logger.debug("Error during Report PDF Generation: ", e);
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


	private void populateStatusValues() {
		statusLOV.getItems().addAll("Active", "Stopped");
		new AutoCompleteComboBoxListener<String>(statusLOV);
		statusLOV.getSelectionModel().selectFirst();
	}

	public void reloadData() {
		hawkerLineNumData.clear();
		hawkerCodeData.clear();
		populateCityValues();
		populateProducts();
		// populateDurationValues();
		populateFrequencyValues();
		populatePaymentTypeValues();
		populateSubscriptionTypeValues();
		populateStatusValues();
		populateInvoiceDates();
	}

	public void releaseVariables() {

		hawkerCodeData = null;
		hawkerLineNumData = null;

		hawkerCodeData = FXCollections.observableArrayList();
		hawkerLineNumData = FXCollections.observableArrayList();
	}

}
