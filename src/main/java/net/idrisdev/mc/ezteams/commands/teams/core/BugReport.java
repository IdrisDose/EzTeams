package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 14/03/2017.
 */
public class BugReport {

    private static EzTeams plugin = EzTeams.get();

    public BugReport() {
    }

    public static CommandSpec buildbugreport() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_BASE)
                .description(Utils.getCmdDescription("Displays the link for bug reporting"))
                .arguments()
                .executor((src, args) -> {
                    if(!(src instanceof Player)) {
                        Utils.sendSrcErrorMessage(src,"Silly console, only humans can use this command!");
                        return CommandResult.success();
                    }
                    Utils.sendPrettyMessage(src,"Please use the following link to report bugs:");
                    Utils.sendPrettyMessage(src,"https://goo.gl/forms/b0dY638KVijH5Wzm1");
                    return CommandResult.success();
                })
                .build();
    }
}
