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
    private static FabricKeyBinding removePortal;

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

        removePortal = FabricKeyBinding.Builder.create(
                new Identifier(MOD_ID, "remove"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "EuLLA"
        ).build();

        ClientTickCallback.EVENT.register(e -> {
            if (testScreen.isPressed()) MinecraftClient.getInstance().openScreen(new TestScreen(MinecraftClient.getInstance().currentScreen));
            if (spawnPortal.isPressed()) {
                if (e.world != null) {
                    portalManager.addPortal(new WorldPortal(
                            new PortalTargetCamera(new Vec3d(0, 54, -9), 0, 0),
                            new Vec3d(-1d, 66d, 422),
                            4,
                            4,
                            180,
                            0,
                            "testportal"
                    ));
                } else {
                    System.out.println("World is null!");
                }
            }
            if (removePortal.isPressed()) portalManager.destroyPortal("testportal");
        });

        this.portalManager = new WorldPortalManager();
    }

    public WorldPortalManager getPortalManager() {
        return portalManager;
    }

}
