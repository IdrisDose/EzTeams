package net.idrisdev.mc.ezteams.config.category;

import net.idrisdev.mc.ezteams.config.category.Teams.*;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 22/01/2017.
 */
@ConfigSerializable
public class TeamsContainerCategory extends ConfigCategory{

    @Setting(value="aqua-container")
    private AquaCategory aqua = new AquaCategory();

    @Setting(value = "galactic-container")
    private GalacticCategory galactic = new GalacticCategory();

    @Setting(value = "magma-container")
    private MagmaCategory magma = new MagmaCategory();

    @Setting(value = "plasma-container")
    private PlasmaCategory plasma = new PlasmaCategory();

    @Setting
    private RocketCategory rocket = new RocketCategory();

}
