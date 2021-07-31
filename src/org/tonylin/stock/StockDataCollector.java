package org.tonylin.stock;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tonylin.stock.dao.DatabaseStockDao;
import org.tonylin.stock.dao.ExcelStockDao;
import org.tonylin.stock.dao.IStockDao;
import org.tonylin.stock.dataprovider.MOPSDataProvider;
import org.tonylin.util.io.Cleaner;
import org.tonylin.util.license.SimpleLicense;

public class StockDataCollector {

	private static Logger logger = LoggerFactory.getLogger(StockDataCollector.class);
	
	/**
	 * Check the programe license.
	 * 
	 */
	static private void checkLicense(){
		String licenseFile = "config/license.lnc";
		if( !new File(licenseFile).exists()){
			System.out.println("License file doesn't exist.  Please contact with the product provider.");
			System.exit(2);
		}
		System.setProperty("simple.license.file", licenseFile);
		if( !SimpleLicense.isLicenseAvailabe()){
			System.out.println("License is expired. Please contact with the product provider.");
			System.exit(2);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//checkLicense();
		
		File tmpFile = new File("c3p0.tmp");
		PrintStream printStream = null;
		try {
			// Create c3p0 tmp file for loggin.
			tmpFile.createNewFile();
			printStream = new PrintStream(tmpFile);
			System.setErr(printStream);
			
			
			Options options = getOptions();
			CommandLineParser cliParser = new PosixParser();
			CommandLine commandline = cliParser.parse(options, args);
			
			// dump help information.
			if( commandline.hasOption('h') ){
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Stock data collector", options);
				
			// check the input and handle the request.
			} else if( commandline.hasOption('m') && commandline.hasOption('y') 
					&& commandline.hasOption('s') ){
				checkCmdOptionsValue(commandline);
				collectDataFromMOPS(commandline.getOptionValue('s'),
						commandline.getOptionValue('y'), commandline.getOptionValue('m'));
				//SimpleLicense.increaseLicenseCount();
				//SimpleLicense.saveLicense();
			// un-expect options. 
			} else {
				throw new RuntimeException("Missing stock's id, year, or month.");
			}
			
		} catch( Exception e ){
			System.out.println("Run stock data collector failed cause by \'" + e.getMessage() +"'");
		} finally {
			Cleaner.close(printStream);
			tmpFile.delete();
		}
	}

	/**
	 * Generate command options.
	 * 
	 * @return
	 */
	static private Options getOptions(){
		Options options = new Options();
		options.addOption( "s", "stock", true, "the stock's id for the target.");
		options.addOption( "y", "year", true, "the year for stock information, please enter the A.D.(ex.2012)");
		options.addOption( "m", "month", true, "the month for stock information.");
		options.addOption( "h", "help", false, "print command information.");
		return options;
	}

	/**
	 * Check the command arguments is valid.
	 * 
	 * @param aCommandline
	 */
	static private void checkCmdOptionsValue(CommandLine aCommandline)
			throws Exception {
		try {
			Integer.valueOf(aCommandline.getOptionValue('s'));
			Integer.valueOf(aCommandline.getOptionValue('y'));
			Integer.valueOf(aCommandline.getOptionValue('m'));
		} catch (NumberFormatException e) {
			throw new Exception("Stock ID, year, and month should be integer.");
		}
	}
	
	/**
	 * Collect stock data from MOPS site, and store in excel and database.
	 * 
	 * @param aStock
	 * @param aYear
	 * @param aMonth
	 */
	static private void collectDataFromMOPS(String aStock, String aYear, String aMonth) throws Exception {
		// translate year from A.D. to R.C.
		String year = String.valueOf(Integer.valueOf(aYear) - 1911);
		String month = String.format("%02d", Integer.valueOf(aMonth));
		
		IStockDao excelStockDao = new ExcelStockDao();
		
		if( excelStockDao.doesDataExist(aStock, aYear, month) ){
			String messageFormat = "Stock %s,%s,%s data exists.";
			String message = String.format(messageFormat, aStock, aYear, month);
			System.out.println(message);
			logger.info("{}", message);
			return;
		}
		MOPSDataProvider mopsDataProvider = new MOPSDataProvider();
		StockInformation stockInformation = mopsDataProvider.queryInvoiceAndBusinessData(aStock, year, month);
		
		String message = "Get stock information: " + stockInformation.getId();
		
		logger.info("{}", message);
		System.out.println(message);
		
		
		// Save data to excel and database.
		try {
			excelStockDao.saveInvoiceAndBusinessData(stockInformation, aYear, month);
			System.out.println("Complate save stock data to excel.");
		} catch(Exception e){
			logger.error("", e);
			System.out.println("儲存至Excel失敗，請確認是否開啟著檔案。");
			return;
		}
		
		IStockDao databaseStockDao = DatabaseStockDao.createStockDao();
		databaseStockDao.saveInvoiceAndBusinessData(stockInformation, aYear, aMonth);
		System.out.println("Complate save stock data to database.");
	}
}
