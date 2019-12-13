package dev.vatuu.eulla.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public final class GlUtils {

    public static FloatBuffer FLOAT_BUFFER = BufferUtils.createFloatBuffer(16);

    public static void enableStencilTest(){
        GL11.glEnable(GL11.GL_STENCIL_TEST);
    }

    public static void disableStencilTest(){
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }
}
