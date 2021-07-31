package org.tonylin.stock.dao;

import java.io.File;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.tonylin.stock.StockInfoCategory.*;
import org.tonylin.stock.StockInformation;


public class SummaryStockExcelTest {

	private String TESTING_FILE = "test.xls";
	
	@After
	public void tearDown(){
		File file = new File(TESTING_FILE);
		file.delete();
	}
	
	@Test
	public void testReadWrite(){
		SummaryStockExcel ssExcel = new SummaryStockExcel();
		
		StockInformation stock1 = new StockInformation("1234");
		stock1.addStockInformation(INVOICE_TM, "123,456");
		stock1.addStockInformation(BUSINESS_INCOMING_TM, "456,123");
		
		StockInformation stock2 = new StockInformation("1234");
		stock2.addStockInformation(INVOICE_TM, "123,4567");
		stock2.addStockInformation(BUSINESS_INCOMING_TM, "456,1237");
	
		ssExcel.addStcokInfo(stock1, "2012", "01");
		ssExcel.addStcokInfo(stock2, "2012", "02");
		
		ssExcel.save(TESTING_FILE);
		
		try {
			ssExcel = new SummaryStockExcel();
			ssExcel.load(TESTING_FILE);
		} catch (Exception e) {
			fail();
		}
		
		StockInformation stock3 = ssExcel.getStockInformation("1234", "2012", "01");
		StockInformation stock4 = ssExcel.getStockInformation("1234", "2012", "02");
		
		assertNotNull(stock3);
		assertNotNull(stock4);
		assertEquals("123,456", stock3.getStockInformation(INVOICE_TM));
		assertEquals("456,123", stock3.getStockInformation(BUSINESS_INCOMING_TM));
		assertEquals("123,4567", stock4.getStockInformation(INVOICE_TM));
		assertEquals("456,1237", stock4.getStockInformation(BUSINESS_INCOMING_TM));
	}
	
}
