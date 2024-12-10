package com.group3.course_registration_system.service;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.Date;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    
    private Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
    private Map<String, Date> tokenExpirations = new ConcurrentHashMap<>();

    public void addToBlacklist(String token, Date expirationDate) {
        tokenBlacklist.add(token);
        tokenExpirations.put(token, expirationDate);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    @Scheduled(fixedRate = 3600000) //  Executed once an hour
    public void cleanupExpiredTokens() {
        Date now = new Date();
        tokenExpirations.entrySet().removeIf(entry -> entry.getValue().before(now));
        tokenBlacklist.retainAll(tokenExpirations.keySet());
    }
}