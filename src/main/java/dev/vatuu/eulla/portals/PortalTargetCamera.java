package dev.vatuu.eulla.portals;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

public class PortalTargetCamera extends Camera {

    private Vec3d portalPos;
    private float portalPitch, portalYaw;

    public PortalTargetCamera(Vec3d position, float pitch, float yaw) {
        this.portalPos = position;
        this.portalPitch = pitch;
        this.portalYaw = yaw;
        this.setPos(position);
        this.setRotation(pitch, yaw);
    }

    public void addToPosition(Vec3d pos) {
        this.setPos(portalPos.add(pos));
    }

    public Vec3d getCameraPos() {
        return this.portalPos;
    }
}
