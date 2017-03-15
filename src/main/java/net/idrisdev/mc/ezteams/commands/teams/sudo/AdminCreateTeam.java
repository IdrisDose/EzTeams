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
 * Created by Idris on 7/03/2017.
 */
public class AdminCreateTeam {

    private static EzTeams plugin = EzTeams.get();

    public AdminCreateTeam() {
    }

    public static CommandSpec buildAdminCreateTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO_ADD_TEAM)
                .description(Utils.getCmdDescription("Add a team to the rankings!"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("prefix")))
                )
                .executor((src, args) -> {
                    String team = args.<String>getOne("team").get().toLowerCase();
                    String prefix = args.<String>getOne("prefix").get();

                    if(Utils.searchTeamsForName(team)){
                        Utils.sendSrcErrorMessage(src, "Team already exists, please remove first.");
                        return CommandResult.empty();
                    }

                    //New ID == Get Current team list, get last object, get the ID for the last object and add 1.
                    int newID = EzTeams.getTeams().get(EzTeams.getTeams().size()-1).getId()+1;

                    Team newTeam = new Team(newID,team,0,prefix);

                    EzTeams.getTeams().add(newTeam);
                    plugin.core.getDao().saveTeam(newTeam);

                    Utils.sendPrettyMessage(src,"Created new team: "+team+".");

                    return CommandResult.empty();
                })
                .build();
    }
}
