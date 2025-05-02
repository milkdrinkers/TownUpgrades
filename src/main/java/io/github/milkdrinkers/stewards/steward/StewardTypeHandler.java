package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.stewards.Reloadable;
import io.github.milkdrinkers.stewards.Stewards;
import io.github.milkdrinkers.stewards.exception.InvalidStewardTypeException;

public class StewardTypeHandler implements Reloadable {

    private StewardTypeRegistry stewardTypeRegistry;

    public String ARCHITECT_ID = "architect";
    public String TREASURER_ID = "treasurer";
    public String BAILIFF_ID = "bailiff";

    @Override
    public void onLoad(Stewards plugin) {
        stewardTypeRegistry = new StewardTypeRegistry();
    }

    @Override
    public void onEnable(Stewards plugin) {
        stewardTypeRegistry.clear();
        registerStewardTypes();
    }

    @Override
    public void onDisable(Stewards plugin) {
        stewardTypeRegistry.clear();
    }

    public StewardTypeRegistry getStewardTypeRegistry() {
        return stewardTypeRegistry;
    }

    private void registerStewardTypes() {
        try {
            StewardType architect = StewardType.builder()
                .setId(ARCHITECT_ID)
                .setMaxLevel(1)
                .setMinLevel(1)
                .setName("Architect")
                .setStartingLevel(1)
                .setSettlerPrefix("Architect ") // TODO Translations
                .build();

            stewardTypeRegistry.register(architect);

            StewardType treasurer = StewardType.builder()
                .setId(TREASURER_ID)
                .setMaxLevel(7)
                .setMinLevel(1)
                .setName("Treasurer")
                .setStartingLevel(1)
                .setSettlerPrefix("Treasurer ") // TODO Translations
                .build();

            stewardTypeRegistry.register(treasurer);

            StewardType bailiff = StewardType.builder()
                .setId(BAILIFF_ID)
                .setMaxLevel(4)
                .setMinLevel(1)
                .setName("Bailiff")
                .setStartingLevel(1)
                .setSettlerPrefix("Bailiff ") // TODO Translations
                .build();

            stewardTypeRegistry.register(bailiff);
        } catch (InvalidStewardTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
