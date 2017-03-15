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
public class AdminDeleteTeam {

    private static EzTeams plugin = EzTeams.get();

    public AdminDeleteTeam() {
    }

    public static CommandSpec buildAdminDeleteTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO_REMOVE_TEAM)
                .description(Utils.getCmdDescription("Removes a team. PERMANENTLY DELETING ALL OF IT'S DATA."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))
                )
                .executor((src, args) -> {
                    String teamName = args.<String>getOne("team").get().toLowerCase();

                    Team team = Utils.getTeam(teamName);
                    if(team==null){
                        Utils.sendSrcErrorMessage(src,"Team seems to be null.");
                        return CommandResult.success();
                    } else if(teamName.equals("dev")||teamName.equals("developer")) {
                        Utils.sendSrcErrorMessage(src, "Sorry, you do not have permission to delete that.");
                        return CommandResult.success();
                    }
                    plugin.core.getDao().deleteTeam(team);
                    EzTeams.getTeams().remove(team);
                    plugin.core.getDao().saveAll();

                    Utils.sendPrettyMessage(src, "Removed team.");
                    return CommandResult.empty();
                })
                .build();
    }
}
