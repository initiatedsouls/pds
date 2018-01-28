package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class PointName {

	private final SimpleStringProperty pointName = new SimpleStringProperty("");
	private final SimpleStringProperty city = new SimpleStringProperty("");
	private final SimpleLongProperty pointId = new SimpleLongProperty();
	private final SimpleStringProperty billCategory = new SimpleStringProperty("");
	private final SimpleDoubleProperty fee = new SimpleDoubleProperty();

	public PointName(Long pointId, String pointName, String city, String billCategory, double fee) {
		setPointId(pointId);
		setPointName(pointName);
		setCity(city);
		setBillCategory(billCategory);
		setFee(fee);
	}

	public void setPointId(Long pointId) {
		this.pointId.set(pointId);
	}

	public void setPointName(String pointName) {
		this.pointName.set(pointName);
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public Long getPointId() {
		return this.pointId.get();
	}

	public String getPointName() {
		return this.pointName.get();
	}

	public String getCity() {
		return this.city.get();
	}

	public void setBillCategory(String billCategory) {
		this.billCategory.set(billCategory);
	}

	public String getBillCategory() {
		return this.billCategory.get();
	}

	public double getFee() {
		return this.fee.get();
	}

	public void setFee(double fee) {
		this.fee.set(fee);
	}

	public void updatePointName() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update point_name set name=?, city=?, bill_category=?, fee=? where point_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getPointName());
			updateStmt.setString(2, getCity().toLowerCase());
			updateStmt.setString(3, getBillCategory().toLowerCase());
			updateStmt.setDouble(4, getFee());
			updateStmt.setLong(5, getPointId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Line number update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}
}
