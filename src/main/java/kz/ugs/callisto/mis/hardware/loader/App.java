package kz.ugs.callisto.mis.hardware.loader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class App {
	
	public static Logger logger = LogManager.getLogger(App.class);
	
    public static void main( String[] args )	{
    	Service service = new Service();
    }
    
   
}
