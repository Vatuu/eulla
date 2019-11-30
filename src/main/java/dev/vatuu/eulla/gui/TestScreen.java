package dev.vatuu.eulla.gui;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.eulla.portals.PortalTargetCamera;
import dev.vatuu.eulla.portals.WorldPortal;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class TestScreen extends Screen {

    private Screen prev;

    private WorldPortal wp;
    private Framebuffer fb;

    public TestScreen(Screen prev){
        super(new TranslatableText("test_screen"));
        this.prev = prev;
    }

    public void init(){
        wp = new WorldPortal(
                new PortalTargetCamera(new Vec3d(0, 64, 0), 0, 0),
                new Vec3d(0, 32, 0),
                1,
                2,
                false);
        this.fb = wp.getRenderedTexture(true);
        fb.resize(this.width / 2, this.height / 2, false);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        drawCenteredString(this.font, "EuLLA Framebuffer View", this.width / 2, 40, 16777215);
        renderBackground();
        fb.beginRead();
        blit(width / 2 - (fb.textureWidth / 2), height / 2 - (fb.textureHeight / 2) , 0, 0, fb.textureWidth, fb.textureHeight, fb.textureWidth, fb.textureHeight);
        fb.endRead();
        super.render(mouseX, mouseY, delta);
    }
}
