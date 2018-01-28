package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class BillingLine {
	private final SimpleLongProperty billLineId = new SimpleLongProperty();
	private final SimpleLongProperty billInvoiceNum = new SimpleLongProperty();
	private final SimpleIntegerProperty lineNum = new SimpleIntegerProperty();
	private final SimpleStringProperty product = new SimpleStringProperty();
	private final SimpleDoubleProperty amount = new SimpleDoubleProperty();
	private final SimpleDoubleProperty teaExpenses = new SimpleDoubleProperty();

	public BillingLine(long billLineId, long billInvoiceNum, int lineNum, String product, double amount,
			double teaExpenses) {
		setBillLineId(billLineId);
		setBillInvoiceNum(billInvoiceNum);
		setLineNum(lineNum);
		setAmount(amount);
		setProduct(product);
		setTeaExpenses(teaExpenses);
	}

	public void setLineNum(int lineNum) {
		this.lineNum.set(lineNum);
	}

	public void setProduct(String product) {
		this.product.set(product);
	}

	public void setBillLineId(long billLineId) {
		this.billLineId.set(billLineId);
	}

	public void setBillInvoiceNum(long billInvoiceNum) {
		this.billInvoiceNum.set(billInvoiceNum);
	}

	public long getBillInvoiceNum() {
		return this.billInvoiceNum.get();
	}

	public long getBillLineId() {
		return this.billLineId.get();
	}

	public String getProduct() {
		return this.product.get();
	}

	public void setAmount(Double amount) {
		this.amount.set(amount);
	}

	public Double getAmount() {

		return this.amount.get();
	}

	public void setTeaExpenses(Double teaExpenses) {
		this.teaExpenses.set(teaExpenses);
	}

	public Double getTeaExpenses() {

		return this.teaExpenses.get();
	}

	public int getLineNum() {

		return this.lineNum.get();
	}

	public void updateBillLineRecord() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update billing_lines set line_num=?, product=?, amount=?, tea_expenses=? where BILL_LINE_ID=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setInt(1, getLineNum());
			updateStmt.setString(2, getProduct());
			updateStmt.setDouble(3, getAmount());
			updateStmt.setDouble(4, getTeaExpenses());
			updateStmt.setLong(5, getBillLineId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Billing Lines record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
