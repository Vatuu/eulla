package dev.vatuu.eulla.extensions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

public interface MinecraftClientAccessor {

    void setFramebuffer(Framebuffer buffer);

    static MinecraftClientAccessor get(MinecraftClient client) {
        return (MinecraftClientAccessor)client;
    }
}
