package net.idrisdev.mc.ezteams.config;

import net.idrisdev.mc.ezteams.config.category.DatabaseCategory;
import net.idrisdev.mc.ezteams.config.category.PointsCategory;
import net.idrisdev.mc.ezteams.config.category.TeamsCategory;
import net.idrisdev.mc.ezteams.config.category.TeamsContainerCategory;
import ninja.leaping.configurate.objectmapping.Setting;

import java.util.Arrays;

/**
 * Created by Idris on 30/12/2016.
 */
public class Config {

    @Setting(value="database-config",comment = "database settings")
    public DatabaseCategory db = new DatabaseCategory();

    @Setting(value="points-config",comment = "Where general points get put")
    public PointsCategory pointsCfg = new PointsCategory();

    @Setting(value="team-config",comment= "Teams Config")
    public TeamsCategory teamsCfg = new TeamsCategory();

    @Setting(value="teams-container",comment= "Container for Teams")
    public TeamsContainerCategory teamsContainer = new TeamsContainerCategory();

}
