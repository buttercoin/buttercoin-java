package com.buttercoin.api;

import org.junit.runner.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class ButtercoinAPITest {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info("Running test {} ...", description.getMethodName());
        }
    };

    protected Buttercoin buttercoin = Buttercoin.newBuilder().requestTimeout(3, TimeUnit.SECONDS).build();
}
