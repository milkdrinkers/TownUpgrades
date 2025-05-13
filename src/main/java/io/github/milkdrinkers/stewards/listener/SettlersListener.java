package io.github.milkdrinkers.stewards.listener;

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

        // If the Steward doesn't have at least one of these traits, something is wrong.
        if (e.getSettler().getNpc().hasTrait(ArchitectTrait.class)) { // TODO destroy settler if not hired on load?
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

}
