package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PrepareBillingXLS {
	
	 private static XSSFWorkbook workbook;
     private static XSSFSheet sheet;
     static {
    	 try {
			workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\vshrimal.ORADEV\\Downloads\\BillTemplate.xlsx"));
			 sheet = workbook.getSheet("template");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     public static void PrepareBillingXLS(){
    	 try {
			FileOutputStream out = new FileOutputStream("C:\\Users\\vshrimal.ORADEV\\Downloads\\output.xls");
			
				CellCopyPolicy ccp = new CellCopyPolicy();
				ccp.setCopyCellStyle(true);
				ccp.setCopyCellValue(true);
				ccp.setCopyMergedRegions(true);
				ccp.setCopyRowHeight(true);
				sheet.copyRows(0, 15, 16, ccp);
				workbook.write(out);
				out.close();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
}
