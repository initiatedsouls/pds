package application;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
public class StopHistoryBackup {
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

	public StopHistoryBackup(long stopHistoryId, String customerName, long customerCode, String mobileNum, String hawkerCode,
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

}
