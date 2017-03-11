package net.idrisdev.mc.ezteams.commands.teams.sudo;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 11/03/2017.
 */
public class SudoResetTeams {

    private static EzTeams plugin = EzTeams.get();

    public SudoResetTeams() {
    }

    public static CommandSpec buildSudoResetTeams() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO_RESET)
                .description(Utils.getCmdDescription("SUDO ONLY - Resets All or a Specific team"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))
                )
                .executor((src, args) -> {
                    String name = args.<String>getOne("team").get();
                    name = name.toLowerCase();
                    if(name.equalsIgnoreCase("all")){
                        for (Team t : EzTeams.getTeams()){
                            setPoints(src,t.getName(),0);
                        }
                        Utils.sendPrettyMessage(src,"Successfully reset all team points.");
                    } else {
                        setPoints(src,name,0);
                        Utils.sendPrettyMessage(src,"Successfully reset team "+name+" points.");
                    }


                    return CommandResult.success();
                })
                .build();
    }

    private static CommandResult setPoints(CommandSource src, String name, int points){

        if(points<0){
            Utils.sendSrcErrorMessage(src,"You cannot use negative numbers.");
            return CommandResult.success();
        }else  if(name.equals("none")){
            Utils.sendSrcErrorMessage(src,"You must specify a target");
            return CommandResult.success();
        }

        Team team = Utils.findTeam(name).get();
        team.setTeamPoints(points);
        return CommandResult.success();
    }
}
