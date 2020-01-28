package dev.vatuu.eulla.render;

import java.nio.FloatBuffer;

import dev.vatuu.eulla.extensions.Matrix4fExt;
import dev.vatuu.eulla.extensions.mixin.Matrix4fMixin;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class GlUtils {

    public static FloatBuffer FLOAT_BUFFER = BufferUtils.createFloatBuffer(16);

    public static void enableStencilTest() {
        GL11.glEnable(GL11.GL_STENCIL_TEST);
    }

    public static void disableStencilTest() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    private static Vec3d[] getFrustumPoints(Matrix4f projection, MatrixStack model){
        Matrix4f mvpMatrix = projection.copy();
        mvpMatrix.multiply(model.peek().getModel());
        mvpMatrix.invert();
        Vec3d[] coords = new Vec3d[8];
        Vector4f[] points = new Vector4f[] {
                new Vector4f(1, 1, 1, 1),
                new Vector4f(-1, 1, 1, 1),
                new Vector4f(1, -1, 1, 1),
                new Vector4f(-1, -1, 1, 1),
                new Vector4f(1, 1, -1, 1),
                new Vector4f(-1, 1, -1, 1),
                new Vector4f(1, -1, -1, 1),
                new Vector4f(-1, -1, -1, 1)
        };
        for(int i = 0; i < points.length; i++){
            Vector4f p = points[i];
            p.transform(mvpMatrix);
            float w = p.getW();
            coords[i] = new Vec3d(p.getX() / w, p.getY() / w, p.getZ() / w);
        }
        return coords;
    }

    public static void drawFrustum(Vec3d position, Matrix4f projection, MatrixStack model, float red, float green, float blue){
        Vec3d[] points = getFrustumPoints(projection, model);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();
        b.begin(GL30.GL_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for(int i = 0; i < points.length; i++)
            b.vertex(points[i].x, points[i].y, points[i].z).color(red, green, blue, 1.0F).next();
        t.draw();
    }

    public static double[] multiplyWithMatrix(double[] vec4d, Matrix4f matrix) {
        return ((Matrix4fExt)((Object)matrix)).multiply4D(vec4d);
    }
}
