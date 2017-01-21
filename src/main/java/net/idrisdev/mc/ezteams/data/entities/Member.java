package net.idrisdev.mc.ezteams.data.entities;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.data.DAO;
import net.idrisdev.mc.ezteams.utils.Utils;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import static net.idrisdev.mc.ezteams.EzTeams.players;
import static net.idrisdev.mc.ezteams.EzTeams.teams;

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
        if(plugin.getDao().ifExists(query))
            plugin.getDao().updatePlayer(this);
        else
            plugin.getDao().insertPlayer(this);
    }

    public void getPlayerData(String id){
        Member container = plugin.getDao().getMemberData(id);
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
