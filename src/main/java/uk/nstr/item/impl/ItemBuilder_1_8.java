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
        String enchantmentName = enchantment.getName();
        String basePackage = VersionUtil.getBasePackage();

        //copy to NMS stack
        Class craftItemStackClass = ReflectionUtil.getClass(basePackage + ".inventory.CraftItemStack");
        Method copyNMSStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asNMSCopy", ItemStack.class);
        Object nmsStack = ReflectionUtil.invoke(copyNMSStackMethod, this.getItemStack());
        Class nmsStackClass = nmsStack.getClass();

        //get enchantment
        Class enchantmentClass = ReflectionUtil.getClass(basePackage + "Enchantment");
        Method getEnchantByNameMethod = ReflectionUtil.getMethod(enchantmentClass, "getByName", String.class);
        Object enchantmentObject = ReflectionUtil.invoke(getEnchantByNameMethod, enchantmentName);

        Method addEnchantment = ReflectionUtil.getMethod(nmsStackClass, "addEnchantment", enchantmentObject.getClass(), Integer.class);
        ReflectionUtil.invoke(addEnchantment, enchantmentObject, level);

        // copy it back
        Method copyBukkitStackMethod = ReflectionUtil.getMethod(craftItemStackClass, "asBukkitCopy", nmsStackClass);
        this.setItemStack((ItemStack) ReflectionUtil.invoke(copyBukkitStackMethod, nmsStack));

        return this;
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        // deprecated in 1.12 but will work for older versions of the game (1.8)
        modifyMeta(itemMeta -> itemMeta.spigot().setUnbreakable(unbreakable));
        return this;
    }
}
