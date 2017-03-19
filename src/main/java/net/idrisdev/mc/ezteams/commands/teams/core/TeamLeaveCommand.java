package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Idris on 22/01/2017.
 */
public class TeamLeaveCommand {

    private static EzTeams plugin = EzTeams.get();

    public TeamLeaveCommand() {
    }

    public static CommandSpec buildTeamLeaveCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_LEAVE)
                .description(Utils.getCmdDescription("For those who wish to abandon the greate cause."))
                .arguments()
                .executor((src, args) -> {

                    if(!(src instanceof Player)){
                        Utils.sendSrcErrorMessage(src,"Only onlineMembers allowed to execute this command!");
                        return CommandResult.empty();

                    }


                    Member mem = Utils.findMember(src.getName());
                    Team team = Utils.findTeam("default").get();

                    if(Utils.leaveValidate(src,mem,team))
                        return CommandResult.success();


                    Team temp = mem.getTeam();
                    mem.leaveTeam(team,temp);

                    Core.getTeamsLog().info(mem.getName()+" left their team. Old TEAM Points: "+temp.getPoints());
                    Utils.executeCmdAsConsole("plainbroadcast &4[&9"+Utils.NAME+"&4] &c"+mem.getName()+" has abandoned their team, they now have 0 points.");

                    return CommandResult.success();
                })
                .build();
    }
}
