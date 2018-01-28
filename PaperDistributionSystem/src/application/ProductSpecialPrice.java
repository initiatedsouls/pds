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

public class ProductSpecialPrice {
	public SimpleLongProperty spclPriceId = new SimpleLongProperty();
	public SimpleLongProperty productId = new SimpleLongProperty();
	public SimpleObjectProperty<LocalDate> fullDate = new SimpleObjectProperty<LocalDate>();
	public SimpleDoubleProperty price = new SimpleDoubleProperty();
	public SimpleStringProperty day = new SimpleStringProperty();

	public ProductSpecialPrice(long spclPriceId, long productId, LocalDate fullDate, double price) {
		setSpclPriceId(spclPriceId);
		setProductId(productId);
		setFullDate(fullDate);
		setPrice(price);
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	public void setFullDate(LocalDate fullDate) {
		this.fullDate.set(fullDate);

	}

	public void setProductId(long productId) {
		this.productId.set(productId);

	}

	public void setSpclPriceId(long spclProductId) {
		this.spclPriceId.set(spclProductId);

	}

	public Long getSpclPriceId() {
		return this.spclPriceId.get();
	}

	public Long getProductId() {
		return this.productId.get();
	}

	public LocalDate getFullDate() {
		return this.fullDate.get();
	}

	public double getPrice() {
		return this.price.get();
	}
	
	public String getDay(){
		return this.fullDate.getValue().getDayOfWeek().toString();
	}

	public void updateProductSpecialPriceRecord() {
		try {

			Connection con = Main.dbConnection;
			if (con.isClosed()) {
				con = Main.reconnect();
			}
			String updateString = "update prod_spcl_price set full_date=?, price=? where spcl_price_id=?";
			PreparedStatement updateStmt = con.prepareStatement(updateString);
			updateStmt.setDate(1, Date.valueOf(getFullDate()));
			updateStmt.setDouble(2, getPrice());
			updateStmt.setDouble(3, getSpclPriceId());
			updateStmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			Main._logger.debug("Error :",e);
			e.printStackTrace();
			Notifications.create().title("Failed").text("Product Special Price record update failed").showError();
		} catch (Exception e) {

			Main._logger.debug("Error :",e);
			e.printStackTrace();
		}
	}

}
