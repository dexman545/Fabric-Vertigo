package vertigo;

import com.google.common.eventbus.Subscribe;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
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
                GLFW.GLFW_KEY_F,
                "Vertigo"
        ).build();
        MinecraftClient mc = MinecraftClient.getInstance();
        VertigoLogic vl = new VertigoLogic();
        KeyBindingRegistry.INSTANCE.register(keyBinding);
        ClientTickCallback.EVENT.register(e ->
        {
            if(keyBinding.isPressed()) {

                vl.attemptLifeSave(e.player);

                //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(1), true);

                //vl.isPlayerDanger(mc.player);
                //System.out.println(mc.player.getPos());

            }
        });

    }

}

