package io.github.milkdrinkers.stewards;

import io.github.milkdrinkers.stewards.command.CommandHandler;
import io.github.milkdrinkers.stewards.config.ConfigHandler;
import io.github.milkdrinkers.stewards.database.handler.DatabaseHandler;
import io.github.milkdrinkers.stewards.database.handler.DatabaseHandlerBuilder;
import io.github.milkdrinkers.stewards.hook.HookManager;
import io.github.milkdrinkers.stewards.listener.ListenerHandler;
import io.github.milkdrinkers.stewards.threadutil.SchedulerHandler;
import io.github.milkdrinkers.stewards.translation.TranslationHandler;
import io.github.milkdrinkers.stewards.updatechecker.UpdateHandler;
import io.github.milkdrinkers.stewards.utility.DB;
import io.github.milkdrinkers.stewards.utility.Logger;
import io.github.milkdrinkers.colorparser.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Main class.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Stewards extends JavaPlugin {
    private static Stewards instance;

    // Handlers/Managers
    private ConfigHandler configHandler;
    private TranslationHandler translationHandler;
    private DatabaseHandler databaseHandler;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateHandler updateHandler;
    private SchedulerHandler schedulerHandler;

    // Handlers list (defines order of load/enable/disable)
    private List<? extends Reloadable> handlers;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static Stewards getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;


        configHandler = new ConfigHandler(this);
        translationHandler = new TranslationHandler(configHandler);
        databaseHandler = new DatabaseHandlerBuilder()
            .withConfigHandler(configHandler)
            .withLogger(getComponentLogger())
            .build();
        hookManager = new HookManager(this);
        commandHandler = new CommandHandler(this);
        listenerHandler = new ListenerHandler(this);
        updateHandler = new UpdateHandler(this);
        schedulerHandler = new SchedulerHandler();

        handlers = List.of(
            configHandler,
            translationHandler,
            databaseHandler,
            hookManager,
            commandHandler,
            listenerHandler,
            updateHandler,
            schedulerHandler
        );

        DB.init(databaseHandler);
        for (Reloadable handler : handlers)
            handler.onLoad(instance);
    }

    @Override
    public void onEnable() {
        for (Reloadable handler : handlers)
            handler.onEnable(instance);

        if (!DB.isReady()) {
            Logger.get().warn(ColorParser.of("<yellow>DatabaseHolder handler failed to start. Database support has been disabled.").build());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        for (Reloadable handler : handlers.reversed()) // If reverse doesn't work implement a new List with your desired disable order
            handler.onDisable(instance);
    }

    /**
     * Use to reload the entire plugin.
     */
    public void onReload() {
        onDisable();
        onLoad();
        onEnable();
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    @NotNull
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    /**
     * Gets hook manager.
     *
     * @return the hook manager
     */
    @NotNull
    public HookManager getHookManager() {
        return hookManager;
    }

    /**
     * Gets update handler.
     *
     * @return the update handler
     */
    @NotNull
    public UpdateHandler getUpdateHandler() {
        return updateHandler;
    }
}
