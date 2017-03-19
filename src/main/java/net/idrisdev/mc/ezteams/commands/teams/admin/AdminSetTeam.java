package net.idrisdev.mc.ezteams.commands.teams.admin;

import net.idrisdev.mc.ezteams.EzTeams;
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

                    if(Utils.teamValidCheck(src,teamname)){
                        return CommandResult.empty();
                    }


                    Member mem = Utils.findMember(target.getName());
                    Team team = Utils.findTeam(teamname).get();

                    if(Utils.validateOther(src,mem,team))
                        return CommandResult.success();


                    Team temp = mem.getTeam();
                    mem.changeTeam(src,team,temp);

                    return CommandResult.success();
                })
                .build();
    }
}
