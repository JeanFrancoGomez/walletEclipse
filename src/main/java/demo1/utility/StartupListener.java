package demo1.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);
	private final long expiredUsersControl = 1*60*1000;
	private ExpiredConfirmUser ecu = new ExpiredConfirmUser(this.expiredUsersControl);
	private Thread userCleanerExpiredThread = new Thread(this.ecu, "Users to confirmed thread");
	private static Map<String, String> sessionMap = Collections.synchronizedMap(new HashMap<String, String>());
	//ConcorretHashmap se vuoi creare una map da zero perche' e' piu' efficiente
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("Executing application init");
		
		// Initial code
		PersistenceUtility.initPersistence();
		
		// Start user cleaner thread
		// Sleep time should be read from configuration
		this.userCleanerExpiredThread.start();
				
		LOGGER.info("Application init complete");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("Executing application destroy");
		
		this.ecu.setKeepRunning(false);
		try {
			this.userCleanerExpiredThread.isDaemon();
			this.userCleanerExpiredThread.join();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PersistenceUtility.destroy();
		LOGGER.info("Application destroy complete");
	}
	
	public static String getCache(String email) {
		return sessionMap.get(email);
	}
	
	public static void putCache(String email, String sessionKey) {
		sessionMap.put(email,sessionKey);
	}
	

}