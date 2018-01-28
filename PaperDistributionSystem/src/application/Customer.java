package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

	private final SimpleLongProperty customerId = new SimpleLongProperty();
	private final SimpleStringProperty name = new SimpleStringProperty("");
	private final SimpleLongProperty customerCode = new SimpleLongProperty();
	private final SimpleStringProperty mobileNum = new SimpleStringProperty("");
	private final SimpleStringProperty hawkerCode = new SimpleStringProperty();
	private final SimpleLongProperty lineNum = new SimpleLongProperty();
	private final SimpleIntegerProperty houseSeq = new SimpleIntegerProperty();
	private final SimpleStringProperty oldHouseNum = new SimpleStringProperty("");
	private final SimpleStringProperty newHouseNum = new SimpleStringProperty("");
	private final SimpleStringProperty addrLine1 = new SimpleStringProperty("");
	private final SimpleStringProperty addrLine2 = new SimpleStringProperty("");
	private final SimpleStringProperty locality = new SimpleStringProperty("");
	private final SimpleStringProperty city = new SimpleStringProperty("");
	private final SimpleStringProperty state = new SimpleStringProperty("");
	private final SimpleStringProperty profile1 = new SimpleStringProperty("");
	private final SimpleStringProperty profile2 = new SimpleStringProperty("");
	private final SimpleStringProperty profile3 = new SimpleStringProperty("");
	private final SimpleStringProperty initials = new SimpleStringProperty("");
	private final SimpleStringProperty employment = new SimpleStringProperty("");
	private final SimpleStringProperty comments = new SimpleStringProperty("");
	private final SimpleStringProperty buildingStreet = new SimpleStringProperty("");
	private final SimpleDoubleProperty totalDue = new SimpleDoubleProperty();
	private final SimpleLongProperty hawkerId = new SimpleLongProperty();
	private final SimpleLongProperty lineId = new SimpleLongProperty();

	public Customer(long customerId, long customerCode, String name, String mobileNum, String hawkerCode, long lineNum,
			int houseSeq, String oldHouseNum, String newHouseNum, String addrLine1, String addrLine2, String locality,
			String city, String state, String profile1, String profile2, String profile3, String initials,
			String employment, String comments, String buildingStreet, double totalDue,long hawkerId, long lineId) {
		setCustomerCode(customerCode);
		setCustomerId(customerId);
		setName(name.toLowerCase());
		setMobileNum(mobileNum);
		setHawkerCode(hawkerCode);
		setLineNum(lineNum);
		setHouseSeq(houseSeq);
		setOldHouseNum(oldHouseNum);
		setNewHouseNum(newHouseNum);
		setAddrLine1(addrLine1);
		setAddrLine2(addrLine2);
		setLocality(locality);
		setCity(city);
		setState(state);
		setProfile1(profile1);
		setProfile2(profile2);
		setProfile3(profile3);
		setInitials(initials);
		setEmployment(employment);
		setComments(comments);
		setBuildingStreet(buildingStreet);
		setTotalDue(totalDue);
		setHawkerId(hawkerId);
		setLineId(lineId);
	}

	public Customer(Customer custRow) {
		// TODO Auto-generated constructor stub
		setCustomerId(custRow.getCustomerId());
		setName(custRow.getName());
		setCustomerCode(custRow.getCustomerCode());
		setMobileNum(custRow.getMobileNum());
		setHawkerCode(custRow.getHawkerCode());
		setLineNum(custRow.getLineNum());
		setHouseSeq(custRow.getHouseSeq());
		setOldHouseNum(custRow.getOldHouseNum());
		setNewHouseNum(custRow.getNewHouseNum());
		setAddrLine1(custRow.getAddrLine1());
		setAddrLine2(custRow.getAddrLine2());
		setLocality(custRow.getLocality());
		setCity(custRow.getCity());
		setState(custRow.getState());
		setProfile1(custRow.getProfile1());
		setProfile2(custRow.getProfile2());
		setProfile3(custRow.getProfile3());
		setInitials(custRow.getInitials());
		setEmployment("" + custRow.getEmployment());
		setComments("" + custRow.getComments());
		setBuildingStreet("" + custRow.getBuildingStreet());
		setHawkerId(custRow.getHawkerId());
		setLineId(custRow.getLineId());
	}

	public Long getCustomerId() {
		return customerId.get();
	}

	public void setCustomerId(long customerId) {
		this.customerId.set(customerId);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
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

	public int getHouseSeq() {
		return houseSeq.get();
	}

	public void setHouseSeq(int houseSeq) {
		this.houseSeq.set(houseSeq);
	}

	public String getOldHouseNum() {
		return oldHouseNum.get();
	}

	public void setOldHouseNum(String oldHouseNum) {
		this.oldHouseNum.set(oldHouseNum);
	}

	public String getNewHouseNum() {
		return newHouseNum.get();
	}

	public void setNewHouseNum(String newHouseNum) {
		this.newHouseNum.set(newHouseNum);
	}

	public String getAddrLine1() {
		return addrLine1.get();
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1.set(addrLine1);
	}

	public String getAddrLine2() {
		return addrLine2.get();
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2.set(addrLine2);
	}

	/*
	 * public String getAddrLine3() { return addrLine3.get(); }
	 * 
	 * public void setAddrLine3(String addrLine3) {
	 * this.addrLine3.set(addrLine3); }
	 */

	public String getLocality() {
		return locality.get();
	}

	public void setLocality(String locality) {
		this.locality.set(locality);
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public String getState() {
		return state.get();
	}

	public void setState(String state) {
		this.state.set(state);
	}

	public String getProfile1() {
		return profile1.get();
	}

	public void setProfile1(String profile1) {
		this.profile1.set(profile1);
	}

	public String getProfile2() {
		return profile2.get();
	}

	public void setProfile2(String profile2) {
		this.profile2.set(profile2);
	}

	public String getProfile3() {
		return profile3.get();
	}

	public void setProfile3(String profile3) {
		this.profile3.set(profile3);
	}

	public String getInitials() {
		return initials.get();
	}

	public void setInitials(String initials) {
		this.initials.set(initials);
	}

	public void setBuildingStreet(String buildingStreet) {
		this.buildingStreet.set(buildingStreet);
	}

	public void setComments(String comments) {
		this.comments.set(comments);

	}

	public String getEmployment() {
		return this.employment.get();
	}

	public String getBuildingStreet() {
		return this.buildingStreet.get();
	}

	public String getComments() {
		return this.comments.get();

	}

	public void setEmployment(String employment) {
		this.employment.set(employment);
	}

	public void setTotalDue(Double totalDue) {
		this.totalDue.set(totalDue);
	}

	public double getTotalDue() {

		return this.totalDue.get();
	}
	

	public Long getHawkerId() {
		return hawkerId.get();
	}

	public void setHawkerId(long hawkerId) {
		this.hawkerId.set(hawkerId);
	}
	

	public Long getLineId() {
		return lineId.get();
	}

	public void setLineId(long lineId) {
		this.lineId.set(lineId);
	}
	public void updateCustomerRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update customer set customer_code=?,  name=?, mobile_num=?, hawker_code=?,  line_Num=?,  house_Seq=?,  old_house_num=?,  new_house_num=?,  ADDRESS_LINE1=?,  ADDRESS_LINE2=?,  locality=?,  city=?,  state=?, profile1=?, profile2=?, profile3=?, initials=?, employment=?, comments=?, building_street=?, total_due=?, hawker_id=?, line_id=? where customer_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setLong(1, getCustomerCode());
			updateStmt.setString(2, getName());
			updateStmt.setString(3, getMobileNum());
			updateStmt.setString(4, getHawkerCode());
			updateStmt.setLong(5, getLineNum());
			updateStmt.setInt(6, getHouseSeq());
			updateStmt.setString(7, getOldHouseNum());
			updateStmt.setString(8, getNewHouseNum());
			updateStmt.setString(9, getAddrLine1());
			updateStmt.setString(10, getAddrLine2());
			updateStmt.setString(11, getLocality());
			updateStmt.setString(12, getCity());
			updateStmt.setString(13, getState());
			updateStmt.setString(14, getProfile1());
			updateStmt.setString(15, getProfile2());
			updateStmt.setString(16, getProfile3());
			updateStmt.setString(17, getInitials());
			updateStmt.setString(18, getEmployment());
			updateStmt.setString(19, getComments());
			updateStmt.setString(20, getBuildingStreet());
			updateStmt.setDouble(21, getTotalDue());
			updateStmt.setLong(22, getHawkerId());
			updateStmt.setLong(23, getLineId());
			updateStmt.setLong(24, getCustomerId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Update failed").text("Update request of customer has failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
