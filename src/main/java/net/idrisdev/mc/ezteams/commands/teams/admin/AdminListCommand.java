package net.idrisdev.mc.ezteams.commands.teams.admin;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 11/03/2017.
 */
public class AdminListCommand {

    private static EzTeams plugin = EzTeams.get();

    public AdminListCommand() {
    }

    public static CommandSpec buildAdminListCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_LIST_TEAMS)
                .description(Utils.getCmdDescription("List ALL the teams possible."))
                .arguments()
                .executor((src, args) -> {

                    Utils.sendPrettyMessage(src,"----Current Team Standings-----");
                    for(Team team:plugin.getTeams()){

                        String name = team.getName();
                        int points = team.getPoints();

                        name=name.substring(0,1).toUpperCase()+name.substring(1);
                        Utils.sendPrettyMessage(src,"Team "+name+" - POINTS: "+points);
                    }
                    Utils.sendPrettyMessage(src,"-----------------------------");
                    return CommandResult.success();
                })
                .build();
    }
}
