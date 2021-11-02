package demo1.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);
	private ExpiredConfirmUser ecu = new ExpiredConfirmUser(5*60*1000);
	private Thread userCleanerThread = new Thread(this.ecu, "Users to confirmed thread");;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("Executing application init");
		
		// Initial code
		PersistenceUtility.initPersistence();
		
		// Start user cleaner thread
		// Sleep time should be read from configuration
		this.userCleanerThread.start();
				
		LOGGER.info("Application init complete");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("Executing application destroy");
		
		this.ecu.setKeepRunning(false);
		try {
			this.userCleanerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PersistenceUtility.destroy();
		LOGGER.info("Application destroy complete");
	}
	
	

}