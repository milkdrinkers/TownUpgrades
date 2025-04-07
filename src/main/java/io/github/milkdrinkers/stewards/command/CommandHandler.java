package io.github.milkdrinkers.stewards.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.Reloadable;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    private final Stewards plugin;

    /**
     * Instantiates the Command handler.
     *
     * @param plugin the plugin
     */
    public CommandHandler(Stewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(Stewards plugin) {
        CommandAPI.onLoad(
            new CommandAPIBukkitConfig(plugin)
                .shouldHookPaperReload(true)
                .silentLogs(true)
                .usePluginNamespace()
                .beLenientForMinorVersions(true)
        );
    }

    @Override
    public void onEnable(Stewards plugin) {
        CommandAPI.onEnable();

        // Register commands here
        new ExampleCommand();
    }

    @Override
    public void onDisable(Stewards plugin) {
        CommandAPI.getRegisteredCommands().forEach(registeredCommand -> CommandAPI.unregister(registeredCommand.namespace() + ':' + registeredCommand.commandName(), true));
        CommandAPI.onDisable();
    }
}