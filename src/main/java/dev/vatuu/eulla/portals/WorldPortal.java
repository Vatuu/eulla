package dev.vatuu.eulla.portals;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public class WorldPortal {

    private Framebuffer view;

    private PortalTargetCamera targetCamera;
    private Camera observerCamera;

    private String id;
    private Vec3d position;
    private float width, height;

    public WorldPortal(PortalTargetCamera target, Vec3d position, float width, float height, boolean staticCamera){
        this.view = new Framebuffer(
                MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
                MinecraftClient.getInstance().getWindow().getFramebufferHeight(),
                true,
                MinecraftClient.IS_SYSTEM_MAC);
        this.view.setClearColor(1f, 1f, 1f, 1f);

        this.targetCamera = target;
        this.position = position;
        this.width = width;
        this.height = height;
        this.id = UUID.randomUUID().toString();
    }

    public void renderPortalView(){
        view.beginWrite(true);

        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.ortho(0, view.textureWidth, view.textureHeight, 0, -1000, 1000);
        RenderSystem.pushMatrix();;
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder buffer = t.getBuffer();
        buffer.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);
        buffer.vertex(250, 0, 0).color(1.0f, 0, 0, 1.0f).next();
        buffer.vertex(0, 250, 0).color(0, 1.0f, 0, 1.0f).next();
        buffer.vertex(500, 250, 0).color(0, 0, 1.0f, 1.0f).next();
        t.draw();

        view.endWrite();

        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.popMatrix();
    }

    public Framebuffer getRenderedTexture(boolean forceRerender){
        if(forceRerender) this.renderPortalView();
        return view;
    }
}
