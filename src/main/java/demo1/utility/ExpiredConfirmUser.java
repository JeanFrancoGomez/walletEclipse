package demo1.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.manager.SessionManager;
import demo1.manager.UsersManager;

public class ExpiredConfirmUser implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpiredConfirmUser.class);
	private boolean keepRunning;
	private final long sleepTime;

	/**
	 * Creates worker thread with configured sleep time.
	 * 
	 * @param sleepTime
	 */
	public ExpiredConfirmUser(long sleepTime) {
		this.sleepTime = sleepTime;
		keepRunning = true;
	}

	@Override
	public void run() {
		UsersManager uManager = new UsersManager();
		SessionManager sManager = new SessionManager();

		while (keepRunning) {
			LOGGER.info("Querying for expired users");
			// Find user with expired registration
			/*
			List<Users> expiredUsers = uManager.findToConfirmUser();

			// Loop on users and delete
			for (Users u : expiredUsers) {
				LOGGER.info("Deleting user: {}", u);
				// TODO Exercise: delete all expired users using JPQL query
				uManager.delete(u);
			}
			*/
			
			//Delete with JPQL query
			uManager.deleteUsersToConfirmUser();
			uManager.deleteExpiredSession();
			sManager.deleteNullSession();

			// Sleeps for configured sleep time
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted", e);
			}
		}
	}

	/**
	 * Set keep running param
	 * 
	 * @param keepRunning
	 */
	public synchronized void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}
}
