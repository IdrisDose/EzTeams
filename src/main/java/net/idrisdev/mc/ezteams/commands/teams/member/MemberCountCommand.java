package net.idrisdev.mc.ezteams.commands.teams.member;

import net.idrisdev.mc.ezteams.EzTeams;
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
public class MemberCountCommand {

    private static EzTeams plugin = EzTeams.get();

    public MemberCountCommand() {
    }

    public static CommandSpec buildMemberCountCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_MEMBER_COUNT)
                .description(Utils.getCmdDescription("View the count of a specific team or all teams"))
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("team")))
                )
                .executor((src, args) -> {
                    String team = args.<String>getOne("team").isPresent()?args.<String>getOne("team").get():"all";
                    team=team.toLowerCase();

                    if(!team.equals("all")){
                        int count = Utils.findTeamCount(team);
                        Utils.sendPrettyMessage(src,"Team "+team+" member count: "+count);
                        return CommandResult.success();
                    }

                    Utils.sendPrettyMessage(src,"---Team Member Count---");

                    for(Team t: EzTeams.getTeams()){
                        String name = t.getName();
                        int count = Utils.findTeamCount(name);
                        name=name.substring(0,1).toUpperCase()+name.substring(1);
                        Utils.sendPrettyMessage(src,"Team "+name+" - COUNT: "+count);
                    }

                    Utils.sendPrettyMessage(src,"----------------------");

                    return CommandResult.success();
                })
                .build();
    }
}
