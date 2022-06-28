package com.changer.session;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


class SessionStorage {

    private final Map<SessionId, Map<String, Object>> sessionMap = new ConcurrentHashMap<>();

    Optional<Object> get(SessionId sessionId, String key) {
        Objects.requireNonNull(key, "Session key cannot be null");
        Map<String, Object> map = sessionMap.get(sessionId);
        if (map == null) {
            return Optional.empty();
        }
        return Optional.of(map.get(key));
    }

    void set(SessionId sessionId, String key, Object value) {
        Objects.requireNonNull(key, "Session key cannot be null");
        Objects.requireNonNull(value, "Session value cannot be null");
        sessionMap.computeIfAbsent(sessionId, k -> new HashMap<>());
        sessionMap.get(sessionId).put(key, value);
    }

    void removeExpired(long maxAge) {
        sessionMap.keySet()
                  .stream()
                  .filter(id -> Instant.now().toEpochMilli() - maxAge < id.getCreationDateInMillis())
                  .forEach(sessionMap::remove);
    }
}
