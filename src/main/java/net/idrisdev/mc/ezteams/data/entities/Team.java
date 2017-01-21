package net.idrisdev.mc.ezteams.data.entities;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by Idris on 5/01/2017.
 */
@ConfigSerializable
public class Team {

    private int id;
    private String name;
    private int points;

    public Team(){

    }

    public Team(int id, String name, int points) {
        this.id=id;
        this.name = name;
        this.points = points;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
