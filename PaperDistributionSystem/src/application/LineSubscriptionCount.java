package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.joda.time.LocalDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LineSubscriptionCount implements Initializable {

	@FXML
	private RadioButton todayRB;

	@FXML
	private ToggleGroup tg;

	@FXML
	private RadioButton tomorrowRB;

	@FXML
	private VBox mainVBOX;

	private HashMap<String, Integer> dailyValues = new HashMap<String, Integer>();
	private HashMap<String, Integer> weeklyValues = new HashMap<String, Integer>();
	private ArrayList<String> prodList = new ArrayList<String>();
	private String hawkerCode;
	private int lineNum;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		todayRB.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				populateProdList();
				refreshDailyCount();
				refreshWeeklyCount();
				setGrid();
			}
		});
		tomorrowRB.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				populateProdList();
				refreshDailyCount();
				refreshWeeklyCount();
				setGrid();
			}
		});
	}

	public void setupBindings(String hawkerCode, int lineNum) {
		this.hawkerCode = hawkerCode;
		this.lineNum = lineNum;
		populateProdList();
		refreshDailyCount();
		refreshWeeklyCount();
		setGrid();
	}

	private void populateProdList() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			prodList.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select distinct prod.name from subscription sub, customer cust, products prod where cust.line_num = ? and cust.HAWKER_CODE=? and sub.CUSTOMER_ID=cust.CUSTOMER_ID and prod.PRODUCT_ID=sub.PRODUCT_ID and sub.status='Active' order by prod.NAME");
			stmt.setInt(1, this.lineNum);
			stmt.setString(2, this.hawkerCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				prodList.add(rs.getString(1));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private void setGrid() {
		mainVBOX.getChildren().clear();
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		// grid.setPadding(new Insets(20, 150, 10, 10));
		// Iterator<Customer> custIter = customerData.iterator();

		grid.add(new Label("Product Name"), 0, 0);
		grid.add(new Label("Daily Count"), 1, 0);
		grid.add(new Label("Weekly Count"), 2, 0);
		int j=0;
		for (int i = 0; i < prodList.size(); i++) {
			String prod = prodList.get(i);
			if ((dailyValues.get(prod)!=null && dailyValues.get(prod)>0) || (weeklyValues.get(prod)!=null && weeklyValues.get(prod)>0)) {
				grid.add(new Label(prodList.get(i)), 0, j + 1);
				grid.add(new Label("" + (dailyValues.containsKey(prod) ? dailyValues.get(prod) : 0)), 1, j + 1);
				grid.add(new Label("" + (weeklyValues.containsKey(prod) ? weeklyValues.get(prod) : 0)), 2, j + 1);
				j++;
			}
		}
		mainVBOX.getChildren().add(grid);
	}

	private void refreshDailyCount() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			dailyValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select prod.name, count(*) cnt from subscription sub, customer cust, products prod where cust.line_num = ? and cust.HAWKER_CODE=? and sub.CUSTOMER_ID=cust.CUSTOMER_ID and prod.PRODUCT_ID=sub.PRODUCT_ID and sub.FREQUENCY='Daily' and sub.status='Active'  group by prod.NAME order by prod.NAME");
			stmt.setInt(1, this.lineNum);
			stmt.setString(2, this.hawkerCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				dailyValues.put(rs.getString(1), rs.getInt(2));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	private void refreshWeeklyCount() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			weeklyValues.clear();
			PreparedStatement stmt = con.prepareStatement(
					"select prod.name, count(*) cnt from subscription sub, customer cust, products prod where cust.line_num = ? and cust.HAWKER_CODE=? and sub.CUSTOMER_ID=cust.CUSTOMER_ID and prod.PRODUCT_ID=sub.PRODUCT_ID and sub.FREQUENCY='Weekly' and lower(sub.DOW)=? and sub.status='Active' group by prod.NAME order by prod.NAME");
			stmt.setInt(1, this.lineNum);
			stmt.setString(2, this.hawkerCode);
			if (todayRB.isSelected()) {
				stmt.setString(3, LocalDate.now().dayOfWeek().getAsText().toLowerCase());
			} else if (tomorrowRB.isSelected()) {
				stmt.setString(3, LocalDate.now().plusDays(1).dayOfWeek().getAsText().toLowerCase());

			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				weeklyValues.put(rs.getString(1), rs.getInt(2));
			}
		} catch (SQLException e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}
}
