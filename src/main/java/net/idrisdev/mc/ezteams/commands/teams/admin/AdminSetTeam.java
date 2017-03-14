package net.idrisdev.mc.ezteams.commands.teams.admin;

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
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.idrisdev.mc.ezteams.utils.Utils.searchTeamsForName;

/**
 * Created by Idris on 22/01/2017.
 */
public class AdminSetTeam {

    private static EzTeams plugin = EzTeams.get();
    private static List<String> currentTeams = new ArrayList<>();
    public AdminSetTeam() {
    }

    public static CommandSpec buildMemberSetTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_ADD)
                .description(Utils.getCmdDescription("Force a member into a team."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("target"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))

                )
                .executor((src, args) -> {
                    Player target = args.<Player>getOne("target").get();
                    String teamname = args.<String>getOne("team").get().toLowerCase();
                    teamname = teamname.toLowerCase();

                    if(!searchTeamsForName(teamname)){
                        Utils.sendSrcErrorMessage(src,teamname+" is not a currently avaiable team.");
                        return CommandResult.empty();
                    } else if(teamname.equals("staff")&& !src.hasPermission(Permissions.TEAMS_JOIN_STAFF)){
                        Utils.sendSrcErrorMessage(src,"You are not allowed to join the staff team.");
                        return CommandResult.empty();
                    } if(!(src instanceof Player)){
                        Utils.sendSrcErrorMessage(src,"Only onlineMembers allowed to execute this command!");
                        return CommandResult.empty();
                    } else if(teamname.equals("default")){
                        Utils.sendSrcErrorMessage(src,"One does not join team default, one must use team leave.");
                        return CommandResult.empty();
                    }



                    Member mem = Utils.findMember(target.getName());
                    Team team = Utils.findTeam(teamname).get();

                    if(mem == null || team == null){
                        Utils.sendSrcErrorMessage(src,"An error occured while joining a team. Msg Idris_.");
                        if(Core.DEBUG) {
                            Utils.sendSrcErrorMessage(src, "mem: " + mem);
                            Utils.sendSrcErrorMessage(src, "team: " + team);
                        }
                        return CommandResult.empty();
                    }else {
                        Team temp = mem.getTeam();
                        mem.setTeam(team);
                        mem.setMemberPoints(mem.getPoints());

                        temp.removeTeamPoints(mem.getPoints());
                        team.setTeamPoints(mem.getPoints());
                        mem.savePlayer();

                        if(!teamname.equals("default")) {
                            Utils.executeCmdAsConsole("lp user "+src.getName()+" meta unset team");
                            Utils.executeCmdAsConsole("lp user "+src.getName()+" meta set team "+team.getPrefix());
                            /*
                            Utils.executeCmdAsConsole("pudel " + src.getName() + " " + temp.getName());
                            Utils.executeCmdAsConsole("puadd " + src.getName() + " " + teamname);
                            */
                        }
                    }
                    return CommandResult.success();
                })
                .build();
    }
}
