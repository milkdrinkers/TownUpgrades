package io.github.milkdrinkers.stewards.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.settlers.api.settler.Companion;
import io.github.milkdrinkers.settlers.api.settler.SettlerBuilder;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.conversation.CreateTownConversation;
import io.github.milkdrinkers.stewards.exception.InvalidStewardException;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.steward.StewardLookup;
import io.github.milkdrinkers.stewards.trait.StewardTrait;
import io.github.milkdrinkers.stewards.utility.Appearance;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.SkinTrait;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class StewardBaseGui {

    public static Gui createBaseGui(Steward steward, Player player) {
        Stewards plugin = Stewards.getInstance();

        Gui gui = Gui.gui().title(Component.text("Steward")).rows(5).create();
        gui.disableItemDrop()
            .disableItemPlace()
            .disableItemSwap()
            .disableItemTake();

        populateBorders(gui);
        populateButtons(gui, steward, player);

        if (steward.getStewardType() == plugin.getStewardTypeHandler().getStewardTypeRegistry().getType(plugin.getStewardTypeHandler().ARCHITECT_ID)) {
            if (steward.getTownUUID() == null)
                populateArchitectNoTownButtons(gui, steward, player);
            else
                populateArchitectTownButtons(gui, steward, player);
        }


        return gui;
    }

    private static void populateBorders(Gui gui) {
        ItemStack borderItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = borderItem.getItemMeta();
        meta.customName(Component.empty());
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        borderItem.setItemMeta(meta);

        gui.getFiller().fillBorder(ItemBuilder.from(borderItem).asGuiItem());
    }

    private static void populateButtons(Gui gui, Steward steward, Player player) {
        ItemStack exitItem = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exitItem.getItemMeta();
        exitMeta.customName(ColorParser.of("<dark_red>Exit").build());
        exitMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        exitItem.setItemMeta(exitMeta);

        gui.setItem(5, 9, ItemBuilder.from(exitItem).asGuiItem(event -> gui.close(player)));


        ItemStack followItem = new ItemStack(Material.PAPER); // TODO: Placeholder item
        ItemMeta followMeta = followItem.getItemMeta();


        if (steward.getSettler().getNpc().getTraitNullable(StewardTrait.class).isFollowing()) {
            followMeta.customName(ColorParser.of("<green>Stop following!").build());
        } else {
            followMeta.customName(ColorParser.of("<green>Follow me!").build());
        }

        followMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        followItem.setItemMeta(followMeta);

        gui.setItem(5, 5, ItemBuilder.from(followItem).asGuiItem(event ->  {
            StewardTrait trait = steward.getSettler().getNpc().getTraitNullable(StewardTrait.class);
            if (trait == null) return;

            if (trait.isFollowing()) {
                ConfirmMoveGui.createGui(steward, player).open(player);
            } else {
                steward.getSettler().getNpc().getNavigator().setTarget(player, false);
                trait.setFollowing(true);
                gui.close(player);
            }
        }));
    }

    private static void populateArchitectNoTownButtons(Gui gui, Steward steward, Player player) {
        ItemStack townItem = new ItemStack(Material.PAPER);
        ItemMeta townMeta = townItem.getItemMeta();
        townMeta.customName(ColorParser.of("<green>Create town").build()); // TODO lore that shows cost for creating town
        townMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        townItem.setItemMeta(townMeta);

        gui.setItem(3, 5, ItemBuilder.from(townItem).asGuiItem(event -> {
            gui.close(player);
            ConversationFactory factory = new ConversationFactory(Stewards.getInstance()).withPrefix(CreateTownConversation.getPrefix).withLocalEcho(false);
            factory.withFirstPrompt(CreateTownConversation.getNewTownPrompt(steward)).buildConversation(player).begin();
        }));
    }

    private static void populateArchitectTownButtons(Gui gui, Steward steward, Player player) { // TODO Check if town has banker and/or bailiff
        ItemStack treasurerItem = new ItemStack(Material.PAPER);
        ItemMeta treasurerMeta = treasurerItem.getItemMeta();
        treasurerMeta.customName(ColorParser.of("<green>Hire the Treasurer").build());
        treasurerMeta.lore(List.of(
            ColorParser.of("<grey>The Treasurer costs " + 1 + "⊚ to hire.").build(),
            ColorParser.of("<grey>The Treasurer will allow you to increase your town bank limit.").build()));
        treasurerMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        treasurerItem.setItemMeta(treasurerMeta);

        ItemStack bailiffItem = new ItemStack(Material.PAPER);
        ItemMeta bailiffMeta = bailiffItem.getItemMeta();
        bailiffMeta.customName(ColorParser.of("<green>Hire the Bailiff").build());
        bailiffMeta.lore(List.of(
            ColorParser.of("<grey>The Bailiff costs " + 1 + "⊚ to hire.").build(),
            ColorParser.of("<grey>The Bailiff will add additional claims to your town.").build())); // TODO translations and config stuff
        bailiffMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        bailiffItem.setItemMeta(bailiffMeta);

        ItemStack portmasterItem = new ItemStack(Material.PAPER);
        ItemMeta portmasterMeta = portmasterItem.getItemMeta();
        portmasterMeta.customName(ColorParser.of("<green>Hire the Port Master").build());
        portmasterMeta.lore(List.of(
            ColorParser.of("<grey>The Port Master costs " + 1 + "⊚ to hire.").build(),
            ColorParser.of("<grey>The Port Master will allow you to create a port.").build()));
        portmasterMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        portmasterItem.setItemMeta(portmasterMeta);

        ItemStack stablemasterItem = new ItemStack(Material.PAPER);
        ItemMeta stablemasterMeta = stablemasterItem.getItemMeta();
        stablemasterMeta.customName(ColorParser.of("<green>Hire the Stable Master").build());
        stablemasterMeta.lore(List.of(
            ColorParser.of("<grey>The Stable Master costs " + 1 + "⊚ to hire.").build(),
            ColorParser.of("<grey>The Stable Master will allow you to create a carriage station.").build())); // TODO translations and config stuff
        stablemasterMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        stablemasterItem.setItemMeta(stablemasterMeta);

        gui.setItem(3, 2, ItemBuilder.from(treasurerItem).asGuiItem(e -> {
            createSteward(Stewards.getInstance().getStewardTypeHandler().TREASURER_ID, player);
            gui.close(player);
        }));

        gui.setItem(3, 4, ItemBuilder.from(bailiffItem).asGuiItem(e -> {
            createSteward(Stewards.getInstance().getStewardTypeHandler().BAILIFF_ID, player);
            gui.close(player);
        }));

        gui.setItem(3, 6, ItemBuilder.from(portmasterItem).asGuiItem(e -> {
            createSteward(Stewards.getInstance().getStewardTypeHandler().PORTMASTER_ID, player);
            gui.close(player);
        }));

        gui.setItem(3, 8, ItemBuilder.from(stablemasterItem).asGuiItem(e -> {
            createSteward(Stewards.getInstance().getStewardTypeHandler().STABLEMASTER_ID, player);
            gui.close(player);
        }));
    }

    private static void createSteward(String stewardTypeId, Player player) { // TODO handle cost
        try {
            boolean female = Math.random() > 0.5;
            String name = Appearance.getMaleName();
            if (female)
                name = Appearance.getFemaleName();

            Companion settler = new SettlerBuilder()
                .setName(name)
                .setLocation((player).getLocation().add(Appearance.randomInt(5), 0, Appearance.randomInt(5))) // TODO NOT THIS LOL
                .createCompanion();

            Steward steward = Steward.builder()
                .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(stewardTypeId))
                .setDailyUpkeepCost(0)
                .setIsEnabled(true)
                .setIsHidden(false)
                .setLevel(1)
                .setSettler(settler)
                .build();

            steward.getSettler().getNpc().getOrAddTrait(StewardTrait.class);

            HologramTrait hologramTrait = steward.getSettler().getNpc().getOrAddTrait(HologramTrait.class);
            hologramTrait.addLine(steward.getStewardType().getName());

            if (female) {
                Appearance.applyFemaleStewardSkin(steward);
            } else {
                Appearance.applyMaleStewardSkin(steward);
            }

            StewardLookup.get().registerSteward(steward);

            settler.spawn();
        } catch (InvalidStewardException e) {
            throw new RuntimeException(e);
        }
    }
}
