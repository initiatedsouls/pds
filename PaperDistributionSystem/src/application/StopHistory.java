package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;

public class StopHistory {

	private final SimpleLongProperty stopHistoryId = new SimpleLongProperty();
	private final SimpleStringProperty customerName = new SimpleStringProperty("");
	private final SimpleLongProperty customerCode = new SimpleLongProperty();
	private final SimpleStringProperty mobileNum = new SimpleStringProperty("");
	private final SimpleStringProperty hawkerCode = new SimpleStringProperty();
	private final SimpleLongProperty lineNum = new SimpleLongProperty();
	private final SimpleLongProperty subscriptionId = new SimpleLongProperty();
	private final SimpleIntegerProperty houseSeq = new SimpleIntegerProperty();
	private final SimpleStringProperty productName = new SimpleStringProperty();
	private final SimpleStringProperty productCode = new SimpleStringProperty();
	private final SimpleStringProperty billCategory = new SimpleStringProperty();
	private final SimpleStringProperty subscriptionType = new SimpleStringProperty();
	private final SimpleStringProperty subscriptionFreq = new SimpleStringProperty();
	private final SimpleStringProperty subscriptionDOW = new SimpleStringProperty();
	private final SimpleObjectProperty<LocalDate> stopDate = new SimpleObjectProperty<LocalDate>();
	private final SimpleObjectProperty<LocalDate> resumeDate = new SimpleObjectProperty<LocalDate>();
	private final SimpleDoubleProperty amount = new SimpleDoubleProperty();

	public StopHistory(long stopHistoryId, String customerName, long customerCode, String mobileNum, String hawkerCode,
			long lineNum, long subscriptionId, int houseSeq, String productName, String productCode,
			String billCategory, LocalDate stopDate, LocalDate resumeDate, String subscriptionType,
			String subscriptionFreq, String subscriptionDOW, double amount) {
		setStopHistoryId(stopHistoryId);
		setCustomerName(customerName);
		setCustomerCode(customerCode);
		setMobileNum(mobileNum);
		setHawkerCode(hawkerCode);
		setLineNum(lineNum);
		setSubscriptionId(subscriptionId);
		setHouseSeq(houseSeq);
		setProductName(productName);
		setProductCode(productCode);
		setBillCategory(billCategory);
		setStopDate(stopDate);
		setResumeDate(resumeDate);
		setSubscriptionType(subscriptionType);
		setSubscriptionFreq(subscriptionFreq);
		setSubscriptionDOW(subscriptionDOW);
		setAmount(amount);
	}

	public long getStopHistoryId() {
		return stopHistoryId.get();
	}

	private void setStopHistoryId(long stopHistoryId) {
		this.stopHistoryId.set(stopHistoryId);

	}

	public String getCustomerName() {
		return customerName.get();
	}

	public void setCustomerName(String customerName) {
		this.customerName.set(customerName);
	}

	public long getCustomerCode() {
		return customerCode.get();
	}

	public void setCustomerCode(long customerCode) {
		this.customerCode.set(customerCode);
	}

	public String getMobileNum() {
		return mobileNum.get();
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum.set(mobileNum);
	}

	public String getHawkerCode() {
		return hawkerCode.get();
	}

	public void setHawkerCode(String hawkerCode) {
		this.hawkerCode.set(hawkerCode);
	}

	public Long getLineNum() {
		return lineNum.get();
	}

	public void setLineNum(long lineNum) {
		this.lineNum.set(lineNum);
	}

	public long getSubscriptionId() {
		return subscriptionId.get();
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId.set(subscriptionId);
	}

	public int getHouseSeq() {
		return houseSeq.get();
	}

	public void setHouseSeq(int houseSeq) {
		this.houseSeq.set(houseSeq);
	}

	public String getProductName() {
		return productName.get();
	}

	public void setProductName(String productName) {
		this.productName.set(productName);
	}

	public String getProductCode() {
		return productCode.get();
	}

	public void setProductCode(String productCode) {
		this.productCode.set(productCode);
	}

	public void setBillCategory(String city) {
		this.billCategory.set(city);

	}

	public String getBillCategory() {
		return billCategory.get();
	}

	public LocalDate getStopDate() {
		return stopDate.get();
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate.set(stopDate);

	}

	public LocalDate getResumeDate() {
		return resumeDate.get();
	}

	public void setResumeDate(LocalDate resumeDate) {
		this.resumeDate.set(resumeDate);
	}

	public String getSubscriptionType() {
		return subscriptionType.get();
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType.set(subscriptionType);
	}

	public String getSubscriptionFreq() {
		return subscriptionFreq.get();
	}

	public void setSubscriptionFreq(String subscriptionFreq) {
		this.subscriptionFreq.set(subscriptionFreq);
	}

	public String getSubscriptionDOW() {
		return subscriptionDOW.get();
	}

	public void setSubscriptionDOW(String subscriptionDOW) {
		this.subscriptionDOW.set(subscriptionDOW);
	}

	public void setAmount(Double amount) {
		this.amount.set(amount);
	}

	public Double getAmount() {

		return this.amount.get();
	}

	public static void splitOnDate(StopHistory stp, LocalDate date) {
		try {

			if (stp.getResumeDate() == null || stp.getResumeDate().isAfter(date.plusDays(1))) {
				Connection con = Main.dbConnection;
				if (con.isClosed()) {
					con = Main.reconnect();
				}
				String insertStmt = "insert into stop_history(SUB_ID,STOP_DATE,RESUME_DATE) values(?,?,?)";
				PreparedStatement stmt = con.prepareStatement(insertStmt);
				stmt.setLong(1, stp.getSubscriptionId());
				stmt.setDate(2, Date.valueOf(date.plusDays(1)));
				stmt.setDate(3, stp.getResumeDate() == null ? null : Date.valueOf(stp.resumeDate.get()));
				stmt.executeUpdate();
			}
			stp.setResumeDate(date.plusDays(1));
			stp.updateStopHistoryRecord();
			stp.setAmount(BillingUtilityClass.calculateStopHistoryAmount(stp));
			stp.updateStopHistoryRecord();
			//Set subscription stop date to new stop history record's stop date
//			Subscription sub = BillingUtilityClass.subForSubId(stp.getSubscriptionId());
//			sub.setPausedDate(date.plusDays(1));
//			sub.updateSubscriptionRecord();
		} catch (SQLException e) {

			Notifications.create().hideAfter(Duration.seconds(5)).title("Error")
					.text("Error in creation of stop history record").showError();
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	public void updateStopHistoryRecord() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update stop_history set STOP_DATE=?, RESUME_DATE=?, AMOUNT=? where stop_history_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setDate(1, Date.valueOf(getStopDate()));
			updateStmt.setDate(2, Date.valueOf(getResumeDate()));
			updateStmt.setDouble(3, getAmount());
			updateStmt.setLong(4, getStopHistoryId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Stop History record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}
}
