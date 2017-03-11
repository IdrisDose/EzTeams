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

/**
 * Created by Idris on 22/01/2017.
 */
public class AdminRemoveFromTeam {
    private static EzTeams plugin = EzTeams.get();

    public AdminRemoveFromTeam() {
    }

    public static CommandSpec buildMemberRemoveTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_REMOVE)
                .description(Utils.getCmdDescription("For those who wish to abandon the greate cause."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("target")))
                )
                .executor((src, args) -> {
                    String teamname = "default";
                    Player target = args.<Player>getOne("target").get();

                    if(!(src instanceof Player)){
                        Utils.sendSrcErrorMessage(src,"Only onlineMembers allowed to execute this command!");
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
                        mem.setPoints(0);
                        mem.savePlayer();
                        Utils.executeCmdAsConsole("lp user "+src.getName()+" meta unset team");
                        //Utils.executeCmdAsConsole("pudel " + src.getName() + " " + temp.getName());

                    }

                    Utils.executeCmdAsConsole("plainbroadcast &4[&9"+Utils.NAME+"&4] &c"+mem.getName()+" has been removed from their team, they now have 0 points.");
                    return CommandResult.success();
                })
                .build();
    }
}
