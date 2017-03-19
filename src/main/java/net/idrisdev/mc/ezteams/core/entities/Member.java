package net.idrisdev.mc.ezteams.core.entities;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.utils.Utils;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.command.CommandSource;

/**
 * Created by Idris on 5/01/2017.
 */

@ConfigSerializable
public class Member {
    private EzTeams plugin = EzTeams.get();


    private String uuid;
    private String name;
    private Team team;
    private int points;

    public Member() {}

    public Member(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        getPlayerData(uuid);
    }

    public Member(String uuid, String name, int team, int points) {
        this.uuid = uuid;
        this.name = name;
        this.team = Utils.findTeam(team);
        this.points = points;
    }


    public void savePlayer(){
        String query = "select * from players where uuid='"+this.uuid+"'";
        if(plugin.core.getDao().ifExists(query))
            plugin.core.getDao().updatePlayer(this);
        else
            plugin.core.getDao().insertPlayer(this);
    }

    private void getPlayerData(String id){
        Member container = plugin.core.getDao().getMemberData(id);
        this.team = container.getTeam();
        this.points = container.getPoints();

        plugin.getLogger().info(this.toString());
        savePlayer();
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return (this.team);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void addMemberPoints(int points){
        this.setPoints(this.getPoints()+points);
        this.getTeam().addTeamPoints(points);
        savePlayer();
    }
    public void removeMemberPoints(int points){
        this.setPoints(this.getPoints()-points);
        this.getTeam().removeTeamPoints(points);
        savePlayer();
    }
    public void setMemberPoints(int newPoints){
        int oldPoints = this.getPoints();
        Team team = this.getTeam();
        this.setPoints(newPoints);
        //If oldPoints less than new points
        if(oldPoints < newPoints){
            //rPoints = newPoints minus oldPoints - get remainder
            int rPoints = newPoints-oldPoints;
            team.addTeamPoints(rPoints);
        //If they had more points before, remove those remaining points from team.
        }else if(oldPoints > newPoints){
            int rPoints = oldPoints-newPoints;
            team.removeTeamPoints(rPoints);
        }

        savePlayer();
    }

    public void leaveTeam(Team team, Team temp){
        this.setPoints(0);
        this.setTeam(team);

        Utils.executeCmdAsConsole("lp user "+this.getName()+" meta unset team");
        //Logging to file that user left their team.
        this.savePlayer();
    }

    public void joinTeam(Team team){
        this.setTeam(team);

        Utils.executeCmdAsConsole("lp user "+this.getName()+" meta unset team");
        Utils.executeCmdAsConsole("lp user "+this.getName()+" meta set team "+team.getPrefix());

        Core.getTeamsLog().info("User "+this.getName()+" joined team "+team.getName());

        this.savePlayer();
    }

    public void changeTeam(CommandSource src, Team team, Team oldTeam) {
        int points = this.getPoints();
        this.setTeam(team);

        oldTeam.memberChange(this);
        team.addTeamPoints(points);

        Utils.executeCmdAsConsole("lp user "+this.getName()+" meta unset team");
        Utils.executeCmdAsConsole("lp user "+this.getName()+" meta set team "+team.getPrefix());

        Utils.sendPrettyMessage(src,"Set user "+this.getName()+" as team "+team.getName());
        Core.getTeamsLog().info(src.getName() +" set "+this.getName()+" team as: "+team.getName());

        this.savePlayer();
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", team=" + team +
                ", points=" + points +
                '}';
    }


}
