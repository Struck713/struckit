package uk.nstr.util;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

//TODO IMPLEMENT VERSIONS SUPPORT
public class ItemUtil {

    /**
     * Writes a String to an ItemStack's metadata.
     *
     * @param itemStack the ItemStack
     * @param key the key
     * @param value the value
     * @return the modified ItemStack
     */
    public static ItemStack writeNBT(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();
        compound.set(key, new NBTTagString(value));
        nmsStack.save(compound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    /**
     * Reads a String from the ItemStack's metadata,
     * depending on the key.
     *
     * @param itemStack the ItemStack
     * @param key the key
     * @return the value at that key
     */
    public static String readNBT(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();

        if (compound == null || !compound.hasKey(key)) {
            return null;
        }

        return compound.getString(key);
    }

    /**
     * Checks if the ItemStack has that key-value pair
     * in its metadata.
     *
     * @param itemStack the ItemStack
     * @param key the key
     * @return if true, it has the value
     *         if false, it does not
     */
    public static boolean hasNBT(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();

        if (compound == null || !compound.hasKey(key)) {
            return false;
        }

        return true;
    }

    /**
     * Counts the amount of drops a block would have of
     * a specific type, if it were broken.
     *
     * @param block the Block
     * @param dropped the specific type
     * @return the amount of drops
     */
    public static int countDrops(Block block, Material dropped) {
        return (int)block.getDrops().stream().filter(stack -> stack.getType().equals(dropped)).count();
    }
    
}
