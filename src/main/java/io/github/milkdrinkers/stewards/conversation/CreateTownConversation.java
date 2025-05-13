package io.github.milkdrinkers.stewards.conversation;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.TownCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.towny.TownyDataUtil;
import io.github.milkdrinkers.stewards.utility.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateTownConversation {

    public static ConversationPrefix getPrefix = new ConversationPrefix() {
        @Override
        public @NotNull String getPrefix(@NotNull ConversationContext context) {
            return "Architect: ";
        }
    };

    static String townName;
    static Steward steward;

    public static Prompt getNewTownPrompt(Steward steward) {
        CreateTownConversation.steward = steward;
        return newTownPrompt;
    }


    private static final Prompt newTownPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "What do you want your town to be called?";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (TownyAPI.getInstance().getTown(input) != null) {
                Player player = (Player) context.getForWhom();
                player.sendMessage(Component.text("That name is already taken."));
                return newTownPrompt;
            }
            townName = input;
            return confirmPrompt;
        }
    };

    private static final Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No", "y", "n", "Y", "N") {

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Do you want to create the town '" + townName + "'? [YES/NO]";
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            if (input.equalsIgnoreCase("no")) return Prompt.END_OF_CONVERSATION;

            Player player = (Player) context.getForWhom();

//            // TOWNY LOGIC
//            try {
//                TownyUniverse.getInstance().newTown(townName);
//            } catch (AlreadyRegisteredException | InvalidNameException e) {
//                player.sendMessage(ColorParser.of("<red>Invalid town name.").build());
//                throw new RuntimeException(e);
//            }
//
//            Town town = TownyUniverse.getInstance().getTown(townName);
//
//            if (town == null) {
//                player.sendMessage(ColorParser.of("<red>Something went wrong.").build());
//                Logger.get().error(String.format("Error fetching new town from name '%s'", townName));
//                return Prompt.END_OF_CONVERSATION;
//            }
//
//            TownyWorld world = TownyAPI.getInstance().getTownyWorld(player.getWorld());
//
//            TownBlock townBlock = new TownBlock((int) player.getX(), (int) player.getZ(), world);
//            townBlock.setTown(town);
//            TownPreClaimEvent preClaimEvent = new TownPreClaimEvent(town, townBlock, player, false, true, false);
//            preClaimEvent.setCancelMessage("&cAnother plugin stopped the claim of (%s)/(%s) town blocks, could not complete the operation.");
//            preClaimEvent.callEvent();
//
//            if (preClaimEvent.isCancelled()) {
//                TownyUniverse.getInstance().removeTownBlock(townBlock);
//                try {
//                    TownyUniverse.getInstance().unregisterTown(town);
//                } catch (NotRegisteredException e) {
//                    throw new RuntimeException(e);
//                }
//                player.sendMessage("<red>Something went wrong.");
//                Logger.get().error("Something prevented town block claim on town creation.");
//                return Prompt.END_OF_CONVERSATION;
//            }
//
//            town.setRegistered(System.currentTimeMillis());
//            town.setMapColorHexCode(TownySettings.getDefaultTownMapColor());
//
//            Resident resident = TownyAPI.getInstance().getResident(player);
//            try {
//                resident.setTown(town);
//            } catch (AlreadyRegisteredException e) {
//                player.sendMessage("<red>Something went wrong.");
//                throw new RuntimeException(e);
//            }
//
//            town.setMayor(resident, false);
//            town.setFounder(resident.getName());
//
//            townBlock.setType(townBlock.getType());
//            town.setSpawn(player.getLocation());
//
//            if (world.isUsingPlotManagementRevert()) {
//                PlotBlockData plotChunk = TownyRegenAPI.getPlotChunk(townBlock);
//                if (plotChunk != null && TownyRegenAPI.getRegenQueueList().contains(townBlock.getWorldCoord())) {
//                    TownyRegenAPI.removeFromActiveRegeneration(plotChunk);
//                    TownyRegenAPI.removeFromRegenQueueList(townBlock.getWorldCoord());
//                    TownyRegenAPI.addPlotChunkSnapshot(plotChunk);
//                } else {
//                    TownyRegenAPI.handleNewSnapshot(townBlock);
//                }
//            }
//
//            if (TownyEconomyHandler.isActive()) {
//                try {
//                    town.getAccount().setBalance(0, "Setting 0 balance for Town");
//                } catch (NullPointerException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            if (TownySettings.isTownTagSetAutomatically())
//                town.setTag(NameUtil.getTagFromName(townName));
//
//            resident.save();
//            townBlock.save();
//            town.save();
//            world.save();
//
//            Towny.getPlugin().updateCache(townBlock.getWorldCoord());
//
//            new NewTownEvent(town).callEvent();

            Resident resident = TownyAPI.getInstance().getResident(player);
            if (resident == null) {
                player.sendMessage(ColorParser.of("<red>Something went wrong with town creation. Please contact staff if this persists.").build());
                Logger.get().error("Something prevented town block claim on town creation. Resident returned null.");
                return Prompt.END_OF_CONVERSATION;
            }

            try {
                TownyDataUtil.addPlayerAndSteward(player, steward);
                TownCommand.newTown(player, townName, resident, false);
            } catch (TownyException e) {
                player.sendMessage(ColorParser.of("<red>Something went wrong with town creation. Please contact staff if this persists.").build());
                Logger.get().error(e.getMessage());
                TownyDataUtil.removePlayerAndSteward(player);
                return Prompt.END_OF_CONVERSATION;
            }

            // STEWARDS SPECIFIC
//            player.sendMessage(ColorParser.of("<green>The town was created!").build());
//
//            TownMetaData.setBankLimit(town, Cfg.get().getInt("treasurer.limit.level-0"));
//            TownMetaData.setArchitect(town, steward);
//
//            steward.setTownUUID(town.getUUID());
//            steward.setTownBlock(townBlock);
//
//            steward.getSettler().getNpc().getOrAddTrait(StewardTrait.class).hire();
//            steward.getSettler().getNpc().getOrAddTrait(StewardTrait.class).setTownBlock(townBlock);
//            steward.getSettler().getNpc().getOrAddTrait(StewardTrait.class).setTownUUID(town.getUUID());

            return Prompt.END_OF_CONVERSATION;
        }
    };

}
