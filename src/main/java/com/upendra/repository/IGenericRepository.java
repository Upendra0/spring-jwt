package com.upendra.repository;

import java.util.List;
import java.util.Map;

/*
 * This interface defines the common set of operations needed for almost all repositories
 * 
 * @author :Upendra Kumar
 */
public interface IGenericRepository<T,K>{
	
	public List<T> getAll();
	
	public T get(K id);
	
	public void create(T object);

	public void update(Map<String, Object> updatabaleFields, K id);

	
	public void delete(K id);
	
	public boolean isPresent(K id);
	
	public void updateByField(Map<String, Object> updatabaleFields, String fieldName, Object fieldValue);

	public T getByField(String fieldName, Object fieldValue);

	public void deleteByField(String fieldName, Object fieldValue);
}
