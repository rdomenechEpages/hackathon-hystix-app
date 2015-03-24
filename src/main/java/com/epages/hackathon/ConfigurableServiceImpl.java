package com.epages.hackathon;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.netflix.config.ConfigurationManager;
// arcaius
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.jmx.ConfigJMXManager;

public class ConfigurableServiceImpl implements Service {
    private final String name;
    private final Logger logger;
    private final Random randomGenerator = new Random();

    private DynamicBooleanProperty blocked;
    private DynamicLongProperty blockedCheckTimeout;
    private DynamicLongProperty averageLatency;
    private DynamicIntProperty latencyVariation;

    static {
        ConfigJMXManager.registerConfigMbean(ConfigurationManager.getConfigInstance());
    }
    public ConfigurableServiceImpl(String name) {
        this.name = name;
        this.blocked = DynamicPropertyFactory.getInstance().getBooleanProperty(getPropertyName("blocked", name), false);
        this.blockedCheckTimeout = DynamicPropertyFactory.getInstance().getLongProperty(getPropertyName("blockedCheckTimeout", name), 50L);
        this.averageLatency = DynamicPropertyFactory.getInstance().getLongProperty(getPropertyName("averageLatency", name), 500L);
        this.latencyVariation = DynamicPropertyFactory.getInstance().getIntProperty(getPropertyName("latencyVariation", name), 50);
        this.logger = LoggerFactory.getLogger(String.format("%s [%s]", this.getClass().getSimpleName(), name));
    }

    private String getPropertyName(String propertyName, String name) {
        return String.format("service.%s.%s", name, propertyName);
    }

    @Override
    public String getValue() {
        logger.trace("Method serve() called");

        logCurrentPropertyValues();

        checkIfBlocked();
        delay();

        return name;
    }

    private void logCurrentPropertyValues() {
        System.out.println("blocked:" + blocked.get());
        System.out.println("blockedCheckTimeout:" + blockedCheckTimeout.get());
        System.out.println("averageLatancy:" + averageLatency.get());
        System.out.println("latencyVariation:" + latencyVariation.get());
    }

    private void checkIfBlocked() {
        if (blocked.get()) {
            logger.debug("Service is blocked!");
            long start = System.currentTimeMillis();

            while (blocked.get()) {
                sleep(blockedCheckTimeout.get());
            }

            logger.debug("Serivce is unblocked after {}ms.", System.currentTimeMillis() - start);
        }
    }

    private void delay() {
        int rawVariation = randomGenerator.nextInt(latencyVariation.get());
        boolean positive = randomGenerator.nextBoolean();
        Long latency = averageLatency.get() + (positive ? rawVariation : -rawVariation);

        sleep(latency > 0L ? latency : 0L);

        logger.trace("serve() method had a latency of {}ms.", latency);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}
