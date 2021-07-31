package org.tonylin.stock.dataprovider.parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.htmlparser.util.ParserException;
import org.junit.Test;
import org.tonylin.stock.StockInformation;
import org.tonylin.util.io.Cleaner;

public class MOPSHtmlParserTest {

	static private String TEST_FOLDER = "testdata";
	
	/**
	 * Get testing stock html from MOPS site.
	 * 
	 * @param aStockID
	 * @return
	 */
	private static String getTestingFile(String aStockID){
		StringBuffer sb = new StringBuffer(TEST_FOLDER);
		sb.append(File.separator).append(aStockID).append(".html");
		return sb.toString();
	}
	
	/**
	 * Do stock information assertion.
	 * 
	 * @param aStockInfo
	 * @param aInvoiceAssertionList
	 * @param aBIAssertionList
	 * @param aCBIAssertionList
	 */
	private void doAssertion(StockInformation aStockInfo, String[] aInvoiceAssertionList, String[] aBIAssertionList, String[] aCBIAssertionList){
		for( int i = 0 ; i < 8 ; i++ ){
			assertEquals( aInvoiceAssertionList == null ? null : aInvoiceAssertionList[i], 
					aStockInfo.getStockInformation(StockInformation.getInvoiceCategoryList().get(i)));
			assertEquals( aBIAssertionList == null ? null : aBIAssertionList[i],
					aStockInfo.getStockInformation(StockInformation.getBICategoryList().get(i)));
			assertEquals( aCBIAssertionList == null ? null : aCBIAssertionList[i],
					aStockInfo.getStockInformation(StockInformation.getCombinedBICategoryList().get(i)));
		}
	}
	
	@Test
	public void testParse1201_2012_01() throws ParserException, StockInformationException{
		String stockId = "1201_2012_01";
		String filePath = getTestingFile(stockId);
		StockInformation stockInfo =  MOPSHtmlParser.getStockInformation(filePath, stockId);
		
		assertEquals( stockId, stockInfo.getId());


		String[] invoiceAssertionList = { "1,180,192", "1,168,823", "11,369", "0.97",
				 "1,180,192", "1,168,823", "11,369", "0.97"};
		String[] biAssertionList = { "1,115,106", "1,110,017", "5,089", "0.46",
				 "1,115,106", "1,110,017", "5,089", "0.46"};
		
		doAssertion(stockInfo, invoiceAssertionList, biAssertionList, null);
	}
	
	@Test
	public void testParse3481_2011_01_file() throws ParserException, StockInformationException{
		String stockId = "3481_2011_01";
		String filePath = getTestingFile(stockId);
		
		testSotckInfo_2011(filePath, stockId);
	}
	
	@Test
	public void testParse3481_2011_01_str() throws ParserException, StockInformationException{
		String stockId = "3481_2011_01";
		String filePath = getTestingFile(stockId);
		File file = new File(filePath);
		FileReader reader = null;
		StringBuffer sb = new StringBuffer("");
		try {
			reader = new FileReader(file);
			int c;
			while( (c = reader.read()) != -1){
				sb.append((char)c);
			}
		} catch (IOException e) {
			fail();
		} finally {
			Cleaner.close(reader);
		}
		testSotckInfo_2011(sb.toString(), stockId);
	}
	
	/**
	 * Test to parse stock information.
	 * 
	 * @param aResource
	 * @param aStockID
	 * @throws StockInformationException
	 */
	private void testSotckInfo_2011(String aResource, String aStockID)
			throws StockInformationException {
		StockInformation stockInfo = MOPSHtmlParser.getStockInformation(
				aResource, aStockID);

		assertEquals(aStockID, stockInfo.getId());

		String[] invoiceAssertionList = { "3,398,947", "78,956", "3,319,991",
				"4,204.86", "3,398,947", "78,956", "3,319,991", "4,204.86" };
		String[] biAssertionList = { "39,988,546", "16,109,639", "23,878,907",
				"148.23", "39,988,546", "16,109,639", "23,878,907", "148.23" };
		String[] cbiAssertionList = { "40,362,324", "16,174,660", "24,187,664",
				"149.54", "40,362,324", "16,174,660", "24,187,664", "149.54" };

		doAssertion(stockInfo, invoiceAssertionList, biAssertionList,
				cbiAssertionList);
	}

	@Test
	public void testNoData() throws ParserException{
		String stockId = "nodata";
		String filePath = getTestingFile(stockId);
		try {
			MOPSHtmlParser.getStockInformation(filePath, stockId);
			fail();
		} catch (StockInformationException e) {
			assertEquals("Doesn't contain stock information.", e.getMessage());
		}
	}
}
