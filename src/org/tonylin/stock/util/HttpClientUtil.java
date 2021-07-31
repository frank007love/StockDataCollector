package org.tonylin.stock.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	static private Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * 釋放HttpEntity資源
	 * 
	 * @param aHttpEntity
	 */
	static public void consumeEntity(HttpEntity aHttpEntity){
		try {
			if (aHttpEntity != null)
				EntityUtils.consume(aHttpEntity);
		} catch (IOException e) {
			logger.warn("Consume HttpEntity failed.", e);
		}
	}
	
	/**
	 * 釋放HttpClient連線
	 * 
	 * @param aHttpClient
	 */
	static public void shutdownHttpClient(HttpClient aHttpClient){
		if( aHttpClient != null ){
			aHttpClient.getConnectionManager().shutdown();
		}
	}
    
    /**
     * Get http response content.
     * 
     * @param aResponse
     * @return
     * @throws IOException
     */
	public static String getReponseContent(HttpResponse aResponse)
			throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(aResponse
				.getEntity().getContent(), "utf-8");

		BufferedReader br = new BufferedReader(inputStreamReader);

		StringBuffer content = new StringBuffer();
		int i;
		while ((i = br.read()) != -1) {
			content.append((char) i);
		}

		logger.debug("HttpEntityStatus: {}", aResponse.getStatusLine());
		logger.debug("HttpEntiyInfo:\n{}", content.toString());
		return content.toString();
	}
}
