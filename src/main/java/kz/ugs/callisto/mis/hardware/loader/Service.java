package kz.ugs.callisto.mis.hardware.loader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Service implements Runnable {
	
	public static Logger logger = LogManager.getLogger(Service.class);
	
	private int intervalSeconds = Integer.valueOf(PropsManager.getInstance().getProperty("intervalSeconds"));
	private String filePath = PropsManager.getInstance().getProperty("filePath");
	private int dbCount;
	private int systemCount;
	private Date systemDate;
	private Access access;
	
	Service()	{
		access = new Access();
		access.setFilePath(PropsManager.getInstance().getProperty("filePath"));
		access.openConnection();
		run();
	}
	
	public void run() {
        while(true) {
           	try {
           		logger.error("Reading db..");
           		readDb();
           		Thread.sleep(intervalSeconds * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
           	
        }
    }
	
	public void readDb()	{	
    	
		List <Map <String, String>> sqlResult; // = (Map <String, String>) accDb.executeQuery("select * from hrpatient");
    	//for (Map.Entry <String, String> entry : sqlResult.entrySet())
			//logger.info(entry.getKey() + " = " + entry.getValue());
		dbCount = access.getCount("select count(*) from hrexamination");
		//dbCount = access.getCount("select count(*) from hrancestry");
		logger.info("Count in db file " + dbCount);
		systemCount = 0;
		if (dbCount > systemCount)	{
			logger.info("Count in db file more than system");
			systemDate = yesterday();
			String systemDateFormatted = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(systemDate);
			logger.info("System last date " + systemDateFormatted);
			sqlResult = (List <Map<String, String>>) access.executeQuery(PropsManager.getInstance().getProperty("sqlquery") + " where Date > #" + systemDateFormatted + "#;");
			if (sqlResult.size() > 0)	{
				logger.info("Found new records, writing..");
				listResult(sqlResult);
			}
			}
	}
	
	private void listResult(List <Map <String, String>> sqlResult)	{
		for (Map <String, String> listItem : sqlResult) {
			for (Map.Entry<String, String> rowItem: listItem.entrySet())
				logger.info(rowItem.getKey() + " = " + rowItem.getValue());
		logger.info("record----------------------------------------");
		}
	}
	
	private Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    //cal.add(Calendar.DATE, -1);
	    cal.add(Calendar.MONTH, -1);
	    return cal.getTime();
	}

}
