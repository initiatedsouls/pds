package application;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;

public class Hawker {

	private final SimpleLongProperty hawkerId = new SimpleLongProperty();
	private SimpleStringProperty name = new SimpleStringProperty("");
	private SimpleStringProperty hawkerCode = new SimpleStringProperty("");
	private SimpleStringProperty mobileNum = new SimpleStringProperty("");
	private SimpleStringProperty agencyName = new SimpleStringProperty("");
	private  SimpleBooleanProperty activeFlag = new SimpleBooleanProperty();
	private  SimpleDoubleProperty fee = new SimpleDoubleProperty();
	private  SimpleStringProperty oldHouseNum = new SimpleStringProperty("");
	private  SimpleStringProperty newHouseNum = new SimpleStringProperty("");
	private  SimpleStringProperty addrLine1 = new SimpleStringProperty("");
	private  SimpleStringProperty addrLine2 = new SimpleStringProperty("");
	private  SimpleStringProperty locality = new SimpleStringProperty("");
	private  SimpleStringProperty city = new SimpleStringProperty("");
	private  SimpleStringProperty state = new SimpleStringProperty("");
	private SimpleStringProperty customerAccess = new SimpleStringProperty("");
	private SimpleStringProperty billingAccess = new SimpleStringProperty("");
	private SimpleStringProperty lineInfoAccess = new SimpleStringProperty("");
	private SimpleStringProperty lineDistAccess = new SimpleStringProperty("");
	private SimpleStringProperty pausedCustAccess = new SimpleStringProperty("");
	private SimpleStringProperty productAccess = new SimpleStringProperty("");
	private SimpleStringProperty reportsAccess = new SimpleStringProperty("");
	private SimpleDoubleProperty totalDue = new SimpleDoubleProperty();
	private SimpleStringProperty profile1 = new SimpleStringProperty("");
	private SimpleStringProperty profile2 = new SimpleStringProperty("");
	private SimpleStringProperty profile3 = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty initials = new SimpleStringProperty("");
	private SimpleStringProperty employment = new SimpleStringProperty("");
	private SimpleStringProperty comments = new SimpleStringProperty("");
	private SimpleStringProperty pointName = new SimpleStringProperty("");
	private SimpleStringProperty buildingStreet = new SimpleStringProperty("");
	private SimpleStringProperty bankAcNo = new SimpleStringProperty("");
	private SimpleStringProperty bankName = new SimpleStringProperty("");
	private SimpleStringProperty ifscCode = new SimpleStringProperty("");
	private SimpleStringProperty stopHistoryAccess = new SimpleStringProperty("");
	private SimpleStringProperty benName = new SimpleStringProperty("");
	private Blob logo;

	public Hawker(long hawkerId, String name, String hawkerCode, String moblieNum, String agencyName,
			boolean activeFlag, double fee, String oldHouseNum, String newHouseNum, String addrLine1, String addrLine2,
			String locality, String city, String state, String customerAccess, String billingAccess,
			String lineInfoAccess, String lineDistAccess, String pausedCustAccess, String productAccess,
			String reportsAccess, String profile1, String profile2, String profile3, String initials, String password,
			String employment, String comments, String pointName, String buildingStreet, String bankAcNo,
			String bankName, String ifscCode, String stopHistoryAccess, Blob logo) {
		super();
		setHawkerId(hawkerId);
		setName(name);
		setHawkerCode(hawkerCode);
		setMobileNum(moblieNum);
		setAgencyName(("" + agencyName));
		setActiveFlag(activeFlag);
		setFee(fee);
		setOldHouseNum(oldHouseNum);
		setNewHouseNum(newHouseNum);
		setAddrLine1(addrLine1);
		setAddrLine2(addrLine2);
		setLocality(locality);
		setCity(city);
		setState(state);
		setCustomerAccess(customerAccess);
		setBillingAccess(billingAccess);
		setLineInfoAccess(lineInfoAccess);
		setLineDistAccess(lineDistAccess);
		setPausedCustAccess(pausedCustAccess);
		setProductAccess(productAccess);
		setReportsAccess(reportsAccess);
		setProfile1(profile1);
		setProfile2(profile2);
		setProfile3(profile3);
		setInitials(initials);
		setPassword(password);
		setEmployment(employment);
		setComments(comments);
		setPointName(pointName);
		setBuildingStreet(buildingStreet);
		setBankAcNo(bankAcNo);
		setBankName(bankName);
		setIfscCode(ifscCode);
		setStopHistoryAccess(stopHistoryAccess);
		setLogo(logo);
	}

	public Hawker(long hawkerId, String name, String hawkerCode, String moblieNum, String agencyName,
			boolean activeFlag, double fee, String oldHouseNum, String newHouseNum, String addrLine1, String addrLine2,
			String locality, String city, String state, String customerAccess, String billingAccess,
			String lineInfoAccess, String lineDistAccess, String pausedCustAccess, String productAccess,
			String reportsAccess, String profile1, String profile2, String profile3, String initials, String password,
			String employment, String comments, String pointName, String buildingStreet, String bankAcNo,
			String bankName, String ifscCode, String stopHistoryAccess, Blob logo, String benName, double totalDue) {
		super();
		setHawkerId(hawkerId);
		setName(name);
		setHawkerCode(hawkerCode);
		setMobileNum(moblieNum);
		setAgencyName(("" + agencyName));
		setActiveFlag(activeFlag);
		setFee(fee);
		setOldHouseNum(oldHouseNum);
		setNewHouseNum(newHouseNum);
		setAddrLine1(addrLine1);
		setAddrLine2(addrLine2);
		setLocality(locality);
		setCity(city);
		setState(state);
		setCustomerAccess(customerAccess);
		setBillingAccess(billingAccess);
		setLineInfoAccess(lineInfoAccess);
		setLineDistAccess(lineDistAccess);
		setPausedCustAccess(pausedCustAccess);
		setProductAccess(productAccess);
		setReportsAccess(reportsAccess);
		setProfile1(profile1);
		setProfile2(profile2);
		setProfile3(profile3);
		setInitials(initials);
		setPassword(password);
		setEmployment(employment);
		setComments(comments);
		setPointName(pointName);
		setBuildingStreet(buildingStreet);
		setBankAcNo(bankAcNo);
		setBankName(bankName);
		setIfscCode(ifscCode);
		setStopHistoryAccess(stopHistoryAccess);
		setLogo(logo);
		setBenName(benName);
		setTotalDue(totalDue);
	}

	public Hawker(Hawker hawkerRow) {
		setHawkerId(hawkerRow.getHawkerId());
		setName(hawkerRow.getName());
		setMobileNum(hawkerRow.getMobileNum());
		setHawkerCode(hawkerRow.getHawkerCode());
		setOldHouseNum(hawkerRow.getOldHouseNum());
		setNewHouseNum(hawkerRow.getNewHouseNum());
		setAddrLine1(hawkerRow.getAddrLine1());
		setAddrLine2(hawkerRow.getAddrLine2());
		setLocality(hawkerRow.getLocality());
		setCity(hawkerRow.getCity());
		setState(hawkerRow.getState());
		setActiveFlag(hawkerRow.getActiveFlag());
		setFee(hawkerRow.getFee());
		setAgencyName(hawkerRow.getAgencyName());
		setCustomerAccess(hawkerRow.getCustomerAccess());
		setBillingAccess(hawkerRow.getBillingAccess());
		setLineInfoAccess(hawkerRow.getLineInfoAccess());
		setLineDistAccess(hawkerRow.getLineDistAccess());
		setPausedCustAccess(hawkerRow.getPausedCustAccess());
		setProductAccess(hawkerRow.getProductAccess());
		setReportsAccess(hawkerRow.getReportsAccess());
		setStopHistoryAccess(hawkerRow.getStopHistoryAccess());
		setInitials(hawkerRow.getInitials());
		setProfile1(hawkerRow.getProfile1());
		setProfile2(hawkerRow.getProfile2());
		setProfile3(hawkerRow.getProfile3());
		setPassword(hawkerRow.getPassword());
		setEmployment(hawkerRow.getEmployment());
		setComments(hawkerRow.getComments());
		setPointName(hawkerRow.getPointName());
		setBuildingStreet(hawkerRow.getBuildingStreet());

		setBankAcNo(hawkerRow.getBankAcNo());
		setBankName(hawkerRow.getBankName());
		setIfscCode(hawkerRow.getIfscCode());
		setLogo(hawkerRow.getLogo());
		setBenName(hawkerRow.getBenName());
		setTotalDue(hawkerRow.getTotalDue());
	}

	public Long getHawkerId() {
		return hawkerId.get();
	}

	public String getName() {
		return name.get();
	}

	public String getHawkerCode() {
		return hawkerCode.get();
	}

	public String getMobileNum() {
		return mobileNum.get();
	}

	public String getAgencyName() {
		return agencyName.get();
	}

	public boolean getActiveFlag() {
		return activeFlag.get();
	}

	public double getFee() {
		return fee.get();
	}

	public String getOldHouseNum() {
		return oldHouseNum.get();
	}

	public String getNewHouseNum() {
		return newHouseNum.get();
	}

	public String getAddrLine1() {
		return addrLine1.get();
	}

	public String getAddrLine2() {
		return addrLine2.get();
	}

	public String getLocality() {
		return locality.get();
	}

	public String getCity() {
		return city.get();
	}

	public String getState() {
		return state.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setHawkerId(long hawkerId) {
		this.hawkerId.set(hawkerId);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setHawkerCode(String hawkerCode) {
		this.hawkerCode.set(hawkerCode);
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum.set(mobileNum);
	}

	public void setAgencyName(String agencyName) {
		this.agencyName.set(agencyName);
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag.set(activeFlag);
	}

	public void setFee(double fee) {
		this.fee.set(fee);
	}

	public void setOldHouseNum(String oldHouseNum) {
		this.oldHouseNum.set(oldHouseNum);
	}

	public void setNewHouseNum(String newHouseNum) {
		this.newHouseNum.set(newHouseNum);
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1.set(addrLine1);
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2.set(addrLine2);
	}

	public void setLocality(String locality) {
		this.locality.set(locality);
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public void setState(String state) {
		this.state.set(state);
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public String getCustomerAccess() {
		return customerAccess.get();
	}

	public void setCustomerAccess(String customerAccess) {
		this.customerAccess.set(customerAccess);
	}

	public String getBillingAccess() {
		return billingAccess.get();
	}

	public void setBillingAccess(String billingAccess) {
		this.billingAccess.set(billingAccess);
	}

	public String getLineInfoAccess() {
		return lineInfoAccess.get();
	}

	public void setLineInfoAccess(String lineInfoAccess) {
		this.lineInfoAccess.set(lineInfoAccess);
	}

	public String getLineDistAccess() {
		return lineDistAccess.get();
	}

	public void setLineDistAccess(String lineDistAccess) {
		this.lineDistAccess.set(lineDistAccess);
	}

	public String getPausedCustAccess() {
		return pausedCustAccess.get();
	}

	public void setPausedCustAccess(String pausedCustAccess) {
		this.pausedCustAccess.set(pausedCustAccess);
	}

	public String getProductAccess() {
		return productAccess.get();
	}

	public void setProductAccess(String productAccess) {
		this.productAccess.set(productAccess);
	}

	public String getReportsAccess() {
		return reportsAccess.get();
	}

	public void setReportsAccess(String reportsAccess) {
		this.reportsAccess.set(reportsAccess);
	}

	public double getTotalDue() {
		return totalDue.get();
	}

	public void setTotalDue(double totalDue) {
		this.totalDue.set(totalDue);
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

	public void setPointName(String pointName) {
		this.pointName.set(pointName);
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

	public String getPointName() {
		return this.pointName.get();
	}

	public String getComments() {
		return this.comments.get();

	}

	public void setEmployment(String employment) {
		this.employment.set(employment);
	}

	public String toString() {
		return getHawkerCode();
	}

	public ObservableBooleanValue isActive() {
		return activeFlag;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode.set(ifscCode);
	}

	public void setBankName(String bankName) {
		this.bankName.set(bankName);

	}

	public void setBankAcNo(String bankAcNo) {
		this.bankAcNo.set(bankAcNo);

	}

	public String getIfscCode() {

		return this.ifscCode.get();
	}

	public String getBankName() {

		return this.bankName.get();
	}

	public String getBankAcNo() {

		return this.bankAcNo.get();
	}

	public String getStopHistoryAccess() {
		return stopHistoryAccess.get();
	}

	public void setStopHistoryAccess(String stopHistoryAccess) {
		this.stopHistoryAccess.set(stopHistoryAccess);
	}

	public String getBenName() {
		return benName.get();
	}

	public void setBenName(String benName) {
		this.benName.set(benName);
	}
	public Blob getLogo() {
		/*logo.setb
		BufferedImage image = null;
		try {
			InputStream in = this.logo.getBinaryStream();  
			image = ImageIO.read(in);
		} catch (SQLException | IOException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}*/
		
		return logo;
	}

	public void setLogo(Blob logo) {
		this.logo = logo;
	}

	public void calculateTotalDue() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String sqlString = "select sum(due) from HAWKER_BILLING where HAWKER_ID=?";
			PreparedStatement stmt = con.prepareStatement(sqlString);
			stmt.setLong(1, getHawkerId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				setTotalDue(rs.getDouble(1));
			}
		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

	public void updateHawkerRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update hawker_info set name=?, hawker_code=?,  mobile_num=?,  agency_name=?,  active_flag=?,  fee=?,  old_house_num=?,  new_house_num=?,  addr_line1=?,  addr_line2=?,  locality=?,  city=?,  state=?, customer_access=?,  billing_access=?,  line_info_access=?,  line_dist_access=?,  paused_cust_access=?,  product_access=?,  reports_access=?, total_Due=?, profile1=?, profile2=?, profile3=?, password=?, initials=?, employment=?, comments=?, point_name=?, building_street=?, bank_ac_no=?, bank_name=?, ifsc_code=?, stop_history_access=?, logo=? , BEN_NAME=? where hawker_id = ?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getName());
			updateStmt.setString(2, getHawkerCode());
			updateStmt.setString(3, getMobileNum());
			updateStmt.setString(4, getAgencyName());
			updateStmt.setString(5, getActiveFlag() ? "Y" : "N");
			updateStmt.setDouble(6, getFee());
			updateStmt.setString(7, getOldHouseNum());
			updateStmt.setString(8, getNewHouseNum());
			updateStmt.setString(9, getAddrLine1());
			updateStmt.setString(10, getAddrLine2());
			updateStmt.setString(11, getLocality());
			updateStmt.setString(12, getCity());
			updateStmt.setString(13, getState());
			updateStmt.setString(14, getCustomerAccess());
			updateStmt.setString(15, getBillingAccess());
			updateStmt.setString(16, getLineInfoAccess());
			updateStmt.setString(17, getLineDistAccess());
			updateStmt.setString(18, getPausedCustAccess());
			updateStmt.setString(19, getProductAccess());
			updateStmt.setString(20, getReportsAccess());
			updateStmt.setDouble(21, getTotalDue());
			updateStmt.setString(22, getProfile1());
			updateStmt.setString(23, getProfile2());
			updateStmt.setString(24, getProfile3());
			updateStmt.setString(25, getPassword());
			updateStmt.setString(26, getInitials());
			updateStmt.setString(27, getEmployment());
			updateStmt.setString(28, getComments());
			updateStmt.setString(29, getPointName());
			updateStmt.setString(30, getBuildingStreet());
			updateStmt.setString(31, getBankAcNo());
			updateStmt.setString(32, getBankName());
			updateStmt.setString(33, getIfscCode());
			updateStmt.setString(34, getStopHistoryAccess());
			updateStmt.setBlob(35, logo);
			updateStmt.setString(36, getBenName());
			updateStmt.setLong(37, getHawkerId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Hawker update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
