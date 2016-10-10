package net.idrisdev.mc.ezteams.data.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 9/10/2016.
 */
@ConfigSerializable
public class DatabaseCategory extends ConfigCategory {
    @Setting(value = "password", comment = "password")
    public String dbPassword = "";
    @Setting(value = "username", comment = "username")
    public String dbUsername = "";
    @Setting(value = "url", comment = "url")
    public String dbURL = "";
}
