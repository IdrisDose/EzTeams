package net.idrisdev.mc.ezteams.config.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 21/01/2017.
 */
@ConfigSerializable
public class PointsCategory extends ConfigCategory {

    @Setting(value="1st-place-win",comment = "How many points for tourney 1st place")
    private int firstPlaceWin = 50;

    @Setting(value="2nd-place-win",comment = "How Many points for tourney 2nd place")
    private int secondPlaceWin = 25;

    @Setting(value="3rd-place-win",comment = "How many points for tourney 3rd place")
    private int thirdPlaceWin = 10;

    @Setting(value="normal-points",comment = "How many points for general things")
    private int generalPoints = 5;

    public int getFirstPlaceWin() {
        return firstPlaceWin;
    }
    public int getSecondPlaceWin() {
        return secondPlaceWin;
    }
    public int getThirdPlaceWin() {
        return thirdPlaceWin;
    }
    public int getGeneralPoints() {
        return generalPoints;
    }
}
