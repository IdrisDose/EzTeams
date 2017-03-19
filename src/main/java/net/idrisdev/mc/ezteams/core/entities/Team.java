package net.idrisdev.mc.ezteams.core.entities;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.Core;

/**
 * Created by Idris on 5/01/2017.
 */
public class Team {
    private EzTeams plugin = EzTeams.get();
    private int id;

    private String name;

    private int points;

    private String prefix;

    public Team(){}

    public Team(int id, String name, int points, String prefix) {
        this.id=id;
        this.name = name;
        this.points = points;
        this.prefix = prefix;
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

    public void addTeamPoints(int points){
        this.setPoints(this.getPoints()+points);

        Core.getTeamsLog().info("Points added ("+points+") TOTAL: "+this.getPoints());
        this.save();
    }

    public void removeTeamPoints(int points){
        this.setPoints(this.getPoints()-points);
        Core.getTeamsLog().info("Points removed: "+points+"; Cur Points: "+this.getPoints());
        this.save();
    }

    public void setTeamPoints(int newPoints){
        this.setPoints(newPoints);
        Core.getTeamsLog().info("Team("+this.name+") points Changed: "+this.getPoints());
        this.save();
    }

    private void save(){
        plugin.core.getDao().saveTeam(this);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return name+"{" +
                "id=" + id +
                ", name=" + name +
                ", points="+ points +
                '}';
    }


    void memberChange(Member member) {
        int memberPoints = member.getPoints();

        if(this.points<memberPoints){
            this.setPoints(0);
        } else{
            this.setPoints(this.getPoints()-memberPoints);
        }

    }
}
