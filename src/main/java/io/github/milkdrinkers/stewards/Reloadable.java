package io.github.milkdrinkers.stewards;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    void onLoad(Stewards plugin);

    /**
     * On plugin enable.
     */
    void onEnable(Stewards plugin);

    /**
     * On plugin disable.
     */
    void onDisable(Stewards plugin);
}
