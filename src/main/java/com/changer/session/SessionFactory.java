/*
Copyright 2022-present © Care.com, Inc. All rights reserved.
This software is the confidential and proprietary information of Care.com, Inc.
*/
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
