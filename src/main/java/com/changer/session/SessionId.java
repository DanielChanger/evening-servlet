package com.changer.session;

import java.time.Instant;
import java.util.Objects;

class SessionId {
    private final String id;
    private final long creationDateInMillis;

    static SessionId of(String id) {
        return new SessionId(id);
    }

    private SessionId(String id) {
        this.id = id;
        this.creationDateInMillis = Instant.now().toEpochMilli();
    }

    String getId() {
        return id;
    }

    long getCreationDateInMillis() {
        return creationDateInMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionId that = (SessionId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionKey{" +
                "id='" + id + '\'' +
                ", creationDateInMillis=" + creationDateInMillis +
                '}';
    }
}
