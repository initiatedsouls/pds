package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Billing {

	private final SimpleLongProperty billInvoiceNum = new SimpleLongProperty();
	private final SimpleLongProperty customerId = new SimpleLongProperty();
	private final SimpleObjectProperty<LocalDate> invoiceDate = new SimpleObjectProperty<LocalDate>();
	private final SimpleStringProperty pdfURL = new SimpleStringProperty();
	private final SimpleDoubleProperty due = new SimpleDoubleProperty();
	private final SimpleStringProperty month = new SimpleStringProperty();

	public Billing(long billInvoiceNum, long customerId, LocalDate invoiceDate, String pdfURL, double due, String month) {
		setBillInvoiceNum(billInvoiceNum);
		setCustomerId(customerId);
		setInvoiceDate(invoiceDate);
		setPdfURL(pdfURL);
		setDue(due);
		setMonth(month);
	}

	public void setPdfURL(String pdfURL) {
		this.pdfURL.set(pdfURL);
	}
	
	public void setMonth(String month) {
		this.month.set(month);
	}


	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate.set(invoiceDate);
	}

	public void setCustomerId(long customerId) {
		this.customerId.set(customerId);
	}

	public void setBillInvoiceNum(long billInvoiceNum) {
		this.billInvoiceNum.set(billInvoiceNum);
	}

	public long getBillInvoiceNum() {
		return this.billInvoiceNum.get();
	}

	public String getPdfURL() {
		return this.pdfURL.get();
	}
	public String getMonth() {
		return this.month.get();
	}

	public LocalDate getInvoiceDate() {
		return this.invoiceDate.get();
	}

	public double getDue() {
		return this.due.get();
	}

	public void setDue(double due) {
		this.due.set(due);
	}

	public void updateBillingRecord() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update billing set INVOICE_DATE=?, PDF_URL=?, DUE=?, MONTH=? where BILL_INVOICE_NUM=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setDate(1, Date.valueOf(getInvoiceDate()));
			updateStmt.setString(2, getPdfURL());
			updateStmt.setDouble(3, getDue());
			updateStmt.setLong(5, getBillInvoiceNum());
			updateStmt.setString(4, getMonth());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Bill History record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
