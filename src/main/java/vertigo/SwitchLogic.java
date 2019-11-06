package vertigo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwitchLogic {

    //helper function
    private static boolean stringContainsItemFromList(String inputStr, ArrayList<String> items) {
        return items.parallelStream().anyMatch(inputStr::contains);
    }

    //list of potential slots
    private ArrayList<Integer> potSlots = new ArrayList<Integer>();

    public ArrayList<Integer> rescueItems(PlayerEntity player) {

        //Beds
        ArrayList<Item> beds = new ArrayList<Item>(Arrays.asList(Items.BLACK_BED, Items.BLUE_BED, Items.BROWN_BED,
                Items.CYAN_BED, Items.GREEN_BED, Items.GRAY_BED, Items.LIGHT_BLUE_BED, Items.LIGHT_GRAY_BED,
                Items.LIME_BED, Items.MAGENTA_BED, Items.ORANGE_BED, Items.PINK_BED, Items.PURPLE_BED, Items.RED_BED,
                Items.WHITE_BED, Items.YELLOW_BED
        ));

        //Lists of item slots
        ArrayList<Integer> water = new ArrayList<Integer>();
        ArrayList<Integer> cobweb = new ArrayList<Integer>();
        ArrayList<Integer> pearl = new ArrayList<Integer>();
        ArrayList<Integer> slime = new ArrayList<Integer>();
        ArrayList<Integer> hay = new ArrayList<Integer>();
        ArrayList<Integer> bed = new ArrayList<Integer>();
        ArrayList<Integer> totem = new ArrayList<Integer>();

        //Get HotBar Slots of effective items
        List<ItemStack> hotbar = player.inventory.main.subList(0, 9);
        for (int i=0; i<9; i++) {
            Item item = hotbar.get(i).getItem();

            //Check if item will work on the block, if it can check what tool it is and add it to it's list
            //ineffective on logs for some reason if (item.isEffectiveOn(block)) {
                if (Items.WATER_BUCKET.equals(item)) {
                    water.add(i);
                } else if (Items.TOTEM_OF_UNDYING.equals(item)) {
                    totem.add(i);
                } else if (Items.ENDER_PEARL.equals(item)) {
                    pearl.add(i);
                } else if (Items.COBWEB.equals(item)) {
                    cobweb.add(i);
                } else if (Items.SLIME_BLOCK.equals(item)) {
                    slime.add(i);
                } else if (Items.HAY_BLOCK.equals(item)) {
                    hay.add(i);
                } else if (beds.contains(item)) {
                    bed.add(i);
                }
        }

        if (!water.isEmpty()) {
            potSlots.add(water.get(0));
            return potSlots;
        } else if (!totem.isEmpty()) {
            potSlots.add(totem.get(0));
            return potSlots;
        } else if (!pearl.isEmpty()) {
            potSlots.add(pearl.get(0));
            return potSlots;
        } else if (!slime.isEmpty()) {
            potSlots.add(slime.get(0));
            return potSlots;
        } else if (!cobweb.isEmpty()) {
            potSlots.add(cobweb.get(0));
            return potSlots;
        } else if (!hay.isEmpty()) {
            potSlots.add(hay.get(0));
            return potSlots;
        } else if (!bed.isEmpty()) {
            potSlots.add(bed.get(0));
            return potSlots;
        }

        return potSlots;

    }

    public int changeSlot(ArrayList<Integer> slots, PlayerEntity player) {
        int currentSlot = player.inventory.selectedSlot;
        if (slots.isEmpty()) {
            return -1;
        } else {
            if (slots.get(0) == currentSlot) {
                System.out.println("No need to change slot");
                return 0;
            }
                //Loop over it since scrollinhotbar only moves one pos
                for (int i = Math.abs(currentSlot - slots.get(0)); i > 0; i--){
                    player.inventory.scrollInHotbar(currentSlot - slots.get(0));
                }

                //player.inventory.selectedSlot = potSlots.get(0);
                return 1;
        }
    }

}
