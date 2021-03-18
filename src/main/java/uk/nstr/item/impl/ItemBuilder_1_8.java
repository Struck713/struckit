package uk.nstr.item.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.nstr.item.ItemBuilder;

public class ItemBuilder_1_8 extends ItemBuilder {

    public ItemBuilder_1_8(Material material) {
        super(material);
    }

    public ItemBuilder_1_8(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        // deprecated in 1.12 but will work for older versions of the game (1.8)
        modifyMeta(itemMeta -> itemMeta.spigot().setUnbreakable(unbreakable));
        return this;
    }
}
