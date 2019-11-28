package vertigo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


public class Vertigo implements ModInitializer {

    //Keybinding
    private static FabricKeyBinding keyBinding;

    private boolean doVertigo = true;

    @Override
    public void onInitialize() {
        System.out.println("Vertigo Loaded");

        //vertigo toggle
        keyBinding = FabricKeyBinding.Builder.create(
                new Identifier("vertigo"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "Vertigo"
        ).build();
        MinecraftClient mc = MinecraftClient.getInstance();
        VertigoLogic vl = new VertigoLogic();
        KeyBindingRegistry.INSTANCE.register(keyBinding);
        ClientTickCallback.EVENT.register(e ->
        {
            if(keyBinding.isPressed()) {
                vl.attemptLifeSave(e.player);
            }
        });

    }

}

