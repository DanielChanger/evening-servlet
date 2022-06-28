package com.changer.session;

public class SessionFactory {
    public Session createSession(long maxAgeInMillis) {
        return new Session(maxAgeInMillis, sessionStorage(), sessionCleaner());
    }

    private SessionCleaner sessionCleaner() {
        return new SessionCleaner(sessionStorage());
    }

    private SessionStorage sessionStorage() {
        return new SessionStorage();
    }
}
