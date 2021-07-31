package org.tonylin.stock.dao;

import org.tonylin.stock.StockInformation;

public interface IStockDao {
	void saveInvoiceAndBusinessData(StockInformation stockInformation, String aYear, String aMonth);
	void deleteInvoiceAndBusinessData(String aId, String aYear, String aMonth);
	boolean doesDataExist(String aStockId, String aYear, String aMonth);
}
