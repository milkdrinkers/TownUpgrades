package io.github.milkdrinkers.stewards.listener;

import com.palmergames.adventure.text.event.HoverEvent;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.event.economy.TownPreTransactionEvent;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.economy.transaction.TransactionType;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.stewards.towny.TownMetaData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyListener implements Listener {

    @EventHandler
    public void onTownyDeposit(TownPreTransactionEvent e) {
        if (e.getTransaction().getType() != TransactionType.DEPOSIT) return;

        if (e.getTransaction().getSendingPlayer() == null) return;

        Town town = e.getTown();

        if ((e.getTransaction().getReceivingAccount().getHoldingBalance() + e.getTransaction().getAmount()) > TownMetaData.getBankLimit(town)) {
            e.getTransaction().getSendingPlayer().sendMessage(
                ColorParser.of("<red>You can't transfer that much money into your town bank. Your town bank limit is: "
                    + TownMetaData.getBankLimit(town)).build());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTownStatusScreen(TownStatusScreenEvent e) {
        com.palmergames.adventure.text.Component hoverComponent =
            com.palmergames.adventure.text.Component.text("Your banker is level " + 1 + ". To increase this limit, upgrade your banker.", NamedTextColor.GRAY); // TODO fetch level from steward

        com.palmergames.adventure.text.Component bankLimit = com.palmergames.adventure.text.Component.newline()
            .append(com.palmergames.adventure.text.Component.text("[", NamedTextColor.GRAY))
            .append(com.palmergames.adventure.text.Component.text("Stewards", NamedTextColor.DARK_GREEN)) // TODO hover more info
            .append(com.palmergames.adventure.text.Component.text("] ", NamedTextColor.GRAY))
            .append(com.palmergames.adventure.text.Component.text("Town bank limit: 10,000⊚.", NamedTextColor.WHITE)) // TODO fetch limit from level
            .hoverEvent(HoverEvent.showText(hoverComponent));

        e.getStatusScreen().addComponentOf("Stewards", bankLimit);
    }
}
