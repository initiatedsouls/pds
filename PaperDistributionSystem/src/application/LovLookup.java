package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class LovLookup {

	SimpleStringProperty code = new SimpleStringProperty("");
	SimpleStringProperty value = new SimpleStringProperty("");
	SimpleIntegerProperty seq = new SimpleIntegerProperty();
	SimpleLongProperty lovLookupId = new SimpleLongProperty();

	public LovLookup(String code, String value, int seq) {
		setCode(code);
		setValue(value);
		setSeq(seq);
	}

	public void setCode(String code) {
		this.code.set(code);

	}

	public void setValue(String value) {
		this.value.set(value);
	}

	public void setSeq(int seq) {
		this.seq.set(seq);
	}

	public void setLovLookupId(long lovLookupId) {
		this.lovLookupId.set(lovLookupId);
	}

	public String getCode() {
		return this.code.get();
	}

	public String getValue() {
		return this.value.get();
	}

	public int getSeq() {
		return this.seq.get();
	}

	public long getLovLookupId() {
		return this.lovLookupId.get();
	}

	public void updateLookup() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update lov_lookup set value=? where lov_lookup_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setString(1, getValue());
			updateStmt.setLong(2, getLovLookupId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}
}
