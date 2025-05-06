package io.github.milkdrinkers.stewards.trait;

import io.github.milkdrinkers.settlers.api.SettlersAPI;
import io.github.milkdrinkers.stewards.gui.StewardBaseGui;
import io.github.milkdrinkers.stewards.steward.Steward;
import io.github.milkdrinkers.stewards.steward.StewardLookup;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class StewardTrait extends Trait {

    protected StewardTrait() {
        super("steward");
    }

    @Persist boolean following = false;
    Player followingPLayer;
    @Persist boolean female; // Stored to keep track of whether the skin and name is "male" or "female"
    @Persist Location anchorLocation;
    @Persist int level;
    @Persist boolean hired = false;

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

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Player getFollowingPlayer() {
        return followingPLayer;
    }

    public void setFollowingPlayer(Player player) {
        this.followingPLayer = player;
    }

    public boolean isHired() {
        return hired;
    }

    public void setHired(boolean hired) {
        this.hired = hired;
    }

    public void hire() {
        this.hired = true;
    }

    /**
     * Attempts to level up the steward.
     *
     * @return false if max level is reached and true if steward leveled up.
     */
    public boolean levelUp() {
        if (StewardLookup.get().getSteward(this.getNPC()).getStewardType().getMaxLevel() == this.level)
            return false;
        level++;
        return true;
    }

    public void load(DataKey key) {
        following = key.getBoolean("following");
        anchorLocation = (Location) key.getRaw("anchorlocation");
        female = key.getBoolean("female");
    }

    public void save(DataKey key) {
        key.setBoolean("following", following);
        key.setRaw("anchorlocation", anchorLocation);
        key.setBoolean("female", female);
    }

    @EventHandler
    public void click(NPCRightClickEvent e) {
        if (e.getNPC() != this.getNPC()) return;

        if (e.getClicker().isSneaking()) return;

        if (following && e.getClicker() != this.getFollowingPlayer()) return;

        if (!following && StewardLookup.get().isPlayerFollowed(e.getClicker())) return;

        Steward steward = StewardLookup.get().getSteward(SettlersAPI.getSettler(e.getNPC()));
        if (steward == null) return;

        StewardBaseGui.createBaseGui(steward, e.getClicker()).open(e.getClicker());
    }

}
