package net.idrisdev.mc.ezteams.commands.teams.member;

import net.idrisdev.mc.ezteams.EzTeams;
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

                    int aquacount = Utils.findTeamCount("aqua");
                    int galacticcount = Utils.findTeamCount("galactic");
                    int magmacount = Utils.findTeamCount("magma");
                    int plasmacount = Utils.findTeamCount("plasma");
                    int rocketcount = Utils.findTeamCount("rocket");


                    Utils.sendPrettyMessage(src,"---Team Member Count---");
                    Utils.sendPrettyMessage(src,"- Team: Aqua - Member Count: "+aquacount);
                    Utils.sendPrettyMessage(src,"- Team: Galactic - Member Count: "+galacticcount);
                    Utils.sendPrettyMessage(src,"- Team: Magma - Member Count: "+magmacount);
                    Utils.sendPrettyMessage(src,"- Team: Plasma - Member Count: "+plasmacount);
                    Utils.sendPrettyMessage(src,"- Team: Rocket - Member Count: "+rocketcount);
                    Utils.sendPrettyMessage(src,"----------------------");

                    return CommandResult.success();
                })
                .build();
    }
}
