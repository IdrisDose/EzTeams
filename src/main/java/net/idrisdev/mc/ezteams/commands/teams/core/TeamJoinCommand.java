package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.data.entities.Member;
import net.idrisdev.mc.ezteams.data.entities.Team;
import net.idrisdev.mc.ezteams.utils.Utils;
import net.idrisdev.mc.ezteams.utils.Permissions;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;

/**
 * Created by Idris on 22/01/2017.
 */
public class TeamJoinCommand {

    private static EzTeams plugin = EzTeams.get();
    private static List<String> currentTeams = plugin.getConfigManager().getConfig().teamsCfg.getTeams();
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

                    if(!currentTeams.contains(teamname)){
                        Utils.sendSrcErrorMessage(src,teamname+" is not a currently avaiable team.");
                        return CommandResult.empty();
                    } else if(teamname.equals("staff")&& !src.hasPermission(Permissions.TEAMS_JOIN_STAFF)){
                        Utils.sendSrcErrorMessage(src,"You are not allowed to join the staff team.");
                        return CommandResult.empty();
                    } if(!(src instanceof Player)){
                        Utils.sendSrcErrorMessage(src,"Only players allowed to execute this command!");
                        return CommandResult.empty();
                    } else if(teamname.equals("default")){
                        Utils.sendSrcErrorMessage(src,"One does not join team default, one must use team leave.");
                        return CommandResult.empty();
                    }



                    Member mem = Utils.findMember(src.getName());
                    Team team = Utils.findTeam(teamname);

                    if(mem == null || team == null){
                        Utils.sendSrcErrorMessage(src,"An error occured while joining a team. Msg Idris_.");
                        if(plugin.DEBUG) {
                            Utils.sendSrcErrorMessage(src, "mem: " + mem);
                            Utils.sendSrcErrorMessage(src, "team: " + team);
                        }
                        return CommandResult.empty();
                    }else {
                        Team temp = mem.getTeam();
                        mem.setTeam(team);
                        mem.savePlayer();

                        if(!teamname.equals("default")) {
                            Utils.executeCmdAsConsole("pudel " + src.getName() + " " + temp.getName());
                            Utils.executeCmdAsConsole("puadd " + src.getName() + " " + teamname);
                        }
                    }
                    return CommandResult.success();
                })
                .build();
    }
}