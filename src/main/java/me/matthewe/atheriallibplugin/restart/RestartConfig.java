package me.matthewe.atheriallibplugin.restart;

import me.matthewe.atheriallibplugin.AtherialLibPlugin;
import me.matthewedevelopment.atheriallib.config.BukkitConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RestartConfig {
    private List<String> restartCommands;
    private long restartDelay;
    private List<String> restartMessages;

    private AtherialLibPlugin plugin;

    public RestartConfig(AtherialLibPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public List<String> getRestartCommands() {
        return restartCommands;
    }

    public long getRestartDelay() {
        return restartDelay;
    }

    public List<String> getRestartMessages() {
        return restartMessages;
    }

    public AtherialLibPlugin getPlugin() {
        return plugin;
    }

    private void load() {
        BukkitConfig bukkitConfig =new BukkitConfig("deploy.yml", plugin);
        FileConfiguration c = bukkitConfig.getConfiguration();
        restartCommands = c.getStringList("restartCommands");
        restartMessages = c.getStringList("restartMessages");
        restartDelay = c.getLong("restartDelay");
    }
}
