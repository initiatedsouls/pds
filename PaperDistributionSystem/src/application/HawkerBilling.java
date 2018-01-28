package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class HawkerBilling {

	private final SimpleLongProperty hwkBillId = new SimpleLongProperty();
	private final SimpleLongProperty hawkerId = new SimpleLongProperty();
	private final SimpleObjectProperty<LocalDate> entryDate = new SimpleObjectProperty<>();
	private final SimpleDoubleProperty amount = new SimpleDoubleProperty();
	private final SimpleStringProperty type = new SimpleStringProperty();
	

	public HawkerBilling(long hwkBillId, long hawkerId, LocalDate entryDate, double amount, String type) {
		// setHwkBillId(hwkBillId);
		setHawkerId(hawkerId);
		setEntryDate(entryDate);
		setHwkBillId(hwkBillId);
		setAmount(amount);
		setType(type);
	}

	public Long getHwkBillId() {
		return hwkBillId.get();
	}

	public void setHwkBillId(long hwkBillId) {
		this.hwkBillId.set(hwkBillId);
	}

	public Long getHawkerId() {
		return hawkerId.get();
	}

	public void setHawkerId(long hawkerId) {
		this.hawkerId.set(hawkerId);
	}

	public LocalDate getEntryDate() {
		return entryDate.get();
	}

	public void setEntryDate(LocalDate startDate) {
		this.entryDate.set(startDate);
	}

	public double getAmount() {
		return this.amount.get();
	}

	public void setAmount(double amount) {
		this.amount.set(amount);
	}
	
	public String getType(){
		return this.type.get();
	}
	
	public void setType(String type){
		this.type.set(type);
		
	}
	public void updateHawkerBillingRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update hawker_billing set amount=? where hwk_bill_id=?";

			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setDouble(1, getAmount());

			updateStmt.setLong(2, getHwkBillId());
			updateStmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
