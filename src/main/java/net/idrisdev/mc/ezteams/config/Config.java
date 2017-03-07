package net.idrisdev.mc.ezteams.config;

import net.idrisdev.mc.ezteams.config.category.DatabaseCategory;
import net.idrisdev.mc.ezteams.config.category.PointsCategory;
import net.idrisdev.mc.ezteams.config.category.TeamsCategory;
import ninja.leaping.configurate.objectmapping.Setting;

/**
 * Created by Idris on 30/12/2016.
 */
public class Config {

    @Setting(value="database-config",comment = "database settings")
    public DatabaseCategory db = new DatabaseCategory();

    @Setting(value="points-config",comment = "Where general points get put")
    public PointsCategory pointsCfg = new PointsCategory();

    @Setting(value="allow-debug-messages",comment = "Allow the viewing of debug messages for errors and such.")
    private boolean debug = false;


    public boolean getDebug(){
        return debug;
    }
}
