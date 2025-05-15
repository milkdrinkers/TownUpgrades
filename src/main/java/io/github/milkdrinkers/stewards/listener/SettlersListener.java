package io.github.milkdrinkers.stewards.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import io.github.alathra.alathraports.api.PortsAPI;
import io.github.milkdrinkers.settlers.api.enums.RemoveReason;
import io.github.milkdrinkers.settlers.api.event.settler.lifecycle.SettlerRemoveEvent;
import io.github.milkdrinkers.settlers.api.event.settler.lifetime.spawning.SettlerSpawnEvent;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.exception.InvalidStewardException;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.steward.StewardLookup;
import io.github.milkdrinkers.stewards.trait.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SettlersListener implements Listener {

    @EventHandler
    public void onSettlerSpawn(SettlerSpawnEvent e) {
        if (!e.getSettler().getNpc().hasTrait(StewardTrait.class)) return;

        if (StewardLookup.get().getSteward(e.getSettler()) != null) return;


        StewardTrait stewardTrait = e.getSettler().getNpc().getOrAddTrait(StewardTrait.class);

        // This theoretically shouldn't change anything, as the anchor location should always update as the NPC moves
        stewardTrait.setAnchorLocation(e.getLocation());

        if (!stewardTrait.isHired()) e.getSettler().delete();

        // If the Steward doesn't have at least one of these traits, something is wrong.
        if (e.getSettler().getNpc().hasTrait(ArchitectTrait.class)) {
            try {
                Steward steward = Steward.builder()
                    .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(
                        Stewards.getInstance().getStewardTypeHandler().ARCHITECT_ID))
                    .setDailyUpkeepCost(0)
                    .setIsEnabled(true)
                    .setIsHidden(false)
                    .setLevel(stewardTrait.getLevel())
                    .setSettler(e.getSettler())
                    .setTownBlock(stewardTrait.getTownBlock())
                    .build();

                StewardLookup.get().registerSteward(steward);
            } catch (InvalidStewardException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSettler().getNpc().hasTrait(BailiffTrait.class)) {
            try {
                Steward steward = Steward.builder()
                    .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(
                        Stewards.getInstance().getStewardTypeHandler().BAILIFF_ID))
                    .setDailyUpkeepCost(0)
                    .setIsEnabled(true)
                    .setIsHidden(false)
                    .setLevel(stewardTrait.getLevel())
                    .setSettler(e.getSettler())
                    .setTownBlock(stewardTrait.getTownBlock())
                    .build();

                StewardLookup.get().registerSteward(steward);
            } catch (InvalidStewardException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSettler().getNpc().hasTrait(PortmasterTrait.class)) {
            try {
                Steward steward = Steward.builder()
                    .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(
                        Stewards.getInstance().getStewardTypeHandler().PORTMASTER_ID))
                    .setDailyUpkeepCost(0)
                    .setIsEnabled(true)
                    .setIsHidden(false)
                    .setLevel(stewardTrait.getLevel())
                    .setSettler(e.getSettler())
                    .setTownBlock(stewardTrait.getTownBlock())
                    .build();

                StewardLookup.get().registerSteward(steward);
            } catch (InvalidStewardException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSettler().getNpc().hasTrait(StablemasterTrait.class)) {
            try {
                Steward steward = Steward.builder()
                    .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(
                        Stewards.getInstance().getStewardTypeHandler().STABLEMASTER_ID))
                    .setDailyUpkeepCost(0)
                    .setIsEnabled(true)
                    .setIsHidden(false)
                    .setLevel(stewardTrait.getLevel())
                    .setSettler(e.getSettler())
                    .setTownBlock(stewardTrait.getTownBlock())
                    .build();

                StewardLookup.get().registerSteward(steward);
            } catch (InvalidStewardException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSettler().getNpc().hasTrait(TreasurerTrait.class)) {
            try {
                Steward steward = Steward.builder()
                    .setStewardType(Stewards.getInstance().getStewardTypeHandler().getStewardTypeRegistry().getType(
                        Stewards.getInstance().getStewardTypeHandler().TREASURER_ID))
                    .setDailyUpkeepCost(0)
                    .setIsEnabled(true)
                    .setIsHidden(false)
                    .setLevel(stewardTrait.getLevel())
                    .setSettler(e.getSettler())
                    .setTownBlock(stewardTrait.getTownBlock())
                    .build();

                StewardLookup.get().registerSteward(steward);
            } catch (InvalidStewardException ex) {
                throw new RuntimeException(ex);
            }
        }


    }

    @EventHandler
    public void onSettlerDelete(SettlerRemoveEvent e) {
        if (e.getReason() != RemoveReason.COMMAND) return;

        if (!e.getSettler().getNpc().hasTrait(StewardTrait.class)) return;

        // This should only run if a steward was deleted using commands, in which case the port/carriage station would not get removed - so we remove it.
        if (e.getSettler().getNpc().hasTrait(PortmasterTrait.class)) {
            PortsAPI.deleteAbstractPort(PortsAPI.getPortFromTown(TownyAPI.getInstance().getTown(StewardLookup.get().getSteward(e.getSettler()).getTownUUID())));
        } else if (e.getSettler().getNpc().hasTrait(StablemasterTrait.class)) {
            PortsAPI.deleteAbstractCarriageStation(PortsAPI.getCarriageStationFromTown(TownyAPI.getInstance().getTown(StewardLookup.get().getSteward(e.getSettler()).getTownUUID())));
        }
    }

}
