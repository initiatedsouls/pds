package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class LineDistributor {
	private final SimpleLongProperty lineDistId = new SimpleLongProperty();
	private final SimpleStringProperty name = new SimpleStringProperty();
	private final SimpleStringProperty mobileNum = new SimpleStringProperty();
	private final SimpleLongProperty hawkerId = new SimpleLongProperty();
	private final SimpleStringProperty hawkerCode = new SimpleStringProperty();
	private final SimpleIntegerProperty lineNum = new SimpleIntegerProperty();
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
	private final SimpleLongProperty lineId = new SimpleLongProperty();

	public LineDistributor(long lineDistId, String name, String mobileNum, long hawkerId, int lineNum,
			String oldHouseNum, String newHouseNum, String addrLine1, String addrLine2, String locality, String city,
			String state, String profile1, String profile2, String profile3, String initials, String employment,
			String comments, String buildingStreet, String hawkerCode, long lineId) {
		setLineDistId(lineDistId);
		setName(name);
		setMobileNum(mobileNum);
		setHawkerId(hawkerId);
		setLineNum(lineNum);
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
		setHawkerCode(hawkerCode);
		setLineId(lineId);
	}

	public Long getLineDistId() {
		return lineDistId.get();
	}

	public void setLineDistId(long lineDistId) {
		this.lineDistId.set(lineDistId);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getMobileNum() {
		return mobileNum.get();
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum.set(mobileNum);
	}

	public Long getHawkerId() {
		return hawkerId.get();
	}

	public void setHawkerId(long hawkerId) {
		this.hawkerId.set(hawkerId);
	}

	public int getLineNum() {
		return lineNum.get();
	}

	public void setLineNum(int lineNum) {
		this.lineNum.set(lineNum);
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

	public void setHawkerCode(String hawkerCode) {
		this.hawkerCode.set(hawkerCode);
	}

	public String getHawkerCode() {
		return hawkerCode.get();
	}
	

	public Long getLineId() {
		return lineId.get();
	}

	public void setLineId(long lineId) {
		this.lineId.set(lineId);
	}


	public void updateLineDistRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update line_distributor set name=?, mobile_num=?, line_num=?,  old_house_num=?,  new_house_num=?,  address_line1=?,  address_line2=?,  locality=?,  city=?, state=?, profile1=?, profile2=?, profile3=?, initials=?, employment=?, comments=?, building_street=?, line_id=?  where line_dist_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getName());
			updateStmt.setString(2, getMobileNum());
			updateStmt.setInt(3, getLineNum());
			updateStmt.setString(4, getOldHouseNum());
			updateStmt.setString(5, getNewHouseNum());
			updateStmt.setString(6, getAddrLine1());
			updateStmt.setString(7, getAddrLine2());
			updateStmt.setString(8, getLocality());
			updateStmt.setString(9, getCity());
			updateStmt.setString(10, getState());
			updateStmt.setString(11, getProfile1());
			updateStmt.setString(12, getProfile2());
			updateStmt.setString(13, getProfile3());
			updateStmt.setString(14, getInitials());
			updateStmt.setString(15, getEmployment());
			updateStmt.setString(16, getComments());
			updateStmt.setString(17, getBuildingStreet());
			updateStmt.setLong(18, getLineId());
			updateStmt.setLong(19, getLineDistId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Line distributor update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
