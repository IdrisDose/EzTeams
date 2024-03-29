package net.idrisdev.mc.ezteams.config;

import net.idrisdev.mc.ezteams.config.category.PointsCategory;
import ninja.leaping.configurate.objectmapping.Setting;

/**
 * Created by Idris on 30/12/2016.
 */
public class Config {

    /*
    @Setting(value="database-config",comment = "database settings")
    public DatabaseCategory db = new DatabaseCategory();
    */
    @Setting(value="points-config",comment = "Where general points get put")
    public PointsCategory pointsCfg = new PointsCategory();

    @Setting(value="allow-debug-messages",comment = "Allow the viewing of debug messages for errors and such.")
    private boolean debug = false;

    @Setting(value="save-interval",comment = "Interval in seconds between each save")
    private int interval = 900;

    public boolean getDebug(){
        return debug;
    }
    public int getInterval(){return interval;}


}
