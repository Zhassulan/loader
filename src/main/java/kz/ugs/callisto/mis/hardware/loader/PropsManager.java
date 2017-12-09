package kz.ugs.callisto.mis.hardware.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Класс для получения данных из файла конфигурации приложения Spring application.properties
 * Паттерн синглтон с ленивой инициализацией (т.е. по запросу)  
 * @author Zhassulan Tokbaev
 * @version 1.0
 * @see PasswordValidator
 **/

public class PropsManager {
	/** ссылка на будущий экземпляр класса **/
	private static volatile PropsManager _instance = null;
	/** класс для обработки свойств **/
	private Properties appProps;
	
	/** конструктор 
	 * @see PropsManager()
	 * **/
	private PropsManager()	{
		appProps = new Properties();
		try {
			appProps.load(PropsManager.class.getResourceAsStream("/application.properties"));
			}
		catch (FileNotFoundException ex) {
			App.logger.info("Error message in PropsManager constructor " + ex.getMessage());
			App.logger.error("Stack trace: ", ex);
		}
		catch (IOException ex) {
			App.logger.info("Error message in PropsManager constructor: " + ex.getMessage());
			App.logger.error("Stack trace: ", ex);
		}
	}
	
	/** метод получения ссылки экземпляра класса
	 * @see getInstance()
	 */
	public static synchronized PropsManager getInstance() {
        if (_instance == null)
        	 synchronized (PropsManager.class) {
                 if (_instance == null)
                     _instance = new PropsManager();
             }
        return _instance;
    }
	
	/** метод получения значения по имени свойства в конфигурации приложения
	 * @see getProperty()
	 * @param String param название параметра
	 */
	public String getProperty(String param)	{
		return appProps.getProperty(param);
	}
}
