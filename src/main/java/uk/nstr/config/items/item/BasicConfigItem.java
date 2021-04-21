package uk.nstr.config.items.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import uk.nstr.item.ItemBuilder;
import uk.nstr.util.TextUtil;
import uk.nstr.util.tuple.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BasicConfigItem extends AbstractConfigItem {

    @Getter private ItemBuilder builder;
    @Getter private String name;
    @Getter private List<String> lore;
    @Getter private boolean glow;

    public BasicConfigItem(Material material,
                           String name,
                           List<String> lore, Map<Enchantment, Integer> enchantments,
                           short data,
                           int amount,
                           boolean glow) {
        this.name = name;
        this.lore = lore;
        this.glow = glow;

        this.builder = ItemBuilder.of(material)
                .amount(amount)
                .data(data)
                .enchantments(enchantments);

    }

    @Override
    public ItemStack toItemStack(Pair<String, String>... placeholders) {
        this.builder.name(TextUtil.color(this.name));
        this.builder.lore(this.getLore(placeholders));

        if (this.glow) {
            this.builder.enchantment(Enchantment.DAMAGE_ARTHROPODS, 1)
                    .itemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        return this.builder.build();
    }

    public List<String> getLore(Pair<String, String>... placeholders) {
        List<String> placeholderLore = new LinkedList<>();
        this.lore.forEach(value -> {
            if (!value.contains("%")) {
                placeholderLore.add(value);
                return;
            }
            for (Pair<String, String> placeholder : placeholders) {
                value = value.replaceAll(placeholder.getKey(), placeholder.getValue());
            }

            placeholderLore.add(value);
        });
        return placeholderLore;
    }

    public String getStringLore(Pair<String, String>... placeholders) {
        StringBuilder builder = new StringBuilder();
        getLore(placeholders).forEach(val -> builder.append(val + "\n"));
        return builder.substring(0, builder.length() - 1);
    }

}