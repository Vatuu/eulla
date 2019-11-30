package dev.vatuu.eulla;

import dev.vatuu.eulla.gui.TestScreen;
import dev.vatuu.eulla.portals.PortalTargetCamera;
import dev.vatuu.eulla.portals.WorldPortal;
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

	public WorldPortal testPortal;

	private static FabricKeyBinding testScreen;

	@Override
	public void onInitialize() {
		INSTANCE = this;

		testScreen = FabricKeyBinding.Builder.create(
				new Identifier(MOD_ID, "test"),
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				"EuLLA"
		).build();

		ClientTickCallback.EVENT.register(e ->
		{
			if(testScreen.isPressed()) MinecraftClient.getInstance().openScreen(new TestScreen(null));
		});
	}
}
