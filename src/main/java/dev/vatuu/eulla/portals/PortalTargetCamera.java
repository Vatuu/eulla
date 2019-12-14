package dev.vatuu.eulla.portals;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

public class PortalTargetCamera extends Camera {

    public PortalTargetCamera(Vec3d position, float pitch, float yaw) {
        this.setPos(position);
        this.setRotation(pitch, yaw);
    }

}
