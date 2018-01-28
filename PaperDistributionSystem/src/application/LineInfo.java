package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class LineInfo {
	private final SimpleLongProperty lineId = new SimpleLongProperty();
	private final SimpleIntegerProperty lineNum = new SimpleIntegerProperty();
	private final SimpleLongProperty hawkerId = new SimpleLongProperty();
	private final SimpleStringProperty lineNumDist = new SimpleStringProperty();

	public LineInfo(long lineId, int lineNum, long hawkerId, String lineNumDist) {
		setLineId(lineId);
		setLineNum(lineNum);
		setHawkerId(hawkerId);
		setLineNumDist(lineNumDist);
	}

	public Long getLineId() {
		return lineId.get();
	}

	public void setLineId(long lineId) {
		this.lineId.set(lineId);
	}

	public int getLineNum() {
		return lineNum.get();
	}

	public void setLineNum(int lineNum) {
		this.lineNum.set(lineNum);
	}

	public Long getHawkerId() {
		return hawkerId.get();
	}

	public void setHawkerId(long hawkerId) {
		this.hawkerId.set(hawkerId);
	}

	public String getLineNumDist() {
		return lineNumDist.get();
	}

	public void setLineNumDist(String lineNumDist) {
		this.lineNumDist.set(lineNumDist);
	}

	public String toString() {
		return getLineNum() + "";
	}

	public void updateLineNumRecord() {
		try {

			Connection con = Main.dbConnection;
			while (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update line_info set line_num=? where line_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setInt(1, getLineNum());
			updateStmt.setLong(2, getLineId());
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
