package net.idrisdev.mc.ezteams.config.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 21/01/2017.
 */
@ConfigSerializable
public class DatabaseCategory extends ConfigCategory{
    @Setting(value="enabled",comment = "Use DB for datastorage? dafault:true")
    public boolean useDB = true;

    @Setting(value= "password", comment="Password for Database")
    private String dbPassword = "";

    @Setting(value = "username", comment="username for database")
    private String dbUsername = "";

    @Setting(value="url",comment = "url for database")
    private String dbURL="jdbc:mysql://";

    public String getDbPassword() {
        return dbPassword;
    }
    public String getDbUsername() {
        return dbUsername;
    }
    public String getDbURL() {
        return dbURL;
    }
}
