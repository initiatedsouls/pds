package application;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class ExportToExcel {
	public static void exportHwkAllLineSubCountToExcel(String hawkerCode, LocalDate forDate){
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				try {

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create().title("Data export started").text("Export of data started, please wait.")
							.hideAfter(Duration.seconds(10)).showInformation();

						}
					});
					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					String filename = hawkerCode+"-AllLineSubsCount-"+ ".xlsx";
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet hawkersheet = workbook.createSheet("AllLineSubsCount");
					XSSFRow row = hawkersheet.createRow(1);
					XSSFCell cell;

					cell = row.createCell(1);
					cell.setCellValue("HWK_ID");
					cell = row.createCell(2);
					cell.setCellValue("HWK_NAME");
					cell = row.createCell(3);
					cell.setCellValue("HWK_CODE");
					cell = row.createCell(4);
					cell.setCellValue("ACTIVE_FLAG");
					cell = row.createCell(5);
					cell.setCellValue("AGENCY_NAME");
					cell = row.createCell(6);
					cell.setCellValue("HWK_MOB_NUM");
					cell = row.createCell(7);
					cell.setCellValue("LINE_HWK_ID");
					cell = row.createCell(8);
					cell.setCellValue("LINE_ID");
					cell = row.createCell(9);
					cell.setCellValue("LINE_NUM");
					cell = row.createCell(10);
					cell.setCellValue("DIST_HWK_ID");
					cell = row.createCell(11);
					cell.setCellValue("LINE_DIST_ID");
					cell = row.createCell(12);
					cell.setCellValue("DIST_LINE_ID");
					cell = row.createCell(13);
					cell.setCellValue("DIST_LINE_NUM");
					cell = row.createCell(14);
					cell.setCellValue("DIST_NAME");
					cell = row.createCell(15);
					cell.setCellValue("CUST_NAME");
					cell = row.createCell(16);
					cell.setCellValue("CUST_HWK_ID");
					cell = row.createCell(17);
					cell.setCellValue("CUST_LINE_ID");
					cell = row.createCell(18);
					cell.setCellValue("CUST_LINE_NUM");
					cell = row.createCell(19);
					cell.setCellValue("CUST_HWK_CODE");
					cell = row.createCell(20);
					cell.setCellValue("HOUSE_SEQ");
					cell = row.createCell(21);
					cell.setCellValue("CUST_ID");
					cell = row.createCell(22);
					cell.setCellValue("CUST_CODE");
					cell = row.createCell(23);
					cell.setCellValue("SUB_ID");
					cell = row.createCell(24);
					cell.setCellValue("SUB_PROD_ID");
					cell = row.createCell(25);
					cell.setCellValue("SUB_CUST_ID");
					cell = row.createCell(26);
					cell.setCellValue("PRD");
					cell = row.createCell(27);
					cell.setCellValue("SUB_DOW");
					cell = row.createCell(28);
					cell.setCellValue("SUB_FREQ");
					cell = row.createCell(29);
					cell.setCellValue("PROD_ID");
					cell = row.createCell(30);
					cell.setCellValue("PROD_NAME");
					cell = row.createCell(31);
					cell.setCellValue("PROD_CODE");
					cell = row.createCell(32);
					cell.setCellValue("FIRST_DELIVERY_DATE");
					cell = row.createCell(33);
					cell.setCellValue("SUB_START_DATE");


					PreparedStatement stmt = con.prepareStatement(
							"select HWK_ID, HWK_NAME, HWK_CODE, ACTIVE_FLAG, AGENCY_NAME, HWK_MOB_NUM, LINE_HWK_ID, LINE_ID, LINE_NUM, DIST_HWK_ID, LINE_DIST_ID, DIST_LINE_ID, DIST_LINE_NUM, DIST_NAME, CUST_NAME, CUST_HWK_ID, CUST_LINE_ID, CUST_LINE_NUM, CUST_HWK_CODE, HOUSE_SEQ, CUST_ID, CUST_CODE, SUB_ID, SUB_PROD_ID, SUB_CUST_ID, PRD, SUB_DOW, SUB_FREQ, PROD_ID, PROD_NAME, PROD_CODE, FIRST_DELIVERY_DATE, SUB_START_DATE FROM HWK_ALL_LINE_PROD_LIST_XLS WHERE HWK_CODE=?");
					stmt.setString(1, hawkerCode);
					ResultSet rs = stmt.executeQuery();
					int i = 2;
					while (rs.next()) {
						row = hawkersheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("HWK_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("HWK_NAME"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("HWK_CODE"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("ACTIVE_FLAG"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("AGENCY_NAME"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getString("HWK_MOB_NUM"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getString("LINE_HWK_ID"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getString("LINE_ID"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("LINE_NUM"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("DIST_HWK_ID"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("LINE_DIST_ID"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("DIST_LINE_ID"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("DIST_LINE_NUM"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("DIST_NAME"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("CUST_NAME"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("CUST_HWK_ID"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("CUST_LINE_ID"));
						cell = row.createCell(18);
						cell.setCellValue(rs.getString("CUST_LINE_NUM"));
						cell = row.createCell(19);
						cell.setCellValue(rs.getString("CUST_HWK_CODE"));
						cell = row.createCell(20);
						cell.setCellValue(rs.getString("HOUSE_SEQ"));
						cell = row.createCell(21);
						cell.setCellValue(rs.getString("CUST_ID"));
						cell = row.createCell(22);
						cell.setCellValue(rs.getString("CUST_CODE"));
						cell = row.createCell(23);
						cell.setCellValue(rs.getString("SUB_ID"));
						cell = row.createCell(24);
						cell.setCellValue(rs.getString("SUB_PROD_ID"));
						cell = row.createCell(25);
						cell.setCellValue(rs.getString("SUB_CUST_ID"));
						cell = row.createCell(26);
						cell.setCellValue(rs.getString("PRD"));
						cell = row.createCell(27);
						cell.setCellValue(rs.getString("SUB_DOW"));
						cell = row.createCell(28);
						cell.setCellValue(rs.getString("SUB_FREQ"));
						cell = row.createCell(29);
						cell.setCellValue(rs.getString("PROD_ID"));
						cell = row.createCell(30);
						cell.setCellValue(rs.getString("PROD_NAME"));
						cell = row.createCell(31);
						cell.setCellValue(rs.getString("PROD_CODE"));
						cell = row.createCell(32);
						cell.setCellValue(rs.getString("FIRST_DELIVERY_DATE"));
						cell = row.createCell(33);
						cell.setCellValue(rs.getString("SUB_START_DATE"));

						i++;
					}

					

					FileOutputStream out = new FileOutputStream(new File("C:\\pds\\" + filename));
					workbook.write(out);
					out.close();
					rs.close();
					stmt.close();
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create().title("Data exported")
									.text("Data successfully exported to location : " + "C:\\pds\\" + filename)
									.hideAfter(Duration.seconds(10)).showInformation();

						}
					});

				} catch (SQLException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}

				return null;
			}

		};
		new Thread(task).start();
	}

	public static void exportDataToExcel() {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				try {

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create().title("Data export started").text("Export of data started, please wait.")
									.hideAfter(Duration.seconds(10)).showInformation();

						}
					});
					Connection con = Main.dbConnection;
					if (con.isClosed()) {
						con = Main.reconnect();
					}
					String filename = "data-" + new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date())
							+ ".xlsx";
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet hawkersheet = workbook.createSheet("HAWKER");
					XSSFRow row = hawkersheet.createRow(1);
					XSSFCell cell;

					cell = row.createCell(1);
					cell.setCellValue("hawker_id");
					cell = row.createCell(2);
					cell.setCellValue("name");
					cell = row.createCell(3);
					cell.setCellValue("hawker_code");
					cell = row.createCell(4);
					cell.setCellValue("mobile_num");
					cell = row.createCell(5);
					cell.setCellValue("agency_name");
					cell = row.createCell(6);
					cell.setCellValue("active_flag");
					cell = row.createCell(7);
					cell.setCellValue("fee");
					cell = row.createCell(8);
					cell.setCellValue("old_house_num");
					cell = row.createCell(9);
					cell.setCellValue("new_house_num");
					cell = row.createCell(10);
					cell.setCellValue("addr_line1");
					cell = row.createCell(11);
					cell.setCellValue("addr_line2");
					cell = row.createCell(12);
					cell.setCellValue("locality");
					cell = row.createCell(13);
					cell.setCellValue("city");
					cell = row.createCell(14);
					cell.setCellValue("state");
					cell = row.createCell(15);
					cell.setCellValue("customer_access");
					cell = row.createCell(16);
					cell.setCellValue("billing_access");
					cell = row.createCell(17);
					cell.setCellValue("line_info_access");
					cell = row.createCell(18);
					cell.setCellValue("line_dist_access");
					cell = row.createCell(19);
					cell.setCellValue("paused_cust_access");
					cell = row.createCell(20);
					cell.setCellValue("product_access");
					cell = row.createCell(21);
					cell.setCellValue("reports_access");
					cell = row.createCell(22);
					cell.setCellValue("profile1");
					cell = row.createCell(23);
					cell.setCellValue("profile2");
					cell = row.createCell(24);
					cell.setCellValue("profile3");
					cell = row.createCell(25);
					cell.setCellValue("initials");
					cell = row.createCell(26);
					cell.setCellValue("password");
					cell = row.createCell(27);
					cell.setCellValue("employment");
					cell = row.createCell(28);
					cell.setCellValue("comments");
					cell = row.createCell(29);
					cell.setCellValue("point_name");
					cell = row.createCell(30);
					cell.setCellValue("building_street");
					cell = row.createCell(31);
					cell.setCellValue("bank_ac_no");
					cell = row.createCell(32);
					cell.setCellValue("bank_name");
					cell = row.createCell(33);
					cell.setCellValue("ifsc_code");
					cell = row.createCell(34);
					cell.setCellValue("stop_history_access");

					PreparedStatement stmt = con.prepareStatement(
							"select hawker_id,name,hawker_code, mobile_num, agency_name, active_flag, fee, old_house_num, new_house_num, addr_line1, addr_line2, locality, city, state,customer_access, billing_access, line_info_access, line_dist_access, paused_cust_access, product_access, reports_access,profile1,profile2,profile3,initials,password, employment, comments, point_name, building_street,bank_ac_no,bank_name,ifsc_code,stop_history_access from hawker_info order by name");

					ResultSet rs = stmt.executeQuery();
					int i = 2;
					while (rs.next()) {
						row = hawkersheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("hawker_id"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("name"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("hawker_code"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("mobile_num"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("agency_name"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getString("active_flag"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getDouble("fee"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getString("old_house_num"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("new_house_num"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("addr_line1"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("addr_line2"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("locality"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("city"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("state"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("customer_access"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("billing_access"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("line_info_access"));
						cell = row.createCell(18);
						cell.setCellValue(rs.getString("line_dist_access"));
						cell = row.createCell(19);
						cell.setCellValue(rs.getString("paused_cust_access"));
						cell = row.createCell(20);
						cell.setCellValue(rs.getString("product_access"));
						cell = row.createCell(21);
						cell.setCellValue(rs.getString("reports_access"));
						cell = row.createCell(22);
						cell.setCellValue(rs.getString("profile1"));
						cell = row.createCell(23);
						cell.setCellValue(rs.getString("profile2"));
						cell = row.createCell(24);
						cell.setCellValue(rs.getString("profile3"));
						cell = row.createCell(25);
						cell.setCellValue(rs.getString("initials"));
						cell = row.createCell(26);
						cell.setCellValue(rs.getString("password"));
						cell = row.createCell(27);
						cell.setCellValue(rs.getString("employment"));
						cell = row.createCell(28);
						cell.setCellValue(rs.getString("comments"));
						cell = row.createCell(29);
						cell.setCellValue(rs.getString("point_name"));
						cell = row.createCell(30);
						cell.setCellValue(rs.getString("building_street"));
						cell = row.createCell(31);
						cell.setCellValue(rs.getString("bank_ac_no"));
						cell = row.createCell(32);
						cell.setCellValue(rs.getString("bank_name"));
						cell = row.createCell(33);
						cell.setCellValue(rs.getString("ifsc_code"));
						cell = row.createCell(34);
						cell.setCellValue(rs.getString("stop_history_access"));

						i++;
					}

					XSSFSheet customersheet = workbook.createSheet("CUSTOMER");
					row = customersheet.createRow(1);

					stmt = con.prepareStatement(
							"select customer_id,customer_code, name,mobile_num,hawker_code, line_Num, house_Seq, old_house_num, new_house_num, ADDRESS_LINE1, ADDRESS_LINE2, locality, city, state,profile1,profile2,profile3,initials, employment, comments, building_street, total_due from customer order by hawker_code,line_num,house_seq");

					cell = row.createCell(1);
					cell.setCellValue("customer_id");
					cell = row.createCell(2);
					cell.setCellValue("customer_code");
					cell = row.createCell(3);
					cell.setCellValue("name");
					cell = row.createCell(4);
					cell.setCellValue("mobile_num");
					cell = row.createCell(5);
					cell.setCellValue("hawker_code");
					cell = row.createCell(6);
					cell.setCellValue("line_Num");
					cell = row.createCell(7);
					cell.setCellValue("house_Seq");
					cell = row.createCell(8);
					cell.setCellValue("old_house_num");
					cell = row.createCell(9);
					cell.setCellValue("new_house_num");
					cell = row.createCell(10);
					cell.setCellValue("ADDRESS_LINE1");
					cell = row.createCell(11);
					cell.setCellValue("ADDRESS_LINE2");
					cell = row.createCell(12);
					cell.setCellValue("locality");
					cell = row.createCell(13);
					cell.setCellValue("city");
					cell = row.createCell(14);
					cell.setCellValue("state");
					cell = row.createCell(15);
					cell.setCellValue("profile1");
					cell = row.createCell(16);
					cell.setCellValue("profile2");
					cell = row.createCell(17);
					cell.setCellValue("profile3");
					cell = row.createCell(18);
					cell.setCellValue("initials");
					cell = row.createCell(19);
					cell.setCellValue("employment");
					cell = row.createCell(20);
					cell.setCellValue("comments");
					cell = row.createCell(21);
					cell.setCellValue("building_street");
					cell = row.createCell(22);
					cell.setCellValue("total_due");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = customersheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("customer_id"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getLong("customer_code"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("name"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("mobile_num"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("hawker_code"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getInt("line_Num"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getInt("house_Seq"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getString("old_house_num"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("new_house_num"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("ADDRESS_LINE1"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("ADDRESS_LINE2"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("locality"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("city"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("state"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("profile1"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("profile2"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("profile3"));
						cell = row.createCell(18);
						cell.setCellValue(rs.getString("initials"));
						cell = row.createCell(19);
						cell.setCellValue(rs.getString("employment"));
						cell = row.createCell(20);
						cell.setCellValue(rs.getString("comments"));
						cell = row.createCell(21);
						cell.setCellValue(rs.getString("building_street"));
						cell = row.createCell(22);
						cell.setCellValue(rs.getDouble("total_due"));
						i++;
					}

					XSSFSheet linesheet = workbook.createSheet("LINE_INFO");
					row = linesheet.createRow(1);

					stmt = con.prepareStatement(
							"select li.line_id, li.line_num, li.hawker_id,li.LINE_NUM || ' ' || ld.NAME as line_num_dist from line_info li, line_distributor ld where li.HAWKER_ID=ld.HAWKER_ID(+) and li.line_num=ld.line_num(+) order by li.line_num");
					cell = row.createCell(1);
					cell.setCellValue("line_id");
					cell = row.createCell(2);
					cell.setCellValue("line_num");
					cell = row.createCell(3);
					cell.setCellValue("hawker_id");
					cell = row.createCell(4);
					cell.setCellValue("line_num_dist");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = linesheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("line_id"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getInt("line_num"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getLong("hawker_id"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("line_num_dist"));
						i++;
					}

					XSSFSheet linedistsheet = workbook.createSheet("LINE_DISTRIBUTOR");
					row = linedistsheet.createRow(1);

					stmt = con.prepareStatement(
							"select ld.line_dist_id, ld.name, ld.mobile_num, ld.hawker_id, ld.line_num,ld.old_house_num, ld.new_house_num, ld.address_line1, ld.address_line2, ld.locality, ld.city, ld.state,ld.profile1,ld.profile2,ld.profile3,ld.initials, ld.employment, ld.comments, ld.building_street, hwk.HAWKER_CODE from line_distributor ld, hawker_info hwk  where ld.hawker_id=hwk.hawker_id order by hwk.hawker_code, ld.line_num, ld.name");

					cell = row.createCell(1);
					cell.setCellValue("line_dist_id");
					cell = row.createCell(2);
					cell.setCellValue("name");
					cell = row.createCell(3);
					cell.setCellValue("mobile_num");
					cell = row.createCell(4);
					cell.setCellValue("hawker_id");
					cell = row.createCell(5);
					cell.setCellValue("line_num");
					cell = row.createCell(6);
					cell.setCellValue("old_house_num");
					cell = row.createCell(7);
					cell.setCellValue("new_house_num");
					cell = row.createCell(8);
					cell.setCellValue("address_line1");
					cell = row.createCell(9);
					cell.setCellValue("address_line2");
					cell = row.createCell(10);
					cell.setCellValue("locality");
					cell = row.createCell(11);
					cell.setCellValue("city");
					cell = row.createCell(12);
					cell.setCellValue("state");
					cell = row.createCell(13);
					cell.setCellValue("profile1");
					cell = row.createCell(14);
					cell.setCellValue("profile2");
					cell = row.createCell(15);
					cell.setCellValue("profile3");
					cell = row.createCell(16);
					cell.setCellValue("initials");
					cell = row.createCell(17);
					cell.setCellValue("employment");
					cell = row.createCell(18);
					cell.setCellValue("comments");
					cell = row.createCell(19);
					cell.setCellValue("building_street");
					cell = row.createCell(20);
					cell.setCellValue("HAWKER_CODE");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = linedistsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("line_dist_id"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("name"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("mobile_num"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getLong("hawker_id"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getInt("line_num"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getString("old_house_num"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getString("new_house_num"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getString("address_line1"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("address_line2"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("locality"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("city"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("state"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("profile1"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("profile2"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("profile3"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("initials"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("employment"));
						cell = row.createCell(18);
						cell.setCellValue(rs.getString("comments"));
						cell = row.createCell(19);
						cell.setCellValue(rs.getString("building_street"));
						cell = row.createCell(20);
						cell.setCellValue(rs.getString("HAWKER_CODE"));

						i++;
					}

					XSSFSheet productsheet = workbook.createSheet("PRODUCTS");
					row = productsheet.createRow(1);

					stmt = con.prepareStatement(
							"select PRODUCT_ID, NAME, TYPE, SUPPORTED_FREQ, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, PRICE, CODE, DOW, FIRST_DELIVERY_DATE, ISSUE_DATE, bill_Category from products order by name");
					cell = row.createCell(1);
					cell.setCellValue("PRODUCT_ID");
					cell = row.createCell(2);
					cell.setCellValue("NAME");
					cell = row.createCell(3);
					cell.setCellValue("TYPE");
					cell = row.createCell(4);
					cell.setCellValue("SUPPORTED_FREQ");
					cell = row.createCell(5);
					cell.setCellValue("MONDAY");
					cell = row.createCell(6);
					cell.setCellValue("TUESDAY");
					cell = row.createCell(7);
					cell.setCellValue("WEDNESDAY");
					cell = row.createCell(8);
					cell.setCellValue("THURSDAY");
					cell = row.createCell(9);
					cell.setCellValue("FRIDAY");
					cell = row.createCell(10);
					cell.setCellValue("SATURDAY");
					cell = row.createCell(11);
					cell.setCellValue("SUNDAY");
					cell = row.createCell(12);
					cell.setCellValue("PRICE");
					cell = row.createCell(13);
					cell.setCellValue("CODE");
					cell = row.createCell(14);
					cell.setCellValue("DOW");
					cell = row.createCell(15);
					cell.setCellValue("FIRST_DELIVERY_DATE");
					cell = row.createCell(16);
					cell.setCellValue("ISSUE_DATE");
					cell = row.createCell(17);
					cell.setCellValue("bill_Category");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = productsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("PRODUCT_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("NAME"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("TYPE"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("SUPPORTED_FREQ"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getDouble("MONDAY"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getDouble("TUESDAY"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getDouble("WEDNESDAY"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getDouble("THURSDAY"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getDouble("FRIDAY"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getDouble("SATURDAY"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getDouble("SUNDAY"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getDouble("PRICE"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("CODE"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("DOW"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getDate("FIRST_DELIVERY_DATE"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getDate("ISSUE_DATE"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("bill_Category"));

						i++;
					}

					XSSFSheet prodspclheet = workbook.createSheet("PROD_SPCL_PRICE");
					row = prodspclheet.createRow(1);

					stmt = con.prepareStatement(
							"select SPCL_PRICE_ID, PRODUCT_ID, FULL_DATE, PRICE from prod_spcl_price order by full_date desc");
					cell = row.createCell(1);
					cell.setCellValue("SPCL_PRICE_ID");
					cell = row.createCell(2);
					cell.setCellValue("PRODUCT_ID");
					cell = row.createCell(3);
					cell.setCellValue("FULL_DATE");
					cell = row.createCell(4);
					cell.setCellValue("PRICE");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = prodspclheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("SPCL_PRICE_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("PRODUCT_ID"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("FULL_DATE"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("PRICE"));

						i++;
					}

					XSSFSheet subsheet = workbook.createSheet("SUBSCRIPTION");
					row = subsheet.createRow(1);

					stmt = con.prepareStatement(
							"select sub.SUBSCRIPTION_ID, sub.CUSTOMER_ID, sub.PRODUCT_ID, prod.name, prod.type, sub.PAYMENT_TYPE, sub.SUBSCRIPTION_COST, sub.SERVICE_CHARGE, sub.FREQUENCY, sub.TYPE sub_type, sub.DOW, sub.STATUS, sub.START_DATE, sub.PAUSED_DATE, prod.CODE, sub.STOP_DATE, sub.DURATION, sub.OFFER_MONTHS, sub.SUB_NUMBER, sub.resume_date from subscription sub, products prod where sub.PRODUCT_ID=prod.PRODUCT_ID order by prod.name");

					cell = row.createCell(1);
					cell.setCellValue("SUBSCRIPTION_ID");
					cell = row.createCell(2);
					cell.setCellValue("CUSTOMER_ID");
					cell = row.createCell(3);
					cell.setCellValue("PRODUCT_ID");
					cell = row.createCell(4);
					cell.setCellValue("name");
					cell = row.createCell(5);
					cell.setCellValue("product type");
					cell = row.createCell(6);
					cell.setCellValue("PAYMENT_TYPE");
					cell = row.createCell(7);
					cell.setCellValue("SUBSCRIPTION_COST");
					cell = row.createCell(8);
					cell.setCellValue("SERVICE_CHARGE");
					cell = row.createCell(9);
					cell.setCellValue("FREQUENCY");
					cell = row.createCell(10);
					cell.setCellValue("SUBSCRIPTION TYPE");
					cell = row.createCell(11);
					cell.setCellValue("DOW");
					cell = row.createCell(12);
					cell.setCellValue("STATUS");
					cell = row.createCell(13);
					cell.setCellValue("START_DATE");
					cell = row.createCell(14);
					cell.setCellValue("STOP_DATE");
					cell = row.createCell(15);
					cell.setCellValue("CODE");
					cell = row.createCell(16);
					cell.setCellValue("END_DATE");
					cell = row.createCell(17);
					cell.setCellValue("DURATION");
					cell = row.createCell(18);
					cell.setCellValue("OFFER_MONTHS");
					cell = row.createCell(19);
					cell.setCellValue("SUB_NUMBER");
					cell = row.createCell(20);
					cell.setCellValue("resume_date");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = subsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("SUBSCRIPTION_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getLong("CUSTOMER_ID"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getLong("PRODUCT_ID"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("name"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("type"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getString("PAYMENT_TYPE"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getDouble("SUBSCRIPTION_COST"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getDouble("SERVICE_CHARGE"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("FREQUENCY"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("sub_TYPE"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("DOW"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("STATUS"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getDate("START_DATE"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("PAUSED_DATE"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("CODE"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("STOP_DATE"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getString("DURATION"));
						cell = row.createCell(18);
						cell.setCellValue(rs.getString("OFFER_MONTHS"));
						cell = row.createCell(19);
						cell.setCellValue(rs.getString("SUB_NUMBER"));
						cell = row.createCell(20);
						cell.setCellValue(rs.getString("resume_date"));

						i++;
					}

					XSSFSheet stopsheet = workbook.createSheet("STOP_HISTORY");
					row = stopsheet.createRow(1);

					stmt = con.prepareStatement(
							"SELECT STP.STOP_HISTORY_ID, CUST.NAME, CUST.CUSTOMER_CODE, CUST.MOBILE_NUM, CUST.HAWKER_CODE, CUST.LINE_NUM, SUB.SUBSCRIPTION_ID, CUST.HOUSE_SEQ, PROD.NAME, PROD.CODE, PROD.BILL_CATEGORY, STP.STOP_DATE, STP.RESUME_DATE, SUB.TYPE, SUB.FREQUENCY, SUB.DOW, STP.AMOUNT FROM STOP_HISTORY STP, CUSTOMER CUST, PRODUCTS PROD , SUBSCRIPTION SUB WHERE STP.SUB_ID =SUB.SUBSCRIPTION_ID AND SUB.CUSTOMER_ID =CUST.CUSTOMER_ID AND SUB.PRODUCT_ID =PROD.PRODUCT_ID ORDER BY SUB.PAUSED_DATE DESC");
					cell = row.createCell(1);
					cell.setCellValue("STOP_HISTORY_ID");
					cell = row.createCell(2);
					cell.setCellValue("NAME");
					cell = row.createCell(3);
					cell.setCellValue("CUSTOMER_CODE");
					cell = row.createCell(4);
					cell.setCellValue("MOBILE_NUM");
					cell = row.createCell(5);
					cell.setCellValue("HAWKER_CODE");
					cell = row.createCell(6);
					cell.setCellValue("LINE_NUM");
					cell = row.createCell(7);
					cell.setCellValue("SUB.SUBSCRIPTION_ID");
					cell = row.createCell(8);
					cell.setCellValue("HOUSE_SEQ");
					cell = row.createCell(9);
					cell.setCellValue("NAME");
					cell = row.createCell(10);
					cell.setCellValue("CODE");
					cell = row.createCell(11);
					cell.setCellValue("BILL_CATEGORY");
					cell = row.createCell(12);
					cell.setCellValue("STOP_DATE");
					cell = row.createCell(13);
					cell.setCellValue("RESUME_DATE");
					cell = row.createCell(14);
					cell.setCellValue("SUB.TYPE");
					cell = row.createCell(15);
					cell.setCellValue("SUB.FREQUENCY");
					cell = row.createCell(16);
					cell.setCellValue("SUB.DOW");
					cell = row.createCell(17);
					cell.setCellValue("AMOUNT");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = stopsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getLong("STOP_HISTORY_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("NAME"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getLong("CUSTOMER_CODE"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("MOBILE_NUM"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("HAWKER_CODE"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getInt("LINE_NUM"));
						cell = row.createCell(7);
						cell.setCellValue(rs.getLong("SUBSCRIPTION_ID"));
						cell = row.createCell(8);
						cell.setCellValue(rs.getInt("HOUSE_SEQ"));
						cell = row.createCell(9);
						cell.setCellValue(rs.getString("NAME"));
						cell = row.createCell(10);
						cell.setCellValue(rs.getString("CODE"));
						cell = row.createCell(11);
						cell.setCellValue(rs.getString("BILL_CATEGORY"));
						cell = row.createCell(12);
						cell.setCellValue(rs.getString("STOP_DATE"));
						cell = row.createCell(13);
						cell.setCellValue(rs.getString("RESUME_DATE"));
						cell = row.createCell(14);
						cell.setCellValue(rs.getString("TYPE"));
						cell = row.createCell(15);
						cell.setCellValue(rs.getString("FREQUENCY"));
						cell = row.createCell(16);
						cell.setCellValue(rs.getString("DOW"));
						cell = row.createCell(17);
						cell.setCellValue(rs.getDouble("AMOUNT"));

						i++;
					}

					XSSFSheet pointnamesheet = workbook.createSheet("POINT_NAME");
					row = pointnamesheet.createRow(1);

					stmt = con.prepareStatement(
							"select point_id,name,city, bill_category, fee from point_name order by name");
					cell = row.createCell(1);
					cell.setCellValue("point_id");
					cell = row.createCell(2);
					cell.setCellValue("name");
					cell = row.createCell(3);
					cell.setCellValue("city");
					cell = row.createCell(4);
					cell.setCellValue("bill_category");
					cell = row.createCell(5);
					cell.setCellValue("fee");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = pointnamesheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("point_id"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("name"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("city"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("bill_category"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("fee"));

						i++;
					}

					XSSFSheet lovsheet = workbook.createSheet("LOV_LOOKUP");
					row = lovsheet.createRow(1);

					stmt = con.prepareStatement(
							"select value, code, seq, lov_lookup_id from lov_lookup order by code,seq");
					cell = row.createCell(1);
					cell.setCellValue("value");
					cell = row.createCell(2);
					cell.setCellValue("code");
					cell = row.createCell(3);
					cell.setCellValue("seq");
					cell = row.createCell(4);
					cell.setCellValue("lov_lookup_id");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = lovsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("value"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("code"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("seq"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("lov_lookup_id"));

						i++;
					}

					XSSFSheet billingsheet = workbook.createSheet("BILLING");
					row = billingsheet.createRow(1);

					stmt = con.prepareStatement(
							"select BILL_INVOICE_NUM, CUSTOMER_ID , INVOICE_DATE, PDF_URL, DUE from BILLING order by invoice_date desc");

					cell = row.createCell(1);
					cell.setCellValue("BILL_INVOICE_NUM");
					cell = row.createCell(2);
					cell.setCellValue("CUSTOMER_ID");
					cell = row.createCell(3);
					cell.setCellValue("INVOICE_DATE");
					cell = row.createCell(4);
					cell.setCellValue("PDF_URL");
					cell = row.createCell(5);
					cell.setCellValue("DUE");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = billingsheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("BILL_INVOICE_NUM"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("CUSTOMER_ID"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("INVOICE_DATE"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("PDF_URL"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("DUE"));

						i++;
					}

					XSSFSheet billinglinesheet = workbook.createSheet("BILLING_LINES");
					row = billinglinesheet.createRow(1);

					stmt = con.prepareStatement(
							"SELECT BILL_LINE_ID, BILL_INVOICE_NUM, LINE_NUM, PRODUCT, AMOUNT, TEA_EXPENSES FROM BILLING_LINES order by BILL_INVOICE_NUM");
					cell = row.createCell(1);
					cell.setCellValue("BILL_LINE_ID");
					cell = row.createCell(2);
					cell.setCellValue("BILL_INVOICE_NUM");
					cell = row.createCell(3);
					cell.setCellValue("LINE_NUM");
					cell = row.createCell(4);
					cell.setCellValue("PRODUCT");
					cell = row.createCell(5);
					cell.setCellValue("AMOUNT");
					cell = row.createCell(6);
					cell.setCellValue("TEA_EXPENSES");

					rs = stmt.executeQuery();
					i = 2;
					while (rs.next()) {
						row = billinglinesheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue(rs.getString("BILL_LINE_ID"));
						cell = row.createCell(2);
						cell.setCellValue(rs.getString("BILL_INVOICE_NUM"));
						cell = row.createCell(3);
						cell.setCellValue(rs.getString("LINE_NUM"));
						cell = row.createCell(4);
						cell.setCellValue(rs.getString("PRODUCT"));
						cell = row.createCell(5);
						cell.setCellValue(rs.getString("AMOUNT"));
						cell = row.createCell(6);
						cell.setCellValue(rs.getString("TEA_EXPENSES"));

						i++;
					}

					FileOutputStream out = new FileOutputStream(new File("C:\\pds\\" + filename));
					workbook.write(out);
					out.close();
					rs.close();
					stmt.close();

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create().title("Data exported")
									.text("Data successfully exported to location : " + "C:\\pds\\" + filename)
									.hideAfter(Duration.seconds(10)).showInformation();

						}
					});

				} catch (SQLException e) {
					Main._logger.debug("Error :",e);
					e.printStackTrace();
				} catch (Exception e) {

					Main._logger.debug("Error :",e);
					e.printStackTrace();
				}

				return null;
			}

		};
		new Thread(task).start();
	}

}
