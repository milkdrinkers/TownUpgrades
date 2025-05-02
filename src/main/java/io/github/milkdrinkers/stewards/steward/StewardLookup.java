package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.settlers.api.settler.AbstractSettler;
import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;

import java.util.HashMap;

public class StewardLookup implements Reloadable {

    private final Stewards plugin;
    private final HashMap<AbstractSettler, Steward> settlerStewardHashmap = new HashMap<>();

    public static StewardLookup get() {
        return Stewards.getInstance().getStewardLookup();
    }

    public StewardLookup(Stewards plugin) {
        this.plugin = plugin;
    }

    public Steward getStewardBySettler(AbstractSettler settler) {
        return settlerStewardHashmap.get(settler);
    }

    public void registerSteward(Steward steward) {
        settlerStewardHashmap.put(steward.getSettler(), steward);
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
