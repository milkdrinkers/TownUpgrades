package io.github.milkdrinkers.stewards.gui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import io.github.alathra.alathraports.api.PortsAPI;
import io.github.alathra.alathraports.core.carriagestations.CarriageStation;
import io.github.alathra.alathraports.core.ports.Port;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.towny.TownMetaData;
import io.github.milkdrinkers.stewards.trait.StewardTrait;
import io.github.milkdrinkers.stewards.utility.Cfg;
import io.github.milkdrinkers.stewards.utility.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfirmUpgradeGui {

    public static Gui createGui(Steward steward, Player player, int cost) {
        Gui gui = Gui.gui().title(Component.text("Upgrade " + steward.getStewardType().getName()))
            .type(GuiType.HOPPER)
            .create();

        gui.disableItemDrop()
            .disableItemPlace()
            .disableItemSwap()
            .disableItemTake();

        populateButtons(gui, steward, player, cost);

        return gui;
    }

    private static void populateButtons(Gui gui, Steward steward, Player player, int cost) {
        ItemStack upgradeItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        upgradeMeta.displayName(ColorParser.of("<green>Upgrade " + steward.getStewardType().getName()).build());
        upgradeMeta.lore(List.of(ColorParser.of("<grey>Upgrading costs " + cost + "⊚.").build()));
        upgradeMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        upgradeItem.setItemMeta(upgradeMeta);

        ItemStack backItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.displayName(ColorParser.of("<red>Back").build());
        backMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        backItem.setItemMeta(backMeta);

        gui.setItem(1, 2, ItemBuilder.from(upgradeItem).asGuiItem(e -> {
            if (!checkTownBank(steward, player, cost)) {
                player.sendMessage(ColorParser.of("<red>You don't have enough money in your town bank to upgrade this steward.").build());
                gui.close(player);
                return;
            }

            Town town = TownyAPI.getInstance().getTown(player);
            if (town == null) {
                player.sendMessage(ColorParser.of("<red>Something went wrong, the steward couldn't be upgraded.").build());
                Logger.get().error("Something went wrong when checking town for {}. Town was null.", player.getName());
                gui.close(player);
                return;
            }

            if (steward.getSettler().getNpc().getTraitNullable(StewardTrait.class).levelUp()) {
                steward.levelUp();
                player.sendMessage(ColorParser.of("<green>You have upgraded your steward to level " + steward.getLevel() + "!").build());

                if (steward.getStewardType().getId().equals(Stewards.getInstance().getStewardTypeHandler().BAILIFF_ID)) {

                    town.addBonusBlocks((Cfg.get().getInt("bailiff.claims.level-" + steward.getLevel()) -
                        Cfg.get().getInt("bailiff.claims.level-" + (steward.getLevel() - 1))));

                } else if (steward.getStewardType().getId().equals(Stewards.getInstance().getStewardTypeHandler().PORTMASTER_ID)) {

                    Port port = PortsAPI.getPortFromTown(town);
                    if (port == null) { // This shouldn't be possible
                        player.sendMessage(ColorParser.of("<red>Something went wrong, the port couldn't be upgraded.").build());
                        gui.close(player);
                        return;
                    }
                    PortsAPI.upgradePort(port);

                } else if (steward.getStewardType().getId().equals(Stewards.getInstance().getStewardTypeHandler().STABLEMASTER_ID)) {

                    CarriageStation station = PortsAPI.getCarriageStationFromTown(town);
                    if (station == null) {
                        player.sendMessage(ColorParser.of("<red>Something went wrong, the carriage station couldn't be upgraded.").build());
                        gui.close(player);
                        return;
                    }
                    PortsAPI.upgradeCarriageStation(station);

                } else if (steward.getStewardType().getId().equals(Stewards.getInstance().getStewardTypeHandler().TREASURER_ID)) {

                    TownMetaData.setBankLimit(town, Cfg.get().getInt("treasurer.limit.level-" + steward.getLevel()));

                }

                town.getAccount().withdraw(cost, "Stewards: Upgraded " + steward.getStewardType().getName());

            } else {
                player.sendMessage(ColorParser.of("<red>Something went wrong, the steward couldn't be upgraded.").build());
            }
            gui.close(player);
        }));

        gui.setItem(1, 4, ItemBuilder.from(backItem).asGuiItem(e -> {
            StewardBaseGui.createBaseGui(steward, player).open(player);
        }));
    }

    private static boolean checkTownBank(Steward steward, Player player, int cost) {
        Town town = TownyAPI.getInstance().getTown(player);
        if (town == null) { // Shouldn't be possible, considering the player was allowed to interact with the steward.
            Logger.get().error("Something went wrong when checking town bank for {}. Town was null.", player.getName());
            return false;
        }

        return town.getAccount().canPayFromHoldings(cost);
    }

}
