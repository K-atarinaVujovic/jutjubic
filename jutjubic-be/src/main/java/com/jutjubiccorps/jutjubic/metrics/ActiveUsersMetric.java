package com.jutjubiccorps.jutjubic.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActiveUsersMetric {
    // using atomic integer because of concurrency
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    public ActiveUsersMetric(MeterRegistry registry){
        Gauge.builder("active_users", activeUsers, AtomicInteger::get)
                .description("Currently active users")
                .register(registry);
    }

    public void userLoggedIn() { System.out.println("======User logged in, active users: " + activeUsers.incrementAndGet()); }
    public void userLoggedOut() { activeUsers.decrementAndGet(); }
}
