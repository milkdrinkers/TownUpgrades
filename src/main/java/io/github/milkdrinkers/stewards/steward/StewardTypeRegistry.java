package io.github.milkdrinkers.stewards.steward;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StewardTypeRegistry {

    private final HashMap<String, StewardType> registry;

    public StewardTypeRegistry() {
        registry = new HashMap<>();
    }

    public void register(StewardType stewardType) {
        registry.put(stewardType.getId(), stewardType);
    }

    public Map<String, StewardType> getAll() {
        return Collections.unmodifiableMap(registry);
    }

    public Set<String> getKeys() {
        return Set.copyOf(registry.keySet());
    }

    public Set<StewardType> getValues() {
        return Set.copyOf(registry.values());
    }

    public StewardType getType(String id) {
        return registry.get(id);
    }

    public boolean isRegistered(String key) {
        return registry.containsKey(key);
    }

    public boolean isRegistered(StewardType stewardType) {
        return registry.containsKey(stewardType.getId());
    }

    protected void clear() {
        registry.clear();
    }
}
