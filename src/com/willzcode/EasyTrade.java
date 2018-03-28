package com.willzcode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by willz on 2018/3/25.
 */
public class EasyTrade extends JavaPlugin implements Listener {
    Inventory tradeInventory = Bukkit.createInventory(null, 54, "公共交易箱");
    Set<Integer> whitelist = new HashSet<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity p = event.getWhoClicked();
        if (p.getOpenInventory().getTopInventory().getName().equals(tradeInventory.getName())) {
            ItemStack item = event.getCurrentItem();

            if (item.getTypeId() == 0) {
                item = p.getItemOnCursor();
                if (item.getTypeId() == 0) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (!whitelist.contains(item.getTypeId())) {
                event.setCancelled(true);
            }
        }
    }

    private void loadConfig() {
        whitelist.clear();
        //whitelist.add(0);
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration file = getConfig();
        for (int id : file.getIntegerList("list")) {
            whitelist.add(id);
        }

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload") && sender.isOp()) {
                loadConfig();
                sender.sendMessage("重载配置!");
                return true;
            }
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(tradeInventory);
        }
        return true;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        loadConfig();
    }
}
