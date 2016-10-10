package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;

import java.util.UUID;

/**
 * Created by Idris on 9/10/2016.
 */
public class PlayerData extends ETUtils {
    private UUID uuid;
    private String name;
    private int Team;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public PlayerData(UUID uuid, String name, int team) {
        this.uuid = uuid;
        this.name = name;
        this.Team = team;
    }


    public void logData() {
        logger.debug(getName(), getUUID().toString());

    }

    public void insert() {
        DataStorage.insertPlayer(this.uuid, this.name, this.Team);
    }

    public final UUID getUUID() {
        return this.uuid;
    }
    public final String getName() {
        return this.name;
    }
    public final String getTeam(){
        final String[] s = {""};
        EzTeams.teams.forEach(teamData -> {
            if(teamData.getID() == this.Team)
                s[0] = teamData.getNAME();
        });
        return s[0];
    }
    public void setTeam(int value){this.Team = value; }
}
