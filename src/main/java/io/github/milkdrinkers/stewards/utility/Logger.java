package io.github.milkdrinkers.stewards.utility;


import io.github.milkdrinkers.stewards.Stewards;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link Stewards#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link Stewards#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return Stewards.getInstance().getComponentLogger();
    }
}
