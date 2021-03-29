package uk.nstr.item.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import uk.nstr.item.ItemBuilder;
import uk.nstr.util.ReflectionUtil;
import uk.nstr.util.VersionUtil;

import java.lang.reflect.Method;

public class ItemBuilder_1_8 extends ItemBuilder {

    public ItemBuilder_1_8(Material material) {
        super(material);
    }

    public ItemBuilder_1_8(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        //copy to NMS stack
        Class craftItemStackClass = ReflectionUtil.getClass(VersionUtil.getCraftBukkit() + ".inventory.CraftItemStack");
        Method copyNMSStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asNMSCopy", ItemStack.class);
        Object nmsStack = ReflectionUtil.invokeStatic(copyNMSStackMethod, this.getItemStack());
        Class nmsStackClass = nmsStack.getClass();

        //get enchantment
        Class enchantmentClass = ReflectionUtil.getClass(VersionUtil.getMinecraftServer() + ".Enchantment");
        Method getEnchantByNameMethod = ReflectionUtil.getMethod(enchantmentClass, "getById", int.class);
        Object enchantmentObject = ReflectionUtil.invokeStatic(getEnchantByNameMethod, enchantment.getId());

        Method addEnchantment = ReflectionUtil.getMethod(nmsStackClass, "addEnchantment", enchantmentClass, int.class);
        ReflectionUtil.invoke(nmsStack, addEnchantment, enchantmentObject, level);

        // copy it back
        Method copyBukkitStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asBukkitCopy", nmsStackClass);
        this.setItemStack((ItemStack) ReflectionUtil.invokeStatic(copyBukkitStackMethod, nmsStack));
        return this;
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        // deprecated in 1.12 but will work for older versions of the game (1.8)
        modifyMeta(itemMeta -> itemMeta.spigot().setUnbreakable(unbreakable));
        return this;
    }
}
