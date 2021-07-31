package org.tonylin.stock.dao;

import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_TM;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_TM;
import static org.tonylin.stock.StockInfoCategory.INVOICE_TM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.StockInformation;
import org.tonylin.util.io.Cleaner;

public class SummaryStockExcel {
	static private Logger logger = LoggerFactory.getLogger(SummaryStockExcel.class);
	static private String SHEET_NAME = "StockSummary";
	
	private Map<String, StockInformation> mStockInfoMap;
	
	public SummaryStockExcel() {
		mStockInfoMap = new HashMap<String, StockInformation>();
	}
	
	/**
	 * Get the key for stock infomation map.
	 * 
	 * @param aId
	 * @param aYear
	 * @param aMonth
	 */
	private String getStockInfokey(String aId, String aYear, String aMonth){
		StringBuffer sb = new StringBuffer(aId);
		sb.append("_").append(aYear).append("_").append(aMonth);
		return sb.toString();
	}
	
	/**
	 * Get stock information by the stock's id, year, and month.
	 * 
	 * @param aId
	 * @param aYear
	 * @param aMonth
	 * @return
	 */
	public StockInformation getStockInformation(String aId, String aYear, String aMonth){
		String key = getStockInfokey(aId, aYear, aMonth);
		return mStockInfoMap.get(key);
	}
	
	/**
	 * Add the stock info with year and month.
	 * 
	 * @param aStockInfo
	 * @param aYear
	 * @param aMonth
	 */
	public void addStcokInfo(StockInformation aStockInfo, String aYear, String aMonth){ 
		String key = getStockInfokey(aStockInfo.getId(), aYear, aMonth);
		mStockInfoMap.put(key, aStockInfo);
	}
	
	/**
	 * Save the stock summary info to file.
	 * 
	 * @param aFilePath
	 */
	public void save(String aFilePath){
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet(SHEET_NAME);
		Row headRow = sheet.createRow(0);
		String[] headerStr = { "股號", "年", "月", "開立發票總金額", "營業收入淨額", "合併營業收入淨額"};
		for( int i = 0 ; i < headerStr.length ; i++ ){
			headRow.createCell(i).setCellValue(headerStr[i]);
		}
		
		List<String> keyList = new ArrayList<String>(mStockInfoMap.keySet());

		Collections.sort(keyList);
		for( int i = 0 ; i < keyList.size() ; i++ ){
			String key = keyList.get(i);
			String[] splitSet = key.split("_");
			StockInformation stock = mStockInfoMap.get(key);
			Row row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(splitSet[0]);
			row.createCell(1).setCellValue(splitSet[1]);
			row.createCell(2).setCellValue(splitSet[2]);
			row.createCell(3).setCellValue(stock.getStockInformation(INVOICE_TM));
			row.createCell(4).setCellValue(stock.getStockInformation(BUSINESS_INCOMING_TM));
			String cmi = stock.getStockInformation(COMBINED_BUSINESS_INCOMING_TM);
			row.createCell(5).setCellValue( cmi == null ? "無" : cmi );
		}
		
		// Save the excel model to the real excel file.
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(aFilePath);
			wb.write(fileOutputStream);
		} catch(Exception e){
			throw new RuntimeException(e);
		} finally {
			Cleaner.close(fileOutputStream);
		}
	}
	
	/**
	 * Load the stock summary excel file to the mStockInfoMap.
	 * 
	 * @param aFilePath
	 * @throws Exception
	 */
	public void load(String aFilePath) throws Exception {
		mStockInfoMap.clear();
		
		File summaryExcel = new File(aFilePath);
		if( !summaryExcel.exists() ){
			return;
		}
		
		InputStream inp = null;
		try {
			inp = new FileInputStream(aFilePath);

			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet(SHEET_NAME);
			boolean isFirst = true;
			for (Row row : sheet) {
				if( isFirst || row.getCell(0) == null ){
					isFirst = false;
					continue;
				}
				String stockID = row.getCell(0).getRichStringCellValue().getString();
				StockInformation stock = new StockInformation(stockID);
				String year = row.getCell(1).getRichStringCellValue().getString();
				String month = row.getCell(2).getRichStringCellValue().getString();
				String invoice = row.getCell(3).getRichStringCellValue().getString();
				String bi = row.getCell(4).getRichStringCellValue().getString();
				String cbi = row.getCell(5).getRichStringCellValue().getString();
				
				String key = getStockInfokey(stockID, year, month);
				stock.addStockInformation(INVOICE_TM, invoice);
				stock.addStockInformation(BUSINESS_INCOMING_TM, bi);
				if( !cbi.equals("無") )
					stock.addStockInformation(COMBINED_BUSINESS_INCOMING_TM, cbi);
				
				mStockInfoMap.put(key, stock);
			}
		} catch (Exception e) {
			throw new Exception("Load stock sumary excel failed.", e);
		} finally {
			Cleaner.close(inp);
		}
	}
	
	
}
