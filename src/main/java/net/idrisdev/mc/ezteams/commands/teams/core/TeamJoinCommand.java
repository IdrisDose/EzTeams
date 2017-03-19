package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

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
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))
                )
                .executor((src, args) -> {
                    String teamName = args.<String>getOne("team").get();
                    teamName=teamName.toLowerCase();

                    if(Utils.teamValidCheck(src,teamName)){
                        return CommandResult.empty();
                    }

                    Member mem = Utils.findMember(src.getName());
                    Team team = Utils.findTeam(teamName).get();

                    // if this returns true, exit command
                    if(Utils.joinValidate(src, mem,team))
                        return CommandResult.success();

                    mem.joinTeam(team);
                    Utils.sendPrettyMessage(src,"Successfully joined team "+teamName);

                    return CommandResult.success();
                })
                .build();
    }


}
