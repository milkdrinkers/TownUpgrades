package io.github.milkdrinkers.stewards.gui;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.object.Town;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.conversation.CreateTownConversation;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.trait.StewardTrait;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StewardBaseGui {

    public static Gui createBaseGui(Steward steward, Player player) {
        Gui gui = Gui.gui().title(Component.text("Steward")).rows(5).create();
        gui.disableItemDrop()
            .disableItemPlace()
            .disableItemSwap()
            .disableItemTake();

        populateBorders(gui);
        populateButtons(gui, steward, player);

        if (steward.getStewardType() == Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType("test"))
            populateArchitectButtons(gui, steward, player);

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

    private static void populateArchitectButtons(Gui gui, Steward steward, Player player) {
        ItemStack townItem = new ItemStack(Material.PAPER);
        ItemMeta townMeta = townItem.getItemMeta();
        townMeta.customName(ColorParser.of("<green>Create town").build());
        townMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        townItem.setItemMeta(townMeta);

        gui.setItem(3, 5, ItemBuilder.from(townItem).asGuiItem(event -> {
            gui.close(player);
            ConversationFactory factory = new ConversationFactory(Stewards.getInstance()).withPrefix(CreateTownConversation.getPrefix).withLocalEcho(false);
            factory.withFirstPrompt(CreateTownConversation.newTownPrompt).buildConversation(player).begin();
        }));
    }
}
