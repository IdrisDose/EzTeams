package net.idrisdev.mc.ezteams.config.category.Teams;

import net.idrisdev.mc.ezteams.core.entities.Member;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Idris on 22/01/2017.
 */
@ConfigSerializable
public class PlasmaCategory extends TeamCategory {

    @Setting(value="team-points", comment = "How many points this team has")
    private int points;

    @Setting(value="team-members",comment = "Whos in this team")
    private List<Member> members = new ArrayList<>();

    @Setting(value = "team-member-count", comment = "How many members in this team")
    private int teamMemberCount = members.isEmpty()?0:members.size();

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public int getTeamMemberCount() {
        return teamMemberCount;
    }

    public void setTeamMemberCount(int teamMemberCount) {
        this.teamMemberCount = teamMemberCount;
    }
}
