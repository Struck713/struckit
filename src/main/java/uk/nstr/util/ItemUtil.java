package uk.nstr.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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

        //get some classes
        Class nbtTagStringClass = ReflectionUtil.getClass(VersionUtil.getMinecraftServer() + ".NBTTagString");
        Constructor nbtTagStringConstructor = ReflectionUtil.getConstructor(nbtTagStringClass, String.class);
        Object nbtTagString = ReflectionUtil.invoke(nbtTagStringConstructor, value);

        //copy to NMS stack
        Object nmsStack = ItemUtil.getAsNMSStack(itemStack);
        Class nmsStackClass = nmsStack.getClass();

        //get the NBTTag
        Object nbtTagCompound = ItemUtil.getNBTTagCompound(nmsStack);
        Class nbtTagCompoundClass = nbtTagCompound.getClass();

        //set a key - value pair on the tag
        Method set = ReflectionUtil.getMethod(nbtTagCompoundClass, "set", String.class, nbtTagStringClass);
        ReflectionUtil.invoke(nbtTagCompound, set, value, nbtTagString);

        //save the new tag to the NMS stack
        Method save = ReflectionUtil.getMethod(nmsStackClass, "save", nbtTagCompound.getClass());
        ReflectionUtil.invoke(nmsStack, save, nbtTagCompound);

        //copy back to bukkit stack
        return ItemUtil.getAsBukkitStack(nmsStack);
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

        if (!ItemUtil.hasNBT(itemStack, key)) {
            return null;
        }

        Object nmsStack = ItemUtil.getAsNMSStack(itemStack);
        Object nbtTagCompound = ItemUtil.getNBTTagCompound(nmsStack);
        Class nbtTagCompoundClass = nbtTagCompound.getClass();

        Method getString = ReflectionUtil.getMethod(nbtTagCompoundClass, "getString", String.class);
        return (String) ReflectionUtil.invoke(nbtTagCompound, getString, key);
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
        Object nmsStack = ItemUtil.getAsNMSStack(itemStack);
        Object nbtTagCompound = ItemUtil.getNBTTagCompound(nmsStack);
        Class nbtTagCompoundClass = nbtTagCompound.getClass();

        Method hasKey = ReflectionUtil.getMethod(nbtTagCompoundClass, "hasKey", String.class);
        boolean ret = (Boolean)ReflectionUtil.invoke(nbtTagCompound, hasKey, key);

        if (nbtTagCompound == null || !ret) {
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

    protected static Object getAsNMSStack(ItemStack itemStack) {
        Class craftItemStackClass = ReflectionUtil.getClass(VersionUtil.getCraftBukkit() + ".inventory.CraftItemStack");
        Method copyNMSStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asNMSCopy", ItemStack.class);
        Object nmsStack = ReflectionUtil.invokeStatic(copyNMSStackMethod, itemStack);
        return nmsStack;
    }

    protected static Object getNBTTagCompound(Object nmsStack) {
        Method getTag = ReflectionUtil.getMethod(nmsStack.getClass(), "getTag");
        return ReflectionUtil.invoke(nmsStack, getTag);
    }

    protected static ItemStack getAsBukkitStack(Object nmsStack) {
        Class craftItemStackClass = ReflectionUtil.getClass(VersionUtil.getCraftBukkit() + ".inventory.CraftItemStack");
        Method copyBukkitStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asBukkitCopy", nmsStack.getClass());
        return (ItemStack) ReflectionUtil.invokeStatic(copyBukkitStackMethod, nmsStack);
    }
    
}
