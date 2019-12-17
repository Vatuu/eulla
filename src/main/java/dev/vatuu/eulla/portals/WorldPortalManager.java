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

    public WorldPortal getPortal(String id) {
        for (WorldPortal p : portals) {
            if (p.getId().equals(id))
                return p;
        }
        System.out.println("Unable to retrieve  \"" + id + "\"");
        return null;
    }

    public boolean destroyPortal(String id) {
        WorldPortal p = getPortal(id);
        if (p == null) {
            System.out.println("Unable to remove Portal \"" + id + "\".");
            return false;
        }
        p.dispose();
        portals.remove(p);
        System.out.println("Removed Portal \"" + id + "\".");
        return true;
    }
}