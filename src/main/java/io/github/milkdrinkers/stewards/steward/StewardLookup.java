package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.settlers.api.settler.AbstractSettler;
import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;
import net.citizensnpcs.api.npc.NPC;

import java.util.HashMap;
import java.util.UUID;

public class StewardLookup implements Reloadable {

    private final Stewards plugin;
    private final HashMap<UUID, Steward> settlerStewardHashmap = new HashMap<>();

    public static StewardLookup get() {
        return Stewards.getInstance().getStewardLookup();
    }

    public StewardLookup(Stewards plugin) {
        this.plugin = plugin;
    }

    public Steward getStewardByUuid(UUID uuid) {
        return settlerStewardHashmap.get(uuid);
    }

    public Steward getStewardByNpc(NPC npc) {
        return getStewardByUuid(npc.getUniqueId());
    }

    public Steward getStewardBySettler(AbstractSettler settler) {
        return getStewardByNpc(settler.getNpc());
    }

    public void registerSteward(Steward steward) {
        settlerStewardHashmap.put(steward.getSettler().getNpc().getUniqueId(), steward);
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
