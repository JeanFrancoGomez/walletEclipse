package demo1.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Session;

public class SessionManager extends AbstractEntityManager<Session> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsersManager.class);
		
	public SessionManager() {
		super(Session.class);
	}

	public void deleteNullSession() {		
		int numUser = super.updateByNamedJPQLStatement("deleteNullSession",null);
		LOGGER.info( numUser + " sessions null deleted.");
	}
	
}
