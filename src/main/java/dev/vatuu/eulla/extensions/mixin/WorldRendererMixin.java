package dev.vatuu.eulla.extensions.mixin;

import dev.vatuu.eulla.extensions.WorldRendererExt;
import dev.vatuu.eulla.render.WorldPortalRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererExt {

    private WorldPortalRenderer portalRenderer;
    private boolean isRenderingPlayer;

    @Inject(at = @At(value = "RETURN"), method = "<init>")
    private void init(MinecraftClient client, BufferBuilderStorage bufferBuilderStorage, CallbackInfo info) {
        this.isRenderingPlayer = true;
        this.portalRenderer = new WorldPortalRenderer();
    }

    @Inject(at = @At(value = "HEAD"), method = "canDrawEntityOutlines", cancellable = true)
    public void canDrawEntityOutlines(CallbackInfoReturnable info) {
        if (isRenderingPlayer) return;
        info.setReturnValue(false);
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=blockentities", shift = At.Shift.BEFORE), method = "render", locals = LocalCapture.CAPTURE_FAILHARD)
    public void render(MatrixStack model, float tickDelta, long limitTime, boolean outline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmap, Matrix4f projection, CallbackInfo info, Profiler profiler) {
        if (isRenderingPlayer) {
            profiler.push("eulla");
            this.portalRenderer.render(projection, model);
            profiler.pop();
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo ci) {
        portalRenderer.destroy();
    }

    @Override
    @Unique
    public WorldPortalRenderer getWorldPortalRenderer() {
        return portalRenderer;
    }

    @Override
    @Unique
    public void setPlayerRendering(boolean yes) {
        isRenderingPlayer = yes;
    }
}
