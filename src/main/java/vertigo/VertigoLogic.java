package vertigo;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.event.client.ClientTickCallback;

import net.minecraft.block.Blocks;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class VertigoLogic {
    private float damageReduction = 0;

    private int getEPF_Falling(PlayerEntity player){
        int EPF = 0;
        //EnchantmentHelper.getLevel(Enchantments.PROTECTION, player.getEquippedStack(EquipmentSlot.CHEST));

        for (ItemStack armor : player.inventory.armor){
            EPF += 2 * EnchantmentHelper.getLevel(Enchantments.PROTECTION, armor);
            EPF += 3 * EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, armor);
        }

        if (EPF > 20) {EPF = 20;}
        //int ffLvl = EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, player.getEquippedStack(EquipmentSlot.FEET));

        /*for (int i =0; i < player.inventory.armor.size(); i++) {
            for (int j = 0; j < player.inventory.armor.get(i).getEnchantments().size(); j++) {
                //System.out.println(player.inventory.armor.get(i).getEnchantments().get(j).toString());
                Object k = new JsonParser().parse(player.inventory.armor.get(i).getEnchantments().get(j).toString());
                JsonObject jo = (JsonObject) k;
                if (jo.get("id").toString().contains("feather_falling")) {
                    String lvlString = jo.get("lvl").toString().replace("s", "");
                    int lvl = Integer.parseInt(lvlString.replace("\"", ""));
                    EPF += lvl * 3;
                } else if (jo.get("id").toString().equals("minecraft:protection")) {
                    String lvlString = jo.get("lvl").toString().replace("s", "");
                    int lvl = Integer.parseInt(lvlString.replace("\"", ""));
                    EPF += lvl * 2;
                }
            }
        }*/
        return EPF;
    }

    public double getDamageAfterArmour(PlayerEntity player){
        if (!player.hasStatusEffect(StatusEffects.JUMP_BOOST)){
            double fallDamage = player.fallDistance - 3.0f;
            //System.out.println("O: "+fallDamage);
            if (!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                fallDamage = (float) Math.floor(fallDamage * (1 - (getEPF_Falling(player) / 25.0f)));
            } else {
                fallDamage = 0f;
            }
            return fallDamage;
        } else {
            int amp = player.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier();
            double fallDamage = player.fallDistance - (3.0f + amp);
            //System.out.println("O: "+fallDamage);
            if (!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                fallDamage = (float) Math.floor(fallDamage * (1 - (getEPF_Falling(player) / 25.0f)));
            } else {
                fallDamage = 0f;
            }
            return fallDamage;
        }
    }

    private boolean isPlayerInDanger(PlayerEntity player){
        //offset actual health to give player tim to save themselves
        double nDmg = (0.6 * player.getHealth()) - getDamageAfterArmour(player);
        if (nDmg <= 0.0) {return true;}

        return false;
    }

    private boolean click = true;
    public void clickSpam(PlayerEntity player){
        if (isPlayerInDanger(player)) {
            KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(1), click);
            click = !click;
            //System.out.println(click);
            KeyBinding.updatePressedStates();
            KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(1));
        }
    }

    public void attemptLifeSave(PlayerEntity player) {
        if (isPlayerInDanger(player)){

            player.pitch = 90f;

            SwitchLogic sl = new SwitchLogic();
            sl.changeSlot(sl.rescueItems(player), player);

            //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(1), true);
            BlockPos loc = player.getBlockPos();
            //System.out.println(loc.getY());


            int y = loc.getY();
            for (int i = 1; i <= 3; i++){
                if (!player.world.getBlockState(loc.offset(Direction.DOWN).add(0, -i, 0)).isAir() ||
                        player.world.getBlockState(loc.offset(Direction.DOWN).add(0, -i, 0)) != Blocks.WATER.getDefaultState()){
                    clickSpam(player);
                    ClientTickCallback.EVENT.register(e ->
                    {
                        clickSpam(player);
                    });
                    //KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(1), true);
                    //System.out.println("meh");
                }
            }

            System.out.println("Life in Danger!");
        } else {KeyBinding.setKeyPressed(InputUtil.Type.MOUSE.createFromCode(1), false);}
    }


}
