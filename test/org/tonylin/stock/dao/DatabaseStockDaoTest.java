package org.tonylin.stock.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tonylin.stock.StockInfoCategory;
import org.tonylin.stock.StockInformation;


public class DatabaseStockDaoTest {

	private StockInformation createStockInformation(String aID, String aInvoice, String aBI, String aCBI){
		StockInformation stockInfo = new StockInformation(aID);
		stockInfo.addStockInformation(StockInfoCategory.BUSINESS_INCOMING_TM, aInvoice);
		stockInfo.addStockInformation(StockInfoCategory.INVOICE_TM, aBI);
		stockInfo.addStockInformation(StockInfoCategory.COMBINED_BUSINESS_INCOMING_TM, aCBI);
		return stockInfo;
	}
	
	@Test
	public void saveInvoiceAndBusinessData(){
		
		IStockDao stockDao = DatabaseStockDao.createStockDao();
		StockInformation stockInfo = createStockInformation("1234", "123,456", "789,123", null);
		
		stockDao.saveInvoiceAndBusinessData(stockInfo, "2011", "02");
		
		assertTrue(stockDao.doesDataExist(stockInfo.getId(), "2011", "02"));
		
		stockDao.deleteInvoiceAndBusinessData(stockInfo.getId(), "2011", "02");
		
		assertFalse(stockDao.doesDataExist(stockInfo.getId(), "2011", "02"));
	}
	
	@Test
	public void doesDataExist(){
		IStockDao stockDao = DatabaseStockDao.createStockDao();
		assertFalse(stockDao.doesDataExist("1011", "2012", "01"));
	}
	
	
}
