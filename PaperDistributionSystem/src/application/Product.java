package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
	SimpleLongProperty productId = new SimpleLongProperty();
	SimpleStringProperty name = new SimpleStringProperty();
	SimpleStringProperty code = new SimpleStringProperty();
	SimpleStringProperty type = new SimpleStringProperty();
	SimpleStringProperty supportingFreq = new SimpleStringProperty();
	SimpleDoubleProperty price = new SimpleDoubleProperty();
	SimpleDoubleProperty monday = new SimpleDoubleProperty();
	SimpleDoubleProperty tuesday = new SimpleDoubleProperty();
	SimpleDoubleProperty wednesday = new SimpleDoubleProperty();
	SimpleDoubleProperty thursday = new SimpleDoubleProperty();
	SimpleDoubleProperty friday = new SimpleDoubleProperty();
	SimpleDoubleProperty saturday = new SimpleDoubleProperty();
	SimpleDoubleProperty sunday = new SimpleDoubleProperty();
	SimpleStringProperty dow = new SimpleStringProperty();
	SimpleObjectProperty<LocalDate> firstDeliveryDate = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	SimpleObjectProperty<LocalDate> issueDate = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	SimpleStringProperty billCategory = new SimpleStringProperty();

	public Product(long productId, String name, String type, String supportingFreq, double monday, double tuesday,
			double wednesday, double thursday, double friday, double saturday, double sunday, double price, String code,
			String dow, LocalDate firstDeliveryDate, LocalDate issueDate, String billCategory) {
		setProductId(productId);
		setName(name);
		setType(type);
		setSupportingFreq(supportingFreq);
		setPrice(price);
		setMonday(monday);
		setTuesday(tuesday);
		setWednesday(wednesday);
		setThursday(thursday);
		setFriday(friday);
		setSaturday(saturday);
		setSunday(sunday);
		setCode(code);
		setDow(dow);
		setFirstDeliveryDate(firstDeliveryDate);
		setIssueDate(issueDate);
		setBillCategory(billCategory);
	}

	public long getProductId() {
		return this.productId.get();
	}

	public String getName() {
		return this.name.get();
	}

	public String getCode() {
		return this.code.get();
	}

	public String getType() {
		return this.type.get();
	}

	public String getSupportingFreq() {
		return this.supportingFreq.get();
	}

	public double getPrice() {
		return this.price.get();
	}

	public double getMonday() {
		return this.monday.get();
	}

	public double getTuesday() {
		return this.tuesday.get();
	}

	public double getWednesday() {
		return this.wednesday.get();
	}

	public double getThursday() {
		return this.thursday.get();
	}

	public double getFriday() {
		return this.friday.get();
	}

	public double getSaturday() {
		return this.saturday.get();
	}

	public double getSunday() {
		return this.sunday.get();
	}

	public void setProductId(long productId) {
		this.productId.set(productId);

	}

	public void setName(String name) {
		this.name.set(name);

	}

	public void setCode(String code) {
		this.code.set(code);

	}

	public void setType(String type) {
		this.type.set(type);
	}

	public void setSupportingFreq(String supportingFreq) {
		this.supportingFreq.set(supportingFreq);
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	public void setMonday(double monday) {
		this.monday.set(monday);
	}

	public void setTuesday(double tuesday) {
		this.tuesday.set(tuesday);
	}

	public void setWednesday(double wednesday) {
		this.wednesday.set(wednesday);
	}

	public void setThursday(double thursday) {
		this.thursday.set(thursday);
	}

	public void setFriday(double friday) {
		this.friday.set(friday);
	}

	public void setSaturday(double saturday) {
		this.saturday.set(saturday);
	}

	public void setSunday(double sunday) {
		this.sunday.set(sunday);
	}

	public String getDow() {
		return dow.get();
	}

	public void setDow(String dow) {
		this.dow.set(dow);
	}

	public LocalDate getFirstDeliveryDate() {
		return firstDeliveryDate.get();
	}

	public void setFirstDeliveryDate(LocalDate firstDeliveryDate) {
		this.firstDeliveryDate.set(firstDeliveryDate);
	}

	public LocalDate getIssueDate() {
		return issueDate.get();
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate.set(issueDate);
	}

	public void setBillCategory(String city) {
		this.billCategory.set(city);

	}

	public String getBillCategory() {
		return billCategory.get();
	}

	public void updateProductRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update products set name=?, type=?, supported_freq=?, monday=?, tuesday=?, wednesday=?, thursday=?, friday=?, saturday=?, sunday=?, price=?, code=?, dow=?, first_delivery_date=?, issue_date=?, bill_category=? where product_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getName());
			updateStmt.setString(2, getType());
			updateStmt.setString(3, getSupportingFreq());
			updateStmt.setDouble(4, getMonday());
			updateStmt.setDouble(5, getTuesday());
			updateStmt.setDouble(6, getWednesday());
			updateStmt.setDouble(7, getThursday());
			updateStmt.setDouble(8, getFriday());
			updateStmt.setDouble(9, getSaturday());
			updateStmt.setDouble(10, getSunday());
			updateStmt.setDouble(11, getPrice());
			updateStmt.setString(12, getCode());
			updateStmt.setString(13, getDow());
			updateStmt.setDate(14, getFirstDeliveryDate() == null ? null : Date.valueOf(getFirstDeliveryDate()));
			updateStmt.setDate(15, getIssueDate() == null ? null : Date.valueOf(getIssueDate()));
			updateStmt.setString(16, getBillCategory());
			updateStmt.setLong(17, getProductId());
			updateStmt.executeUpdate();
			con.commit();

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

}
