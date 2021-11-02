package demo1.manager;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.utility.PersistenceUtility;

/**
 * AbstractEntityManager is an abstract class for providing basic operations on
 * entities.
 */
public abstract class AbstractEntityManager<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractEntityManager.class);

	protected Class<T> clazz;

	/**
	 * Create the AbstractEntityManager with provided class of type T. This is used
	 * for the EntityManager methods
	 *
	 * @param clazz The class of type T
	 */
	protected AbstractEntityManager(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Persist an entity, creating the EntityManager.
	 *
	 * @param entity The entity manager.
	 * @return The persisted entity.
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected T persist(T entity) {
		try {
			EntityManager em = PersistenceUtility.createEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		} catch (Exception ex) {
			logger.error("Error persisting entity: {}.", entity, ex);
			throw new ManagerException("Error persisting entity.", ex);
		}
	}

	/**
	 * Find an entity using provided id.
	 *
	 * @param entityId The id of the entity to be found.
	 * @return The entity if found, null otherwise.
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected T find(Object entityId) {
		try {
			EntityManager em = PersistenceUtility.createEntityManager();
			return em.find(clazz, entityId);
		} catch (Exception ex) {
			logger.error("Error finding entity with id: {}.", entityId, ex);
			throw new ManagerException("Error finding entity.", ex);
		}
	}

	/**
	 * @return Get all the entities of managed class.
	 */
	protected List<T> findAll() {
		try {
			EntityManager em = PersistenceUtility.createEntityManager();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
			CriteriaQuery<T> all = cq.select(rootEntry);
			TypedQuery<T> allQuery = em.createQuery(all);

			return allQuery.getResultList();
		} catch (Exception ex) {
			logger.error("Error finding all entities.", ex);
			throw new ManagerException("Error finding all entities.", ex);
		}
	}

	/**
	 * Execute JPQL query with provided parameters that returns only one result.
	 *
	 * @param queryName   The JPQL named query to execute
	 * @param queryParams (Optional) map of query parameters to replace in the
	 *                    query.
	 * @return The result of the query
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected T findOneByNamedJPQLQuery(String queryName, Map<String, Object> queryParams) {
		try {
			// Get entity manager for the thread using the persistence context.
			EntityManager em = PersistenceUtility.createEntityManager();

			// Create a typed named query
			TypedQuery<T> customQuery = em.createNamedQuery(queryName, this.clazz);

			// Check for existing query parameters to be set in the query
			if (queryParams != null && !queryParams.isEmpty()) {
				for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
					customQuery.setParameter(entry.getKey(), entry.getValue());
				}
			}

			return customQuery.getSingleResult();
		} catch (NoResultException nre) {
			if (logger.isDebugEnabled()) {
				logger.debug("No result found for query: {} - with params: {} ", queryName, queryParams);
			}
			return null;
		} catch (Exception ex) {
			logger.error("Error executing named query: {}.", queryName, ex);
			throw new ManagerException("Error executing named query.", ex);
		}
	}

	/**
	 * Execute JPQL query with provided parameters
	 *
	 * @param queryName   The JPQL named query to execute
	 * @param queryParams (Optional) map of query parameters to replace in the
	 *                    query.
	 * @return The result of the query
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected List<T> findByNamedJPQLQuery(String queryName, Map<String, Object> queryParams) {
		return this.findByNamedJPQLQuery(queryName, queryParams, -1, -1);
	}

	/**
	 * Execute JPQL query with provided parameters
	 *
	 * @param queryName   The JPQL named query to execute
	 * @param queryParams (Optional) map of query parameters to replace in the
	 *                    query.
	 * @param firstResult The starting offset for the query.
	 * @param maxResults  The maximum number of elements to be retrieved.
	 * @return The result of the query
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected List<T> findByNamedJPQLQuery(String queryName, Map<String, Object> queryParams, int firstResult,
			int maxResults) {
		try {
			// Get entity manager for the thread using the persistence context.
			EntityManager em = PersistenceUtility.createEntityManager();

			// Create a typed named query
			TypedQuery<T> customQuery = em.createNamedQuery(queryName, this.clazz);

			// Check for existing query parameters to be set in the query
			if (queryParams != null && !queryParams.isEmpty()) {
				for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
					customQuery.setParameter(entry.getKey(), entry.getValue());
				}
			}

			// Set first result and max results if greater than 0
			if (firstResult >= 0 && maxResults > 0) {
				customQuery.setFirstResult(firstResult);
				customQuery.setMaxResults(maxResults);
			}

			return customQuery.getResultList();
		} catch (Exception ex) {
			logger.error("Error executing named query: {}.", queryName, ex);
			throw new ManagerException("Error executing named query.", ex);
		}
	}

	/**
	 * Executes a "count" query and returns the result as a long.
	 *
	 * @param queryName   The name of the named query to be used.
	 * @param queryParams The query params to be used.
	 * @return The number of found records or 0 if no result is found.
	 */
	protected long countRecordsByNamedJPQLQuery(String queryName, Map<String, Object> queryParams) {
		try {
			// Get entity manager for the thread using the persistence context.
			EntityManager em = PersistenceUtility.createEntityManager();

			// Create a typed named query for Long type
			TypedQuery<Long> customQuery = em.createNamedQuery(queryName, Long.class);

			// Check for existing query parameters to be set in the query
			if (queryParams != null && !queryParams.isEmpty()) {
				for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
					customQuery.setParameter(entry.getKey(), entry.getValue());
				}
			}

			// Executes query and returns result
			return customQuery.getSingleResult();
		} catch (Exception ex) {
			logger.error("Error executing count named query: {}.", queryName, ex);
			throw new ManagerException("Error executing count named query.", ex);
		}
	}

	/**
	 * Perform an update on the database using a named JPQL statement.
	 *
	 * @param updateStatement The name of the statement to be used.
	 * @param statementParams (optional) map of statement parameters to replace in
	 *                        the statement.
	 * @return The number of updated records.
	 */
	protected int updateByNamedJPQLStatement(String updateStatement, Map<String, Object> statementParams) {
		try {
			// Get entity manager for write, opening a transaction
			EntityManager em = PersistenceUtility.createEntityManager();
			em.getTransaction().begin();

			// Create named query, we don't need it typed as we are going to perform an
			// update
			Query customQuery = em.createNamedQuery(updateStatement);

			// Check for existing statement parameters to be set in the query
			if (statementParams != null && !statementParams.isEmpty()) {
				for (Map.Entry<String, Object> entry : statementParams.entrySet()) {
					customQuery.setParameter(entry.getKey(), entry.getValue());
				}
			}

			int res = customQuery.executeUpdate();
			em.getTransaction().commit();
			return res;
		} catch (Exception ex) {
			logger.error("Error executing update statement: {}.", updateStatement, ex);
			throw new ManagerException("Error executing update statement.", ex);
		}
	}

	/**
	 * Update entity.
	 *
	 * @param entity The entity to update.
	 * @return The updated entity.
	 * @throws ManagerException If something goes wrong, Exception are enclosed in a
	 *                          {@link ManagerException}
	 */
	protected T merge(T entity) {
		try {
			EntityManager em = PersistenceUtility.createEntityManager();
			em.getTransaction().begin();
			T updatedEntity = em.merge(entity);
			em.getTransaction().commit();
			return updatedEntity;
		} catch (Exception ex) {
			logger.error("Error updating entity: {}.", entity, ex);
			throw new ManagerException("Error updating entity.", ex);
		}
	}

	/**
	 * Delete an entity using its id.
	 *
	 * @param entityId The entity to be deleted.
	 * @return The entity before deletion or null if the entity is not found.
	 */
	protected T remove(Object entityId) {
		T deletedEntity = null;

		try {
			EntityManager em = PersistenceUtility.createEntityManager();
			em.getTransaction().begin();
			// Find entity, if found, open transaction, remove and commit
			deletedEntity = em.find(this.clazz, entityId);
			if (deletedEntity != null) {
				em.remove(deletedEntity);
			}
			em.getTransaction().commit();
			return deletedEntity;
		} catch (Exception ex) {
			logger.error("Error deleting entity: {}.", deletedEntity, ex);
			throw new ManagerException("Error deleting entity.", ex);
		}
	}
}