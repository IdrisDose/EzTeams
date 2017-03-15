package net.idrisdev.mc.ezteams.commands.teams.sudo;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * Created by Idris on 11/03/2017.
 */
public class TeamSetPrefix {

    private static EzTeams plugin = EzTeams.get();

    public TeamSetPrefix() {
    }

    public static CommandSpec buildTeamSetPrefix() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO_ADD_TEAM)
                .description(Utils.getCmdDescription("Add a team to the rankings!"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("prefix")))
                )
                .executor((src, args) -> {
                    String name = args.<String>getOne("team").get();
                    String prefix = args.<String>getOne("prefix").get();

                    name=name.toLowerCase();
                    if(!Utils.searchTeamsForName(name)){
                        Utils.sendSrcErrorMessage(src, "Team does not exist, use tadmin list");
                        return CommandResult.empty();
                    }
                    Team team = Utils.findTeam(name).get();

                    team.setPrefix(prefix);

                    EzTeams.getTeams().remove(team);
                    EzTeams.getTeams().add(team);
                    plugin.core.getDao().saveTeam(team);

                    Utils.sendPrettyMessage(src,"Changed team "+team.getName()+"'s prefix.");
                    return CommandResult.empty();
                })
                .build();
    }
}
