/*
Copyright 2022-present © Care.com, Inc. All rights reserved.
This software is the confidential and proprietary information of Care.com, Inc.
*/
package com.changer.session;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class SessionCleaner {
    private final SessionStorage storage;

    SessionCleaner(SessionStorage storage) {
        this.storage = storage;
    }

    void scheduleCleaner(long maxAge) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> storage.removeExpired(maxAge),
                maxAge,
                maxAge,
                TimeUnit.MILLISECONDS
        );
    }
}
