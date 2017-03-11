package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
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
public class HelpCommand {

    private static EzTeams plugin = EzTeams.get();

    public HelpCommand() {
    }

    public static CommandSpec buildHelpCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_BASE)
                .description(Utils.getCmdDescription("Help for teams"))
                .arguments()
                .executor((src, args) -> {
                    Utils.sendPrettyMessage(src,"----EzTeams Help----");
                    Utils.sendPrettyMessage(src,"-See <Website> for command docs-");
                    Utils.sendPrettyMessage(src,"-------------------");
                    return CommandResult.success();
                })
                .build();
    }
}
