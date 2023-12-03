package com.upendra.repository;

import org.springframework.stereotype.Repository;

import com.upendra.model.User;

@Repository
public class UserRepository extends GenericRepository<User, Long> {

	public User getByEmail(String email) {
		return getByField("email", email);		
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
