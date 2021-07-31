package org.tonylin.stock.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.StockInformation;
import org.tonylin.stock.dataprovider.parser.MOPSHtmlParser;
import org.tonylin.stock.util.HttpClientUtil;

public class MOPSDataProvider {

	static private Logger logger = LoggerFactory.getLogger(MOPSDataProvider.class);
	
	final static private String MOPS_SITE = "http://mops.twse.com.tw";
	final static private String INVOICE_BUSINESS_DATA_URL;
	
	static {
		// 查詢開立發票總金額	與營業收入淨額的網頁
		INVOICE_BUSINESS_DATA_URL = new StringBuffer(MOPS_SITE).append("/mops/web/t05st10").toString(); 
	}
	
	/**	
	 * Query the aStockId's  invoice and business data according to the aYear and aMonth.
	 * 
	 * @param aStockId
	 * @param aYear
	 * @param aMonth
	 */
	public StockInformation queryInvoiceAndBusinessData(String aStockId, String aYear, String aMonth){
		
		logger.debug("Query stock {},{},{} data.", new String[]{
				aStockId, aYear, aMonth
		});
		
		HttpClient client = new DefaultHttpClient();
		 
		// 發送給公開資訊觀測站的參數
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("encodeURIComponent", "1"));
		nvps.add(new BasicNameValuePair("run", "Y"));
		nvps.add(new BasicNameValuePair("step", "0"));
		nvps.add(new BasicNameValuePair("yearmonth", aYear+aMonth));
		nvps.add(new BasicNameValuePair("colorchg", ""));
		nvps.add(new BasicNameValuePair("TYPEK", "sii"));
		nvps.add(new BasicNameValuePair("co_id", aStockId));
		nvps.add(new BasicNameValuePair("off", "1"));
		nvps.add(new BasicNameValuePair("year", aYear));
		nvps.add(new BasicNameValuePair("month", aMonth));
		nvps.add(new BasicNameValuePair("firstin", "true"));
		
		HttpEntity entity = null;
		StockInformation stockInformation = null;
		try {
			// 對開立發票總金額與營業收入淨額的網頁發出Post請求
			HttpPost httpPost = new HttpPost(INVOICE_BUSINESS_DATA_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpPost.addHeader("Referer", INVOICE_BUSINESS_DATA_URL);
			HttpResponse response = client.execute(httpPost);
			
        	entity = response.getEntity();
	
        	String content = HttpClientUtil.getReponseContent(response);
        	stockInformation = MOPSHtmlParser.getStockInformation(content, aStockId);
           
		} catch(Exception e){
			e.printStackTrace();
			String errMsgFormat = "Get %s invoice and business data failed: %s";
			throw new RuntimeException(String.format(errMsgFormat, aStockId, e.getMessage()));
		} finally {
			// 釋放連線資源
			HttpClientUtil.consumeEntity(entity);
			HttpClientUtil.shutdownHttpClient(client);
		}
		return stockInformation;
	}
	
}
