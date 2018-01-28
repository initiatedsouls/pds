package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;

public class Subscription {
	SimpleLongProperty subscriptionId = new SimpleLongProperty();
	SimpleLongProperty customerId = new SimpleLongProperty();
	SimpleLongProperty productId = new SimpleLongProperty();
	SimpleStringProperty productName = new SimpleStringProperty("");
	SimpleStringProperty productType = new SimpleStringProperty("");
	SimpleStringProperty paymentType = new SimpleStringProperty("");
	SimpleDoubleProperty cost = new SimpleDoubleProperty();
	SimpleDoubleProperty serviceCharge = new SimpleDoubleProperty();
	SimpleStringProperty frequency = new SimpleStringProperty();
	SimpleStringProperty subscriptionType = new SimpleStringProperty();
	SimpleStringProperty dow = new SimpleStringProperty();
	SimpleStringProperty status = new SimpleStringProperty();
	SimpleObjectProperty<LocalDate> startDate = new SimpleObjectProperty<LocalDate>();
	SimpleObjectProperty<LocalDate> pausedDate = new SimpleObjectProperty<LocalDate>();
	SimpleStringProperty productCode = new SimpleStringProperty();
	SimpleObjectProperty<LocalDate> stopDate = new SimpleObjectProperty<LocalDate>();
	SimpleIntegerProperty offerMonths = new SimpleIntegerProperty();
	SimpleStringProperty duration = new SimpleStringProperty();
	SimpleStringProperty subNumber = new SimpleStringProperty();
	SimpleObjectProperty<LocalDate> resumeDate = new SimpleObjectProperty<LocalDate>();
	SimpleBooleanProperty pausedProp = new SimpleBooleanProperty();
	SimpleBooleanProperty activeProp = new SimpleBooleanProperty();
	SimpleDoubleProperty addToBill = new SimpleDoubleProperty();
	SimpleBooleanProperty chequeRcvd = new SimpleBooleanProperty();

	public Subscription(long subscriptionId, long customerId, long productId, String productName, String productType,
			String paymentType, double cost, double serviceCharge, String frequency, String subscriptionType,
			String dow, String status, LocalDate startDate, LocalDate pausedDate, String productCode,
			LocalDate stopDate, String duration, int offerMonths, String subNumber, LocalDate resumeDate, 
			double addToBill, Boolean chequeRcvd) {
		setSubscriptionId(subscriptionId);
		setCustomerId(customerId);
		setProductId(productId);
		setProductName(productName);
		setProductType(productType);
		setPaymentType(paymentType);
		setCost(cost);
		setServiceCharge(serviceCharge);
		setFrequency(frequency);
		setSubscriptionType(subscriptionType);
		setDow(dow);
		setStatus(status);
		setStartDate(startDate);
		setPausedDate(pausedDate);
		setProductCode(productCode);
		setStopDate(stopDate);
		setDuration(duration);
		setOfferMonths(offerMonths);
		setSubNumber(subNumber);
		setResumeDate(resumeDate);
		setAddToBill(addToBill);
		setChequeRcvd(chequeRcvd);
	}

	public long getSubscriptionId() {
		return subscriptionId.get();
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId.set(subscriptionId);
	}

	public long getCustomerId() {
		return customerId.get();
	}

	public void setCustomerId(long customerId) {
		this.customerId.set(customerId);
	}

	public long getProductId() {
		return productId.get();
	}

	public void setProductId(long productId) {
		this.productId.set(productId);
	}

	public String getPaymentType() {
		return paymentType.get();
	}

	public void setPaymentType(String paymentType) {
		this.paymentType.set(paymentType);
	}

	public double getCost() {
		return cost.get();
	}

	public void setCost(double cost) {
		this.cost.set(cost);
	}

	public double getServiceCharge() {
		return serviceCharge.get();
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge.set(serviceCharge);
	}

	public String getFrequency() {
		return frequency.get();
	}

	public void setFrequency(String frequency) {
		this.frequency.set(frequency);
	}

	public String getSubscriptionType() {
		return subscriptionType.get();
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType.set(subscriptionType);
	}

	public String getDow() {
		return dow.get();
	}

	public void setDow(String dow) {
		this.dow.set(dow);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
		this.pausedProp.set(status.equalsIgnoreCase("Stopped"));
		this.activeProp.set(!status.equalsIgnoreCase("Stopped"));
	}

	public LocalDate getStartDate() {
		return startDate.get();
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate.set(startDate);
	}

	public LocalDate getPausedDate() {
		return pausedDate.get();
	}

	public void setPausedDate(LocalDate pausedDate) {
		this.pausedDate.set(pausedDate);
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

	public String getProductType() {
		return productType.get();
	}

	public void setProductType(String productType) {
		this.productType.set(productType);
	}

	public void setOfferMonths(int offerMonths) {
		this.offerMonths.set(offerMonths);

	}

	public void setDuration(String duration) {
		this.duration.set(duration);

	}

	public String getDuration() {
		return duration.get();
	}

	public String getSubNumber() {
		return subNumber.get();
	}

	public void setSubNumber(String subNumber) {
		this.subNumber.set(subNumber);

	}

	public LocalDate getStopDate() {
		return stopDate.get();
	}

	public int getOfferMonths() {
		return offerMonths.get();
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
	

	public double getAddToBill() {
		return addToBill.get();
	}

	public void setAddToBill(double addToBill) {
		this.addToBill.set(addToBill);
	}
	
	public Boolean getChequeRcvd(){
		return this.chequeRcvd.get();
	}
	
	public void setChequeRcvd(Boolean chequeRcvd){
		this.chequeRcvd.set(chequeRcvd);
	}
	

	public SimpleBooleanProperty isChequeReceived() {
		return chequeRcvd;
	}

	public void updateSubscriptionRecord() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update subscription set PAYMENT_TYPE=?, SUBSCRIPTION_COST=?, SERVICE_CHARGE=?, FREQUENCY=?, TYPE=?, DOW=?, STATUS=?, START_DATE=?, PAUSED_DATE=?, STOP_DATE=?, DURATION=?, OFFER_MONTHS=?, RESUME_DATE=?, ADD_TO_BILL=?, CHEQUE_RCVD=?  where subscription_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getPaymentType());
			updateStmt.setDouble(2, getCost());
			updateStmt.setDouble(3, getServiceCharge());
			updateStmt.setString(4, getFrequency());
			updateStmt.setString(5, getSubscriptionType());
			updateStmt.setString(6, getDow());
			updateStmt.setString(7, getStatus());
			updateStmt.setDate(8, getStartDate() == null ? null : Date.valueOf(getStartDate()));
			updateStmt.setDate(9, getPausedDate() == null ? null : Date.valueOf(getPausedDate()));
			updateStmt.setDate(10, getStopDate() == null ? null : Date.valueOf(getStopDate()));
			updateStmt.setString(11, getDuration());
			updateStmt.setInt(12, getOfferMonths());
			updateStmt.setDate(13, getResumeDate() == null ? null : Date.valueOf(getResumeDate()));
			updateStmt.setDouble(14, getAddToBill());
			updateStmt.setString(15, getChequeRcvd()?"Y":"N");
			updateStmt.setLong(16, getSubscriptionId());
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
			updateStmt.close();
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
