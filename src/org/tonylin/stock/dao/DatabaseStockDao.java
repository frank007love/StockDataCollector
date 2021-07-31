package org.tonylin.stock.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.StockInfoCategory;
import org.tonylin.stock.StockInformation;
import org.tonylin.stock.dao.po.MopsInvoiceIncoming;
import org.tonylin.stock.dao.po.MopsInvoiceIncomingPK;


public class DatabaseStockDao implements IStockDao {

	static private Logger mLogger = LoggerFactory.getLogger(DatabaseStockDao.class); 
	private static IStockDao mInstance = null;
	
	/**
	 * Create the database stock dao instance.
	 * 
	 * @return
	 */
	public static IStockDao createStockDao(){
		if( mInstance == null ) { 
			mInstance = new DatabaseStockDao();
		}
		return mInstance;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeSession();
	}
	
	public Session getSession() throws HibernateException{
		return HibernateUtil.currentSession();
	}
	
	public void closeSession(){
		HibernateUtil.closeSession();
	}
	
	/**
	 * Save stock data to database.
	 */
	@Override
	public void saveInvoiceAndBusinessData(StockInformation stockInformation, String aYear, String aMonth) {
		if( doesDataExist(stockInformation.getId(), aYear, aMonth) ){
			mLogger.warn("Stock {}, {}, {} exists.", new String[]{
					stockInformation.getId(), aYear, aMonth
			});
			return;
		}
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSession();
			transaction = session.beginTransaction();
			
			// init persistent object
			MopsInvoiceIncomingPK pk = new MopsInvoiceIncomingPK(stockInformation.getId(), aYear, aMonth);
			MopsInvoiceIncoming mii = new MopsInvoiceIncoming(pk);
			mii.setInvoice(stockInformation.getStockInformation(StockInfoCategory.INVOICE_TM));
			mii.setCbi(stockInformation.getStockInformation(StockInfoCategory.COMBINED_BUSINESS_INCOMING_TM));
			mii.setBi(stockInformation.getStockInformation(StockInfoCategory.BUSINESS_INCOMING_TM));
			
			// save data to database
			session.save(mii);
			session.flush();
			
			transaction.commit();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction(transaction);
			throw new RuntimeException(e);
		} finally {
			
			closeSession();
		}
	}

	/**
	 * Check stock data whether exists in database.
	 */
	@Override
	public boolean doesDataExist(String aStockId, String aYear, String aMonth) {
		
		Session session = null;
		Object stockInfo = null;
		try {
			session = getSession();
			stockInfo = session.get(MopsInvoiceIncoming.class, new MopsInvoiceIncomingPK(aStockId, aYear, aMonth));
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} finally {
			closeSession();
		}
		return stockInfo != null;
	}

	/**
	 * Remove stock data from database.
	 */
	@Override
	public void deleteInvoiceAndBusinessData(String aId, String aYear,
			String aMonth) {
		if( !doesDataExist(aId, aYear, aMonth) ){
			return;
		}
		Session session = null;
		Transaction transaction = null;
		Object stockInfo = null;
		try {
			session = getSession();
			transaction = session.beginTransaction();
			
			// delete stock info from database.
			stockInfo = session.get(MopsInvoiceIncoming.class, new MopsInvoiceIncomingPK(aId, aYear, aMonth));
			session.delete(stockInfo);
			session.flush();
			
			transaction.commit();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction(transaction);
			throw new RuntimeException(e);
		} finally {
			closeSession();
		}
	}

}
