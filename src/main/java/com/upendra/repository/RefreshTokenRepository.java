package com.upendra.repository;

import org.springframework.stereotype.Repository;

import com.upendra.model.RefreshToken;
import com.upendra.model.User;

@Repository
public class RefreshTokenRepository extends GenericRepository<RefreshToken, Long> {

	public RefreshToken findByToken(String token) {
		return getByField("token", token);
	}
	
	public RefreshToken findByUser(User user) {
		return getByField("user", user);
	}
	
	@Override
	public Class<RefreshToken> getEntityClass() {
		return RefreshToken.class;
	}
}
