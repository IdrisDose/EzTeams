package net.idrisdev.mc.ezteams.data;

/**
 * Created by Idris on 10/10/2016.
 */
public class TeamData {
    private int ID;
    private String NAME;
    private int POINTS;

    public TeamData(int id, String name, int points) {
        this.ID = id;
        this.NAME = name;
        this.POINTS = points;
    }

    public final int getID() {
        return this.ID;
    }

    public final String getNAME() {
        return this.NAME;
    }

    public final int getPOINTS() {
        return POINTS;
    }

    public void setPOINTS(int value) {
        this.POINTS = value;
    }

    @Override
    public String toString() {
        if (getPOINTS() == 0)
            return getNAME() + " HAS NO POINTS! ";
        else if (getPOINTS() == 1)
            return getNAME() + " HAS ONLY ONE POINT!";
        else
            return getNAME() + " HAS " + getPOINTS() + " POINTS!";
    }
}
