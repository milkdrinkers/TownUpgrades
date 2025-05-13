package io.github.milkdrinkers.stewards.towny;

import io.github.milkdrinkers.stewards.steward.Steward;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TownyDataUtil {

    private static final Map<UUID, UUID> playerStewardMap = new HashMap<>();

    public static Map<UUID, UUID> getUuidSet() {
        return playerStewardMap;
    }

    public static UUID getStewardUUID(UUID playerUUID) {
        return playerStewardMap.get(playerUUID);
    }

    public static UUID getStewardUUID(Player player) {
        return getStewardUUID(player.getUniqueId());
    }

    public static void addPlayerAndSteward(UUID playerUUID, UUID stewardUUID) { // TODO potentially clarify method names
        playerStewardMap.put(playerUUID, stewardUUID);
    }

    public static void addPlayerAndSteward(Player player, Steward steward) {
        addPlayerAndSteward(player.getUniqueId(), steward.getSettler().getNpc().getUniqueId()); // TODO convenience method steward.getUniqueId
    }

    public static void removePlayerAndSteward(UUID playerUUID) {
        playerStewardMap.remove(playerUUID);
    }

    public static void removePlayerAndSteward(Player player) {
        removePlayerAndSteward(player.getUniqueId());
    }

    public static boolean isStewardCreatedTown(UUID playerUUID) {
        return playerStewardMap.containsKey(playerUUID);
    }

    public static boolean isStewardCreatedTown(Player player) {
        return isStewardCreatedTown(player.getUniqueId());
    }
}