package dev.vatuu.eulla.portals;

import java.util.HashSet;
import java.util.Set;

public class WorldPortalManager {

    private Set<WorldPortal> portals;

    public WorldPortalManager() {
        this.portals = new HashSet<>();
    }

    public Set<WorldPortal> getPortals() {
        return portals;
    }

    public void addPortal(WorldPortal portal) {
        boolean exists = false;
        for (WorldPortal p : portals) {
            if (p.getId().equals(portal.getId())) {
                exists = true;
                break;
            }
        }
        if (exists) {
            System.out.println("Portal already exists. Skipping.");
        } else {
            portals.add(portal);
        }
    }
}