package uk.nstr.item;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * Simple GuiBuilder for building GUIs
 * on-the-fly.
 *
 */
public class GuiBuilder implements Listener {

    private JavaPlugin plugin;
    @Getter private Inventory inventory;
    private Map<Integer, Consumer<Player>> slots;
    private BiConsumer<GuiBuilder, Player> callback;

    public GuiBuilder(JavaPlugin plugin, String name, int size) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, size, name);
        this.slots = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public GuiBuilder setBackground(ItemStack itemStack) {
        for (int i = 0; i<inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                continue;
            }

            this.inventory.setItem(i, itemStack);
        }

        return this;
    }

    public GuiBuilder set(int slot, ItemStack itemStack, Consumer<Player> click) {
        this.inventory.setItem(slot, itemStack);
        this.slots.put(slot, click);
        return this;
    }

    public GuiBuilder callback(BiConsumer<GuiBuilder, Player> callback) {
        this.callback = callback;
        return this;
    }

    public GuiBuilder open(Player player) {
        player.openInventory(this.inventory);
        return this;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);

        //null out everything
        this.plugin = null;
        this.inventory = null;
        this.slots.clear();
        this.slots = null;
        this.callback = null;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || clicked == null) return;
        if (!inventory.equals(this.inventory)) return;

        event.setCancelled(true);

        int slot = event.getSlot();
        if (!this.slots.containsKey(slot)) return;

        Consumer<Player> clickHandler = this.slots.get(slot);
        clickHandler.accept(player);
        if (this.callback != null) this.callback.accept(this, player);
    }

}