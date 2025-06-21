package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.settlers.api.settler.AbstractSettler;
import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StewardLookup implements Reloadable {

    private final Stewards plugin;
    private final HashMap<UUID, Steward> settlerStewardHashmap = new HashMap<>();
    private final HashMap<UUID, Steward> stewardFollowingPlayerHashmap = new HashMap<>();
    private final HashMap<UUID, Steward> architectMap = new HashMap<>();

    public static StewardLookup get() {
        return Stewards.getInstance().getStewardLookup();
    }

    public StewardLookup(Stewards plugin) {
        this.plugin = plugin;
    }

    public void removeStewardFollowingPlayer(UUID uuid) {
        stewardFollowingPlayerHashmap.remove(uuid);
    }

    public void removeStewardFollowingPlayer(Player player) {
        removeStewardFollowingPlayer(player.getUniqueId());
    }

    public void setStewardFollowingPlayer(UUID uuid, Steward steward) {
        stewardFollowingPlayerHashmap.put(uuid, steward);
    }

    public void setStewardFollowingPlayer(Player player, Steward steward) {
        setStewardFollowingPlayer(player.getUniqueId(), steward);
    }

    public boolean isPlayerFollowed(UUID uuid) {
        return stewardFollowingPlayerHashmap.containsKey(uuid);
    }

    public boolean isPlayerFollowed(Player player) {
        return isPlayerFollowed(player.getUniqueId());
    }

    public Steward getStewardFollwingPlayer(UUID uuid) {
        return stewardFollowingPlayerHashmap.get(uuid);
    }

    public Steward getStewardFollowingPlayer(Player player) {
        return getStewardFollwingPlayer(player.getUniqueId());
    }

    public Steward getSteward(UUID uuid) {
        return settlerStewardHashmap.get(uuid);
    }

    public Steward getSteward(NPC npc) {
        return getSteward(npc.getUniqueId());
    }

    public Steward getSteward(AbstractSettler settler) {
        return getSteward(settler.getNpc());
    }

    public void registerSteward(Steward steward) {
        settlerStewardHashmap.put(steward.getSettler().getNpc().getUniqueId(), steward);
    }

    public void unregisterSteward(UUID uuid) {
        settlerStewardHashmap.remove(uuid);
    }

    public void unregisterSteward(NPC npc) {
        unregisterSteward(npc.getUniqueId());
    }

    public void unregisterSteward(AbstractSettler settler) {
        unregisterSteward(settler.getNpc());
    }

    public void unregisterSteward(Steward steward) {
        unregisterSteward(steward.getSettler());
    }

    public void setArchitect(UUID uuid, Steward steward) {
        architectMap.put(uuid, steward);
    }

    public void setArchitect(Player player, Steward steward) {
        setArchitect(player.getUniqueId(), steward);
    }

    public void clearHasArchitect(UUID uuid) {
        architectMap.remove(uuid);
    }

    public void clearHasArchitect(Player player) {
        clearHasArchitect(player.getUniqueId());
    }

    public boolean hasArchitect(UUID uuid) {
        return architectMap.containsKey(uuid);
    }

    public boolean hasArchitect(Player player) {
        return hasArchitect(player.getUniqueId());
    }

    public Steward getArchitect(UUID uuid) {
        return architectMap.get(uuid);
    }

    public Steward getArchitect(Player player) {
        return getArchitect(player.getUniqueId());
    }

    @Override
    public void onLoad(Stewards plugin) {

    }

    @Override
    public void onEnable(Stewards plugin) {

    }

    @Override
    public void onDisable(Stewards plugin) {

    }
}
