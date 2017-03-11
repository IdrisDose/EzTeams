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
public class AdminRemoveTeam {

    private static EzTeams plugin = EzTeams.get();

    public AdminRemoveTeam() {
    }

    public static CommandSpec buildAdminRemoveTeam() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_REMOVE_TEAM)
                .description(Utils.getCmdDescription("Removes a team. PERMANENTLY DELETING ALL OF IT'S DATA."))
                .arguments()
                .executor((src, args) -> {


                    return CommandResult.empty();
                })
                .build();
    }
}