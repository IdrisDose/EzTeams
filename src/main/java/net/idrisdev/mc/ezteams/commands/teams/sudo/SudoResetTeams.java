package net.idrisdev.mc.ezteams.commands.teams.sudo;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

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
                        //Set all teams and members points to 0
                        EzTeams.getTeams().stream().filter(t -> setPoints(src, t.getName(), 0)).forEach(t -> Utils.sendSrcErrorMessage(src, "Oops, Something went wrong!"));
                        plugin.core.getDao().saveAll();
                        Utils.sendPrettyMessage(src,"Successfully reset all team points.");

                    } else {
                        if(!setPoints(src,name,0))
                            Utils.sendPrettyMessage(src,"Successfully reset team "+name+" points.");
                        else
                            Utils.sendSrcErrorMessage(src,"Oops, Something went wrong!");
                    }


                    return CommandResult.success();
                })
                .build();
    }

    /**
     * Sets points of a specified team
     * @param src - CommandSource
     * @param name - Name of the team
     * @param points - Points to set team and members to (0)
     * @return true if error, false if no error occurs.
     */
    private static boolean setPoints(CommandSource src, String name, int points){
        if(Utils.validatePoints(src,points,name))
            return true;

        Team team = Utils.findTeam(name).get();
        team.setTeamPoints(points);
            EzTeams.getAllPlayers().stream().filter(m -> m.getTeam().equals(team)).forEach(m -> setPlayerPoints(src,m,points));
        return false;
    }


    /**
     * Sets a players point to 0
     * @param src - CommandSource of the executor
     * @param m - Member
     * @param points - Points (0)
     * @return true if error, false if no error occured.
     */
    private static boolean setPlayerPoints(CommandSource src, Member m, int points){
        if(points<0){
            Utils.sendSrcErrorMessage(src,"You cannot set negative numbers.");
            return true;
        } else if(m==null){
            Utils.sendSrcErrorMessage(src, "Error occured notify Idris_.");
            return true;
        }

        m.setPoints(points);
        return false;
    }
}
