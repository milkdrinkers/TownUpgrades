package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;

public class StewardTypeHandler implements Reloadable {

    private StewardTypeRegistry stewardTypeRegistry;

    @Override
    public void onLoad(Stewards plugin) {
        stewardTypeRegistry = new StewardTypeRegistry();
    }

    @Override
    public void onEnable(Stewards plugin) {

    }

    @Override
    public void onDisable(Stewards plugin) {
        stewardTypeRegistry.clear();
    }

    public StewardTypeRegistry getStewardTypeRegistry() {
        return stewardTypeRegistry;
    }
}
