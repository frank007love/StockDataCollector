package org.tonylin.stock;

import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_AS1;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_AS2;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_ASP1;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_ASP2;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_LY;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_LYS;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_TM;
import static org.tonylin.stock.StockInfoCategory.BUSINESS_INCOMING_TYS;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_AS1;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_AS2;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_ASP1;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_ASP2;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_LY;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_LYS;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_TM;
import static org.tonylin.stock.StockInfoCategory.COMBINED_BUSINESS_INCOMING_TYS;
import static org.tonylin.stock.StockInfoCategory.INVOICE_AS1;
import static org.tonylin.stock.StockInfoCategory.INVOICE_AS2;
import static org.tonylin.stock.StockInfoCategory.INVOICE_ASP1;
import static org.tonylin.stock.StockInfoCategory.INVOICE_ASP2;
import static org.tonylin.stock.StockInfoCategory.INVOICE_LY;
import static org.tonylin.stock.StockInfoCategory.INVOICE_LYS;
import static org.tonylin.stock.StockInfoCategory.INVOICE_TM;
import static org.tonylin.stock.StockInfoCategory.INVOICE_TYS;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockInformation {

	private String mId;
	private Map<StockInfoCategory, String> mInformation;
	
	private static List<StockInfoCategory> InvoiceCategoryList = Collections
			.unmodifiableList(Arrays.asList(INVOICE_TM, INVOICE_LY,
					INVOICE_AS1, INVOICE_ASP1, INVOICE_TYS, INVOICE_LYS,
					INVOICE_AS2, INVOICE_ASP2));
	private static List<StockInfoCategory> BICategoryList = Collections
			.unmodifiableList(Arrays.asList(BUSINESS_INCOMING_TM,
					BUSINESS_INCOMING_LY, BUSINESS_INCOMING_AS1,
					BUSINESS_INCOMING_ASP1, BUSINESS_INCOMING_TYS,
					BUSINESS_INCOMING_LYS, BUSINESS_INCOMING_AS2,
					BUSINESS_INCOMING_ASP2));
	private static List<StockInfoCategory> CBICategoryList = Collections
			.unmodifiableList(Arrays.asList(COMBINED_BUSINESS_INCOMING_TM,
					COMBINED_BUSINESS_INCOMING_LY,
					COMBINED_BUSINESS_INCOMING_AS1,
					COMBINED_BUSINESS_INCOMING_ASP1,
					COMBINED_BUSINESS_INCOMING_TYS,
					COMBINED_BUSINESS_INCOMING_LYS,
					COMBINED_BUSINESS_INCOMING_AS2,
					COMBINED_BUSINESS_INCOMING_ASP2));
	
	public StockInformation(String aId){
		mId = aId;
		mInformation = new HashMap<StockInfoCategory, String>();
	}
	
	/**
	 * Get business incoming category list.
	 * 
	 * @return
	 */
	public static List<StockInfoCategory> getBICategoryList(){
		return BICategoryList;
	}
	
	/**
	 * Get combined business incoming category list.
	 * 
	 * @return
	 */
	public static List<StockInfoCategory> getCombinedBICategoryList(){
		return CBICategoryList;
	}
	
	/**
	 * Get invoice category list.
	 * 
	 * @return
	 */
	public static List<StockInfoCategory> getInvoiceCategoryList(){
		return InvoiceCategoryList;
	}
	
	/**
	 * Get the stock id.
	 * 
	 * @return
	 */
	public String getId(){ 
		return mId;
	}
	
	/**
	 * Get the stock information by category. If the stock doesn't 
	 * contain the information, it will return null.
	 * 
	 * @param aCategory
	 * @return
	 */
	public String getStockInformation(StockInfoCategory aCategory){
		return mInformation.get(aCategory);
	}
	
	/**
	 * Add stock information.
	 * 
	 * @param aCategory
	 * @param aInformation
	 */
	public void addStockInformation(StockInfoCategory aCategory, String aInformation){
		mInformation.put(aCategory, aInformation);
	}
}
