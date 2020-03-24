package dev.vatuu.eulla;

import dev.vatuu.eulla.portals.PortalTargetCamera;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
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
        this.setPos(target.getPos().x, target.getPos().y, target.getPos().z);
        this.setRotation(target.getYaw(), target.getPitch());
        this.lastRenderX = target.getPos().getX();
        this.lastRenderY = target.getPos().getY();
        this.lastRenderZ = target.getPos().getZ();
        this.prevX = target.getPos().getX();
        this.prevY = target.getPos().getY();
        this.prevZ = target.getPos().getZ();
        this.prevYaw = target.getYaw();
        this.prevPitch = target.getPitch();
        this.noClip = true;

        this.updatePosition(target.getPos().x, target.getPos().y, target.getPos().z);
        target.update(this.world, this, false, false, MinecraftClient.getInstance().getTickDelta());
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
        TYPE = FabricEntityTypeBuilder.create(EntityCategory.MISC, (EntityType.EntityFactory<PortalViewEntity>)(t,w)->null).disableSaving().disableSummon().size(EntityDimensions.fixed(0,0)).build();
    }
}
