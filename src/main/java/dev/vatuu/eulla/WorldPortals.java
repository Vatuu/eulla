package dev.vatuu.eulla;

import dev.vatuu.eulla.gui.TestScreen;
import dev.vatuu.eulla.portals.PortalTargetCamera;
import dev.vatuu.eulla.portals.WorldPortal;
import dev.vatuu.eulla.portals.WorldPortalManager;
import dev.vatuu.eulla.render.Shaders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class WorldPortals implements ModInitializer {

    public static String MOD_ID = "eulla";
    public static WorldPortals INSTANCE;

    private static FabricKeyBinding testScreen;
    private static FabricKeyBinding spawnPortal;

    private WorldPortalManager portalManager;

    @Override
    public void onInitialize() {
        INSTANCE = this;

        Shaders.init();

        testScreen = FabricKeyBinding.Builder.create(
            new Identifier(MOD_ID, "test"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "EuLLA"
        ).build();

        spawnPortal = FabricKeyBinding.Builder.create(
            new Identifier(MOD_ID, "spawn"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "EuLLA"
        ).build();

        ClientTickCallback.EVENT.register(e -> {
            if (testScreen.isPressed()) MinecraftClient.getInstance().openScreen(new TestScreen(null));
            if (spawnPortal.isPressed()) {
                if (e.world != null) {
                    portalManager.addPortal(new WorldPortal(
                            new PortalTargetCamera(new Vec3d(0, 64, 0), 0, 0),
                            new Vec3d(123.5d, 68.5d, -156.5d),
                            3,
                            3,
                            90,
                            0,
                            "testportal",
                            false
                    ));
                } else {
                    System.out.println("World is null!");
                }
            }
        });

        this.portalManager = new WorldPortalManager();
    }

    public WorldPortalManager getPortalManager() {
        return portalManager;
    }

}
