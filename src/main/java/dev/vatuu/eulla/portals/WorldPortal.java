package dev.vatuu.eulla.portals;

import dev.vatuu.eulla.PortalViewEntity;
import dev.vatuu.eulla.extensions.MinecraftClientAccessor;
import dev.vatuu.eulla.extensions.WorldRendererExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class WorldPortal {

    private Framebuffer view;
    private WorldRenderer renderer;

    private PortalTargetCamera targetCamera;

    private String id;
    private Vec3d position;
    private int width, height;
    private float pitch, yaw;
    private int fbWidth, fbHeight;

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw, boolean staticCamera) {
        this(target, position, width, height, pitch, yaw, UUID.randomUUID().toString(), false);
    }

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw, String id, boolean staticCamera) {
        this.targetCamera = target;
        this.renderer = new WorldRenderer(MinecraftClient.getInstance(), new BufferBuilderStorage());
        renderer.setWorld(MinecraftClient.getInstance().world);
        this.position = position;
        this.width = width;
        this.height = height;
        this.pitch = pitch;
        this.yaw = yaw;
        this.id = id;
    }

    public Framebuffer getView() {
        if (view == null) {
            Window w = MinecraftClient.getInstance().getWindow();
            fbWidth = w.getWidth();
            fbHeight = w.getHeight();
            this.view = new Framebuffer(
                fbWidth,
                fbHeight,
                true,
                MinecraftClient.IS_SYSTEM_MAC);
            this.view.setClearColor(0.25f, 0.25f, 0.25f, 1f);
            view.clear(true);
        }
        return this.view;
    }

    public void renderPortalView() {
        Framebuffer view = getView();

        view.clear(false);
        view.beginWrite(true);

        MinecraftClient c = MinecraftClient.getInstance();
        boolean oldHidden = c.options.hudHidden;
        Entity oldCamera = c.cameraEntity;
        Framebuffer oldBuffer = c.getFramebuffer();

        PlayerEntity player = c.player;

        PortalViewEntity entity = new PortalViewEntity(c.world, targetCamera);

        MatrixStack projection = new MatrixStack();
        Matrix4f model = projection.peek().getModel();
        
        projection.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(targetCamera.getPitch() + player.pitch));
        projection.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(targetCamera.getYaw() + 180.0F + player.yaw));

        c.options.hudHidden = true;
        ((MinecraftClientAccessor) c).setFramebuffer(view);
        c.setCameraEntity(entity);
        ((WorldRendererExt) renderer).setRenderingPortal(entity);

        renderer.render(
                projection,
                c.getTickDelta(),
                Util.getMeasuringTimeNano(),
                false,
                targetCamera,
                c.gameRenderer,
                c.gameRenderer.getLightmapTextureManager(),
                model
        );

        c.options.hudHidden = oldHidden;
        ((MinecraftClientAccessor) c).setFramebuffer(oldBuffer);
        ((WorldRendererExt) renderer).setRenderingPortal(null);
        c.setCameraEntity(oldCamera);

        entity.kill();

        view.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
    }

    public void dispose() {
        if (view != null) view.delete();
    }

    public String getId() {
        return id;
    }

    public PortalTargetCamera getTarget() {
        return targetCamera;
    }

    public Vec3d getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

}
