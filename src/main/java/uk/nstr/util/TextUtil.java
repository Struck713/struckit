package uk.nstr.util;

import org.bukkit.ChatColor;

public class TextUtil {

    /**
     * Colors the provided String.
     *
     * @param text the text
     * @return the text, but now with color
     */
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Takes an input string and makes it
     * plural or not plural. Depending on the
     * number.
     *
     * @param number the number of items
     * @param singular the singular version of the word
     * @return the word
     */
    public static String plural(int number, String singular) {
        return number + " " + singular + (number == 1 ? "" : "s");
    }

}