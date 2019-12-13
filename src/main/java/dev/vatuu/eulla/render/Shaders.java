package dev.vatuu.eulla.render;

import dev.vatuu.eulla.WorldPortals;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class Shaders {

    public static int PORTAL_SHADER;

    public static void init() {
        PORTAL_SHADER = 0;
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public Identifier getFabricId() { return new Identifier(WorldPortals.MOD_ID, "shaders"); }

            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                return CompletableFuture.runAsync(() -> {
                    if(PORTAL_SHADER != 0) GL30.glDeleteProgram(PORTAL_SHADER);
                    PORTAL_SHADER = loadShader(manager, "portal_shader");
                }, applyExecutor).thenCompose(x -> synchronizer.whenPrepared(null));
            }
        });
    }

    private static int loadShader(ResourceManager manager, String id) {
        String vshs = "";
        String fshs = "";
        try(InputStream in = manager.getResource(new Identifier(WorldPortals.MOD_ID, "shaders/" + id + ".vert")).getInputStream()){
            vshs = IOUtils.toString(in, StandardCharsets.UTF_8);
        }catch(IOException e) { e.printStackTrace(); }
        try(InputStream in = manager.getResource(new Identifier(WorldPortals.MOD_ID, "shaders/" + id + ".frag")).getInputStream()){
            vshs = IOUtils.toString(in, StandardCharsets.UTF_8);
        }catch(IOException e) { e.printStackTrace(); }

        int vsh = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        int fsh = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        int prog = GL30.glCreateProgram();

        while(true){
            GL30.glShaderSource(vsh, vshs);
            GL30.glShaderSource(fsh, fshs);

            GL30.glCompileShader(vsh);
            if(GL30.glGetShaderi(vsh, GL30.GL_COMPILE_STATUS) == GL11.GL_FALSE){
                System.out.println("Failed to compile Vertex Shader \"" + id + "\"");
                break;
            }
            GL30.glCompileShader(fsh);
            if(GL30.glGetShaderi(fsh, GL30.GL_COMPILE_STATUS) == GL11.GL_FALSE){
                System.out.println("Failed to compile Fragment Shader \"" + id + "\"");
                break;
            }

            GL30.glAttachShader(prog, vsh);
            GL30.glAttachShader(prog, fsh);
            GL30.glLinkProgram(prog);

            if(GL30.glGetProgrami(prog, GL30.GL_LINK_STATUS) == GL11.GL_FALSE){
                System.out.println("Failed to link Program \"" + id + "\"");
                break;
            }

            GL30.glDeleteShader(vsh);
            GL30.glDeleteShader(fsh);
            return prog;
        }

        GL30.glDeleteShader(vsh);
        GL30.glDeleteShader(fsh);
        GL30.glDeleteProgram(prog);
        return 0;
    }
}
