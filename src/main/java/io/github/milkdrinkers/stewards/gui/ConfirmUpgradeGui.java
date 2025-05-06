package io.github.milkdrinkers.stewards.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.trait.StewardTrait;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfirmUpgradeGui {

    public static Gui createGui(Steward steward, Player player, Steward upgradeSteward, int cost) {
        Gui gui = Gui.gui().title(Component.text("Upgrade " + upgradeSteward.getStewardType().getName()))
            .type(GuiType.HOPPER)
            .create();

        gui.disableItemDrop()
            .disableItemPlace()
            .disableItemSwap()
            .disableItemTake();

        populateButtons(gui, steward, player, upgradeSteward, cost);

        return gui;
    }

    private static void populateButtons(Gui gui, Steward steward, Player player, Steward upgradeSteward, int cost) {
        ItemStack upgradeItem = new ItemStack(Material.PAPER);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        upgradeMeta.displayName(ColorParser.of("<green>Upgrade " + upgradeSteward.getStewardType().getName()).build());
        upgradeMeta.lore(List.of(ColorParser.of("<grey>Upgrading costs " + cost + "⊚.").build()));
        upgradeMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        upgradeItem.setItemMeta(upgradeMeta);

        ItemStack backItem = new ItemStack(Material.PAPER);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.displayName(ColorParser.of("<red>Back").build());
        backMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        backItem.setItemMeta(backMeta);

        gui.setItem(1, 2, ItemBuilder.from(upgradeItem).asGuiItem(e -> {
            if (upgradeSteward.getSettler().getNpc().getTraitNullable(StewardTrait.class).levelUp()) {
                upgradeSteward.levelUp();
                player.sendMessage(ColorParser.of("<green>You have upgraded your steward to level " + upgradeSteward.getLevel() + "!").build());
            } else {
                player.sendMessage(ColorParser.of("<red>Something went wrong, the steward couldn't be upgraded.").build());
            }
            gui.close(player);
        }));

        gui.setItem(1, 4, ItemBuilder.from(backItem).asGuiItem(e -> {
            StewardBaseGui.createBaseGui(steward, player).open(player);
        }));
    }

}
