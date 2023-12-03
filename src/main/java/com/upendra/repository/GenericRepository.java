package com.upendra.repository;

import java.util.List;
import java.util.Map;

import com.upendra.repository.IGenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

public abstract class GenericRepository<T,K> implements IGenericRepository<T, K> {

	@PersistenceContext 
	protected EntityManager entityManager;
		
	protected String fetchAllQuery = "from " + getEntityClass().getName();
		
	protected String hqlDeleteQuery = "DELETE FROM " +  getEntityClass().getName() +" Where id=:id";

	String hqlGetIdQuery = "SELECT id FROM " + getEntityClass().getName() + " Where :id=id";

	public GenericRepository() {}
	
	protected abstract Class<T> getEntityClass();

	
	@Override
	public List<T> getAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(getEntityClass());
		Root<T> root = cr.from(getEntityClass());
		cr.select(root);
		return entityManager
				.createQuery(cr)
				.getResultList();
	}

	@Override
	public T get(K id) {
		return entityManager
				.find(getEntityClass(), id);
	}

	@Override
	public void create(T object) {
		entityManager.persist(object);
	}

	@Override
	public void delete(K id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<T> delete = cb.createCriteriaDelete(getEntityClass());
		Root<T> root = delete.from(getEntityClass());
		delete.where(cb.equal(root.get("id"), id));
		entityManager.createQuery(delete).executeUpdate();
	}

	@Override
	public boolean isPresent(K id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(getEntityClass());
		query.select(cb.count(root)).where(cb.equal(root.get("id"), id));
		Long count = entityManager.createQuery(query).getSingleResult();
		return count > 0;
	}

	@Override
	public void update(Map<String, Object> updatabaleFields, K id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<T> query = cb.createCriteriaUpdate(getEntityClass());
		Root<T> root = query.from(getEntityClass());
		query.where(cb.equal(root.get("id"), id));
		for(String field: updatabaleFields.keySet()){
			query.set(field, updatabaleFields.get(field));
		}
		entityManager.createQuery(query).executeUpdate();
	}
	
	@Override
	public T getByField(String fieldName, Object fieldValue) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = cb.createQuery(getEntityClass());
		Root<T> root = criteriaQuery.from(getEntityClass());
		criteriaQuery.select(root).where(cb.equal(root.get(fieldName), fieldValue));
		T t = null;
		try {
			t = entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException ignored) {
        }
		return t; 
	}

	@Override
	public void deleteByField(String fieldName, Object fieldValue) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(getEntityClass());
		Root<T> root = criteriaDelete.from(getEntityClass());
		criteriaDelete.where(cb.equal(root.get(fieldName), fieldValue));
		entityManager.createQuery(criteriaDelete).executeUpdate();
	}

	@Override
	public void updateByField(Map<String, Object> updatabaleFields, String fieldName, Object fieldValue) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<T> query = cb.createCriteriaUpdate(getEntityClass());
		Root<T> root = query.from(getEntityClass());
		query.where(cb.equal(root.get(fieldName), fieldValue));
		for(String field: updatabaleFields.keySet()){
			query.set(field, updatabaleFields.get(field));
		}
		entityManager.createQuery(query).executeUpdate();
	}
}
