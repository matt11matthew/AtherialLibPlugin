package me.matthewe.atheriallibplugin;

import me.matthewedevelopment.atheriallib.AtherialLib;
import me.matthewedevelopment.atheriallib.command.AnnotationlessAtherialCommand;
import me.matthewedevelopment.atheriallib.command.AtherialCommand;
import me.matthewedevelopment.atheriallib.command.Command;
import me.matthewedevelopment.atheriallib.command.spigot.AtherialLibSpigotCommand;
import me.matthewedevelopment.atheriallib.minigame.load.edit.EditLoadedGameMap;
import me.matthewedevelopment.atheriallib.minigame.load.game.GameLoadedGameMap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;


public abstract class AtherialAddon extends JavaPlugin {
    protected AtherialLibPlugin core;

    public AtherialAddon() {
        this.core = (AtherialLibPlugin) AtherialLibPlugin.getInstance();
    }

    public  void onAddonLoad() {

    }
    public void setupGame(String gameName, Class<? extends GameLoadedGameMap<?>> liveClass, Class<? extends
            EditLoadedGameMap<?>> editClass, Class<?> gameMapDataClass) {
        core.setupGame(gameName,liveClass,editClass,gameMapDataClass);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
    public AtherialLibPlugin getCore() {
        return core;
    }

    public abstract void onStart();
    public abstract void onStop();

    @Override
    public void onDisable() {
    }
    public void registerCommand(AtherialLibSpigotCommand atherialLibSpigotCommand) {
        this.registerAtherialCommand(atherialLibSpigotCommand);
    }

    public void registerAtherialCommand(AtherialCommand command) {
        if (command instanceof AnnotationlessAtherialCommand) {
            final AnnotationlessAtherialCommand atherialCommand = (AnnotationlessAtherialCommand)command;
            Command annotationCommand = new Command() {
                public Class<? extends Annotation> annotationType() {
                    return Command.class;
                }

                public String name() {
                    return atherialCommand.getName();
                }

                public String[] aliases() {
                    return atherialCommand.getAliases();
                }

                public String description() {
                    return atherialCommand.getDescription();
                }

                public String usage() {
                    return atherialCommand.getUsage();
                }
            };
            command.setCommand(annotationCommand);
        } else {
            Command annotationCommand = (Command)command.getClass().getAnnotation(Command.class);
            if (annotationCommand != null) {
                command.setCommand(annotationCommand);
            }
        }

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap spigotCommandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
            AtherialLib.ReflectCommand reflectCommand = new AtherialLib.ReflectCommand(command.getCommand().name());
            reflectCommand.setAliases(Arrays.asList(command.getCommand().aliases()));
            reflectCommand.setDescription(command.getCommand().description());
            reflectCommand.setUsage(command.getCommand().usage());
            reflectCommand.setExecutor(command);
            boolean register = spigotCommandMap.register(reflectCommand.spigotCommand.getCommand().name(), reflectCommand);
            if (register) {
                AtherialLibPlugin.getInstance().getLogger().info("Registered command /" + command.getCommand().name());
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
    @Override
    public void onEnable() {
    }
}
