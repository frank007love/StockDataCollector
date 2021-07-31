package org.tonylin.stock.dataprovider.parser;

public class StockInformationException extends Exception {
	private static final long serialVersionUID = 1118000946237908826L;

	public StockInformationException(String aMessage){
		super(aMessage);
	}
	
	public StockInformationException(Throwable t){
		super(t);
	}
	
	
	public StockInformationException(String aMessage, Throwable t){
		super(aMessage, t);
	}
}
