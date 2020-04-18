package us.ryguy.xpminer.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import us.ryguy.xpminer.XPMiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cmdCompleter implements TabCompleter {

    XPMiner xpMiner = (XPMiner) Bukkit.getPluginManager().getPlugin("XPMiner");

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> complete = new ArrayList<>();
        if (args.length == 1) {
            complete.add("reload");
            complete.add("getvalue");
            complete.add("setvalue");
            Collections.sort(complete);
            return complete;
        }
        if (args[0].equalsIgnoreCase("getvalue") || args[0].equalsIgnoreCase("setvalue")) {
            for (Object o : xpMiner.getConfig().getConfigurationSection("Materials").getKeys(false)) {
                if (!xpMiner.getDisabledBlocks().contains((String) o)) {
                    complete.add(((String) o).toLowerCase());
                }
            }
            Collections.sort(complete);
            return complete;
        }

        return null;
    }
}
