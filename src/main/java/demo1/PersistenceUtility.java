package demo1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtility {
	private static EntityManagerFactory emf;

	/**
	 * Init persistence context.
	 */
	public static final void initPersistence() {
		emf = Persistence.createEntityManagerFactory("demo1");
	}

	/**
	 * Close Persistence context.
	 */
	public static final void destroy() {
		emf.close();
	}

	/**
	 * 
	 * @return Create new entity manager.
	 */
	public static final EntityManager createEntityManager() {
		return emf.createEntityManager();
	}
}

