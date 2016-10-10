package net.idrisdev.mc.ezteams.commands.executors;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Idris on 7/10/2016.
 */
public class TeamsList extends ETUtils implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        sendSrcPlainMessage(src,"----- Available Teams-------");
        EzTeams.teams.stream()
                .filter(teamData -> !(teamData.getNAME().toLowerCase().equals("staff")))
                .filter(teamData -> !(teamData.getNAME().toLowerCase().equals("default")))
                .forEach(teamData -> sendSrcPlainMessage(src,teamData.getNAME()));
        sendSrcPlainMessage(src,"----------------------------");
        return CommandResult.success();
    }
}
