package demo1.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Users;
import demo1.enity.UsersStatus;

public class UsersManager extends AbstractEntityManager<Users> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsersManager.class);
	
	public UsersManager() {
		super(Users.class);
	}
	
	/**
	 * Creates new user with passed characteristics as an attribute.
	 * 
	 * @param user
	 * @return
	 */
	public Users create(Users user) {
		LOGGER.info("User "+ user.toString() + "was created.");
		return super.persist(user);
	}

	public Users find(int id) {
		return super.find(id);
	}

	public Users merge(Users user) {
		LOGGER.info("User "+ user.toString() + "was modified.");
		return super.merge(user);
	}

	/**
	 * Delete an user with primary key.
	 * 
	 * @param user
	 * @return
	 */
	public Users delete(Users user) {
		LOGGER.info("User "+ user.toString() + "was deleted.");
		return super.remove(user.getIdusers());
	}

	/**
	 * Get an user using his email address.
	 * 
	 * @param email
	 * @return
	 */
	public Users findByEmail(String email) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("emailData", email);

		return super.findOneByNamedJPQLQuery("findUserByEmail", params);
	}
	
	/**
	 * Get an user using his secret key, used for confirm the user in the database.
	 * 
	 * @param key
	 * @return
	 */
	public Users findUserBySecretKey(String key) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("secretKeyData", key);

		return super.findOneByNamedJPQLQuery("findUserBySecretKey", params);
	}
	
	/**
	 * Get users with expired registration
	 * 
	 * @return
	 */
	public List<Users> findToConfirmUser() {
		// Date dayBefore = Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC));
		Date fiveMinBefore = new Date(System.currentTimeMillis() - (5* 60 * 1000));
		
		Map<String, Object> params = new HashMap<>(2);
		params.put("stateData", UsersStatus.TO_CONFIRM);
		params.put("dateKeyData", fiveMinBefore);
		
		return super.findByNamedJPQLQuery("findUsersNotConfirmed", params); 
	}

	/**
	 * Delete users with expired registration
	 * 
	 * @return
	 */
	public void deleteUsersToConfirmUser() {
		// Date dayBefore = Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC));
		Date fiveMinBefore = new Date(System.currentTimeMillis() - (5* 60 * 1000));
		
		Map<String, Object> params = new HashMap<>(2);
		params.put("stateData", UsersStatus.TO_CONFIRM);
		params.put("dateKeyData", fiveMinBefore);
		
		int numUserDelete = super.updateByNamedJPQLStatement("deleteUsersToConfirm", params);
		LOGGER.info( numUserDelete + " users was/were deleted because not are confirmed.");
	}
	
}
