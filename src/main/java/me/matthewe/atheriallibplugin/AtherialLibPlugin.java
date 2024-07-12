package me.matthewe.atheriallibplugin;

import me.matthewedevelopment.atheriallib.AtherialLib;
import me.matthewedevelopment.atheriallib.config.BukkitConfig;
import me.matthewedevelopment.atheriallib.config.yaml.CustomTypeRegistry;
import me.matthewedevelopment.atheriallib.dependency.Dependency;
import me.matthewedevelopment.atheriallib.dependency.headdatabase.HeadDatabaseDependency;
import me.matthewedevelopment.atheriallib.dependency.luckperms.LuckPermsDependency;
import me.matthewedevelopment.atheriallib.dependency.vault.VaultDependency;
import me.matthewedevelopment.atheriallib.handler.HandlerManager;
import me.matthewedevelopment.atheriallib.playerdata.AtherialProfile;
import me.matthewedevelopment.atheriallib.utilities.AtherialTasks;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AtherialLibPlugin extends AtherialLib {


    private static List<AtherialAddon> addons = new ArrayList<>();

    public static void registerAddon(AtherialAddon cloudAddon) {
        addons.add(cloudAddon);
    }




    public AtherialLibPlugin() {

//        setDebug(true);
        this.enableMySql();

    }


    @Override
    public void onPostProfileLoad() {

        if  (profileManager==null) {
            getLogger().severe("PROFILE MANAGER UNLOADED!");
            return;
        }
        for (AtherialAddon addon : addons) {
            getLogger().info("Loaded addon " + addon.getClass().getSimpleName());
            addon.onStart();
        }
        AtherialTasks.runIn( () -> {
            for (AtherialAddon cloudAddon : addons) {
                getLogger().info("Finished loading addon " + cloudAddon.getClass().getSimpleName());
                cloudAddon.onAddonLoad();
            }
        }, 20);

    }

    @Override
    public void onStart() {


        dependencyManager.getDependency(VaultDependency.class).init();


    }


    @Override
    public void registerHandlers() {
        //Register all handlers here!

    }

    @Override
    public void onStop() {
        for (AtherialAddon cloudAddon : addons) {
            cloudAddon.onStop();
        }
    }

    @Override
    public void initDependencies() {

        BukkitConfig bukkitConfig = new BukkitConfig("depend.yml", this);

        FileConfiguration c = bukkitConfig.getConfiguration();
        List<Dependency> dependencies = new ArrayList<>();
        if (c.getBoolean("luckperms")) {
            dependencies.add(new LuckPermsDependency(this));
        }
        if (c.getBoolean("vault")) {
            dependencies.add(new VaultDependency(this));
        }
        if (c.getBoolean("headDatabase")) {
            dependencies.add(new HeadDatabaseDependency(this));
        }
        if (c.isSet("debug")) {

            if (c.getBoolean("debug")) {
                setDebug(true);
            }
        }


        dependencyManager.loadDependencies(dependencies.toArray(new Dependency[]{}));

    }



    @Override
    public void registerTypes() {

    }

    @Override
    protected void handleConfigReloads() {
        //Not needed but if you want universal config reload easier do it


    }

    @Override
    public List<Class<? extends AtherialProfile>> getProfileClazzes() {
        return new ArrayList<>(); //Place profiels here!!
    }
}
