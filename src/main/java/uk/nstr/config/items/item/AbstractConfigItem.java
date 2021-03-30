package uk.nstr.config.items.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import uk.nstr.util.TextUtil;
import uk.nstr.util.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public abstract class AbstractConfigItem {

    public static AbstractConfigItem of(ConfigurationSection section) {

        // custom items
//        if (section.isString("custom-id")) {
//            double chance = section.getDouble("chance");
//            return new CustomConfigItem(section.getString("custom-id"), chance);
//        }

        // basic items
        Material material = Material.matchMaterial(section.getString("material"));
        String name = TextUtil.color(section.getString("name"));
        List<String> lore = section.getStringList("lore")
                .stream()
                .map(TextUtil::color)
                .collect(Collectors.toList());

        boolean glow = false;
        if (section.isBoolean("glow")) glow = section.getBoolean("glow");

        int amount = 1;
        if (section.isInt("amount")) amount = section.getInt("amount");

        short data = 0;
        if (section.isInt("data")) data = (short)section.getInt("data");

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if (section.isList("enchants")) {
            List<String> enchantsList = section.getStringList("enchants");
            for (String enchant : enchantsList) {

                //strings should be formatted 'enchant:level'

                if (enchant.indexOf(':') == -1) continue; //not formatted correctly
                String[] enchantSplit = enchant.split(":");
                if (enchantSplit.length != 2) continue; //not formatted correctly
                Enchantment enchantment = Enchantment.getByName(enchantSplit[0]);
                if (enchantment == null) continue;
                if (!NumberUtils.isNumber(enchantSplit[1])) continue;
                int level = Integer.parseInt(enchantSplit[1]);

                enchantments.put(enchantment, level);
            }

        }

        return new BasicConfigItem(material, name, lore, enchantments, data, amount, glow);
    }

    public abstract ItemStack toItemStack(Pair<String, String>... placeholders);

}