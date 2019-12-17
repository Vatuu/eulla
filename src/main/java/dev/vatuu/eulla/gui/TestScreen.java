package dev.vatuu.eulla.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.eulla.WorldPortals;
import dev.vatuu.eulla.portals.PortalTargetCamera;
import dev.vatuu.eulla.portals.WorldPortal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.TranslatableText;
import org.lwjgl.opengl.GL11;

public class TestScreen extends Screen {

    private Screen prev;

    private WorldPortal wp;

    public TestScreen(Screen prev) {
        super(new TranslatableText("test_screen"));
        this.prev = prev;
    }

    public void init() {
        this.wp = WorldPortals.INSTANCE.getPortalManager().getPortal("testportal");
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        drawCenteredString(this.font, "EuLLA Framebuffer View", this.width / 2, 20, 0xffffff);

        if (wp != null) {
            drawCenteredString(this.font, "Current Portal: \"" + wp.getId() + "\"", this.width / 2, 60, 0xffffff);
            PortalTargetCamera c = wp.getTarget();
            drawCenteredString(this.font, "Target", width / 2, height - 50, 0xffffff);
            drawCenteredString(this.font, String.format("X %.2f | Y %.2f | Z %.2f ( %.2f | %.2f )", c.getPos().x, c.getPos().y, c.getPos().z, c.getPitch(), c.getYaw()), this.width / 2, height - 40, 0xffffff);

            Framebuffer fb = wp.getView();
            double scalefactor = 0.5 / MinecraftClient.getInstance().getWindow().getScaleFactor();

            RenderSystem.pushMatrix();
            RenderSystem.scaled(scalefactor, scalefactor, scalefactor);
            RenderSystem.enableTexture();
            fb.beginRead();
            int fbX = (int) (width / scalefactor) / 2 - fb.textureWidth / 2;
            int fbY = (int) (height / scalefactor) / 2 - fb.textureHeight / 2;
            drawTexturedQuad(fbX, fbY, fb.textureWidth, fb.textureHeight);
            fb.endRead();
            RenderSystem.popMatrix();
        } else {
            drawCenteredString(this.font, "No Portal available.", width / 2, height / 2, 0xffffff);
        }
        super.render(mouseX, mouseY, delta);
    }

    private void drawTexturedQuad(int x, int y, int width, int height) {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();
        b.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
        b.vertex(x + width, y, 0).texture(1, 1).next();
        b.vertex(x, y, 0).texture(0, 1).next();
        b.vertex(x, y + height, 0).texture(0, 0).next();
        b.vertex(x + width, y + height, 0).texture(1, 0).next();
        t.draw();
    }
}
