package net.idrisdev.mc.ezteams.config.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Idris on 22/01/2017.
 */

@ConfigSerializable
public class TeamsCategory extends ConfigCategory{

    @Setting(value= "teamlist",comment = "Best not to change this, it might break things")
    private List<String> teams = Arrays.asList("default","aqua","magma","rocket","plasma","galactic","staff");


    public List<String> getTeams() {
        return teams;
    }
}
