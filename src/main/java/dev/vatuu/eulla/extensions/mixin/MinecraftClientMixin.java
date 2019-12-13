package dev.vatuu.eulla.extensions.mixin;

import dev.vatuu.eulla.render.WorldPortalRenderer;
import dev.vatuu.eulla.extensions.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientAccessor {

    @Shadow @Final @Mutable private Framebuffer framebuffer;


    @Override
    public void setFramebuffer(Framebuffer buffer) {
        this.framebuffer = buffer;
    }


}
