package dev.vatuu.eulla.portals;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.vatuu.eulla.WorldPortals;
import dev.vatuu.eulla.render.GlUtils;
import dev.vatuu.eulla.render.Shaders;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class WorldPortal {

    private Framebuffer view;

    private PortalTargetCamera targetCamera;

    private String id;
    private Vec3d position;
    private int width, height;
    private float pitch, yaw;;
    private int fbWidth, fbHeight;
    private boolean needsRender;

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw, boolean staticCamera) {
        this(target, position, width, height, pitch, yaw, UUID.randomUUID().toString(), false);
    }

    public WorldPortal(PortalTargetCamera target, Vec3d position, int width, int height, float pitch, float yaw, String id, boolean staticCamera){
        this.targetCamera = target;
        this.position = position;
        this.width = width;
        this.height = height;
        this.pitch = pitch;
        this.yaw = yaw;
        this.id = id;
        this.needsRender = true;
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

        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0, fbWidth, 0, fbHeight, -100, 100);
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);

        //Render the World

        view.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.popMatrix();
    }

    public void render() {
        if (needsRender) {
            this.renderPortalView();
            needsRender = false;
        }
    }

    public void scheduleUpdate() {
        needsRender = true;
    }

    public void dispose() {
        if (view != null) view.delete();
    }

    public String getId() {
        return id;
    }
    public Vec3d getPosition() { return position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public float getPitch() { return pitch; }
    public float getYaw() { return yaw; }

}
