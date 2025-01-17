package uk.nstr.util;

import org.bukkit.Bukkit;

public class VersionUtil {

    public static String getCraftBukkit() {
        return "org.bukkit.craftbukkit." + getVersion();
    }

    public static String getMinecraftServer() {
        return "net.minecraft.server." + getVersion();
    }

    /**
     * Uses reflection to get the current version.
     *
     * @return the version, as a String
     */
    public static String getVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    /**
     * Grabs the string and then removes the
     * revision number from the end.
     *
     * @return the modified version String
     */
    public static String getSubVersionString() {
        String version = VersionUtil.getVersion();
        return version.substring(1, version.length() - 3);
    }

    /**
     * Takes the sub-version String and makes
     * it into an Integer.
     *
     * @return the sub-version, as an Integer
     */
    public static int getSubVersion() {
        String subversion = VersionUtil.getSubVersionString()
                .replaceAll("\\_", "");
        return Integer.parseInt(subversion);
    }

}
