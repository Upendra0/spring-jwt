package com.upendra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;

@Service
public class BlockedTokenCacheService {

    @Autowired
    Cache<String, Boolean> blockedTokenCache;

    public void blockToken(String token){
        blockedTokenCache.put(token, true);
    }

    public boolean isTokenBlocked(String token){
        return blockedTokenCache.getIfPresent(token)!=null;
    }

}
