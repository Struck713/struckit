package uk.nstr.item.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.nstr.item.ItemBuilder;

public class ItemBuilder_1_8_PLUS extends ItemBuilder {

    public ItemBuilder_1_8_PLUS(Material material) {
        super(material);
    }

    public ItemBuilder_1_8_PLUS(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        modifyMeta(itemMeta -> itemMeta.setUnbreakable(unbreakable));
        return this;
    }

}
