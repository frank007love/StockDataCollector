package org.tonylin.stock.dataprovider.parser;

import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.StockInfoCategory;
import org.tonylin.stock.StockInformation;

public class MOPSHtmlParser {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MOPSHtmlParser.class);
	
	/**
	 * Extract the valid data from the aOriginalStr.
	 * For example, the original one is "&nbsp;            1,180,192".
	 * We will skip ""&nbsp;            ", and return 1,180,192.
	 * 
	 * @param aOriginalStr
	 * @return
	 */
	private static String extractValidData(String aOriginalStr){
		return aOriginalStr.replace("&nbsp;","").trim();
	}
	
	/**
	 * Parse invoice and business incoming data from tr node to aStockInformation by
	 * each stock info category.
	 * 
	 * @param aStockInformation
	 * @param aNode
	 * @param aInvoiceCate
	 * @param aBIncomingCate
	 */
	private static void parseIBITableValues(StockInformation aStockInformation,
			Node aNode, StockInfoCategory aInvoiceCate, StockInfoCategory aBIncomingCate){
		// parse each field
		NodeList nodeList = aNode.getChildren();
		nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("td"));
		String invoiceStr = nodeList.elementAt(0).getFirstChild().getText();
		String incommingStr = nodeList.elementAt(1).getFirstChild().getText();
		
		// add info to aStockInformation
		aStockInformation.addStockInformation(aInvoiceCate, extractValidData(invoiceStr));
		aStockInformation.addStockInformation(aBIncomingCate, extractValidData(incommingStr));
	}
	
	/**
	 * Parse combined business incoming data from tr node to aStockInformation by
	 * each stock info category.
	 * 
	 * @param aStockInformation
	 * @param aNode
	 * @param aCBICategory
	 */
	private static void parseCBITableValues(StockInformation aStockInformation,
			Node aNode, StockInfoCategory aCBICategory){
		// parse each field
		NodeList nodeList = aNode.getChildren();
		nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("td"));
		String cbieStr = nodeList.elementAt(0).getFirstChild().getText();
		// add info to aStockInformation
		aStockInformation.addStockInformation(aCBICategory, extractValidData(cbieStr));
	}
	
	/**
	 * Translate the invoice and business incoming table to StockInformation data. 
	 * 
	 * @param aStockInformation
	 * @param aTable
	 * @throws ParserException 
	 */
	private static void translateIBITable(StockInformation aStockInformation, Node aTable) throws ParserException{
		NodeList trList = aTable.getChildren();
		trList = trList.extractAllNodesThatMatch(new TagNameFilter("tr"));
		
		List<StockInfoCategory> inioiceCategoryList = StockInformation.getInvoiceCategoryList();
		List<StockInfoCategory> biCategoryList = StockInformation.getBICategoryList();
		for( int i = 1; i <= inioiceCategoryList.size() ; i++ ){
			parseIBITableValues(aStockInformation, trList.elementAt(i), 
					inioiceCategoryList.get(i-1), biCategoryList.get(i-1));
		}
	}
	
	/**
	 * Translate the combined business incoming table to StockInformation data. 
	 * 
	 * @param aStockInformation
	 * @param aTable
	 */
	private static void translateCBITable(StockInformation aStockInformation, Node aTable){
		NodeList trList = aTable.getChildren();
		trList = trList.extractAllNodesThatMatch(new TagNameFilter("tr"));
		
		LOGGER.debug("CBITable size = {}", trList.size());
		
		List<StockInfoCategory> cbiCategoryList = StockInformation.getCombinedBICategoryList();
		for( int i = 1 ; i <= cbiCategoryList.size() && i < trList.size() ; i++){
			LOGGER.debug("CBITable tr node = {}", trList.elementAt(i).toHtml());
			parseCBITableValues(aStockInformation, trList.elementAt(i), 
					cbiCategoryList.get(i-1));
		}
	}
	
	/**
	 * Get the stock information from MOPS.
	 * 
	 * @param aResource
	 * @return
	 * @throws StockInformationException 
	 * @throws ParserException
	 */
	public static StockInformation getStockInformation(String aResource, String aID) throws StockInformationException {
		StockInformation stockInfo = null;
		try {
			Parser parser = new Parser(aResource);
			
			NodeList nodeList = parser.parse(new TagWithAttributesFilter("div", "id", "table01"));
			nodeList = nodeList.extractAllNodesThatMatch(new TagWithAttributesFilter("table", "class", "hasBorder"), true);

			LOGGER.debug("Extract table size: {}", nodeList.size());
			
			if( nodeList.size() == 0 ){
				throw new StockInformationException("Doesn't contain stock information.");
			}
			
			stockInfo = new StockInformation(aID);
			
			Node table1 = nodeList.elementAt(0);
			LOGGER.debug("Table1:\n {}", table1.toHtml());
			translateIBITable(stockInfo, table1);
			
			Node table2 = nodeList.elementAt(1);
			LOGGER.debug("Table2:\n {}", table2.toHtml());
			translateCBITable(stockInfo, table2);
		} catch( ParserException e ){
			throw new StockInformationException("Parse stock information failed.", e);
		}
		
		return stockInfo;
	}
}
