package net.idrisdev.mc.ezteams.commands.teams.admin;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 7/03/2017.
 */
public class AdminAddTeam {

    private static EzTeams plugin = EzTeams.get();

    public AdminAddTeam() {
    }

    public static CommandSpec buildAdminAddTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_ADD_TEAM)
                .description(Utils.getCmdDescription("Add a team to the rankings!"))
                .arguments()
                .executor((src, args) -> {
                    return CommandResult.empty();
                })
                .build();
    }
}
