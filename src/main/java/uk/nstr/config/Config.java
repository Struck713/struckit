package uk.nstr.config;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public abstract class Config {

    private JavaPlugin plugin;
    private String name;
    private File file;

    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        File directory = this.plugin.getDataFolder();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.file = new File(directory, this.name + ".yml");
        if (!this.file.exists()) {
            this.copyResource();
        }

        this.load();
    }

    public abstract void load();

    protected void copyResource() {
        InputStream stream = this.plugin.getResource(this.name + ".yml");
        try {
            Files.copy(stream, this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
