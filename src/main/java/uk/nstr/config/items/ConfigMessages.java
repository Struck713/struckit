package uk.nstr.config.items;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import uk.nstr.util.TextUtil;
import uk.nstr.util.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ConfigMessages {

    public static ConfigMessages of(ConfigurationSection section) {
        Map<String, String> messages = new HashMap<>();
        section.getKeys(false).forEach((key) -> {
            if (!section.isString(key)) return;
            messages.put(key, section.getString(key));
        });
        return new ConfigMessages(messages);
    }


    private Map<String, String> messages;

    public String getMessage(String name, Pair<String, String>... placeholders) {
        String message = this.messages.get(name);
        if (message == null) {
            return "";
        }

        for (Pair<String, String> placeholder : placeholders) {
            message = message.replaceAll(placeholder.getKey(), placeholder.getValue());
        }

        return TextUtil.color(message);
    }

}