package dev.vatuu.eulla;

import dev.vatuu.eulla.portals.PortalTargetCamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class PortalViewEntity extends Entity {

    private static EntityType<PortalViewEntity> TYPE;

    public PortalViewEntity(World w) {
        super(TYPE, w);
    }

    public PortalViewEntity(World w, PortalTargetCamera target) {
        super(TYPE, w);
        this.setPos(target.getPos().getX(), target.getPos().getY(), target.getPos().getZ());
        this.setRotation(target.getYaw(), target.getPitch());
    }

    public void writeCustomDataToTag(CompoundTag tag) {
    }

    public void readCustomDataFromTag(CompoundTag tag) {
    }

    public void initDataTracker() {
    }

    public Packet<?> createSpawnPacket() {
        return null;
    }

    static {
        TYPE = new EntityType<>((t, w) -> null, EntityCategory.MISC, false, false, false, false, EntityDimensions.fixed(0f, 0f));
    }
}
