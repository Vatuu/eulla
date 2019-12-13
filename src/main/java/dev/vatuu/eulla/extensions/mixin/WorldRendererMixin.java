package dev.vatuu.eulla.extensions.mixin;

import dev.vatuu.eulla.render.WorldPortalRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    private WorldPortalRenderer portalRenderer;

    @Inject(at = @At(value = "RETURN"), method = "<init>")
    private void init(MinecraftClient client, BufferBuilderStorage bufferBuilderStorage, CallbackInfo info){
        this.portalRenderer = new WorldPortalRenderer();
    }


    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=blockentities", shift = At.Shift.BEFORE), method = "render", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void render(MatrixStack model, float tickDelta, long limitTime, boolean outline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmap, Matrix4f projection, CallbackInfo info, Profiler profiler){
        profiler.push("eulla");
        this.portalRenderer.render(projection, model);
        profiler.pop();
    }
}
