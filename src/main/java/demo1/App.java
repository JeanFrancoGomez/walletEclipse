package demo1;

import javax.persistence.EntityManager;

import demo1.enity.Users;

public class App {

	public static void main(String[] args) {
		PersistenceUtility.initPersistence();

		EntityManager em = null;
		try {
			// Get EntityManager from EntityManagerFactory
			em = PersistenceUtility.createEntityManager();

			Users newUser = new Users("emailProva", "nameProva", "surnameProva", "passwordProva", "secretKeyProva", 15);

			// Begin transaction
			em.getTransaction().begin();
			em.persist(newUser);
			em.getTransaction().commit(); // Apply changes to db
		} catch (Exception e) {
			e.printStackTrace();

			// Check and force rollback
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}

		PersistenceUtility.destroy();

	}

}
