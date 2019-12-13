package dev.vatuu.eulla.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.eulla.WorldPortals;
import dev.vatuu.eulla.portals.WorldPortal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class WorldPortalRenderer {

    private int mvpUniformLocation;
    private int texUniformLocation;
    private int attributePos;

    public WorldPortalRenderer(){
        mvpUniformLocation = GlStateManager.getUniformLocation(Shaders.PORTAL_SHADER, "mvp");
        texUniformLocation = GlStateManager.getUniformLocation(Shaders.PORTAL_SHADER, "portal");
        attributePos = GlStateManager.getAttribLocation(Shaders.PORTAL_SHADER, "pos");
    }

    public void render(Matrix4f projection, MatrixStack model){
        WorldPortals.INSTANCE.getPortalManager().getPortals().forEach(p -> render(p, projection, model) );
    }

    private void render(WorldPortal portal, Matrix4f projection, MatrixStack model){
        Framebuffer portalView = portal.getView();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        IntBuffer intBuffer = BufferUtils.createIntBuffer(1);

        int vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL30.glUseProgram(Shaders.PORTAL_SHADER);

        model.push();

        Vec3d camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().negate();
        model.translate(camPos.x, camPos.y, camPos.z);
        model.translate(portal.getPosition().x, portal.getPosition().y, portal.getPosition().z);
        model.multiply(new Quaternion(new Vector3f(1, 0, 0), portal.getYaw(), true));
        model.multiply(new Quaternion(new Vector3f(0, 1, 0), portal.getPitch(), true));
        model.scale(portal.getWidth(), portal.getHeight(), 1);

        Matrix4f mvp = projection.copy();
        mvp.multiply(model.peek().getModel());
        buffer.clear();
        mvp.writeToBuffer(buffer);
        buffer.rewind();
        RenderSystem.glUniformMatrix4(mvpUniformLocation, false, buffer);

        portalView.beginRead();
        intBuffer.clear();
        intBuffer.put(0);
        intBuffer.rewind();
        RenderSystem.glUniform1(texUniformLocation, intBuffer );

        float[] points = new float[] {
                -1, -1, 0,
                1, -1, 0,
                1, 1, 0,
                -1, 1, 0
        };
        buffer.put(points);

        buffer.rewind();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vbo, GL30.GL_DYNAMIC_DRAW);
        GL30.glVertexAttribPointer(attributePos, 3, GL30.GL_FLOAT, false, 3, 0);
        GL30.glDrawArrays(GL30.GL_QUADS, 0, 4);

        model.pop();
        portalView.endRead();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glDeleteBuffers(vbo);
        GL30.glUseProgram(0);
    }
}
