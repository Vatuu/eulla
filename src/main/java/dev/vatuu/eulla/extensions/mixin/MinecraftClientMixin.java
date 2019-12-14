package dev.vatuu.eulla.extensions.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

import dev.vatuu.eulla.extensions.MinecraftClientAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientAccessor {

    @Shadow @Final @Mutable private Framebuffer framebuffer;

    @Override
    public void setFramebuffer(Framebuffer buffer) {
        this.framebuffer = buffer;
    }

}
