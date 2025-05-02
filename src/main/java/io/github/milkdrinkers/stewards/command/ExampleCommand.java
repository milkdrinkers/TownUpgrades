package io.github.milkdrinkers.stewards.command;

import com.palmergames.bukkit.towny.TownyAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.milkdrinkers.settlers.api.SettlersAPI;
import io.github.milkdrinkers.settlers.api.settler.Companion;
import io.github.milkdrinkers.settlers.api.settler.SettlerBuilder;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.exception.InvalidStewardException;
import io.github.milkdrinkers.stewards.exception.InvalidStewardTypeException;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.steward.StewardLookup;
import io.github.milkdrinkers.stewards.steward.StewardType;
import io.github.milkdrinkers.stewards.trait.StewardTrait;
import io.github.milkdrinkers.stewards.utility.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class containing the code for the example command.
 */
class ExampleCommand {
    private static final String BASE_PERM = "example.command";

    /**
     * Instantiates and registers a new command.
     */
    protected ExampleCommand() {
        new CommandAPICommand("example")
            .withFullDescription("Example command.")
            .withShortDescription("Example command.")
            .withSubcommands(
                new TranslationCommand().command()
            )
            .executes(this::executorExample)
            .register();
    }

    private void executorExample(CommandSender sender, CommandArguments args)  {
        try {
            StewardType type = StewardType.builder()
                .setId("test")
                .setMaxLevel(1)
                .setMinLevel(1)
                .setName("test")
                .setStartingLevel(1)
                .setSettlerPrefix("test")
                .build();

            Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().register(type);

            Companion settler = new SettlerBuilder()
                .setName("test")
                .setLocation(((Player) sender).getLocation())
                .createCompanion();

            Steward steward = Steward.builder()
                .setStewardType(type)
                .setDailyUpkeepCost(0)
                .setIsEnabled(true)
                .setIsHidden(false)
                .setLevel(1)
                .setSettler(settler)
                .build();

            steward.getSettler().getNpc().getOrAddTrait(StewardTrait.class);

            StewardLookup.get().registerSteward(steward);

            Logger.get().info(steward.getSettler().getNpc().getTraits().toString());

            settler.spawn();
        } catch (InvalidStewardException | InvalidStewardTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
