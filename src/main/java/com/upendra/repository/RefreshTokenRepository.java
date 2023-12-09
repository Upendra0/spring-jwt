package com.upendra.repository;

import com.upendra.model.RefreshToken;
import com.upendra.model.User;
import org.springframework.data.repository.ListCrudRepository;

public interface RefreshTokenRepository extends ListCrudRepository<RefreshToken, Long> {
    RefreshToken findByUser(User user);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);
}
