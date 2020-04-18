package us.ryguy.xpminer.cmds;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.ryguy.xpminer.XPMiner;

import java.util.ArrayList;

public class XPMinerCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        XPMiner xpMiner = (XPMiner) Bukkit.getPluginManager().getPlugin("XPMiner");
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("xpminer.reload") || sender.hasPermission("xpminer.op")) {
                    try {
                        xpMiner.reloadConfig();
                        xpMiner.checkConfig();
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.GRAY + " Config Reloaded!");
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "There was a(n) " + e.getClass().getSimpleName() + " in attempting to reload configuration!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + e.getMessage());
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + e.getLocalizedMessage());
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "Try to use a rich text editor to edit your configuration, and if that shows nothing, and you believe it is a bug, report it!");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.GRAY + " No Permission!");
                }
            } else if (args[0].equalsIgnoreCase("getvalue")) {
                if (args.length == 2) {
                    if (sender.hasPermission("xpminer.getvalue") || sender.hasPermission("xpminer.op")) {
                        ArrayList<String> mnames = new ArrayList<>();
                        for (Material m : Material.values()) {
                            if (m.isBlock()) {
                                mnames.add(m.name().toLowerCase());
                            }
                        }
                        if (mnames.contains(args[1].toLowerCase())) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " takes " + ChatColor.GOLD + filterExpValue(xpMiner.getConfig().getConfigurationSection("Materials").getString(args[1].toUpperCase())) + ChatColor.GRAY + " to break!");
                            if (xpMiner.getDisabledBlocks().contains(args[1].toLowerCase())) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "Block " + ChatColor.GOLD + args[1] + " is currently " + ChatColor.RED + ChatColor.UNDERLINE + "disabled" + ChatColor.RESET + ChatColor.GRAY + " as it was misconfigured. Please Investigate!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " isn't a valid block!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.RED + " No Permission!");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.RED + " Invalid Usage!");
                    sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.GRAY + " Usage: /xpminer getvalue [block]");
                }
            } else if (args[0].equalsIgnoreCase("setvalue")) {
                if (sender.hasPermission("xpminer.setvalue") || sender.hasPermission("xpminer.op")) {
                    if (args.length == 3) {
                        if (xpMiner.isInteger(args[2]) || (xpMiner.isInteger(args[2].substring(0, args[2].length() - 1)) && args[2].substring(args[2].length() - 1).toLowerCase().equalsIgnoreCase("l"))) {
                            ArrayList<String> mnames = new ArrayList<>();
                            for (Material m : Material.values()) {
                                if (m.isBlock()) {
                                    mnames.add(m.name().toLowerCase());
                                }
                            }
                            if (mnames.contains(args[1].toLowerCase())) {
                                xpMiner.getConfig().getConfigurationSection("Materials").set(args[1].toUpperCase(), Integer.valueOf(args[2]));
                                xpMiner.saveConfig();
                                sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GRAY + "Set " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " to require " + ChatColor.GOLD + filterExpValue(xpMiner.getConfig().getConfigurationSection("Materials").getString(args[1].toUpperCase())) + ChatColor.GRAY + "!");
                            } else {
                                //Invalid block
                                sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner] " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " isn't a valid block!");
                            }
                        } else {
                            //Invalid Config
                            sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.RED + " You attempted to set the block to require an invalid value!");
                            sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.GRAY + " Try a value like " + ChatColor.GOLD + "5L" + ChatColor.GRAY + " for 5 levels or " + ChatColor.GOLD + "150 " + ChatColor.GRAY + "for 150 experience!");
                        }
                    } else {
                        //Invalid # of args
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.RED + " Invalid Usage!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.GRAY + " Usage: /xpminer setvalue [block] [value]");
                    }
                } else {
                    //Invalid Permissions
                    sender.sendMessage(ChatColor.DARK_AQUA + "[XPMiner]" + ChatColor.RED + " No Permission!");
                }
            } else {
                //Args doesn't match pre-existing cmd
                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET.toString() + ChatColor.BOLD + "" + ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + " XPMiner Help " + ChatColor.GRAY + "]" + ChatColor.STRIKETHROUGH + "---");
                sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer reload" + ChatColor.GRAY + " - Reload's XPMiner Config!");
                sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer getvalue" + ChatColor.GRAY + " - Gets the required amount of experience // levels needed to break a block!");
                sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer setvalue" + ChatColor.GRAY + " - Sets the required amount of experience // levels needed to break a block!");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET.toString() + ChatColor.BOLD + "" + ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + " XPMiner Help " + ChatColor.GRAY + "]" + ChatColor.STRIKETHROUGH + "---");
            sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer reload" + ChatColor.GRAY + " - Reload's XPMiner Config!");
            sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer getvalue" + ChatColor.GRAY + " - Gets the required amount of experience // levels needed to break a block!");
            sender.sendMessage(ChatColor.DARK_AQUA + "/xpminer setvalue" + ChatColor.GRAY + " - Sets the required amount of experience // levels needed to break a block!");
        }
        return true;
    }

    public String filterExpValue(String s) {
        String filtrate;
        if (s.toLowerCase().contains("l")) {
            s = s.substring(0, s.length() - 1);
            if (Integer.parseInt(s) == 1) {
                filtrate = ChatColor.GOLD + s + " level";
            } else {
                filtrate = ChatColor.GOLD + s + " levels";
            }
        } else {
            filtrate = ChatColor.GOLD + s + " experience";
        }
        return filtrate;
    }
}