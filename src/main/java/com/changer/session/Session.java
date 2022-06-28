/*
Copyright 2022-present © Care.com, Inc. All rights reserved.
This software is the confidential and proprietary information of Care.com, Inc.
*/
package com.changer.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Facade to work with session. Allows getting and setting session attributes.
 * Schedules periodical expired session clean-up.
 */
public class Session {
    private static final String SESSION_ID_NAME = "SESSION_ID";
    private final SessionStorage storage;
    private final long maxAge;

    public Session(long maxAge, SessionStorage storage, SessionCleaner cleaner) {
        this.storage = storage;
        this.maxAge = maxAge;
        cleaner.scheduleCleaner(maxAge);
    }

    /**
     * Retrieves current session ID from request cookies and use it to get value by key.
     * In case of session ID or value absence, returns {@link Optional#empty()}.
     */
    public Optional<Object> get(String key, HttpServletRequest request) {
        Optional<String> sessionIdFromRequest = getSessionIdFromRequest(request);
        if (sessionIdFromRequest.isPresent()) {
            SessionId sessionId = SessionId.of(sessionIdFromRequest.get());
            return storage.get(sessionId, key);
        }
        return Optional.empty();
    }

    /**
     * Sets new key-value data into session, creates session in case request has no session ID yet and
     * sets new session ID into {@link HttpServletResponse}.
     * Session ID is generated in a random manner.
     */
    public void set(String key, Object value, HttpServletRequest request, HttpServletResponse response) {
        Optional<String> sessionIdFromRequest = getSessionIdFromRequest(request);
        if (sessionIdFromRequest.isPresent()) {
            storage.set(SessionId.of(sessionIdFromRequest.get()), key, value);
            return;
        }
        SessionId sessionId = SessionId.of(String.valueOf(ThreadLocalRandom.current().nextInt()));
        storage.set(sessionId, key, value);
        setSessionIdToResponse(response, sessionId);
    }

    private static Optional<String> getSessionIdFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(SESSION_ID_NAME))
                .map(Cookie::getValue)
                .findFirst();

    }

    private void setSessionIdToResponse(HttpServletResponse response, SessionId sessionId) {
        Cookie cookie = new Cookie(SESSION_ID_NAME, sessionId.getId());
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(maxAge));
        response.addCookie(cookie);
    }
}
