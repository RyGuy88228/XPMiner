package us.ryguy.xpminer.event;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import us.ryguy.xpminer.XPMiner;

import java.util.ArrayList;

public class BlockBreak implements Listener {
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        XPMiner xpMiner = (XPMiner) Bukkit.getPluginManager().getPlugin("XPMiner");
        ArrayList<String> disabledBlocks = xpMiner.getDisabledBlocks();
        Block b = e.getBlock();
        Player pl = e.getPlayer();
        String v = xpMiner.getConfig().getConfigurationSection("Materials").getString(b.getType().name().toUpperCase());
        if (disabledBlocks.contains(e.getBlock().getType().name().toLowerCase())) {
            xpMiner.getServer().getConsoleSender().sendMessage(org.bukkit.ChatColor.RED + "[XPMiner] Player " + e.getPlayer().getDisplayName() + " broke disabled block " + e.getBlock().getType().name() + "! Check your configuration!");
            return;
        }
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && !e.getPlayer().hasPermission("xpminer.bypass")) {
            //Takes away levels if specifically specified in the configuration by putting an "l" in the string. i.e. 5L
            if (v.toLowerCase().contains("l")) {
                v = v.substring(0, v.length() - 1);
                int i = Integer.parseInt(v);
                if ((pl.getLevel() - i) < 0) {
                    e.setCancelled(true);
                    pl.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "You do not have enough experience required to break " + ChatColor.GOLD + b.getType().name() + ChatColor.GRAY + "!");
                    pl.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "You need " + ChatColor.GOLD + (i - pl.getLevel()) + ChatColor.GRAY + " more levels!");
                } else {
                    pl.giveExpLevels(-i);
                }
            } else {
                int i = Integer.parseInt(v);
                if ((pl.getTotalExperience() - i) < 0) {
                    e.setCancelled(true);
                    pl.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "You do not have enough experience to break " + ChatColor.GOLD + b.getType().name() + ChatColor.GRAY + "!");
                    pl.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "You need " + ChatColor.GOLD + (i - pl.getTotalExperience()) + ChatColor.GRAY + "more experience!");
                } else {
                    pl.giveExp(-i);
                }
            }
        }
    }
}