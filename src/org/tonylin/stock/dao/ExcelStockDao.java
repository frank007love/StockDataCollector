package org.tonylin.stock.dao;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.StockInformation;
import org.tonylin.stock.SystemConfig;
import org.tonylin.util.io.Cleaner;

import static org.tonylin.stock.StockInfoCategory.*;

public class ExcelStockDao implements IStockDao{
	
	static private Logger logger = LoggerFactory.getLogger(ExcelStockDao.class);
	static private String STOCK_SUMMARY_FILE;

	static {
		File outputfolder = new File(SystemConfig.getOutputFolder());
		if( !outputfolder.exists() && !outputfolder.mkdir()){
			logger.error("Create output folder {} failed.", SystemConfig.getOutputFolder());
		}
		STOCK_SUMMARY_FILE = SystemConfig.getOutputFolder() + File.separator + "StockSummary.xls";
	}
	
	/**
	 * Get excel file path.
	 * 
	 * @param aStockId
	 * @param aYear
	 * @param aMonth
	 * @return
	 */
	private String getExcelPath(String aStockId, String aYear, String aMonth){
		StringBuffer sb = new StringBuffer(SystemConfig.getOutputFolder());
		sb.append(File.separator);
		sb.append(aStockId).append("_").append(aYear).append("_").append(aMonth).append(".xls");
		return sb.toString();
	}
	
	/**
	 * Check the stock excel file whether exists.
	 * 
	 * @param aStockId
	 * @param aYear
	 * @param aMonth
	 * @return
	 */
	public boolean doesDataExist(String aStockId, String aYear, String aMonth){
		File excelFile = new File(getExcelPath(aStockId, aYear, aMonth));
		return excelFile.exists();
	}
	
	/**
	 * Check the aStr. Return 無  when aStr is null; 
	 * otherwise, return the original value.  
	 * 
	 * @param aStr
	 * @return
	 */
	private String getDataStr(String aStr){
		return aStr == null ? "無" : aStr;
	}
	
	@Override
	public void saveInvoiceAndBusinessData(StockInformation stockInformation, String aYear, String aMonth) {
		// save the stock info to summary excel.
		SummaryStockExcel summaryStockExcel = new SummaryStockExcel();
		try {
			summaryStockExcel.load(STOCK_SUMMARY_FILE);
			summaryStockExcel.addStcokInfo(stockInformation, aYear, aMonth);
			summaryStockExcel.save(STOCK_SUMMARY_FILE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		saveIBADataToEachExcel(stockInformation, aYear, aMonth);
	}

	/**
	 * Save the invoice and business data to each excel by the stock id,
	 * year, and month.
	 * 
	 * @param stockInformation
	 * @param aYear
	 * @param aMonth
	 */
	private void saveIBADataToEachExcel(StockInformation stockInformation,
			String aYear, String aMonth) {
		// create excel workbook and sheet model.
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("StockInfo");
		
		String[] header = { "項目", "開立發票總金額", "營業收入淨額", 
				"本月", stockInformation.getStockInformation(INVOICE_TM), stockInformation.getStockInformation(BUSINESS_INCOMING_TM),
				"去年同期", stockInformation.getStockInformation(INVOICE_LY), stockInformation.getStockInformation(BUSINESS_INCOMING_LY),
				"增減金額", stockInformation.getStockInformation(INVOICE_AS1), stockInformation.getStockInformation(BUSINESS_INCOMING_AS1),
				"增減百分比", stockInformation.getStockInformation(INVOICE_ASP1), stockInformation.getStockInformation(BUSINESS_INCOMING_ASP1),
				"本年累計", stockInformation.getStockInformation(INVOICE_TYS), stockInformation.getStockInformation(BUSINESS_INCOMING_TYS),
				"去年累計", stockInformation.getStockInformation(INVOICE_TYS), stockInformation.getStockInformation(BUSINESS_INCOMING_LYS),
				"增減金額", stockInformation.getStockInformation(INVOICE_AS2), stockInformation.getStockInformation(BUSINESS_INCOMING_AS2),
				"增減百分比", stockInformation.getStockInformation(INVOICE_ASP2), stockInformation.getStockInformation(BUSINESS_INCOMING_ASP2)};
		
		// create invoice and business data row.
		int currentRow = 0;
		for( int i = 0 ; (i*3) < header.length ; i++ ){
			int baseindex = i*3;
			Row row = sheet.createRow(i);
			currentRow = i;
			row.createCell(0).setCellValue(getDataStr(header[baseindex]));
			row.createCell(1).setCellValue(getDataStr(header[baseindex+1]));
			row.createCell(2).setCellValue(getDataStr(header[baseindex+2]));
		}

		// If the combined business data exist, then create the data row. 
		// Otherwise, create the alert row.
		if( stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_TM) != null){
			String[] header2 = { "項目", "合併營業收入淨額", 
					"本月", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_TM),
					"去年同期", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_LY), 
					"增減金額", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_AS1), 
					"增減百分比", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_ASP1),
					"本年累計", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_TYS),
					"去年累計", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_LYS), 
					"增減金額", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_AS2),
					"增減百分比", stockInformation.getStockInformation(COMBINED_BUSINESS_INCOMING_ASP2)};
			
			for( int i = 0 ; i*2 < header2.length ; i++ ){
				Row row = sheet.createRow(currentRow+2);
				int baseindex = i*2;
				currentRow++;
				row.createCell(0).setCellValue(getDataStr(header2[baseindex]));
				row.createCell(1).setCellValue(getDataStr(header2[baseindex+1]));
			}
		} else {
			Row row = sheet.createRow(currentRow+2);
			row.createCell(0).setCellValue("未公告合併營業收入(採自願公告制)");
		}
		
		// Save the excel model to the real excel file.
		String excelFile = getExcelPath(stockInformation.getId(), aYear, aMonth);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(excelFile);
			wb.write(fileOutputStream);
		} catch(Exception e){
			logger.error("", e);
		} finally {
			Cleaner.close(fileOutputStream);
		}
	}

	@Override
	public void deleteInvoiceAndBusinessData(String aId, String aYear,
			String aMonth) {
		throw new RuntimeException("Not support!");
	}

}
