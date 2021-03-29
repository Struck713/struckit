package uk.nstr.config.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

@AllArgsConstructor
@Getter
public class ConfigLocation {

    public static ConfigLocation of(ConfigurationSection section) {
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        String world = section.getString("world");
        return new ConfigLocation(x, y, z, world);
    }

    private double x;
    private double y;
    private double z;
    private String world;

    public Location toLocation() {
        World world = Bukkit.getWorld(this.world);
        return new Location(world, this.x, this.y, this.z);
    }

}