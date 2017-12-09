package kz.ugs.callisto.mis.hardware.loader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.PropertyMap.Property;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import net.ucanaccess.jdbc.UcanaccessDriver;
import net.ucanaccess.jdbc.UcanaccessConnection;

/**
 * @author ZTokbayev
 *
 */
public class Access {
	
	public static Logger logger = LogManager.getLogger(Access.class);
	
	private Database db;
	private Set <String> tables;
	private String filePath;
	private int systemCount;
	private Date systemDate;
	private Connection conn;
	private String accessKey = ";singleconnection=true";
	private String openMode = ";openExclusive=false;";
	private String keepMirror = ";keepMirror=C:/temp/mirror";
	
	
	public Connection getConn() {
		return conn;
	}

	public void openConnection() {
		try {
			/*
			String url = UcanaccessDriver.URL_PREFIX + filePath + ";newDatabaseVersion=V2003";
			Driver jdbcDriver = (Driver) Class.forName("net.ucanaccess.jdbc.UcanaccessDriver").newInstance();
	        DriverManager.registerDriver(jdbcDriver);
	        */
			//Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//conn = DriverManager.getConnection("jdbc:ucanaccess://" + filePath);
			conn = getUcanaccessConnection(filePath, keepMirror);
	        //conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void closeConnection()	{
		try {
			conn.close();	
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private static Connection getUcanaccessConnection(String pathNewDB, String key) throws SQLException, IOException {
	   String url = UcanaccessDriver.URL_PREFIX + pathNewDB; // + key; //newDatabaseVersion=V2003";
	   return DriverManager.getConnection(url);
	}

	public Object executeQuery(String query) {
		Statement st = null;
		try {
			List <Map <String, String>> listSqlResult = new ArrayList();
			
			
			UcanaccessConnection ucConn = (UcanaccessConnection) conn;
			ucConn.addFunctions(this.getClass());
			
			st = ucConn.createStatement();
			logger.info("Trying to execute query: " + query);
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				Map <String, String> sqlRow = new HashMap <String, String> ();
				for (int i = 1; i <= rsmd.getColumnCount(); i++)
					sqlRow.put(rsmd.getColumnName(i), rs.getString(i));
				listSqlResult.add(sqlRow);
			}
			return listSqlResult;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (st != null)
				try {
					st.close();
					//closeConnection();
				} catch (SQLException e2) {
					logger.error(e2.getMessage(), e2);
				}
				
		}

		return null;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void setSystemCount(int systemCount) {
		this.systemCount = systemCount;
	}

	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
	
	public int getCount(String query)	{
		int res = 0;
		List <Map <String, String>> sqlResult = (List <Map <String, String>>) executeQuery(query);
		for (Map <String, String> listItem : sqlResult) {
			for (Map.Entry<String, String> rowItem: listItem.entrySet())
				return Integer.valueOf(rowItem.getValue()); 
		}
		return res;
	}
	
}
