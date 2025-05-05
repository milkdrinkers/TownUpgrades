package io.github.milkdrinkers.stewards.trait;

import io.github.milkdrinkers.settlers.api.SettlersAPI;
import io.github.milkdrinkers.stewards.gui.StewardBaseGui;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.steward.StewardLookup;
import io.github.milkdrinkers.stewards.utility.Logger;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

public class StewardTrait extends Trait {

    protected StewardTrait() {
        super("steward");
    }

    @Persist boolean following = false;
    @Persist Location anchorLocation;

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public Location getAnchorLocation() {
        return anchorLocation;
    }

    public void setAnchorLocation(Location anchorLocation) {
        this.anchorLocation = anchorLocation;
    }

    public void load(DataKey key) {
        following = key.getBoolean("following");
        anchorLocation = (Location) key.getRaw("anchorlocation");
    }

    public void save(DataKey key) {
        key.setBoolean("following", following);
        key.setRaw("anchorlocation", anchorLocation);
    }

    @EventHandler
    public void click(NPCRightClickEvent e) {
        if (e.getNPC() != this.getNPC()) return;

        if (e.getClicker().isSneaking()) return;

        Steward steward = StewardLookup.get().getStewardBySettler(SettlersAPI.getSettler(e.getNPC()));
        if (steward == null) return;

        StewardBaseGui.createBaseGui(steward, e.getClicker()).open(e.getClicker());
    }

}
