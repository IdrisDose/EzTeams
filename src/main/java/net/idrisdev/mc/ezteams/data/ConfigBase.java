package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.data.config.DatabaseCategory;
import net.idrisdev.mc.ezteams.data.config.PlayerCategory;
import ninja.leaping.configurate.objectmapping.Setting;

/**
 * Created by Idris on 9/10/2016.
 */
public class ConfigBase {
    @Setting
    public DatabaseCategory database = new DatabaseCategory();
    @Setting
    public PlayerCategory player = new PlayerCategory();
}
