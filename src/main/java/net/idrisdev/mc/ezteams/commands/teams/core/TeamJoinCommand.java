package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import static net.idrisdev.mc.ezteams.utils.Utils.searchTeamsForName;

/**
 * Created by Idris on 22/01/2017.
 */
public class TeamJoinCommand {

    private static EzTeams plugin = EzTeams.get();

    public TeamJoinCommand() {

    }
    public static CommandSpec buildTeamJoinCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_JOIN)
                .description(Utils.getCmdDescription("Join a team using this command!"))
                .arguments(
                        GenericArguments.onlyOne(Utils.stringarg("team"))
                )
                .executor((src, args) -> {
                    String teamname = args.<String>getOne("team").get();
                    teamname=teamname.toLowerCase();

                    if(Utils.teamValidCheck(src,teamname)){
                        return CommandResult.empty();
                    }

                    Member mem = Utils.findMember(src.getName());
                    Team team = Utils.findTeam(teamname).get();
                    Team defTeam = Utils.findTeam("default").get();

                    if(mem == null || team == null){
                        Utils.sendSrcErrorMessage(src,"An error occured while joining a team. Msg Idris_.");
                        if(Core.DEBUG) {
                            Utils.sendSrcErrorMessage(src, "mem: " + mem);
                            Utils.sendSrcErrorMessage(src, "team: " + team);
                        }
                        return CommandResult.empty();
                    }else {

                        if(!mem.getTeam().equals(defTeam)){
                            Utils.sendSrcErrorMessage(src,"You are not in team default, you have to use team leave first!");
                            return CommandResult.success();
                        }

                        Team temp = mem.getTeam();
                        mem.setTeam(team);
                        mem.savePlayer();

                        if(!teamname.equals("default")) {
                            Utils.executeCmdAsConsole("lp user "+src.getName()+" meta unset team");
                            Utils.executeCmdAsConsole("lp user "+src.getName()+" meta set team "+team.getPrefix());
                            /*
                            Utils.executeCmdAsConsole("pudel " + src.getName() + " " + temp.getName());
                            Utils.executeCmdAsConsole("puadd " + src.getName() + " " + teamname);
                            */
                        }

                        Utils.sendPrettyMessage(src,"Successfully joined team "+teamname);
                    }
                    return CommandResult.success();
                })
                .build();
    }


}
