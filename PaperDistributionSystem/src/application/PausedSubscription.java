package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PausedSubscription {
	public SimpleLongProperty customerId = new SimpleLongProperty();
	public SimpleLongProperty subscriptionId = new SimpleLongProperty();
	public SimpleLongProperty productId = new SimpleLongProperty();
	public SimpleStringProperty customerName = new SimpleStringProperty("");
	public SimpleLongProperty customerCode = new SimpleLongProperty();
	public SimpleStringProperty mobileNum = new SimpleStringProperty("");
	public SimpleStringProperty hawkerCode = new SimpleStringProperty("");
	public SimpleStringProperty lineNum = new SimpleStringProperty("");
	public SimpleIntegerProperty houseSeq = new SimpleIntegerProperty();
	public SimpleStringProperty productName = new SimpleStringProperty("");
	public SimpleStringProperty productType = new SimpleStringProperty("");
	public SimpleStringProperty subscriptionType = new SimpleStringProperty("");
	public SimpleStringProperty frequency = new SimpleStringProperty("");
	public SimpleStringProperty paymentType = new SimpleStringProperty("");
	public SimpleDoubleProperty subscriptionCost = new SimpleDoubleProperty();
	public SimpleDoubleProperty serviceCharge = new SimpleDoubleProperty();
	SimpleObjectProperty<LocalDate> pausedDate = new SimpleObjectProperty<LocalDate>();
	SimpleObjectProperty<LocalDate> resumeDate = new SimpleObjectProperty<LocalDate>();
	public SimpleStringProperty addr1 = new SimpleStringProperty("");
	public SimpleStringProperty addr2 = new SimpleStringProperty("");

	public PausedSubscription(long customerId, long subscriptionId, long productId, String customerName,
			long customerCode, String mobileNum, String hawkerCode, String lineNum, int houseSeq, String productName,
			String productType, String subscriptionType, String frequency, String paymentType, double subscriptionCost,
			double serviceCharge, LocalDate pausedDate, LocalDate resumeDate, String addr1, String addr2) {
		this.customerId.set(customerId);
		this.subscriptionId.set(subscriptionId);
		this.productId.set(productId);
		this.customerName.set(customerName);
		this.customerCode.set(customerCode);
		this.mobileNum.set(mobileNum);
		this.hawkerCode.set(hawkerCode);
		this.lineNum.set(lineNum);
		this.houseSeq.set(houseSeq);
		this.productName.set(productName);
		this.productType.set(productType);
		this.subscriptionType.set(subscriptionType);
		this.frequency.set(frequency);
		this.paymentType.set(paymentType);
		this.subscriptionCost.set(subscriptionCost);
		this.serviceCharge.set(serviceCharge);
		this.setPausedDate(pausedDate);
		this.setResumeDate(resumeDate);
		this.setAddr1(addr1);
		this.setAddr2(addr2);
	}

	public long getCustomerId() {
		return customerId.get();
	}

	public void setCustomerId(long customerId) {
		this.customerId.set(customerId);
	}

	public long getSubscriptionId() {
		return subscriptionId.get();
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId.set(subscriptionId);
	}

	public long getProductId() {
		return productId.get();
	}

	public void setProductId(long productId) {
		this.productId.set(productId);
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

	public String getLineNum() {
		return lineNum.get();
	}

	public void setLineNum(String lineNum) {
		this.lineNum.set(lineNum);
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

	public String getProductType() {
		return productType.get();
	}

	public void setProductType(String productType) {
		this.productType.set(productType);
	}

	public String getSubscriptionType() {
		return subscriptionType.get();
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType.set(subscriptionType);
	}

	public String getFrequency() {
		return frequency.get();
	}

	public void setFrequency(String frequency) {
		this.frequency.set(frequency);
	}

	public String getPaymentType() {
		return paymentType.get();
	}

	public void setPaymentType(String paymentType) {
		this.paymentType.set(paymentType);
	}

	public double getSubscriptionCost() {
		return subscriptionCost.get();
	}

	public void setSubscriptionCost(double subscriptionCost) {
		this.subscriptionCost.set(subscriptionCost);
	}

	public double getServiceCharge() {
		return serviceCharge.get();
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge.set(serviceCharge);
	}

	public LocalDate getPausedDate() {
		return pausedDate.get();
	}

	public void setPausedDate(LocalDate pausedDate) {
		this.pausedDate.set(pausedDate);
	}

	public LocalDate getResumeDate() {
		return resumeDate.get();
	}

	public void setResumeDate(LocalDate resumeDate) {
		this.resumeDate.set(resumeDate);
	}

	public String getAddr1() {
		return addr1.get();
	}

	public void setAddr1(String addrLine1) {
		this.addr1.set(addrLine1);
	}

	public String getAddr2() {
		return addr2.get();
	}

	public void setAddr2(String addrLine2) {
		this.addr2.set(addrLine2);
	}


	public void resumeSubscription() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update subscription set STATUS=?, PAUSED_DATE=NULL, resume_date=null  where subscription_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, "Active");
			updateStmt.setLong(2, this.subscriptionId.get());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Subscription record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
