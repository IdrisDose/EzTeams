package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.data.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 22/01/2017.
 */
public class TeamListCommand {

    private static EzTeams plugin = EzTeams.get();

    public TeamListCommand() {
    }

    public static CommandSpec buildTeamListCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_LIST)
                .description(Utils.getCmdDescription("This shows all the current teams with their points"))
                .arguments()
                .executor((src, args) -> {
                    Utils.sendPrettyMessage(src,"----Current Team Standings-----");
                    for(Team team:plugin.teams){

                        String name = team.getName();
                        int points = team.getPoints();
                        if(!name.equals("staff")&&!name.equals("default")){
                            name=name.substring(0,1).toUpperCase()+name.substring(1);
                            Utils.sendPrettyMessage(src,"Team "+name+" - POINTS: "+points);
                        }

                    }
                    Utils.sendPrettyMessage(src,"------------------------------");
                    return CommandResult.success();
                })
                .build();
    }
}
