package com.killeffects.gui;

import com.killeffects.KillEffectsPlugin;
import com.killeffects.database.DatabaseManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EffectGUI implements InventoryHolder {
    private final KillEffectsPlugin plugin;
    private final Player player;
    private Inventory inventory;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public EffectGUI(KillEffectsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void open() {
        Inventory inv = inventory = Bukkit.createInventory(this, 54, mm.deserialize("<gray>Select Kill Effect</gray>"));
        
        DatabaseManager.PlayerData data = plugin.getDatabaseManager().getPlayerData(player.getUniqueId());
        String selected = data.getSelectedEffect();

        int slot = 10;
        for (String effectId : plugin.getEffectRegistry().getEffects().keySet()) {
            boolean isSelected = effectId.equals(selected);
            
            ItemStack item = new ItemStack(isSelected ? Material.ENCHANTED_BOOK : Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(mm.deserialize("<yellow>" + effectId + "</yellow>"));
            
            List<net.kyori.adventure.text.Component> lore = new ArrayList<>();
            lore.add(mm.deserialize("<gray>Status: " + (isSelected ? "<green>Selected" : "<red>Not Selected") + "</gray>"));
            lore.add(mm.deserialize(""));
            lore.add(mm.deserialize("<yellow>Click to select!</yellow>"));
            meta.lore(lore);
            
            meta.getPersistentDataContainer().set(effectKey(plugin), PersistentDataType.STRING, effectId);
            item.setItemMeta(meta);
            if (slot >= inv.getSize()) break;
            inv.setItem(slot++, item);
            
            if (slot == 17) slot = 19;
            if (slot == 26) slot = 28;
        }

        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private static NamespacedKey effectKey(KillEffectsPlugin plugin) {
        return new NamespacedKey(plugin, "effect");
    }

    private void select(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String effectId = item.getItemMeta().getPersistentDataContainer().get(effectKey(plugin), PersistentDataType.STRING);
        if (effectId == null) return;

        plugin.getDatabaseManager().getPlayerData(player.getUniqueId()).setSelectedEffect(effectId);
        player.sendMessage(mm.deserialize(plugin.getConfig().getString("messages.prefix") + "Selected effect: <yellow>" + effectId + "</yellow>"));
        player.closeInventory();
    }

    public static class ClickListener implements Listener {
        private final KillEffectsPlugin plugin;

        public ClickListener(KillEffectsPlugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onClick(InventoryClickEvent event) {
            Inventory top = event.getView().getTopInventory();
            if (!(top.getHolder() instanceof EffectGUI gui)) return;
            event.setCancelled(true);
            if (top.equals(event.getClickedInventory())) gui.select(event);
        }

        @EventHandler
        public void onDrag(InventoryDragEvent event) {
            if (event.getView().getTopInventory().getHolder() instanceof EffectGUI) event.setCancelled(true);
        }
    }
}
