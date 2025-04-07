package io.github.milkdrinkers.stewards.utility;

import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.config.ConfigHandler;
import io.github.milkdrinkers.crate.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public abstract class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link Config}
     *
     * @return the config
     */
    @NotNull
    public static Config get() {
        return Stewards.getInstance().getConfigHandler().getConfig();
    }
}
