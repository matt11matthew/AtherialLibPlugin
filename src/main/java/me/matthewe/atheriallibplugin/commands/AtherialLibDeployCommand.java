package me.matthewe.atheriallibplugin.commands;

import me.matthewe.atheriallibplugin.AtherialLibPlugin;
import me.matthewe.atheriallibplugin.restart.RestartConfig;
import me.matthewedevelopment.atheriallib.command.AnnotationlessAtherialCommand;
import me.matthewedevelopment.atheriallib.utilities.AtherialTasks;
import me.matthewedevelopment.atheriallib.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class AtherialLibDeployCommand  extends AnnotationlessAtherialCommand {
    private AtherialLibPlugin plugin;
    private RestartConfig config;
    public AtherialLibDeployCommand(AtherialLibPlugin plugin) {
        super("atherialdeploy");
        this.plugin = plugin;
        config =new RestartConfig(plugin);

    }

    private final String CONSOLE_ONLY_MSG = ChatColor.RED +ChatColor.BOLD.toString()+  "This command can only be executed by console";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(CONSOLE_ONLY_MSG);
            return;
        }
        if ((args.length == 1) && args[0].equalsIgnoreCase("closeall")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.closeInventory();
            }
        }
        if ((args.length == 1) && args[0].equalsIgnoreCase("reboot")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                for (String restartMessage : config.getRestartMessages()) {
                    onlinePlayer.sendMessage(ChatUtils.colorizeNew(new String(restartMessage).replace("%seconds%", (config.getRestartDelay()/20)+"")));
                }
            }
            AtherialTasks.runIn(() -> {
                for (String restartCommand : config.getRestartCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), restartCommand);
                }
            }, config.getRestartDelay());
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Arrays.asList("reboot");
    }
}
