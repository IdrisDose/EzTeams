package net.idrisdev.mc.ezteams.core.tasks;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.config.Config;
import net.idrisdev.mc.ezteams.utils.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by Idris on 23/01/2017.
 */
public class AutoSaveTask implements Runnable{
    private final EzTeams plugin;
    private Config config;
    public AutoSaveTask(Config config, EzTeams plugin){
        this.plugin = plugin;
        this.config = config;

    }

    @Override
    public void run() {
        try {
            plugin.getLogger().info("Saving Team Data");
            Utils.plainbroadcastAsConsole("Now saving teams data, may lag.");
            plugin.core.getDao().saveAll();
            Utils.plainbroadcastAsConsole("Successfully saved teams data!");
        } catch ( Exception e ){
            plugin.getLogger().error("ERROR RUNNING AUTOSAVE TASK!!!!");
            e.printStackTrace();
        }
    }
}
