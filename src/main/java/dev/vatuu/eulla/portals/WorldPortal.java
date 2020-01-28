package dev.vatuu.eulla.portals;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.eulla.PortalViewEntity;
import dev.vatuu.eulla.extensions.MinecraftClientAccessor;
import dev.vatuu.eulla.extensions.WorldRendererExt;
import dev.vatuu.eulla.render.GlUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL30;

import java.util.UUID;

public class WorldPortal {

    private final int CLIPPING_PLANE = GL30.GL_CLIP_PLANE0;

    private Framebuffer bufferView;
    private WorldRenderer renderer;

    private PortalTargetCamera targetCamera;

    private String id;
    private Vec3d position;
    private int width, height;
    private float pitch, yaw;
    private int fbWidth, fbHeight;

    private Vec3d portalNormal;

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw) {
        this(target, position, width, height, pitch, yaw, UUID.randomUUID().toString());
    }

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw, String id) {
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
        if (bufferView == null) {
            Window w = MinecraftClient.getInstance().getWindow();
            fbWidth = w.getWidth();
            fbHeight = w.getHeight();
            this.bufferView = new Framebuffer(
                fbWidth,
                fbHeight,
                true,
                MinecraftClient.IS_SYSTEM_MAC);
            this.bufferView.setClearColor(0.25f, 0.25f, 0.25f, 1f);
            bufferView.clear(true);
        }
        return this.bufferView;
    }

    public void renderPortalView(MatrixStack model, float delta) {
        Framebuffer buffer = getView();

        buffer.clear(false);
        buffer.beginWrite(true);

        MinecraftClient c = MinecraftClient.getInstance();
        boolean oldHidden = c.options.hudHidden;
        Entity oldCamera = c.cameraEntity;
        Framebuffer oldBuffer = c.getFramebuffer();

        Vec3d playerCam = c.gameRenderer.getCamera().getPos();
        Vec3d offset = playerCam.subtract(position);
        targetCamera.addToPosition(offset);

        PortalViewEntity entity = new PortalViewEntity(c.world, targetCamera);

        c.options.hudHidden = true;
        ((MinecraftClientAccessor) c).setFramebuffer(bufferView);
        c.setCameraEntity(entity);
        ((WorldRendererExt) renderer).setRenderingPortal(entity);

        RenderSystem.enableCull();

        GL30.glClipPlane(CLIPPING_PLANE, getClippingPlaneArray(pitch, yaw, model.peek().getModel()));
        GL30.glEnable(CLIPPING_PLANE);

        renderer.scheduleTerrainUpdate();

        renderer.render(
                model,
                delta,
                Util.getMeasuringTimeNano(),
                false,
                targetCamera,
                c.gameRenderer,
                c.gameRenderer.getLightmapTextureManager(),
                Matrix4f.viewboxMatrix(c.options.fov, fbWidth / fbHeight, 0.05f, c.options.viewDistance * 4.0f)
        );

        GL30.glDisable(CLIPPING_PLANE);

        RenderSystem.disableCull();

        c.options.hudHidden = oldHidden;
        ((MinecraftClientAccessor) c).setFramebuffer(oldBuffer);
        ((WorldRendererExt) renderer).setRenderingPortal(null);
        c.setCameraEntity(oldCamera);

        entity.kill();

        bufferView.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
    }

    public void dispose() {
        if (bufferView != null) bufferView.delete();
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

    private double[] getClippingPlaneArray(float pitch, float yaw, Matrix4f modelview) {
        Vec3d normal = new Vec3d(0, 0, -1).rotateX(pitch).rotateY(yaw);
        Vec3d camera = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        double[] ret = new double[] { normal.x, normal.y, normal.z, -normal.dotProduct(position.subtract(camera)) };
        return GlUtils.multiplyWithMatrix(ret, modelview);
    }
}
