package uk.nstr.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.nstr.item.impl.ItemBuilder_1_8;
import uk.nstr.item.impl.ItemBuilder_1_8_PLUS;
import uk.nstr.util.ReflectionUtil;
import uk.nstr.util.TextUtil;
import uk.nstr.util.VersionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ItemBuilder {

    private static int VERSION = 0;

    public static ItemBuilder of(Material material) {
        if (VERSION == 0) {
            VERSION = VersionUtil.getSubVersion();
        }

        if (VERSION <= 18) {
            return new ItemBuilder_1_8(material);
        }

        return new ItemBuilder_1_8_PLUS(material);
    }

    public static ItemBuilder of(ItemStack itemStack) {
        if (VERSION == 0) {
            VERSION = VersionUtil.getSubVersion();
        }

        if (VERSION <= 18) {
            return new ItemBuilder_1_8(itemStack);
        }

        return new ItemBuilder_1_8_PLUS(itemStack);
    }

    private ItemStack itemStack;

    protected ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    protected ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name
     * @return this ItemBuilder
     */
    public ItemBuilder name(String name) {
        modifyMeta(itemMeta -> itemMeta.setDisplayName(
                TextUtil.color(name)
        ));
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore the lore
     * @return this ItemBuilder
     */
    public ItemBuilder lore(String lore) {
        lore = TextUtil.color(lore);

        String[] lineBreaks = lore.split("\n");
        List<String> loreList = Arrays.asList(lineBreaks);
        this.lore(loreList);
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lores the lore list
     * @return this ItemBuilder
     */
    public ItemBuilder lore(String... lores) {
        this.lore(Arrays.asList(lores));
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore the lore list
     * @return this ItemBuilder
     */
    public ItemBuilder lore(List<String> lore) {
        List<String> colored = lore.stream().map(TextUtil::color).collect(Collectors.toList());
        modifyMeta(itemMeta -> itemMeta.setLore(lore));
        return this;
    }

    /**
     * Sets the ItemFlags of the item.
     *
     * @param flags the ItemFlags
     * @return this ItemBuilder
     */
    public ItemBuilder itemFlags(ItemFlag... flags) {
        modifyMeta(itemMeta -> itemMeta.addItemFlags(flags));
        return this;
    }

    /**
     * Adds an enchantment to the item. There
     * is no need to check if the Enchantment
     * is safe to add or not, the ItemBuilder will
     * do that for you.
     *
     * @param enchantment the Enchantment
     * @param level the level
     * @return this ItemBuilder
     */
    public abstract ItemBuilder enchantment(Enchantment enchantment, int level);

    /**
     * Adds enchantments to the item
     * from a Map of Enchantment keys and
     * Integer values.
     *
     * @param enchants the Enchantment map
     * @return this ItemBuilder
     */
    public ItemBuilder enchantments(Map<Enchantment, Integer> enchants) {
        enchants.forEach((enchant, level) -> this.enchantment(enchant, level));
        return this;
    }

    /**
     * Sets the item to unbreakable.
     *
     * @param unbreakable if true, it will make the item unbreakable.
     *                    if false, it will make the item breakable.
     * @return this ItemBuilder
     */
    public abstract ItemBuilder unbreakable(boolean unbreakable);

//    public ItemBuilder nbt(String key, String value) {
//        this.itemStack = ItemUtil.writeNBT(this.itemStack, key, value);
//        return this;
//    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the amount
     * @return this ItemBuilder
     */
    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the data of the item. This
     * will only change the durability
     * in most newer versions.
     *
     * @param data the data
     * @return this ItemBuilder
     */
    public ItemBuilder data(short data) {
        this.itemStack.setDurability(data);
        return this;
    }

    /**
     * Sets the item to glow.
     */
    public ItemBuilder glow(Enchantment enchantment) {
        this.enchantment(Enchantment.DURABILITY, 1);
        this.itemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Builds a Bukkit ItemStack.
     *
     * @return the ItemStack
     */
    public ItemStack build() {
        return this.itemStack;
    }

    protected void modifyMeta(Consumer<ItemMeta> itemMetaConsumer) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMetaConsumer.accept(itemMeta);
        this.itemStack.setItemMeta(itemMeta);
    }

    protected ItemStack getItemStack() {
        return this.itemStack;
    }

    protected void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

}