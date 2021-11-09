package demo1.manager;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Users;
import demo1.enity.UsersRole;
import demo1.enity.UsersStatus;
import demo1.servlet.RegisterServlet;
import demo1.utility.PersistenceUtility;

public class CreateAdmin {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServlet.class);

	public static void main(String[] args) {
		PersistenceUtility.initPersistence();
		UsersManager userManager = new UsersManager();
		Users admin = new Users(UsersRole.ADMIN, "admin@wallet.com", "admin", "admin", "admin", 
				null, new Date(), UsersStatus.CONFIRMED);
		userManager.create(admin);
		LOGGER.info("The admin was created");
		PersistenceUtility.destroy();
	}
	
}
