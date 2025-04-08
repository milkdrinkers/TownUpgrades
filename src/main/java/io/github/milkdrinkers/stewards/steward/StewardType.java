package io.github.milkdrinkers.stewards.steward;

import io.github.milkdrinkers.stewards.exception.InvalidStewardTypeException;
import org.jetbrains.annotations.NotNull;

public class StewardType {

    private final String id;
    private final String name;
    private final int maxLevel;
    private final int minLevel;
    private final int startingLevel;
    private final String settlerPrefix;

    public StewardType(String id, String name, int maxLevel, int minLevel, int startingLevel, String settlerPrefix) {
        this.id = id;
        this.name = name;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
        this.startingLevel = startingLevel;
        this.settlerPrefix = settlerPrefix;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getStartingLevel() {
        return startingLevel;
    }

    public String getSettlerPrefix() {
        return settlerPrefix;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private int maxLevel;
        private int minLevel;
        private int startingLevel;
        private String settlerPrefix;

        public Builder setId(@NotNull String id) {
            this.id = id;
            return this;
        }

        public Builder setName(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder setMaxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public Builder setMinLevel(int minLevel) {
            this.minLevel = minLevel;
            return this;
        }

        public Builder setStartingLevel(int startingLevel) {
            this.startingLevel = startingLevel;
            return this;
        }

        public Builder setSettlerPrefix(@NotNull String settlerPrefix) {
            this.settlerPrefix = settlerPrefix;
            return this;
        }

        public StewardType build() throws InvalidStewardTypeException {

            // Check for levels less than 1
            if (maxLevel <= 0)
                maxLevel = 1;
            if (minLevel <= 0)
                minLevel = 1;
            if (startingLevel <= 0)
                startingLevel = 1;

            // Check for exceeding max level or below min level
            if (minLevel > maxLevel)
                minLevel = maxLevel;
            if (startingLevel > maxLevel)
                startingLevel = maxLevel;

            if (id == null) {
                throw new InvalidStewardTypeException("StewardType Id is null");
            }

            if (name == null) {
                throw new InvalidStewardTypeException("StewardType Name is null");
            }

            if (settlerPrefix == null) {
                throw new InvalidStewardTypeException("StewardType Settler Prefix is null");
            }

            return new StewardType(id, name, maxLevel, minLevel, startingLevel, settlerPrefix);
        }
    }
}
