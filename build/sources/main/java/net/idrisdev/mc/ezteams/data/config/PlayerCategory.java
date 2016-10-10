package net.idrisdev.mc.ezteams.data.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 9/10/2016.
 */
@ConfigSerializable
public class PlayerCategory extends ConfigCategory {

    @Setting(value = "store-player-data-in-database", comment = "Whether player data should be stored per world. True will store all data in the default world.")
    public boolean storePlayerData = false;
}
