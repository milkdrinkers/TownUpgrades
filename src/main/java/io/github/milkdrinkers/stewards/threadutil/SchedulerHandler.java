package io.github.milkdrinkers.stewards.threadutil;

import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.threadutil.PlatformBukkit;
import io.github.milkdrinkers.threadutil.Scheduler;

import java.time.Duration;

/**
 * A wrapper handler class for handling thread-util lifecycle.
 */
public class SchedulerHandler implements Reloadable {
    @Override
    public void onLoad(Stewards plugin) {
        Scheduler.init(new PlatformBukkit(plugin)); // Initialize thread-util
    }

    @Override
    public void onEnable(Stewards plugin) {

    }

    @Override
    public void onDisable(Stewards plugin) {
        Scheduler.shutdown(Duration.ofSeconds(60));
    }
}
