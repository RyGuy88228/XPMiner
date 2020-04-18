package us.ryguy.xpminer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import us.ryguy.xpminer.cmds.XPMinerCMD;
import us.ryguy.xpminer.cmds.cmdCompleter;
import us.ryguy.xpminer.event.BlockBreak;

import java.util.ArrayList;

public class XPMiner extends JavaPlugin {
    ArrayList<String> disabledBlocks = new ArrayList<>();

    public void onEnable() {

        if (getConfig().get("Materials") == null) {
            getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[XPMiner] Configuration Initializing!");
            initConfiguration();
        }
        checkConfig();
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getCommand("xpminer").setExecutor(new XPMinerCMD());
        getCommand("xpminer").setTabCompleter(new cmdCompleter());
        saveConfig();
    }

    public void onDisable() {

    }

    //Config
    //Implement Config check on Launch & console logging
    public void initConfiguration() {
        this.getConfig().createSection("Materials");
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                getConfig().getConfigurationSection("Materials").addDefault(m.name(), 0);
                getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[XPMiner] Block " + ChatColor.GOLD + m.name() + ChatColor.AQUA + " was added to the configuration file!");
            }
        }
        getConfig().options().copyDefaults(true);
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[XPMiner] Configuration Created!");
    }

    public void checkConfig() {
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[XPMiner] Checking your Configuration File!");
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> onames = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                names.add(m.name());
            }
        }
        //Check Config
        for (Object o : getConfig().getConfigurationSection("Materials").getKeys(false)) {
            onames.add((String) o);
            String val = getConfig().getConfigurationSection("Materials").getString((String) o);
            //Checks if there are spaces within the configuration
            String[] spaces = val.split(" ");
            if (spaces.length > 1) {
                String rtrn = String.join("", spaces);
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XPMiner] Block " + o + " was misconfigured to be, " + val + ", as it had spaces. It was fixed to be " + rtrn + "!");
                getConfig().getConfigurationSection("Materials").set((String) o, Integer.valueOf(rtrn));
                saveConfig();
            }
            //not working
            if (val.isEmpty() || val.trim().length() == 0 || getConfig().getConfigurationSection("Materials").getString((String) o) == null) {
                getConfig().getConfigurationSection("Materials").set((String) o, 0);
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XPMiner] Block " + (String) o + " was not given a value, so it has been set to take away no experience!");
                saveConfig();
            }
            if (!names.contains(o)) {
                getConfig().getConfigurationSection("Materials").set((String) o, null);
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XPMiner] Block " + (String) o + " was no longer needed (Did you downgrade versions? It was removed from the configuration");
                saveConfig();
            }
            if (!isInteger(val)) {
                if (!isInteger(val.substring(0, val.length() - 1)) && !val.substring(val.length() - 1).toLowerCase().equalsIgnoreCase("l")) {
                    disabledBlocks.add((String) o);
                    getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XPMiner] Block " + (String) o + " was misconfigured as " + val + " and I could not fix it through code. Please Investigate!");
                }
            }
        }
        for (String st : names) {
            if (!onames.contains(st)) {
                getConfig().getConfigurationSection("Materials").addDefault(st.toUpperCase(), 0);
                getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[XPMiner] Did you update versions? Missing Block " + ChatColor.GOLD + st.toUpperCase() + ChatColor.AQUA + " was added to the configuration file!");
                getConfig().options().copyDefaults(true);
                saveConfig();
            }
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XPMiner] Disabled Blocks: " + disabledBlocks);
    }

    public boolean isInteger(String s) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(s);
            isValidInteger = true;
        } catch (NumberFormatException e) {

        }
        return isValidInteger;
    }

    public ArrayList<String> getDisabledBlocks() {
        return disabledBlocks;
    }
}
